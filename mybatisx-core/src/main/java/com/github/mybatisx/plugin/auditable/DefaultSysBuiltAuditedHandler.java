/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.github.mybatisx.plugin.auditable;

import com.github.mybatisx.Objects;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.auditable.event.AuditedEvent;
import com.github.mybatisx.auditable.event.DefaultAuditedEvent;
import com.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import com.github.mybatisx.auditable.meta.AuditedMetadata;
import com.github.mybatisx.auditable.meta.DefaultAuditedMetadata;
import com.github.mybatisx.basic.keygen.KeyGenerator;
import com.github.mybatisx.basic.metadata.AuditMeta;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.cache.LocalCacheFactory;
import com.github.mybatisx.core.auditable.SourceTableParser;
import com.github.mybatisx.core.utils.Columns;
import com.github.mybatisx.event.EventPhase;
import com.github.mybatisx.plugin.auditable.cache.CacheData;
import com.github.mybatisx.plugin.auditable.cache.DefaultSingleCacheData;
import com.github.mybatisx.reflect.Reflections;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import com.github.mybatisx.support.config.MyBatisLocalConfigurationCache;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * 默认内置审计拦截处理器
 * @author wvkity
 * @created 2021-07-21
 * @since 1.0.0
 */
public class DefaultSysBuiltAuditedHandler extends AbstractAuditedHandler<PropertyWrapper>
    implements SysBuiltAuditedHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultSysBuiltAuditedHandler.class);
    /**
     * 缓存实现类
     */
    public static final String PROP_KEY_SYS_CACHE_CLASS = "sysAuditedCacheClass";
    /**
     * 缓存配置项前缀
     */
    public static final String PROP_KEY_SYS_CACHE_CFG_PREFIX = "sysAuditedCacheCfgPrefix";
    /**
     * 实体类映射表解析器
     */
    private final SourceTableParser parser = new SourceTableParser();

    public DefaultSysBuiltAuditedHandler(boolean rollbackRestore, AuditedEventPublisher eventPublisher) {
        this.rollbackRestore = rollbackRestore;
        this.auditedEventPublisher = eventPublisher;
    }

    @Override
    protected AuditedEvent auditedHandle(MappedStatement ms, Object parameter, List<Object> sources, boolean isInsert
        , boolean isLogicDelete) {
        final MyBatisGlobalConfiguration mgc =
            MyBatisLocalConfigurationCache.getGlobalConfiguration(ms.getConfiguration());
        final Table table = this.parser.parse(ms, sources.get(0));
        if (Objects.nonNull(table)) {
            final List<AuditedMetadata> metadataList = new ArrayList<>();
            if (isInsert) {
                Optional.ofNullable(this.idAudited(ms, parameter, table, mgc, sources))
                    .ifPresent(metadataList::addAll);
                Optional.ofNullable(this.logicDeleteAudited(ms, parameter, table, mgc, sources, false))
                    .ifPresent(metadataList::addAll);
                Optional.ofNullable(this.versionAudited(ms, parameter, table, mgc, sources))
                    .ifPresent(metadataList::addAll);
            } else if (isLogicDelete) {
                Optional.ofNullable(this.logicDeleteAudited(ms, parameter, table, mgc, sources, true))
                    .ifPresent(metadataList::addAll);
            }
            if (Objects.isNotEmpty(metadataList)) {
                return new DefaultAuditedEvent(metadataList, EventPhase.AFTER_ROLLBACK, "sysBuiltAuditedEvent");
            }
        }
        return null;
    }

    /**
     * 获取{@link PropertyWrapper}对象
     * @param cacheKey 缓存key
     * @param supplier {@link ColumnSupplier}
     * @return {@link PropertyWrapper}
     */
    protected PropertyWrapper getPropCache(final String cacheKey, ColumnSupplier supplier) {
        final CacheData<PropertyWrapper> cache = this.getCache(cacheKey);
        PropertyWrapper property = null;
        if (Objects.nonNull(cache)) {
            property = cache.getData();
        } else {
            final Column it = supplier.get();
            if (Objects.nonNull(it)) {
                property = Columns.toProperty(it);
                this.cache(cacheKey, new DefaultSingleCacheData(property));
            }
        }
        return property;
    }

    /**
     * 主键审计
     * @param ms        {@link MappedStatement}
     * @param parameter 参数
     * @param table     {@link Table}
     * @param mgc       {@link MyBatisGlobalConfiguration}
     * @param sources   待审计对象列表
     * @return {@link AuditedMetadata}列表
     */
    protected List<AuditedMetadata> idAudited(final MappedStatement ms, final Object parameter, final Table table,
                                              final MyBatisGlobalConfiguration mgc, final List<Object> sources) {
        final String cacheKey = ms.getId() + ".plugin-sys-audited-id";
        final PropertyWrapper property = this.getPropCache(cacheKey, table::getIdColumn);
        if (Objects.nonNull(property)) {
            boolean isUuid = false;
            if (property.isSnowflake() || (isUuid = property.isUuid())) {
                final KeyGenerator kg = mgc.getKeyGenerator();
                if (Objects.nonNull(kg)) {
                    final boolean isString = String.class.equals(property.getJavaType());
                    final List<AuditedMetadata> metadataList = new ArrayList<>();
                    for (Object target : sources) {
                        final Object value = isUuid ? kg.nextUuid() : isString ? kg.nextIdString() : kg.nextId();
                        final AuditedMetadata metadata = this.audited(ms, parameter, target, property, value);
                        if (Objects.nonNull(metadata)) {
                            metadataList.add(metadata);
                        }
                    }
                    return metadataList;
                }
            }
        }
        return null;
    }

    /**
     * 逻辑删除审计
     * @param ms        {@link MappedStatement}
     * @param parameter 参数
     * @param table     {@link Table}
     * @param mgc       {@link MyBatisGlobalConfiguration}
     * @param sources   待审计对象列表
     * @param isDeleted 是否删除
     * @return {@link AuditedMetadata}列表
     */
    protected List<AuditedMetadata> logicDeleteAudited(final MappedStatement ms, final Object parameter,
                                                       final Table table, final MyBatisGlobalConfiguration mgc,
                                                       final List<Object> sources, final boolean isDeleted) {
        final Column it = table.getLogicalDeleteColumn();
        if (Objects.nonNull(it)) {
            final String cacheKey = ms.getId() + ".plugin-sys-audited-logic-delete";
            final PropertyWrapper property = this.getPropCache(cacheKey, () -> it);
            if (Objects.nonNull(property)) {
                final AuditMeta meta = it.getAuditMeta();
                Object value = null;
                if (isDeleted) {
                    value = meta.getDeletedValue();
                } else {
                    if (mgc.isLogicDeletedInit()) {
                        value = meta.getUndeletedValue();
                    }
                }
                return this.audited(ms, parameter, sources, property, value);
            }
        }
        return null;
    }

    /**
     * 乐观锁审计
     * @param ms        {@link MappedStatement}
     * @param parameter 参数
     * @param table     {@link Table}
     * @param mgc       {@link MyBatisGlobalConfiguration}
     * @param sources   待审计对象列表
     * @return {@link AuditedMetadata}列表
     */
    protected List<AuditedMetadata> versionAudited(final MappedStatement ms, final Object parameter,
                                                   final Table table, final MyBatisGlobalConfiguration mgc,
                                                   final List<Object> sources) {
        final Column it = table.getOptimisticLockColumn();
        if (Objects.nonNull(it)) {
            final String cacheKey = ms.getId() + ".plugin-sys-audited-version";
            final PropertyWrapper property = this.getPropCache(cacheKey, () -> it);
            if (Objects.nonNull(property)) {
                final AuditMeta meta = it.getAuditMeta();
                Object value = null;
                if (mgc.isOptimisticLockInit() && (Number.class.isAssignableFrom(it.getJavaType()))) {
                    value = meta.getVersionInitValue();
                }
                return this.audited(ms, parameter, sources, property, value);
            }
        }
        return null;
    }

    /**
     * 审计
     * @param ms        {@link MappedStatement}
     * @param parameter 参数
     * @param sources   待审计对象列表
     * @param property  {@link PropertyWrapper}
     * @param value     值
     * @return {@link AuditedMetadata}
     */
    protected List<AuditedMetadata> audited(final MappedStatement ms, final Object parameter,
                                            final List<Object> sources, final PropertyWrapper property,
                                            final Object value) {
        if (Objects.nonNull(value)) {
            final List<AuditedMetadata> metadataList = new ArrayList<>();
            for (Object target : sources) {
                final AuditedMetadata metadata = this.audited(ms, parameter, target, property, value);
                if (Objects.nonNull(metadata)) {
                    metadataList.add(metadata);
                }
            }
            return metadataList;
        }
        return null;
    }

    /**
     * 审计
     * @param ms        {@link MappedStatement}
     * @param parameter 参数
     * @param target    目标对象
     * @param property  {@link PropertyWrapper}
     * @param value     值
     * @return {@link AuditedMetadata}
     */
    protected AuditedMetadata audited(final MappedStatement ms, final Object parameter, final Object target,
                                      final PropertyWrapper property, final Object value) {
        if (property.canInvoke(target)) {
            final Object oldValue = property.getValue(target);
            if (Objects.isNull(oldValue)) {
                property.invoke(target, value);
                if (this.rollbackRestore) {
                    return new DefaultAuditedMetadata(target, property, null);
                }
            }
        }
        return null;
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter) {
        return this.isInsert(ms) || this.isUpdate(ms);
    }

    @Override
    public boolean canAudited(MappedStatement ms, Object parameter) {
        return !Reflections.isSimpleJavaObject(parameter);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Objects.isNull(this.localCache)) {
            this.localCache = LocalCacheFactory.create(this.getProperty(PROP_KEY_SYS_CACHE_CLASS), this.properties,
                this.getProperty(PROP_KEY_SYS_CACHE_CFG_PREFIX));
        }
    }

    @FunctionalInterface
    public interface ColumnSupplier {

        /**
         * 获取{@link Column}对象
         * @return {@link Column}对象
         */
        Column get();
    }
}
