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
package com.github.mybatisx.spring.jdbc.datasource.transaction.interceptor;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 读写事务拦截器
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
public class ReadWriteTxAdviceInterceptor {

    /**
     * 超时时间
     */
    private int execMethodTimeout = 3000;

    /**
     * 切入点表达式
     */
    private final String pointcutExpression;

    /**
     * 事务管理器
     */
    private final TransactionManager transactionManager;

    public ReadWriteTxAdviceInterceptor(String pointcutExpression, TransactionManager transactionManager) {
        this.pointcutExpression = pointcutExpression;
        this.transactionManager = transactionManager;
    }

    /**
     * 创建事务拦截器
     * @return {@link TransactionInterceptor}
     */
    public TransactionInterceptor createTxAdvice() {
        final NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        final Map<String, TransactionAttribute> nameMap = new HashMap<>();
        // 只读事务，不做更新操作
        final  RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        nameMap.put("get*", readOnlyTx);
        nameMap.put("query*", readOnlyTx);
        nameMap.put("select*", readOnlyTx);
        nameMap.put("find*", readOnlyTx);
        nameMap.put("exists*", readOnlyTx);
        nameMap.put("list*", readOnlyTx);
        nameMap.put("translate*", readOnlyTx);
        nameMap.put("count*", readOnlyTx);
        // 当前存在事务就使用当前事务，否则创建一个新的事务
        final RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(this.execMethodTimeout);
        nameMap.put("save*", requiredTx);
        nameMap.put("add*", requiredTx);
        nameMap.put("create*", requiredTx);
        nameMap.put("insert*", requiredTx);
        nameMap.put("update*", requiredTx);
        nameMap.put("modify*", requiredTx);
        nameMap.put("delete*", requiredTx);
        nameMap.put("del*", requiredTx);
        nameMap.put("remove*", requiredTx);
        nameMap.put("drop*", requiredTx);
        nameMap.put("merge*", requiredTx);
        nameMap.put("put*", requiredTx);
        nameMap.put("sync*", requiredTx);
        nameMap.put("*", requiredTx);
        source.setNameMap(nameMap);
        return new TransactionInterceptor(this.transactionManager, source);
    }

    public Advisor createTxAdvisor(final TransactionInterceptor txAdvice) {
        final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(this.pointcutExpression);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }

    public int getExecMethodTimeout() {
        return execMethodTimeout;
    }

    public void setExecMethodTimeout(int execMethodTimeout) {
        this.execMethodTimeout = execMethodTimeout;
    }

    public String getPointcutExpression() {
        return pointcutExpression;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}
