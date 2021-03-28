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
package com.wvkity.mybatis.core.plugin.auditable.support;

import com.wvkity.mybatis.core.auditable.AuditType;
import com.wvkity.mybatis.core.auditable.OriginalProperty;
import com.wvkity.mybatis.core.auditable.time.TimeProviderProxy;
import com.wvkity.mybatis.core.auditable.time.provider.TimeProvider;

import java.util.Optional;

/**
 * 默认源数据审计处理
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class DefaultMetadataAuditable extends AbstractMetadataAuditable {

    private final AuditorAware auditorAware;

    public DefaultMetadataAuditable(AuditorAware auditorAware) {
        this.auditorAware = auditorAware;
    }

    @Override
    protected Object getNewValue(OriginalProperty property) {
        final AuditType type = property.getAuditType();
        if (type == AuditType.ID) {
            return this.auditorAware.getCurUserId();
        } else if (type == AuditType.NAME) {
            return this.auditorAware.getCurUserName();
        } else if (type == AuditType.DATE) {
            return Optional.ofNullable(TimeProviderProxy.create().target(property.getJavaType()).build())
                .map(TimeProvider::getNow).orElse(null);
        }
        return null;
    }
}
