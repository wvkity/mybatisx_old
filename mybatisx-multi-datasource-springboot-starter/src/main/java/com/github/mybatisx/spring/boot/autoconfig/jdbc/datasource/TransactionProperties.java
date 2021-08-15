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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import org.springframework.transaction.annotation.Propagation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 事务配置
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
public class TransactionProperties {

    /**
     * 切入点表达式
     */
    private String pointcutExpression;
    /**
     * 读库事务是否只读
     */
    private boolean readOnly = true;
    /**
     * 事务超时时间
     */
    private int timeout = 5000;
    /**
     * 只读事务传播特性
     */
    private Propagation readPropagation = Propagation.NOT_SUPPORTED;
    /**
     * 回滚规则
     */
    private Set<Class<?>> rollbackRules = new HashSet<>(Collections.singleton(Exception.class));
    /**
     * 只读事务方法
     */
    private Set<String> readOnlyMethods;
    /**
     * 必须事务方法
     */
    private Set<String> requireMethods;

    public TransactionProperties() {
    }

    public String getPointcutExpression() {
        return pointcutExpression;
    }

    public void setPointcutExpression(String pointcutExpression) {
        this.pointcutExpression = pointcutExpression;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Propagation getReadPropagation() {
        return readPropagation;
    }

    public void setReadPropagation(Propagation readPropagation) {
        this.readPropagation = readPropagation;
    }

    public Set<Class<?>> getRollbackRules() {
        return rollbackRules;
    }

    public void setRollbackRules(Set<Class<?>> rollbackRules) {
        this.rollbackRules = rollbackRules;
    }

    public Set<String> getReadOnlyMethods() {
        return readOnlyMethods;
    }

    public void setReadOnlyMethods(Set<String> readOnlyMethods) {
        this.readOnlyMethods = readOnlyMethods;
    }

    public Set<String> getRequireMethods() {
        return requireMethods;
    }

    public void setRequireMethods(Set<String> requireMethods) {
        this.requireMethods = requireMethods;
    }
}
