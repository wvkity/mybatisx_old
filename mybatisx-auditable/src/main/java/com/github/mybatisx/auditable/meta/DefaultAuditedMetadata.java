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
package com.github.mybatisx.auditable.meta;

import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.auditable.exception.AuditedException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认审计元数据
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultAuditedMetadata implements AuditedMetadata {

    private static final long serialVersionUID = -7736471311791700429L;
    /**
     * 目标对象
     */
    private final Object target;
    /**
     * 属性
     */
    private final String property;
    /**
     * 属性类型
     */
    private final Class<?> javaType;
    /**
     * 值
     */
    private final Object value;
    /**
     * 方法调用
     */
    private final MethodInvoker invoker;
    /**
     * 标识是否已执行完成
     */
    private final AtomicBoolean completed;

    public DefaultAuditedMetadata(Object target, PropertyWrapper property, Object value) {
        this.target = target;
        this.property = property.getName();
        this.javaType = property.getJavaType();
        this.value = value;
        this.invoker = new SetterInvoker(property.getEntity(), property.getSetter());
        this.completed = new AtomicBoolean(false);
    }

    private void reset() {
        this.completed.compareAndSet(true, false);
    }

    @Override
    public void invoke() {
        if (this.completed.compareAndSet(false, true)) {
            try {
                this.invoker.invoke(this.target, this.value);
            } catch (AuditedException e) {
                this.reset();
                throw e;
            } catch (Exception e) {
                this.reset();
                throw new AuditedException(e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean isCompleted() {
        return this.completed.get();
    }

    public Object getTarget() {
        return target;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DefaultAuditedMetadata{" +
            "target=" + target +
            ", property='" + property + '\'' +
            ", javaType=" + javaType +
            ", value=" + value +
            ", invoker=" + invoker +
            ", completed=" + completed +
            '}';
    }
}
