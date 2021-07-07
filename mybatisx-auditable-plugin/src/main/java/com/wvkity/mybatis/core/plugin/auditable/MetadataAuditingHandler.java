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

import com.wvkity.mybatis.core.auditable.MatchMode;
import com.wvkity.mybatis.core.auditable.OriginalProperty;
import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;
import com.wvkity.mybatis.core.auditable.event.AuditedEventPublisher;
import com.wvkity.mybatis.core.cache.LocalCacheFactory;
import com.wvkity.mybatis.core.plugin.auditable.cache.CacheData;
import com.wvkity.mybatis.core.plugin.auditable.cache.DefaultCacheData;
import com.wvkity.mybatis.core.plugin.auditable.support.AuditMatcher;
import com.wvkity.mybatis.core.plugin.auditable.support.MetadataAuditable;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 元数据审计处理器
 * @author wvkity
 * @created 2021-03-05
 * @since 1.0.0
 */
public class MetadataAuditingHandler extends AbstractAuditingHandler<List<OriginalProperty>> {

    private final AuditMatcher auditMatcher;
    private final MetadataAuditable metadataAuditable;

    public MetadataAuditingHandler(AuditMatcher auditMatcher, MetadataAuditable metadataAuditable,
                                   AuditedEventPublisher<List<AuditedAlterData>> eventPublisher) {
        this.auditMatcher = auditMatcher;
        this.metadataAuditable = metadataAuditable;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<AuditedAlterData> invoke(MappedStatement ms, Object parameter, Object target,
                                         boolean isInsert, boolean isLogicDelete) {
        final List<OriginalProperty> properties = this.matches(ms, target, isInsert, isLogicDelete);
        if (properties != null && !properties.isEmpty()) {
            return this.invoke(ms, target, properties);
        }
        return null;
    }

    /**
     * 解析获取属性信息
     * @param ms          {@link MappedStatement}
     * @param target      目标对象
     * @param insert      是否为保存操作
     * @param logicDelete 是否为逻辑删除操作
     * @return {@link OriginalProperty}列表
     */
    protected List<OriginalProperty> matches(final MappedStatement ms, final Object target,
                                             final boolean insert, final boolean logicDelete) {
        if (this.auditMatcher != null) {
            final String cacheKey = ms.getId() + DEF_MA_CACHE_SUFFIX;
            final CacheData<List<OriginalProperty>> cache = this.getCache(cacheKey);
            final List<OriginalProperty> cacheProps;
            if (cache != null && (cacheProps = cache.getData()) != null && !cacheProps.isEmpty()) {
                return cacheProps;
            }
            final List<OriginalProperty> properties =
                this.auditMatcher.matches(ms, target, MatchMode.METADATA, insert, logicDelete);
            if (properties != null && !properties.isEmpty()) {
                this.cache(cacheKey, new DefaultCacheData<>(properties));
            }
            return properties;
        }
        return null;
    }

    /**
     * 注入值
     * @param ms         {@link MappedStatement}
     * @param target     目标对象
     * @param properties {@link OriginalProperty}列表
     * @return {@link AuditedAlterData}列表
     */
    protected List<AuditedAlterData> invoke(MappedStatement ms, Object target, List<OriginalProperty> properties) {
        return properties.stream().map(it -> this.metadataAuditable.invoke(target, it))
            .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.localCache = LocalCacheFactory.create(properties.getProperty(PROP_KEY_MA_CACHE_CLASS), properties,
            properties.getProperty(PROP_KEY_MA_CACHE_CFG_PREFIX));
    }
}
