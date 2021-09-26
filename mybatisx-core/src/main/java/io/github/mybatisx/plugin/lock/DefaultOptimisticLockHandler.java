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
package io.github.mybatisx.plugin.lock;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.basic.metadata.Descriptor;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.criteria.update.UCriteria;
import io.github.mybatisx.datetime.DateTimeProviderProxy;
import io.github.mybatisx.plugin.handler.AbstractUpdateHandler;
import io.github.mybatisx.reflect.Reflections;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.helper.TableHelper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认乐观锁拦截处理器
 * @author wvkity
 * @created 2021-07-29
 * @since 1.0.0
 */
public class DefaultOptimisticLockHandler extends AbstractUpdateHandler implements OptimisticLockHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultOptimisticLockHandler.class);
    /**
     * 乐观锁方法
     */
    public static final String PROP_METHODS = "optimisticLockMethods";
    /**
     * 更新成功后自动更新到目标对象
     */
    public static final String PROP_AUTO_OVERRIDE_TARGET = "autoOverrideTarget";
    /**
     * 乐观锁方法
     */
    private final Set<String> optimisticLockMethods = new HashSet<>();
    /**
     * 更新成功后自动更新到目标对象
     */
    protected boolean autoOverrideTarget;

    public DefaultOptimisticLockHandler(boolean autoOverrideTarget, Set<String> methods) {
        this.autoOverrideTarget = autoOverrideTarget;
        if (Objects.isNotNullElement(methods)) {
            this.optimisticLockMethods.addAll(methods);
        }
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter) {
        return SqlCommandType.UPDATE == ms.getSqlCommandType() && Objects.nonNull(parameter)
            && parameter instanceof Map;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object handle(Invocation invocation, MappedStatement ms, Object parameter) throws Throwable {
        if (this.filter(ms, parameter)) {
            final String execMethod = this.execMethod(ms);
            if (this.optimisticLockMethods.contains(execMethod)) {
                final Map<String, Object> paramMap = (Map<String, Object>) parameter;
                final Criteria<?> criteria = this.getCriteriaTarget(parameter);
                final Object entity = this.getEntityTarget(parameter);
                final boolean hasEntity = Objects.nonNull(entity);
                final boolean hasCriteria = Objects.nonNull(criteria);
                if (hasEntity && hasCriteria) {
                    // 混合
                    return this.doHandleOfMixed(invocation, ms, paramMap, entity, criteria);
                } else if (hasEntity) {
                    // 只有实体对象
                    return this.doHandleOfEntityOnly(invocation, ms, paramMap, entity);
                } else {
                    // 只有条件包装对象
                    return this.doHandleOfCriteriaOnly(invocation, ms, paramMap, criteria);
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 处理混合模式
     * @param invocation {@link Invocation}
     * @param ms         {@link MappedStatement}
     * @param paramMap   参数
     * @param entity     实体参数对象
     * @return 受影响行数
     * @throws Throwable 更新过程可能出现异常
     */
    protected Object doHandleOfEntityOnly(final Invocation invocation, final MappedStatement ms,
                                          final Map<String, Object> paramMap,
                                          final Object entity) throws Throwable {
        final Optional<Column> optional = this.optimisticLock(entity.getClass());
        if (optional.isPresent()) {
            final Column it = optional.get();
            final Descriptor descriptor = it.getDescriptor();
            final Object originalValue;
            if (Objects.nonNull((originalValue = Reflections.methodInvoke(entity, descriptor.getGetter())))) {
                final Object newValue = this.newValue(originalValue, it.getJavaType());
                if (originalValue != newValue) {
                    return this.doHandle(invocation, ms, paramMap, entity, it, newValue);
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 处理混合模式
     * @param invocation {@link Invocation}
     * @param ms         {@link MappedStatement}
     * @param paramMap   参数
     * @param entity     实体参数对象
     * @param criteria   {@link Criteria}
     * @return 受影响行数
     * @throws Throwable 更新过程可能出现异常
     */
    protected Object doHandleOfMixed(final Invocation invocation, final MappedStatement ms,
                                     final Map<String, Object> paramMap,
                                     final Object entity, final Criteria<?> criteria) throws Throwable {
        final Optional<Column> optional = this.optimisticLock(entity.getClass());
        if (optional.isPresent()) {
            final Object originalValue;
            if (Objects.nonNull((originalValue = criteria.getVersionConditionValue()))) {
                final Column it = optional.get();
                final Descriptor descriptor = it.getDescriptor();
                Object curValue;
                try {
                    curValue = Reflections.methodInvoke(entity, descriptor.getGetter());
                } catch (Exception ignore) {
                    curValue = Boolean.FALSE;
                }
                if (Objects.isNull(curValue)) {
                    final Object newValue = this.newValue(originalValue, it.getJavaType());
                    if (originalValue != newValue) {
                        return this.doHandle(invocation, ms, paramMap, entity, it, newValue);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 处理混合模式
     * @param invocation {@link Invocation}
     * @param ms         {@link MappedStatement}
     * @param paramMap   参数
     * @param entity     实体参数对象
     * @param it         {@link Column}
     * @param newValue   新的值
     * @return 受影响行数
     * @throws Throwable 更新过程可能出现异常
     */
    protected Object doHandle(final Invocation invocation, final MappedStatement ms,
                              final Map<String, Object> paramMap, final Object entity,
                              final Column it, final Object newValue) throws Throwable {
        paramMap.put(Constants.PARAM_OPTIMISTIC_LOCK_KEY, newValue);
        try {
            final Object result = invocation.proceed();
            if (result instanceof Integer) {
                if (((Integer) result != 0) && this.autoOverrideTarget) {
                    this.overrideValue(entity, it.getDescriptor().getSetter(), newValue);
                }
            }
            return result;
        } finally {
            paramMap.remove(Constants.PARAM_OPTIMISTIC_LOCK_KEY);
        }
    }

    /**
     * 处理只有{@link Criteria}对象
     * @param invocation {@link Invocation}
     * @param ms         {@link MappedStatement}
     * @param paramMap   参数
     * @param criteria   {@link Criteria}
     * @return 受影响行数
     * @throws Throwable 更新过程可能出现异常
     */
    protected Object doHandleOfCriteriaOnly(final Invocation invocation, final MappedStatement ms,
                                            final Map<String, Object> paramMap,
                                            final Criteria<?> criteria) throws Throwable {
        if (criteria instanceof UCriteria) {
            final Optional<Column> optional = this.optimisticLock(criteria.getEntityClass());
            if (optional.isPresent()) {
                final UCriteria<?, ?> update = (UCriteria<?, ?>) criteria;
                // 存在乐观锁条件，且更新值为null
                final Object originalValue;
                if (Objects.nonNull((originalValue = update.getVersionConditionValue()))
                    && Objects.isNull(update.getVersionUpdateValue())) {
                    final Column column = optional.get();
                    final Object newValue = this.newValue(originalValue, column.getJavaType());
                    if (newValue != originalValue) {
                        update.setVersion(newValue);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 创建新乐观锁值
     * @param originalValue 原始值
     * @param javaType      JAVA类型
     * @return 新的值
     */
    protected Object newValue(final Object originalValue, final Class<?> javaType) {
        if (long.class.equals(javaType) || Long.class.equals(javaType)) {
            return (long) originalValue + 1;
        } else if (int.class.equals(javaType) || Integer.class.equals(javaType)) {
            return (int) originalValue + 1;
        } else {
            final Object newValue = DateTimeProviderProxy.getNow(javaType);
            if (Objects.nonNull(newValue)) {
                return newValue;
            }
        }
        return originalValue;
    }

    /**
     * 获取乐观锁
     * @param clazz 实体类
     * @return 乐观锁
     */
    protected Optional<Column> optimisticLock(final Class<?> clazz) {
        return TableHelper.getTable(clazz).optimisticLockOptional();
    }

    /**
     * 获取{@link Criteria}对象
     * @param parameter 参数
     * @return {@link Criteria}对象
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Criteria<?> getCriteriaTarget(final Object parameter) {
        if (parameter instanceof Criteria) {
            return (Criteria<?>) parameter;
        } else if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (Objects.isNotEmpty(paramMap)) {
                if (paramMap.containsKey(Constants.PARAM_CRITERIA)) {
                    final Object value = paramMap.get(Constants.PARAM_CRITERIA);
                    if (value instanceof Criteria) {
                        return (Criteria<?>) value;
                    }
                }
                for (Object value : paramMap.values()) {
                    if (value instanceof Criteria) {
                        return (Criteria<?>) value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取实体对象
     * @param parameter 参数
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    protected Object getEntityTarget(final Object parameter) {
        if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (Objects.isNotEmpty(paramMap)) {
                if (paramMap.containsKey(Constants.PARAM_ENTITY)) {
                    final Object value = paramMap.get(Constants.PARAM_ENTITY);
                    if (!Reflections.isSimpleJavaObject(value) && !(value instanceof Criteria)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 覆盖值
     * @param target 目标对象
     * @param setter setter方法
     * @param value  值
     */
    protected void overrideValue(final Object target, final Method setter, final Object value) {
        try {
            Reflections.methodInvoke(target, setter, value);
        } catch (Exception ignore) {
            // ignore
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.optimisticLockMethods.add("update");
        this.optimisticLockMethods.add("updateWithoutNull");
        this.optimisticLockMethods.add("updateByCriteria");
        this.optimisticLockMethods.add("updateMixed");
        final String methods = this.getProperty(PROP_METHODS);
        if (Objects.isNotBlank(methods)) {
            this.optimisticLockMethods.addAll(Arrays.stream(methods.split(Constants.COMMA))
                .filter(Objects::isNotBlank).map(String::trim).collect(Collectors.toSet()));
        }
        final String overrideTargetStr = this.getProperty(PROP_AUTO_OVERRIDE_TARGET);
        if (Objects.isNotBlank(overrideTargetStr)) {
            this.autoOverrideTarget = Objects.toBool(overrideTargetStr);
        }
    }
}
