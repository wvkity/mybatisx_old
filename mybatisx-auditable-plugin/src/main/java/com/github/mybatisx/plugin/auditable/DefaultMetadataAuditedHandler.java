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
import com.github.mybatisx.auditable.AuditedPattern;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import com.github.mybatisx.auditable.meta.AuditedMetadata;
import com.github.mybatisx.cache.LocalCache;
import com.github.mybatisx.cache.LocalCacheFactory;
import com.github.mybatisx.plugin.auditable.cache.CacheData;
import com.github.mybatisx.plugin.auditable.cache.DefaultMultiCacheData;
import com.github.mybatisx.plugin.auditable.support.AuditedPropertyLoader;
import com.github.mybatisx.plugin.auditable.support.MetadataAuditable;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认元数据审计拦截处理器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultMetadataAuditedHandler extends AbstractMetadataAuditedHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultMetadataAuditedHandler.class);
    /**
     * 缓存后缀
     */
    public static final String MA_CACHE_PREFIX = "metadata:audited";
    /**
     * 审计属性加载器
     */
    protected final AuditedPropertyLoader propertyLoader;
    /**
     * 元数据审计处理
     */
    protected final MetadataAuditable metadataAuditable;

    public DefaultMetadataAuditedHandler(boolean rollbackEnable, AuditedPropertyLoader propertyLoader,
                                         MetadataAuditable metadataAuditable,
                                         AuditedEventPublisher auditedEventPublisher) {
        this.rollbackRestore = rollbackEnable;
        this.propertyLoader = propertyLoader;
        this.metadataAuditable = metadataAuditable;
        this.auditedEventPublisher = auditedEventPublisher;
    }

    public DefaultMetadataAuditedHandler(boolean rollbackEnable, AuditedPropertyLoader propertyLoader,
                                         MetadataAuditable metadataAuditable,
                                         LocalCache<String, CacheData<List<PropertyWrapper>>> localCache,
                                         AuditedEventPublisher auditedEventPublisher) {
        this.rollbackRestore = rollbackEnable;
        this.localCache = localCache;
        this.propertyLoader = propertyLoader;
        this.metadataAuditable = metadataAuditable;
        this.auditedEventPublisher = auditedEventPublisher;
    }

    public DefaultMetadataAuditedHandler(boolean rollbackEnable,
                                         Set<String> interceptMethods, Set<String> ignoreMethods,
                                         Set<String> logicMethods,
                                         AuditedPropertyLoader propertyLoader, MetadataAuditable metadataAuditable,
                                         LocalCache<String, CacheData<List<PropertyWrapper>>> localCache,
                                         AuditedEventPublisher auditedEventPublisher) {
        this.rollbackRestore = rollbackEnable;
        this.interceptMethods = interceptMethods;
        this.ignoreMethods = ignoreMethods;
        this.logicDeleteMethods = logicMethods;
        this.localCache = localCache;
        this.propertyLoader = propertyLoader;
        this.metadataAuditable = metadataAuditable;
        this.auditedEventPublisher = auditedEventPublisher;
    }

    @Override
    public boolean canAudited(MappedStatement ms, Object parameter) {
        return super.canAudited(ms, parameter) && Objects.nonNull(this.metadataAuditable)
            && Objects.nonNull(this.propertyLoader);
    }

    @Override
    public List<PropertyWrapper> loadProperties(MappedStatement ms, Object target, boolean isInsert,
                                                boolean isLogicDelete) {
        final String cacheKey = ms.getId() + ":" + MA_CACHE_PREFIX;
        CacheData<List<PropertyWrapper>> cacheData = this.getCache(cacheKey);
        List<PropertyWrapper> pws;
        if (cacheData != null && Objects.isNotEmpty((pws = cacheData.getData()))) {
            return pws;
        }
        pws = this.propertyLoader.load(ms, target, AuditedPattern.METADATA, isInsert, isLogicDelete);
        if (Objects.isNotEmpty(pws)) {
            this.cache(cacheKey, new DefaultMultiCacheData(pws));
        }
        return pws;
    }

    @Override
    public List<AuditedMetadata> audited(MappedStatement ms, Object parameter, Object target,
                                         List<PropertyWrapper> properties) {
        if (Objects.isNotEmpty(properties)) {
            return properties.stream().map(it -> this.metadataAuditable.invoke(ms, it, target))
                .filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Objects.isNull(this.localCache)) {
            this.localCache = LocalCacheFactory.create(properties.getProperty(PROP_KEY_CACHE_CLASS),
                properties, properties.getProperty(PROP_KEY_CACHE_CFG_PREFIX));
        }
    }
}
