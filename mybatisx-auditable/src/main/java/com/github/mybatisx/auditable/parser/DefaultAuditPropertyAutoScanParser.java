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
package com.github.mybatisx.auditable.parser;

import com.github.mybatisx.auditable.AuditPolicy;
import com.github.mybatisx.auditable.AuditType;
import com.github.mybatisx.auditable.DefaultAutomaticAuditPropertyCache;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.auditable.annotation.Created;
import com.github.mybatisx.auditable.annotation.CreatedById;
import com.github.mybatisx.auditable.annotation.CreatedByName;
import com.github.mybatisx.auditable.annotation.CreatedDate;
import com.github.mybatisx.auditable.annotation.Deleted;
import com.github.mybatisx.auditable.annotation.DeletedById;
import com.github.mybatisx.auditable.annotation.DeletedByName;
import com.github.mybatisx.auditable.annotation.DeletedDate;
import com.github.mybatisx.auditable.annotation.LastModifiedById;
import com.github.mybatisx.auditable.annotation.LastModifiedByName;
import com.github.mybatisx.auditable.annotation.LastModifiedDate;
import com.github.mybatisx.auditable.annotation.Modified;
import com.github.mybatisx.auditable.annotation.Remark;
import com.github.mybatisx.auditable.annotation.Time;
import com.github.mybatisx.auditable.annotation.Unique;
import com.github.mybatisx.auditable.matcher.AuditMatcher;
import com.github.mybatisx.auditable.matcher.DefaultAuditMatcher;
import com.github.mybatisx.auditable.matcher.DefaultNonAuditMatcher;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * 默认审计属性自动识别解析器
 * @author wvkity
 * @created 2021-07-14
 * @since 1.0.0
 */
public class DefaultAuditPropertyAutoScanParser implements AuditPropertyAutoScanParser {

    @Override
    public AuditMatcher parse(PropertyWrapper property) {
        if (property != null && property.nonPrimaryKey()) {
            final String prop = property.getName();
            if (DefaultAutomaticAuditPropertyCache.contains(prop)) {
                final Class<? extends Annotation> target = DefaultAutomaticAuditPropertyCache.get(prop);
                if (target != null) {
                    final DefaultAuditMatcher.Builder builder = DefaultAuditMatcher.Builder.create().matches(true)
                        .entity(property.getEntity()).property(prop);
                    if (target.isAnnotationPresent(Unique.class)) {
                        builder.auditType(AuditType.ID)
                            .createdById(Objects.equals(target, CreatedById.class))
                            .lastModifiedById(Objects.equals(target, LastModifiedById.class))
                            .deletedById(Objects.equals(target, DeletedById.class));
                    }
                    if (target.isAnnotationPresent(Remark.class)) {
                        builder.auditType(AuditType.NAME)
                            .createdByName(Objects.equals(target, CreatedByName.class))
                            .lastModifiedByName(Objects.equals(target, LastModifiedByName.class))
                            .deletedByName(Objects.equals(target, DeletedByName.class));
                    }
                    if (target.isAnnotationPresent(Time.class)) {
                        builder.auditType(AuditType.TIME)
                            .createdDate(Objects.equals(target, CreatedDate.class))
                            .lastModifiedDate(Objects.equals(target, LastModifiedDate.class))
                            .deletedDate(Objects.equals(target, DeletedDate.class));
                    }
                    return builder.policy(AuditPolicy.get(target.getAnnotation(Created.class)))
                        .policy(AuditPolicy.get(target.getAnnotation(Modified.class)))
                        .policy(AuditPolicy.get(target.getAnnotation(Deleted.class))).build();
                }
            }
        }
        return new DefaultNonAuditMatcher();
    }
}
