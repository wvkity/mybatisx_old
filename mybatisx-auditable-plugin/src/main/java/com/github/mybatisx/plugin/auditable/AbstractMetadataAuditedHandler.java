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
import com.github.mybatisx.auditable.event.AuditedListener;
import com.github.mybatisx.auditable.event.AuditedNotListener;
import com.github.mybatisx.auditable.event.DefaultAuditedEvent;
import com.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import com.github.mybatisx.batch.BatchDataWrapper;
import com.github.mybatisx.cache.LocalCache;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.event.EventPhase;
import com.github.mybatisx.plugin.auditable.cache.CacheData;
import com.github.mybatisx.plugin.handler.AbstractUpdateHandler;
import com.github.mybatisx.reflect.Reflections;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象元数据审计拦截处理器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public abstract class AbstractMetadataAuditedHandler extends AbstractUpdateHandler implements
    MetadataAuditedHandler {

    protected static final String PARAM_COLLECTION = "collection";
    protected static final String PARAM_LIST = "list";
    protected static final String PARAM_ARRAY = "array";
    protected static final String PARAM_ENTITY = "entity";
    public static final String PROP_KEY_AUDITED_INTERCEPT_METHODS = "auditedInterceptMethods";
    public static final String PROP_KEY_AUDITED_IGNORE_METHODS = "auditedIgnoreMethods";
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
     * 拦截方法列表
     */
    protected Set<String> interceptMethods;
    /**
     * 忽略方法列表
     */
    protected Set<String> ignoreMethods;
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
        if (this.filter(ms, parameter) && this.canAudited(ms, invocation.getMethod())) {
            final AuditedEvent event = this.handleParameter(ms, parameter);
            if (Objects.nonNull(event)) {
                this.auditedEventPublisher.publishEvent(event);
            }
        }
        return invocation.proceed();
    }

    /**
     * 检查是否可审计
     * @param ms     {@link MappedStatement}
     * @param method 当前执行的方法
     * @return boolean
     */
    protected boolean canAudited(final MappedStatement ms, final Method method) {
        if (this.isAnnotationPresent(ms, AuditedNotListener.class)) {
            return false;
        }
        final String execMethod = this.execMethod(ms);
        if (Objects.isNotEmpty(this.ignoreMethods) && this.ignoreMethods.contains(execMethod)) {
            return false;
        }
        return Objects.isEmpty(this.interceptMethods) || this.interceptMethods.contains(execMethod)
            || this.isAnnotationPresent(ms, AuditedListener.class);
    }


    /**
     * 处理参数
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @return {@link AuditedEvent}
     */
    protected AuditedEvent handleParameter(final MappedStatement ms, final Object parameter) {
        final List<Object> args = this.getOriginalParameter(parameter);
        if (Objects.isNotEmpty(args)) {
            final Object first = args.get(0);
            final boolean isInsert = this.isInsert(ms);
            final boolean isLogicDelete = this.logicDeleteMethods.contains(this.execMethod(ms));
            final List<PropertyWrapper> pws = this.loadProperties(ms, first, isInsert, isLogicDelete);
            if (Objects.isNotEmpty(pws)) {
                if (this.rollbackEnable) {
                    final DefaultAuditedEvent event = DefaultAuditedEvent.of(EventPhase.AFTER_ROLLBACK,
                        "metadataAuditedEvent");
                    args.forEach(it -> event.addAll(this.audited(ms, parameter, it, pws)));
                    return event;
                } else {
                    args.forEach(it -> this.audited(ms, parameter, it, pws));
                }
            }
        }
        return null;
    }

    /**
     * 获取原始参数列表
     * @param parameter 方法参数
     * @return 参数列表
     */
    @SuppressWarnings("unchecked")
    protected List<Object> getOriginalParameter(final Object parameter) {
        if (parameter instanceof Collection) {
            return this.toList((Collection<Object>) parameter);
        } else if (Objects.isArray(parameter)) {
            return this.toList(Arrays.asList((Object[]) parameter));
        } else if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (paramMap.containsKey(PARAM_COLLECTION)) {
                final Object value = paramMap.get(PARAM_COLLECTION);
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                }
            }
            if (paramMap.containsKey(PARAM_LIST)) {
                final Object value = paramMap.get(PARAM_LIST);
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                }
            }
            if (paramMap.containsKey(PARAM_ARRAY)) {
                final Object value = paramMap.get(PARAM_ARRAY);
                if (Objects.isArray(value)) {
                    return this.toList(Arrays.asList((Object[]) value));
                }
            }
            if (paramMap.containsKey(PARAM_ENTITY)) {
                final Object value = paramMap.get(PARAM_ENTITY);
                if (value != null) {
                    return this.toList(Collections.singletonList(value));
                }
            }
            if (paramMap.containsKey(Constants.PARAM_ENTITIES)) {
                final Object value = paramMap.get(Constants.PARAM_ENTITIES);
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                } else if (Objects.isArray(value)) {
                    return this.toList(Arrays.asList((Object[]) value));
                }
            }
            if (paramMap.containsKey(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER)) {
                final Object value = paramMap.get(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER);
                if (value instanceof BatchDataWrapper) {
                    return this.toList(((BatchDataWrapper<Object>) value).getData());
                }
            }
            if (paramMap.size() == 1) {
                return this.toList(paramMap.values());
            }
        } else if (Reflections.isSimpleJavaType(parameter.getClass())) {
            return this.toList(Collections.singletonList(parameter));
        }
        return null;
    }

    private List<Object> toList(final Collection<Object> values) {
        if (Objects.isNotNullElement(values)) {
            return values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
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
        if (Objects.isEmpty(this.interceptMethods)) {
            if (Objects.isNull(this.interceptMethods)) {
                this.interceptMethods = new HashSet<>();
            }
            final String aimStr = properties.getProperty(PROP_KEY_AUDITED_INTERCEPT_METHODS);
            if (Objects.isNotBlank(aimStr)) {
                this.interceptMethods.addAll(Arrays.stream(aimStr.split(Constants.COMMA)).filter(Objects::isNotBlank)
                    .map(String::trim).collect(Collectors.toSet()));
            }
        }
        if (Objects.isEmpty(this.ignoreMethods)) {
            if (Objects.isNull(this.ignoreMethods)) {
                this.ignoreMethods = new HashSet<>();
            }
            final String aimStr = properties.getProperty(PROP_KEY_AUDITED_IGNORE_METHODS);
            if (Objects.isNotBlank(aimStr)) {
                this.ignoreMethods.addAll(Arrays.stream(aimStr.split(Constants.COMMA)).filter(Objects::isNotBlank)
                    .map(String::trim).collect(Collectors.toSet()));
            }
        }
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
