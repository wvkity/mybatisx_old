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
import com.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import com.github.mybatisx.cache.LocalCache;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.plugin.auditable.cache.CacheData;
import com.github.mybatisx.plugin.handler.AbstractUpdateHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象审计拦截处理器
 * @author wvkity
 * @created 2021-07-21
 * @since 1.0.0
 */
public abstract class AbstractAuditedHandler extends AbstractUpdateHandler implements
    AuditedHandler<CacheData<List<PropertyWrapper>>> {

    public static final String PROP_KEY_AUDITED_LOGIC_DELETE_METHODS = "auditedLogicDeleteMethods";
    /**
     * 是否回滚
     */
    protected boolean rollbackEnable;
    /**
     * 是否启用识别拦截注解
     */
    protected boolean annotationEnable;
    /**
     * 逻辑删除方法
     */
    protected Set<String> logicDeleteMethods;
    /**
     * 本地缓存
     */
    protected LocalCache<String, CacheData<List<PropertyWrapper>>> localCache;
    /**
     * 事件发布器
     */
    protected AuditedEventPublisher auditedEventPublisher;

    @Override
    protected Object handle(Invocation invocation, MappedStatement ms, Object parameter) throws Throwable {
        if (this.filter(ms, parameter) && this.canAudited(ms)) {
            final AuditedEvent event = this.auditedHandle(ms, parameter);
            if (Objects.nonNull(event)) {
                this.auditedEventPublisher.publishEvent(event);
            }
        }
        return invocation.proceed();
    }

    @Override
    public CacheData<List<PropertyWrapper>> getCache(String cacheKey) {
        if (Objects.isNotBlank(cacheKey)) {
            return this.localCache.get(cacheKey);
        }
        return null;
    }

    @Override
    public void cache(String cacheKey, CacheData<List<PropertyWrapper>> data) {
        if (Objects.isNotBlank(cacheKey) && Objects.nonNull(data)) {
            this.localCache.put(cacheKey, data);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Objects.isEmpty(this.logicDeleteMethods)) {
            if (Objects.isNull(this.logicDeleteMethods)) {
                this.logicDeleteMethods = new HashSet<>();
            }
            final String ldmStr = properties.getProperty(PROP_KEY_AUDITED_LOGIC_DELETE_METHODS);
            if (Objects.isNotBlank(ldmStr)) {
                this.logicDeleteMethods.addAll(Arrays.stream(ldmStr.split(Constants.COMMA)).filter(Objects::isNotBlank)
                    .map(String::trim).collect(Collectors.toSet()));
            }
        }
        this.logicDeleteMethods.add("logicDelete");
        this.logicDeleteMethods.add("logicDeleteByCriteria");
    }

}
