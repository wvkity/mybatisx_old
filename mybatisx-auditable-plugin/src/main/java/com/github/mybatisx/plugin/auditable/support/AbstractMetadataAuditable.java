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
package com.github.mybatisx.plugin.auditable.support;

import com.github.mybatisx.auditable.OriginalProperty;
import com.github.mybatisx.auditable.alter.AuditedAlterData;
import com.github.mybatisx.auditable.alter.DefaultAuditedAlterData;

import java.lang.reflect.ReflectPermission;

/**
 * 抽象源数据审计处理
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public abstract class AbstractMetadataAuditable implements MetadataAuditable {

    @Override
    public AuditedAlterData invoke(Object target, OriginalProperty property) {
        if (this.canInjectValue(target, property)) {
            final Object newValue = this.getNewValue(property);
            if (newValue != null) {
                return this.invoke(target, property, property.getValue(target), newValue);
            }
        }
        return null;
    }

    /**
     * 检查是否可审计
     * @param target   目标对象
     * @param property {@link OriginalProperty}
     * @return boolean
     */
    protected boolean canInjectValue(final Object target, final OriginalProperty property) {
        return !property.isPrimaryKey() && property.canInvoke(target);
    }


    /**
     * 注入值
     * @param target   目标对象
     * @param property {@link OriginalProperty}
     * @param oldValue 旧的值
     * @param newValue 新的值
     * @return {@link AuditedAlterData}
     */
    protected AuditedAlterData invoke(final Object target, final OriginalProperty property,
                                      final Object oldValue, final Object newValue) {
        property.invoke(target, newValue);
        return new DefaultAuditedAlterData(target, property, oldValue);
    }

    protected boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException ignore) {
            return false;
        }
        return true;
    }

    /**
     * 获取新的值
     * @param property {@link OriginalProperty}
     * @return 新的值
     */
    protected abstract Object getNewValue(final OriginalProperty property);

}
