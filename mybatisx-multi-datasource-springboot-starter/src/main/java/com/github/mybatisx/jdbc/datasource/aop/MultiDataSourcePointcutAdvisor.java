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
import com.github.mybatisx.jdbc.datasource.annotation.DataSource;
import com.github.mybatisx.jdbc.datasource.annotation.Master;
import com.github.mybatisx.jdbc.datasource.annotation.Slave;
import com.github.mybatisx.transaction.interceptor.MultiDataSourceTxInterceptor;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 读写数据源切点顾问
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
public class MultiDataSourcePointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private static final long serialVersionUID = 4306482901328440564L;

    private final Pointcut pointcut;
    private final Advice advice;

    public MultiDataSourcePointcutAdvisor(String pointcutExpression, MultiDataSourceTxInterceptor advice) {
        this.pointcut = this.buildPointcut(pointcutExpression);
        this.advice = advice;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    protected Pointcut buildPointcut(final String expression) {
        final ComposablePointcut it = new ComposablePointcut(new AnnotationMatchingPointcut(DataSource.class));
        it.union(new AnnotationMethodMatchingPointcut(DataSource.class));
        it.union(new AnnotationMatchingPointcut(Master.class));
        it.union(new AnnotationMethodMatchingPointcut(Master.class));
        it.union(new AnnotationMatchingPointcut(Slave.class));
        it.union(new AnnotationMethodMatchingPointcut(Slave.class));
        if (Objects.isNotBlank(expression)) {
            final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(expression);
            it.union((Pointcut) pointcut);
        }
        return it;
    }
}
