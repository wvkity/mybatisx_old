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
package com.wvkity.mybatis.spring.jdbc.datasource.aop;

import com.wvkity.mybatis.spring.jdbc.datasource.ReadWriteDataSourceContextHolder;
import com.wvkity.mybatis.spring.jdbc.datasource.transaction.ReadWriteDataSourceTransactionException;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读写数据源AOP处理器
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
@Aspect
public class ReadWriteDataSourceAspectProcessor implements BeanPostProcessor {

    /**
     * 当之前操作是写的时候，是否强制从从库读 默认（false） 当之前操作是写，默认强制从写库读
     */
    private boolean forceChooseReadWhenWrite = false;
    /**
     * 只读操作方法缓存
     */
    private final Map<String, Boolean> readMethodCache = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NameMatchTransactionAttributeSource) {
            try {
                final NameMatchTransactionAttributeSource source = (NameMatchTransactionAttributeSource) bean;
                final Field nameMapField = ReflectionUtils.findField(NameMatchTransactionAttributeSource.class, "nameMap");
                nameMapField.setAccessible(true);
                @SuppressWarnings("unchecked") final Map<String, TransactionAttribute> nameMap =
                    (Map<String, TransactionAttribute>) nameMapField.get(source);
                for (Map.Entry<String, TransactionAttribute> entry : nameMap.entrySet()) {
                    final RuleBasedTransactionAttribute attribute = (RuleBasedTransactionAttribute) entry.getValue();
                    if (!attribute.isReadOnly()) {
                        continue;
                    }
                    final String methodName = entry.getKey();
                    Boolean isForceChooseRead = Boolean.FALSE;
                    if (this.forceChooseReadWhenWrite) {
                        // 强制从读库读取，挂起之前的事务(设置为NOT_SUPPORTED)
                        attribute.setPropagationBehavior(Propagation.NOT_SUPPORTED.value());
                        isForceChooseRead = Boolean.TRUE;
                    } else {
                        // 设置为SUPPORT
                        attribute.setPropagationBehavior(Propagation.SUPPORTS.value());
                    }
                    this.readMethodCache.put(methodName, isForceChooseRead);
                }
            } catch (Exception e) {
                throw new ReadWriteDataSourceTransactionException("process read/write transaction error", e);
            }
        }
        return bean;
    }

    /**
     * 根据方法名选择不同的数据源
     * @param methodName 方法名
     * @return 数据源
     */
    private boolean isChooseReadDataSource(final String methodName) {
        String bestMappedNameMatch = null;
        for (String mappedName : this.readMethodCache.keySet()) {
            if (isMatch(methodName, mappedName)) {
                bestMappedNameMatch = mappedName;
                break;
            }
        }
        final Boolean isForceChooseRead = this.readMethodCache.get(bestMappedNameMatch);
        // 强制使用读库
        if (Objects.equals(isForceChooseRead, Boolean.TRUE)) {
            return true;
        }
        // 如果之前选择了写，现在还是选择写
        if (ReadWriteDataSourceContextHolder.isWrite()) {
            return false;
        }
        // 如果不为null选择读，否则选择写
        return Objects.nonNull(isForceChooseRead);
    }

    protected boolean isMatch(final String methodName, final String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public void setForceChooseReadWhenWrite(boolean forceChooseReadWhenWrite) {
        this.forceChooseReadWhenWrite = forceChooseReadWhenWrite;
    }
}
