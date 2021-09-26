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
import io.github.mybatisx.jdbc.datasource.CurrentThreadDataSource;
import io.github.mybatisx.jdbc.datasource.DataSourceNodeType;
import io.github.mybatisx.jdbc.datasource.LocalDataSource;
import io.github.mybatisx.jdbc.datasource.MultiDataSourceContextHolder;
import io.github.mybatisx.jdbc.datasource.resolver.ProxyClassResolver;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 读写数据源拦截器
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
@Aspect
public class MultiDataSourceAdvice implements MethodBeforeAdvice, MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceAdvice.class);

    /**
     * 代理类解析器
     */
    protected ProxyClassResolver proxyClassResolver;
    /**
     * 切面资源
     */
    protected AspectResource aspectResource;
    /**
     * 只读事务方法缓存
     */
    protected final Map<String, Boolean> readMethodCache;

    public MultiDataSourceAdvice(ProxyClassResolver proxyClassResolver, AspectResource aspectResource) {
        this.proxyClassResolver = proxyClassResolver;
        this.aspectResource = aspectResource;
        this.readMethodCache = aspectResource.getReadMethodCache();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            MultiDataSourceContextHolder.remove();
        }
    }


    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        final Class<?> targetClass = this.proxyClassResolver.getTargetClass(target);
        final MultiDataSourceAspectResource.ResourceSpec spec =
            new MultiDataSourceAspectResource.ResourceSpec(method, targetClass, this.readMethodCache);
        LocalDataSource dataSource = spec.get();
        final Boolean forceChoiceRead = spec.getForceChoiceRead();
        // 当前选择读库，若之前选择是写库且当前不强制切换到读库，则必须切换至写库
        if (dataSource.isRead() && Objects.nonNull(forceChoiceRead)
            && !Objects.equals(forceChoiceRead, Boolean.TRUE) && MultiDataSourceContextHolder.isChoiceWrite()) {
            log.debug("Force the primary data source selection.");
            dataSource = CurrentThreadDataSource.of(DataSourceNodeType.MASTER, dataSource.getGroup(),
                dataSource.getName());
        }
        MultiDataSourceContextHolder.push(dataSource);
    }
}
