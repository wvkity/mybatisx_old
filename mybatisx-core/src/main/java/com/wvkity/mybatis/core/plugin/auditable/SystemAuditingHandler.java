/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.wvkity.mybatis.core.plugin.auditable;

import com.wvkity.mybatis.basic.keygen.KeyGenerator;
import com.wvkity.mybatis.basic.metadata.Auditor;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.PrimaryKey;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.core.auditable.MetadataParser;
import com.wvkity.mybatis.core.auditable.OriginalProperty;
import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;
import com.wvkity.mybatis.core.auditable.alter.DefaultAuditedAlterData;
import com.wvkity.mybatis.core.auditable.event.AuditedEventPublisher;
import com.wvkity.mybatis.core.cache.LocalCacheFactory;
import com.wvkity.mybatis.core.plugin.auditable.cache.CacheData;
import com.wvkity.mybatis.core.plugin.auditable.cache.DefaultCacheData;
import com.wvkity.mybatis.core.utils.Columns;
import com.wvkity.mybatis.support.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.support.config.MyBatisLocalConfigurationCache;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * 系统属性审计处理器
 * @author wvkity
 * @created 2021-03-12
 * @since 1.0.0
 */
public class SystemAuditingHandler extends AbstractAuditingHandler<OriginalProperty> {

    private final MetadataParser parser;

    public SystemAuditingHandler(AuditedEventPublisher<List<AuditedAlterData>> eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.parser = new MetadataParser();
    }

    @Override
    public List<AuditedAlterData> invoke(final MappedStatement ms, final Object parameter,
                                         final Object target, final boolean isInsert, final boolean isLogicDelete) {
        final Table table = this.parser.parse(ms, target);
        if (table != null) {
            final List<AuditedAlterData> restores = new ArrayList<>(4);
            if (isInsert) {
                Optional.ofNullable(this.idInvoke(ms, table, target)).ifPresent(restores::add);
                final MyBatisGlobalConfiguration mgc =
                    MyBatisLocalConfigurationCache.getGlobalConfiguration(ms.getConfiguration());
                if (mgc.isLogicDeletePropertyAutoAudit()) {
                    Optional.ofNullable(this.logicDeleteInvoke(ms, table, target, true)).ifPresent(restores::add);
                }
            } else if (isLogicDelete) {
                Optional.ofNullable(this.logicDeleteInvoke(ms, table, target, false)).ifPresent(restores::add);
            }
            if (!CollectionUtils.isEmpty(restores)) {
                return restores;
            }
        }
        return null;
    }

    /**
     * 主键属性审计
     * @param ms     {@link MappedStatement}
     * @param table  {@link Table}
     * @param target 目标对象
     * @return {@link AuditedAlterData}
     */
    protected AuditedAlterData idInvoke(final MappedStatement ms, final Table table, final Object target) {
        final Column idCol = table.getIdColumn();
        if (idCol != null) {
            final PrimaryKey primaryKey = idCol.getPrimaryKey();
            if (primaryKey.isSnowflake() || primaryKey.isUuid()) {
                final MyBatisGlobalConfiguration mgc =
                    MyBatisLocalConfigurationCache.getGlobalConfiguration(ms.getConfiguration());
                final KeyGenerator generator = mgc.getKeyGenerator();
                if (generator != null) {
                    final Object value = primaryKey.isUuid() ? generator.nextUuid() :
                        String.class.equals(idCol.getJavaType()) ? generator.nextIdString() : generator.nextId();
                    final String cacheKey = ms.getId() + "-" + DEF_SA_CACHE_SUFFIX + "_id";
                    return this.invoke(idCol, target, value, cacheKey);
                }
            }
        }
        return null;
    }

    /**
     * 逻辑删除属性审计
     * @param ms      {@link MappedStatement}
     * @param table   {@link Table}
     * @param target  目标对象
     * @param isUnDel 是否未删除
     * @return {@link AuditedAlterData}
     */
    protected AuditedAlterData logicDeleteInvoke(final MappedStatement ms, final Table table,
                                                 final Object target, boolean isUnDel) {
        final Column logicDeleteCol = table.getLogicalDeleteColumn();
        if (logicDeleteCol != null) {
            final Auditor auditor = logicDeleteCol.getAuditor();
            final Object value = isUnDel ? auditor.getUndeletedValue() : auditor.getDeletedValue();
            if (value != null) {
                final String cacheKey = ms.getId() + "-" + DEF_SA_CACHE_SUFFIX + "_logic_del";
                return this.invoke(logicDeleteCol, target, value, cacheKey);
            }
        }
        return null;
    }

    /**
     * 注入值
     * @param column   {@link Column}
     * @param target   目标对象
     * @param value    值
     * @param cacheKey 缓存key
     * @return {@link AuditedAlterData}
     */
    protected AuditedAlterData invoke(final Column column, final Object target, final Object value,
                                      final String cacheKey) {
        final CacheData<OriginalProperty> cache = this.getCache(cacheKey);
        final OriginalProperty property;
        if (cache != null && (property = cache.getData()) != null) {
            return this.invoke(property, target, value);
        } else {
            final OriginalProperty logicDelProp = Columns.toProperty(column);
            this.cache(cacheKey, new DefaultCacheData<>(logicDelProp));
            return this.invoke(logicDelProp, target, value);
        }
    }

    /**
     * 注入值
     * @param property {@link OriginalProperty}
     * @param target   目标对象
     * @param value    值
     * @return {@link AuditedAlterData}
     */
    protected AuditedAlterData invoke(final OriginalProperty property, final Object target, final Object value) {
        if (property.canInvoke(target)) {
            final Object oldValue = property.getValue(target);
            property.invoke(target, value);
            if (this.rollback) {
                return new DefaultAuditedAlterData(target, property, oldValue);
            }
        }
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.localCache = LocalCacheFactory.create(properties.getProperty(PROP_KEY_SA_CACHE_CLASS), properties,
            properties.getProperty(PROP_KEY_SA_CACHE_CFG_PREFIX));
    }
}
