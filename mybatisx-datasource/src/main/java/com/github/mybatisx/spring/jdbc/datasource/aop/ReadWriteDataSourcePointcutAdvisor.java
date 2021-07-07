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
package com.github.mybatisx.spring.jdbc.datasource.aop;

import com.github.mybatisx.spring.jdbc.datasource.annotation.Slave;
import com.github.mybatisx.spring.jdbc.datasource.annotation.DataSource;
import com.github.mybatisx.spring.jdbc.datasource.annotation.Master;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.StringUtils;

/**
 * @author wvkity
 * @created 2020-11-23
 * @since 1.0.0
 */
public class ReadWriteDataSourcePointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private static final long serialVersionUID = -2796890472816407703L;
    /*private static final String DEFAULT_EXECUTION_EXPRESSION =
        "%s@annotation(com.wvkity.mybatis.spring.jdbc.datasource.annotation.DataSource)" +
            "||@annotation(com.wvkity.mybatis.spring.jdbc.datasource.annotation.Master)" +
            "||@annotation(com.wvkity.mybatis.spring.jdbc.datasource.annotation.Slave)";*/
    /**
     * 切入点
     */
    private final Pointcut pointcut;
    /**
     * 通知
     */
    private final Advice advice;

    public ReadWriteDataSourcePointcutAdvisor(String executionExpression, ReadWriteDataSourceRoundAdvice roundAdvice) {
        this.advice = roundAdvice;
        this.pointcut = this.create(executionExpression);
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

    /**
     * 创建组合切入点对象
     * @param executionExpression execution表达式
     * @return {@link Pointcut}
     */
    protected Pointcut create(final String executionExpression) {
        final ComposablePointcut it = new ComposablePointcut(new AnnotationMatchingPointcut(DataSource.class));
        it.union(new AnnotationMethodMatchingPointcut(DataSource.class));
        it.union(new AnnotationMatchingPointcut(Master.class));
        it.union(new AnnotationMethodMatchingPointcut(Master.class));
        it.union(new AnnotationMatchingPointcut(Slave.class));
        it.union(new AnnotationMethodMatchingPointcut(Slave.class));
        if (StringUtils.hasText(executionExpression)) {
            final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(executionExpression);
            it.union((Pointcut) pointcut);
        }
        return it;
    }


}
