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
package com.github.mybatisx.auditable.alter;

import com.github.mybatisx.auditable.AuditingException;
import com.github.mybatisx.auditable.OriginalProperty;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 审计变更数据
 * @author wvkity
 * @created 2021-03-10
 * @since 1.0.0
 */
public class DefaultAuditedAlterData implements AuditedAlterData {

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
    private final Class<?> type;
    /**
     * 值
     */
    private final Object value;
    /**
     * 方法调用
     */
    private final MethodLookup lookup;
    /**
     * 标识是否已执行完成
     */
    private final AtomicBoolean completed;

    public DefaultAuditedAlterData(Object target, OriginalProperty property, Object value) {
        this.target = target;
        this.property = property.getName();
        this.type = property.getJavaType();
        this.value = value;
        this.lookup = new SetterLookup(target.getClass(), property.getSetter());
        this.completed = new AtomicBoolean(false);
    }

    @Override
    public void invoke() {
        if (this.completed.compareAndSet(false, true)) {
            try {
                this.lookup.invoke(this.target, this.value);
            } catch (AuditingException e) {
                this.reset();
                throw e;
            } catch (Exception e) {
                this.reset();
                throw new AuditingException(e.getMessage(), e);
            }
        }
    }

    private void reset() {
        this.completed.compareAndSet(true, false);
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

    public Class<?> getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DefaultAuditedAlterData{" +
            "target=" + target +
            ", property='" + property + '\'' +
            ", type=" + type +
            ", value=" + value +
            ", lookup=" + lookup +
            ", completed=" + completed +
            '}';
    }
}
