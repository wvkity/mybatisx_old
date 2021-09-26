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
package io.github.mybatisx.auditable.matcher;

import io.github.mybatisx.auditable.AuditPolicy;
import io.github.mybatisx.auditable.AuditType;

import java.util.HashSet;
import java.util.Set;

/**
 * 默认审计属性匹配器
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public class DefaultAuditMatcher implements AuditMatcher {

    private final boolean matches;
    private final boolean createdById;
    private final boolean createdByName;
    private final boolean createdDate;
    private final boolean lastModifiedById;
    private final boolean lastModifiedByName;
    private final boolean lastModifiedDate;
    private final boolean deletedById;
    private final boolean deletedByName;
    private final boolean deletedDate;
    private final AuditType auditType;
    private final Set<AuditPolicy> policies;

    public DefaultAuditMatcher(boolean matches, boolean createdById, boolean createdByName, boolean createdDate,
                               boolean lastModifiedById, boolean lastModifiedByName, boolean lastModifiedDate,
                               boolean deletedById, boolean deletedByName, boolean deletedDate, AuditType auditType,
                               Set<AuditPolicy> policies) {
        this.matches = matches;
        this.createdById = createdById;
        this.createdByName = createdByName;
        this.createdDate = createdDate;
        this.lastModifiedById = lastModifiedById;
        this.lastModifiedByName = lastModifiedByName;
        this.lastModifiedDate = lastModifiedDate;
        this.deletedById = deletedById;
        this.deletedByName = deletedByName;
        this.deletedDate = deletedDate;
        this.auditType = auditType;
        this.policies = policies;
    }

    public static final class Builder {
        private Class<?> entity;
        private String property;
        private boolean matches;
        private AuditType auditType;
        private boolean createdById;
        private boolean createdByName;
        private boolean createdDate;
        private boolean lastModifiedById;
        private boolean lastModifiedByName;
        private boolean lastModifiedDate;
        private boolean deletedById;
        private boolean deletedByName;
        private boolean deletedDate;
        private final Set<AuditPolicy> policies = new HashSet<>(3);

        private Builder() {
        }

        public Builder entity(Class<?> entity) {
            this.entity = entity;
            return this;
        }

        public Builder property(String property) {
            this.property = property;
            return this;
        }

        public Builder matches(boolean matches) {
            this.matches = matches;
            return this;
        }

        public Builder auditType(AuditType auditType) {
            if (this.auditType != null && !this.auditType.equals(auditType)) {
                throw new RuntimeException("(entity: " + this.entity.getName() + ", property: " + this.property +
                    "): The audit type value already exists and cannot be assigned repeatedly, old value is '" +
                    this.auditType + "', new value is '" + auditType + "'.");
            }
            this.auditType = auditType;
            return this;
        }

        public Builder createdById(boolean createdById) {
            this.createdById = createdById;
            return this;
        }

        public Builder createdByName(boolean createdByName) {
            this.createdByName = createdByName;
            return this;
        }

        public Builder createdDate(boolean createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder lastModifiedById(boolean lastModifiedById) {
            this.lastModifiedById = lastModifiedById;
            return this;
        }

        public Builder lastModifiedByName(boolean lastModifiedByName) {
            this.lastModifiedByName = lastModifiedByName;
            return this;
        }

        public Builder lastModifiedDate(boolean lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }

        public Builder deletedById(boolean deletedById) {
            this.deletedById = deletedById;
            return this;
        }

        public Builder deletedByName(boolean deletedByName) {
            this.deletedByName = deletedByName;
            return this;
        }

        public Builder deletedDate(boolean deletedDate) {
            this.deletedDate = deletedDate;
            return this;
        }

        public Builder policy(final AuditPolicy policy) {
            if (policy != null) {
                this.policies.add(policy);
            }
            return this;
        }

        public DefaultAuditMatcher build() {
            return new DefaultAuditMatcher(this.matches, this.createdById, this.createdByName, this.createdDate,
                this.lastModifiedById, this.lastModifiedByName, this.lastModifiedDate, this.deletedById,
                this.deletedByName, this.deletedDate, this.auditType, this.policies);
        }

        public static Builder create() {
            return new Builder();
        }
    }

    @Override
    public boolean canMatches() {
        return this.matches;
    }

    @Override
    public boolean isCreatedById() {
        return this.createdById;
    }

    @Override
    public boolean isCreatedByName() {
        return this.createdByName;
    }

    @Override
    public boolean isCreatedDate() {
        return this.createdDate;
    }

    @Override
    public boolean isLastModifiedById() {
        return this.lastModifiedById;
    }

    @Override
    public boolean isLastModifiedByName() {
        return this.lastModifiedByName;
    }

    @Override
    public boolean isLastModifiedDate() {
        return this.lastModifiedDate;
    }

    @Override
    public boolean isDeletedById() {
        return this.deletedById;
    }

    @Override
    public boolean isDeletedByName() {
        return this.deletedByName;
    }

    @Override
    public boolean isDeletedDate() {
        return this.deletedDate;
    }

    @Override
    public AuditType getType() {
        return this.auditType;
    }

    @Override
    public Set<AuditPolicy> getPolicies() {
        return this.policies;
    }

    @Override
    public String toString() {
        return "DefaultAuditMatcher{" +
            "matches=" + matches +
            ", createdById=" + createdById +
            ", createdByName=" + createdByName +
            ", createdDate=" + createdDate +
            ", lastModifiedById=" + lastModifiedById +
            ", lastModifiedByName=" + lastModifiedByName +
            ", lastModifiedDate=" + lastModifiedDate +
            ", deletedById=" + deletedById +
            ", deletedByName=" + deletedByName +
            ", deletedDate=" + deletedDate +
            ", auditType=" + auditType +
            ", policies=" + policies +
            '}';
    }
}
