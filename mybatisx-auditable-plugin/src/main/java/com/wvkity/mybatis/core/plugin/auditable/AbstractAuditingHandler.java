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

import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;
import com.wvkity.mybatis.core.auditable.event.AuditedEventPublisher;
import com.wvkity.mybatis.core.auditable.event.DefaultAuditedEvent;
import com.wvkity.mybatis.core.batch.BatchDataWrapper;
import com.wvkity.mybatis.core.cache.LocalCache;
import com.wvkity.mybatis.core.plugin.auditable.cache.CacheData;
import com.wvkity.mybatis.core.plugin.handler.AbstractUpdateHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象审计处理器
 * @author wvkity
 * @created 2021-03-05
 * @since 1.0.0
 */
public abstract class AbstractAuditingHandler<T> extends AbstractUpdateHandler implements AuditingHandler<CacheData<T>> {

    protected static final String PARAM_COLLECTION = "collection";
    protected static final String PARAM_LIST = "list";
    protected static final String PARAM_ARRAY = "array";
    protected static final String PARAM_ENTITY = "entity";
    protected static final String DEF_MA_CACHE_SUFFIX = "metadata_auditing";
    protected static final String DEF_SA_CACHE_SUFFIX = "system_auditing";

    protected boolean rollback;
    protected Set<String> interceptMethods;
    protected Set<String> filterMethods;
    protected Set<String> logicDeleteMethods;
    protected LocalCache<String, CacheData<T>> localCache;
    protected AuditedEventPublisher<List<AuditedAlterData>> eventPublisher;

    @Override
    protected Object handle(Invocation invocation, MappedStatement ms, Object parameter) throws Throwable {
        if (this.filter(ms, parameter)) {
            final String execMethod = this.execMethod(ms);
            if ((this.interceptMethods.isEmpty() || this.interceptMethods.contains(execMethod))
                && (this.filterMethods.isEmpty() || !this.filterMethods.contains(execMethod))) {
                final Object newArg = this.handleParameter(ms, parameter);
                if (newArg != null && newArg != parameter) {
                    invocation.getArgs()[1] = newArg;
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 处理参数
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @return 处理后的参数
     */
    protected Object handleParameter(final MappedStatement ms, final Object parameter) {
        final Collection<Object> originalParams = this.getOriginalParameter(parameter);
        final boolean isInsert = ms.getSqlCommandType() == SqlCommandType.INSERT;
        if (originalParams != null && !originalParams.isEmpty()) {
            final boolean isLogicDelete = this.logicDeleteMethods.contains(this.execMethod(ms));
            for (Object target : originalParams) {
                final List<AuditedAlterData> alterData = this.invoke(ms, parameter, target, isInsert, isLogicDelete);
                if (this.rollback && this.eventPublisher != null && alterData != null && !alterData.isEmpty()) {
                    this.eventPublisher.publishEvent(DefaultAuditedEvent.rollback(alterData, null));
                }
            }
        }
        return parameter;
    }

    /**
     * 获取原始参数列表
     * @param parameter 方法参数
     * @return 参数列表
     */
    @SuppressWarnings("unchecked")
    protected Collection<Object> getOriginalParameter(final Object parameter) {
        if (parameter instanceof Collection) {
            return (Collection<Object>) parameter;
        } else if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (paramMap.containsKey(PARAM_COLLECTION)) {
                final Object value = paramMap.get(PARAM_COLLECTION);
                if (value instanceof Collection) {
                    return (Collection<Object>) value;
                }
            }
            if (paramMap.containsKey(PARAM_LIST)) {
                final Object value = paramMap.get(PARAM_LIST);
                if (value instanceof Collection) {
                    return (Collection<Object>) value;
                }
            }
            if (paramMap.containsKey(PARAM_ARRAY)) {
                final Object value = paramMap.get(PARAM_ARRAY);
                if (value != null && value.getClass().isArray()) {
                    return new ArrayList<>(Arrays.asList((Object[]) value));
                }
            }
            if (paramMap.containsKey(PARAM_ENTITY)) {
                final Object value = paramMap.get(PARAM_ENTITY);
                if (value != null) {
                    return new ArrayList<>(Collections.singletonList(value));
                }
            }
            if (paramMap.containsKey(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER)) {
                final Object value = paramMap.get(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER);
                if (value instanceof BatchDataWrapper) {
                    return (Collection<Object>) ((BatchDataWrapper<?>) value).getData();
                }
            }
            if (paramMap.size() == 1) {
                return paramMap.values();
            }
        }
        return null;
    }

    @Override
    public CacheData<T> getCache(String cacheKey) {
        return this.isNotBlank(cacheKey) ? this.localCache.get(cacheKey) : null;
    }

    @Override
    public void cache(String cacheKey, CacheData<T> data) {
        if (this.isNotBlank(cacheKey) && data != null) {
            this.localCache.put(cacheKey, data);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.rollback = Optional.ofNullable(this.properties.getProperty(PROP_KEY_ROLLBACK))
            .map("true"::equals).orElse(false);
        final String interceptMethodStr;
        if (this.isNotBlank((interceptMethodStr = properties.getProperty(PROP_KEY_AUDITING_INTERCEPT_METHODS)))) {
            this.interceptMethods = Collections.unmodifiableSet(Arrays.stream(interceptMethodStr.split(","))
                .filter(this::isNotBlank).map(String::trim).collect(Collectors.toSet()));
        } else {
            this.interceptMethods = Collections.unmodifiableSet(new HashSet<>(0));
        }
        final String filterMethodStr;
        if (this.isNotBlank((filterMethodStr = properties.getProperty(PROP_KEY_AUDITING_FILTER_METHODS)))) {
            this.filterMethods = Collections.unmodifiableSet(Arrays.stream(filterMethodStr.split(","))
                .filter(this::isNotBlank).map(String::trim).collect(Collectors.toSet()));
        } else {
            this.filterMethods = Collections.unmodifiableSet(new HashSet<>(0));
        }
        final Set<String> logicDelMethods = new HashSet<>();
        logicDelMethods.add("logicDelete");
        logicDelMethods.add("logicDeleteByCriteria");
        final String logicDeleteMethodStr;
        if (this.isNotBlank((logicDeleteMethodStr = properties.getProperty(PROP_KEY_AUDITING_LOGIC_DELETE_METHODS)))) {
            logicDelMethods.addAll(Arrays.stream(logicDeleteMethodStr.split(","))
                .filter(this::isNotBlank).map(String::trim).collect(Collectors.toSet()));
        }
        this.logicDeleteMethods = Collections.unmodifiableSet(logicDelMethods);
    }

    protected boolean isNotBlank(final String value) {
        return value != null && !value.trim().isEmpty();
    }

}
