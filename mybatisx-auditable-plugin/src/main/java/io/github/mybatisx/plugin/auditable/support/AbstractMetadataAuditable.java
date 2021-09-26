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
package io.github.mybatisx.plugin.auditable.support;

import io.github.mybatisx.Objects;
import io.github.mybatisx.auditable.PropertyWrapper;
import io.github.mybatisx.auditable.meta.AuditedMetadata;
import io.github.mybatisx.auditable.meta.DefaultAuditedMetadata;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 抽象元数据审计处理
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public abstract class AbstractMetadataAuditable implements MetadataAuditable {

    @Override
    public AuditedMetadata invoke(MappedStatement ms, PropertyWrapper property, Object target) {
        if (this.canInvoke(property, target) && Objects.isNull(property.getValue(target))) {
            final Object newValue = this.getNewValue(ms, property, target);
            if (Objects.nonNull(newValue)) {
                return this.invoke(ms, property, target, null, newValue);
            }
        }
        return null;
    }

    /**
     * 审计
     * @param ms       {@link MappedStatement}
     * @param property {@link PropertyWrapper}
     * @param target   目标对象
     * @param oldValue 旧值
     * @param newValue 新值
     * @return {@link AuditedMetadata}
     */
    protected AuditedMetadata invoke(final MappedStatement ms, final PropertyWrapper property, final Object target,
                                     final Object oldValue, final Object newValue) {
        property.invoke(target, newValue);
        return new DefaultAuditedMetadata(target, property, oldValue);
    }

    /**
     * 检查是否可调用
     * @param property {@link PropertyWrapper}
     * @param target   目标对象
     * @return boolean
     */
    public boolean canInvoke(final PropertyWrapper property, final Object target) {
        return property.nonPrimaryKey() && property.canInvoke(target);
    }

    /**
     * 获取新的值
     * @param ms       {@link MappedStatement}
     * @param property {@link PropertyWrapper}
     * @param target   目标对象
     * @return 新值
     */
    abstract Object getNewValue(final MappedStatement ms, final PropertyWrapper property, final Object target);
}
