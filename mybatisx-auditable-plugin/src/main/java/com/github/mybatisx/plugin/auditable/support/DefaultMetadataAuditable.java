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
package com.github.mybatisx.plugin.auditable.support;

import com.github.mybatisx.Objects;
import com.github.mybatisx.auditable.AuditType;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.datetime.DateTimeProviderProxy;
import com.github.mybatisx.datetime.provider.DateTimeProvider;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Optional;

/**
 * 默认元数据审计处理
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultMetadataAuditable extends AbstractMetadataAuditable {

    protected final AuditorAware auditorAware;

    public DefaultMetadataAuditable(AuditorAware auditorAware) {
        this.auditorAware = auditorAware;
    }

    @Override
    Object getNewValue(MappedStatement ms, PropertyWrapper property, Object target) {
        final AuditType type = property.getAuditType();
        if (Objects.nonNull(type) && Objects.nonNull(this.auditorAware)) {
            switch (type) {
                case ID:
                    return this.auditorAware.getOptUserId().orElse(null);
                case NAME:
                    return this.auditorAware.getOptUserName().orElse(null);
                case TIME:
                    return Optional.ofNullable(DateTimeProviderProxy.create().target(property.getJavaType()).build())
                        .map(DateTimeProvider::getNow).orElse(null);
                default:
                    return null;
            }
        }
        return null;
    }
}
