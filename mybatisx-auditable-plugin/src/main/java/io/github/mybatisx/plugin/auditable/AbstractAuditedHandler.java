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
package io.github.mybatisx.plugin.auditable;

import io.github.mybatisx.Objects;
import io.github.mybatisx.auditable.event.AuditedEvent;
import io.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import io.github.mybatisx.batch.BatchDataWrapper;
import io.github.mybatisx.cache.LocalCache;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.plugin.auditable.cache.CacheData;
import io.github.mybatisx.plugin.handler.AbstractUpdateHandler;
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
public abstract class AbstractAuditedHandler<T> extends AbstractUpdateHandler implements
    AuditedHandler<CacheData<T>> {

    /**
     * 逻辑删除方法配置属性
     */
    public static final String PROP_KEY_AUDITED_LOGIC_DELETE_METHODS = "auditedLogicDeleteMethods";
    /**
     * 事务回滚属性审计还原配置属性
     */
    public static final String PROP_KEY_AUDITED_ROLLBACK_RESTORE = "auditedRollbackRestore";
    /**
     * 缓存实现类
     */
    public static final String PROP_KEY_CACHE_CLASS = "auditedCacheClass";
    /**
     * 缓存配置项前缀
     */
    public static final String PROP_KEY_CACHE_CFG_PREFIX = "auditedCacheCfgPrefix";
    /**
     * 是否回滚还原数据
     */
    protected boolean rollbackRestore;
    /**
     * 逻辑删除方法
     */
    protected Set<String> logicDeleteMethods;
    /**
     * 本地缓存
     */
    protected LocalCache<String, CacheData<T>> localCache;
    /**
     * 事件发布器
     */
    protected AuditedEventPublisher auditedEventPublisher;

    /**
     * 检查是否为逻辑删除方法
     * @param ms {@link MappedStatement}
     * @return boolean
     */
    protected boolean isLogicDeleted(final MappedStatement ms) {
        return this.isUpdate(ms) && Objects.nonNull(this.logicDeleteMethods)
            && this.logicDeleteMethods.contains(this.execMethod(ms));
    }

    @Override
    protected Object handle(Invocation invocation, MappedStatement ms, Object parameter) throws Throwable {
        if (this.filter(ms, parameter) && this.canAudited(ms, parameter)) {
            final AuditedEvent event = this.auditedHandle(ms, parameter);
            if (Objects.nonNull(event)) {
                this.auditedEventPublisher.publishEvent(event);
            }
        }
        return invocation.proceed();
    }

    /**
     * 获取实体参数
     * @param parameter 参数
     * @return 实体参数
     */
    @SuppressWarnings("unchecked")
    protected List<Object> getSourceParameter(final Object parameter) {
        return this.getOriginalParameter(parameter, it -> {
            if (it.containsKey(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER)) {
                final Object value = it.get(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER);
                if (value instanceof BatchDataWrapper) {
                    return ((BatchDataWrapper<Object>) value).getData();
                }
            }
            return true;
        });
    }

    @Override
    public AuditedEvent auditedHandle(MappedStatement ms, Object parameter) {
        final List<Object> sources = this.getSourceParameter(parameter);
        if (Objects.isNotEmpty(sources)) {
            final boolean isInsert = this.isInsert(ms);
            final boolean isLogicDelete = this.isLogicDeleted(ms);
            return this.auditedHandle(ms, parameter, sources, isInsert, isLogicDelete);
        }
        return null;
    }

    @Override
    public CacheData<T> getCache(String cacheKey) {
        if (Objects.isNotBlank(cacheKey)) {
            return this.localCache.get(cacheKey);
        }
        return null;
    }

    @Override
    public void cache(String cacheKey, CacheData<T> data) {
        if (Objects.isNotBlank(cacheKey) && Objects.nonNull(data)) {
            this.localCache.put(cacheKey, data);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        final String rrStr;
        if (Objects.isNotBlank((rrStr = this.getProperty(PROP_KEY_AUDITED_ROLLBACK_RESTORE)))) {
            this.rollbackRestore = Objects.toBool(rrStr);
        }
        if (Objects.isEmpty(this.logicDeleteMethods)) {
            if (Objects.isNull(this.logicDeleteMethods)) {
                this.logicDeleteMethods = new HashSet<>();
            }
            final String ldmStr = this.getProperty(PROP_KEY_AUDITED_LOGIC_DELETE_METHODS);
            if (Objects.isNotBlank(ldmStr)) {
                this.logicDeleteMethods.addAll(Arrays.stream(ldmStr.split(Constants.COMMA)).filter(Objects::isNotBlank)
                    .map(String::trim).collect(Collectors.toSet()));
            }
        }
        this.logicDeleteMethods.add("logicDelete");
        this.logicDeleteMethods.add("logicDeleteByCriteria");
    }

    /**
     * 审计处理
     * @param ms            {@link MappedStatement}
     * @param parameter     参数
     * @param sources       实体参数
     * @param isInsert      是否保存操作
     * @param isLogicDelete 是否逻辑删除操作
     * @return {@link AuditedEvent}
     */
    protected abstract AuditedEvent auditedHandle(final MappedStatement ms, final Object parameter,
                                                  final List<Object> sources, final boolean isInsert,
                                                  final boolean isLogicDelete);

}
