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
package com.github.mybatisx.jdbc.datasource.aop;

import com.github.mybatisx.Objects;
import com.github.mybatisx.jdbc.datasource.CurrentThreadDataSource;
import com.github.mybatisx.jdbc.datasource.DataSourceNodeType;
import com.github.mybatisx.jdbc.datasource.LocalDataSource;
import com.github.mybatisx.jdbc.datasource.MultiDataSourceContextHolder;
import com.github.mybatisx.jdbc.datasource.annotation.DataSource;
import com.github.mybatisx.jdbc.datasource.annotation.Master;
import com.github.mybatisx.jdbc.datasource.annotation.Slave;
import com.github.mybatisx.jdbc.datasource.exception.MultiDataSourceTransactionException;
import com.github.mybatisx.jdbc.datasource.resolver.ProxyClassResolver;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 读写数据源切换AOP处理器
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
@Aspect
public class MultiDataSourceAspectProcessor implements BeanPostProcessor, DataSourceDeterminingProcessor {

    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceAspectProcessor.class);
    /**
     * 数据源缓存
     */
    private static final Map<String, LocalDataSource> DATA_SOURCE_CACHE = new ConcurrentReferenceHashMap<>(256);
    /**
     * 当之前操作是写的时候，是否强制从从库读 默认（false） 当之前操作是写，默认强制从写库读
     */
    private boolean forceChoiceReadWhenWrite = false;
    /**
     * 代理类解析器
     */
    private ProxyClassResolver proxyClassResolver;
    /**
     * 只读事务方法缓存
     */
    private final Map<String, Boolean> readMethodCache = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NameMatchTransactionAttributeSource) {
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
        return bean;
    }

    @Override
    public Object determineDataSource(MethodInvocation invocation) throws Throwable {
        try {
            // 切换数据源
            final Method method = invocation.getMethod();
            final Class<?> targetClass = this.proxyClassResolver.getTargetClass(invocation);
            final DsSpec spec = new DsSpec(invocation, method, targetClass, this.readMethodCache);
            LocalDataSource dataSource = spec.get();
            final Boolean forceChoiceRead = spec.getForceChoiceRead();
            // 当前选择读库，若之前选择是写库且当前不强制切换到读库，则必须切换至写库
            if (dataSource.isRead() && Objects.nonNull(forceChoiceRead)
                && !Objects.equals(forceChoiceRead, Boolean.TRUE) && MultiDataSourceContextHolder.isChoiceWrite()) {
                log.debug("Force the primary data source selection.");
                dataSource = CurrentThreadDataSource.of(DataSourceNodeType.MASTER, dataSource.getGroup(), "");
            }
            log.debug("Switch data source: '{}'", dataSource);
            MultiDataSourceContextHolder.set(dataSource);
            return invocation.proceed();
        } finally {
            MultiDataSourceContextHolder.remove();
        }
    }

    public boolean isForceChoiceReadWhenWrite() {
        return forceChoiceReadWhenWrite;
    }

    public void setForceChoiceReadWhenWrite(boolean forceChoiceReadWhenWrite) {
        this.forceChoiceReadWhenWrite = forceChoiceReadWhenWrite;
    }

    public ProxyClassResolver getProxyClassResolver() {
        return proxyClassResolver;
    }

    public void setProxyClassResolver(ProxyClassResolver proxyClassResolver) {
        this.proxyClassResolver = proxyClassResolver;
    }

    private static class DsSpec {
        private DataSourceNodeType type;
        private String group;
        private String name;
        private final String cacheKey;
        private final Boolean forceChoiceRead;
        private LocalDataSource dataSource;

        DsSpec(MethodInvocation invocation, Method method, Class<?> targetClass,
               Map<String, Boolean> readMethodCache) {
            this.cacheKey = targetClass.getCanonicalName() + "." + method.getName();
            this.forceChoiceRead = this.isForceReadMethod(method, readMethodCache);
            LocalDataSource cache = this.readCache();
            if (Objects.nonNull(cache)) {
                this.dataSource = cache;
            } else {
                this.parse(method, targetClass);
            }
        }

        LocalDataSource get() {
            if (Objects.nonNull(this.dataSource)) {
                return this.dataSource;
            }
            // 重新从读取缓存
            this.dataSource = this.readCache();
            if (Objects.nonNull(this.dataSource)) {
                return this.dataSource;
            }
            this.dataSource = CurrentThreadDataSource.of(this.type, this.group, this.name);
            DATA_SOURCE_CACHE.putIfAbsent(this.cacheKey, this.dataSource);
            return this.dataSource;
        }

        private void parse(Method method, Class<?> targetClass) {
            DataSourceNodeType type = null;
            String group = "";
            String name = "";
            final DataSource ds = AnnotationUtils.findAnnotation(targetClass, DataSource.class);
            if (Objects.nonNull(ds)) {
                type = ds.type();
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
            return DATA_SOURCE_CACHE.get(this.cacheKey);
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
