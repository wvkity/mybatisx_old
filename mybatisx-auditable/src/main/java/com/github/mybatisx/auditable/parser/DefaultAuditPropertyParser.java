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
import com.github.mybatisx.auditable.annotation.NotAudit;
import com.github.mybatisx.auditable.annotation.Remark;
import com.github.mybatisx.auditable.annotation.Time;
import com.github.mybatisx.auditable.annotation.Unique;
import com.github.mybatisx.auditable.matcher.AuditMatcher;
import com.github.mybatisx.auditable.matcher.DefaultAuditMatcher;
import com.github.mybatisx.auditable.matcher.DefaultNonAuditMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

/**
 * 默认审计属性解析器
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public class DefaultAuditPropertyParser implements AuditPropertyParser {

    /**
     * 不存在注解的情况下，是否开启自动扫描
     */
    private final boolean autoScan;
    private final AuditPropertyAutoScanParser parser;

    public DefaultAuditPropertyParser(boolean autoScan, AuditPropertyAutoScanParser parser) {
        this.autoScan = autoScan;
        this.parser = parser;
    }

    @Override
    public AuditMatcher parse(PropertyWrapper property) {
        // 主键禁止审计
        if (property.nonPrimaryKey()) {
            final Set<? extends Annotation> annotations = property.getAnnotations();
            if (annotations != null && !annotations.isEmpty()) {
                final AuditAntMetadata aam = new AuditAntMetadata(annotations);
                if (aam.isAnnotationPresent(NotAudit.class)) {
                    return new DefaultNonAuditMatcher();
                }
                final DefaultAuditMatcher.Builder builder = DefaultAuditMatcher.Builder.create()
                    .matches(true).entity(property.getEntity()).property(property.getName());
                final boolean hasUnique = aam.isAnnotationPresent(Unique.class);
                if (hasUnique) {
                    builder.auditType(AuditType.ID)
                        .createdById(aam.isAnnotationPresent(CreatedById.class))
                        .lastModifiedById(aam.isAnnotationPresent(LastModifiedById.class))
                        .deletedById(aam.isAnnotationPresent(DeletedById.class));
                }
                final boolean hasRemark = aam.isAnnotationPresent(Remark.class);
                if (hasRemark) {
                    builder.auditType(AuditType.NAME)
                        .createdByName(aam.isAnnotationPresent(CreatedByName.class))
                        .lastModifiedByName(aam.isAnnotationPresent(LastModifiedByName.class))
                        .deletedByName(aam.isAnnotationPresent(DeletedByName.class));
                }
                final boolean hasTime = aam.isAnnotationPresent(Time.class);
                if (hasTime) {
                    builder.auditType(AuditType.TIME)
                        .createdDate(aam.isAnnotationPresent(CreatedDate.class))
                        .lastModifiedDate(aam.isAnnotationPresent(LastModifiedDate.class))
                        .deletedDate(aam.isAnnotationPresent(DeletedDate.class));
                }
                if (hasUnique || hasRemark || hasTime) {
                    return builder.policy(AuditPolicy.get(aam.getAnnotation(Created.class)))
                        .policy(AuditPolicy.get(aam.getAnnotation(Modified.class)))
                        .policy(AuditPolicy.get(aam.getAnnotation(Deleted.class)))
                        .build();
                } else if (this.isAutoScan()) {
                    return this.parser.parse(property);
                }
            } else if (this.isAutoScan()) {
                return this.parser.parse(property);
            }
        }
        return new DefaultNonAuditMatcher();
    }

    public boolean isAutoScan() {
        return this.autoScan && this.parser != null;
    }

    public static class AuditAntMetadata {
        private final Set<? extends Annotation> annotations;

        public AuditAntMetadata(Set<? extends Annotation> annotations) {
            this.annotations = annotations;
        }

        public <T extends AnnotatedElement> boolean isAnnotationPresent(final T type) {
            return this.annotations.stream().anyMatch(it -> it.annotationType().equals(type));
        }

        @SuppressWarnings("unchecked")
        public <T extends Annotation> T getAnnotation(final Class<T> target) {
            return (T) this.annotations.stream().filter(it ->
                it.annotationType().equals(target)).findFirst().orElse(null);
        }
    }
}
