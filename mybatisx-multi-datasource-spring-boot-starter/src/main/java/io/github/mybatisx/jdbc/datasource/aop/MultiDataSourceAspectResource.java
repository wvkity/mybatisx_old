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
package io.github.mybatisx.jdbc.datasource.aop;

import io.github.mybatisx.Objects;
import io.github.mybatisx.immutable.ImmutableMap;
import io.github.mybatisx.jdbc.datasource.CurrentThreadDataSource;
import io.github.mybatisx.jdbc.datasource.DataSourceNodeType;
import io.github.mybatisx.jdbc.datasource.LocalDataSource;
import io.github.mybatisx.jdbc.datasource.annotation.DataSource;
import io.github.mybatisx.jdbc.datasource.annotation.Master;
import io.github.mybatisx.jdbc.datasource.annotation.Slave;
import io.github.mybatisx.jdbc.datasource.exception.MultiDataSourceTransactionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读写数据源资源
 * @author wvkity
 * @created 2021-08-17
 * @since 1.0.0
 */
public class MultiDataSourceAspectResource implements BeanPostProcessor, AspectResource {

    /**
     * 方法自身数据源缓存
     */
    protected static final Map<String, LocalDataSource> SELF_DATA_SOURCE_CACHE = new ConcurrentReferenceHashMap<>(256);
    /**
     * 当之前操作是写的时候，是否强制从从库读 默认（false） 当之前操作是写，默认强制从写库读
     */
    protected boolean forceChoiceReadWhenWrite = false;
    /**
     * 只读事务方法缓存
     */
    protected final Map<String, Boolean> readMethodCache = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NameMatchTransactionAttributeSource) {
            this.parseResource(bean);
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    protected void parseResource(final Object bean) {
        NameMatchTransactionAttributeSource tas = (NameMatchTransactionAttributeSource) bean;
        try {
            final Field nameMapField =
                ReflectionUtils.findField(NameMatchTransactionAttributeSource.class, "nameMap");
            Objects.requireNonNull(nameMapField, "The nameMap attribute cannot be null.");
            nameMapField.setAccessible(true);
            final Map<String, TransactionAttribute> nameMap =
                (Map<String, TransactionAttribute>) nameMapField.get(tas);
            if (Objects.isNotEmpty(nameMap)) {
                for (Map.Entry<String, TransactionAttribute> entry : nameMap.entrySet()) {
                    final RuleBasedTransactionAttribute rbt = (RuleBasedTransactionAttribute) entry.getValue();
                    if (!rbt.isReadOnly()) {
                        continue;
                    }
                    final String methodName = entry.getKey();
                    Boolean isForceChoiceRead = Boolean.FALSE;
                    if (this.forceChoiceReadWhenWrite) {
                        // 强制从读库读取，挂起之前的事务
                        rbt.setPropagationBehavior(Propagation.NOT_SUPPORTED.value());
                        isForceChoiceRead = Boolean.TRUE;
                    } else {
                        // 切换到写事务
                        rbt.setPropagationBehavior(Propagation.SUPPORTS.value());
                    }
                    readMethodCache.put(methodName, isForceChoiceRead);
                }
            }
        } catch (Exception e) {
            throw new MultiDataSourceTransactionException("Failed to process dynamic read/write transaction " +
                "data source", e);
        }
    }

    public boolean isForceChoiceReadWhenWrite() {
        return forceChoiceReadWhenWrite;
    }

    @Override
    public void setForceChoiceReadWhenWrite(boolean forceChoiceReadWhenWrite) {
        this.forceChoiceReadWhenWrite = forceChoiceReadWhenWrite;
    }

    @Override
    public Map<String, Boolean> getReadMethodCache() {
        return ImmutableMap.of(this.readMethodCache);
    }

    public static class ResourceSpec {
        private DataSourceNodeType type;
        private String group;
        private String name;
        private final String cacheKey;
        private final Boolean forceChoiceRead;
        private LocalDataSource dataSource;

        ResourceSpec(Method method, Class<?> targetClass, Map<String, Boolean> readMethodCache) {
            this.cacheKey = targetClass.getCanonicalName() + "." + method.getName();
            this.forceChoiceRead = this.isForceReadMethod(method, readMethodCache);
            LocalDataSource cache = this.readCache();
            if (Objects.nonNull(cache)) {
                this.dataSource = cache;
            } else {
                this.parse(method, targetClass);
            }
        }

        public LocalDataSource get() {
            if (Objects.nonNull(this.dataSource)) {
                return this.dataSource;
            }
            // 重新从读取缓存
            this.dataSource = this.readCache();
            if (Objects.nonNull(this.dataSource)) {
                return this.dataSource;
            }
            this.dataSource = CurrentThreadDataSource.of(this.type, this.group, this.name);
            SELF_DATA_SOURCE_CACHE.putIfAbsent(this.cacheKey, this.dataSource);
            return this.dataSource;
        }

        private void parse(Method method, Class<?> targetClass) {
            DataSourceNodeType type = null;
            String group = "";
            String name = "";
            final DataSource ds = AnnotationUtils.findAnnotation(targetClass, DataSource.class);
            if (Objects.nonNull(ds)) {
                if (ds.type() != DataSourceNodeType.UNKNOWN) {
                    type = ds.type();
                }
                group = ds.group();
                name = ds.value();
            }
            final Master master = AnnotationUtils.findAnnotation(method, Master.class);
            if (Objects.nonNull(master)) {
                type = DataSourceNodeType.MASTER;
                if (Objects.isNotBlank(master.group())) {
                    group = master.group();
                }
                if (Objects.isNotBlank(master.value())) {
                    name = master.value();
                }
            } else {
                final Slave slave = AnnotationUtils.findAnnotation(method, Slave.class);
                if (Objects.nonNull(slave)) {
                    type = DataSourceNodeType.SLAVE;
                    if (Objects.isNotBlank(slave.group())) {
                        group = slave.group();
                    }
                    if (Objects.isNotBlank(slave.value())) {
                        name = slave.value();
                    }
                }
            }
            if (Objects.isNull(type)) {
                final Transactional it = AnnotationUtils.findAnnotation(method, Transactional.class);
                if (Objects.nonNull(it)) {
                    if (it.readOnly()) {
                        type = DataSourceNodeType.SLAVE;
                    } else {
                        type = DataSourceNodeType.MASTER;
                    }
                }
            }
            if (Objects.isNull(type)) {
                if (Objects.equals(this.forceChoiceRead, Boolean.TRUE)) {
                    type = DataSourceNodeType.SLAVE;
                } else {
                    type = DataSourceNodeType.MASTER;
                }
            }
            this.type = type;
            this.group = group;
            this.name = name;
        }

        private LocalDataSource readCache() {
            return SELF_DATA_SOURCE_CACHE.get(this.cacheKey);
        }

        Boolean getForceChoiceRead() {
            return forceChoiceRead;
        }

        private Boolean isForceReadMethod(final Method method, final Map<String, Boolean> readMethodCache) {
            final String methodName = method.getName();
            String bestNameMatch = null;
            for (String matcher : readMethodCache.keySet()) {
                if (this.matches(methodName, matcher)) {
                    bestNameMatch = matcher;
                    break;
                }
            }
            return Objects.nonNull(bestNameMatch) ? readMethodCache.get(bestNameMatch) : null;
        }

        private boolean matches(final String methodName, final String methodNameMatcher) {
            return PatternMatchUtils.simpleMatch(methodNameMatcher, methodName);
        }
    }
}
