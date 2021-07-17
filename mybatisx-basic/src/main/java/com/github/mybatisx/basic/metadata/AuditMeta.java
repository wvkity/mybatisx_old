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
package com.github.mybatisx.basic.metadata;

import java.util.Set;

/**
 * 审计信息
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public class AuditMeta {

    /**
     * 是否为乐观锁标识
     */
    private final boolean version;
    /**
     * 是否为多租户标识
     */
    private final boolean multiTenant;
    /**
     * 是否为保存操作时间自动填充标识
     */
    private final boolean createdDate;
    /**
     * 是否为保存操作用户ID自动填充标识
     */
    private final boolean createdById;
    /**
     * 是否为保存操作用户名自动填充标识
     */
    private final boolean createdByName;
    /**
     * 是否为更新操作时间自动填充标识
     */
    private final boolean lastModifiedDate;
    /**
     * 是否为更新操作用户ID自动填充标识
     */
    private final boolean lastModifiedById;
    /**
     * 是否为更新操作用户名自动填充标识
     */
    private final boolean lastModifiedByName;
    /**
     * 是否为删除操作时间自动填充标识
     */
    private final boolean deletedDate;
    /**
     * 是否为删除操作用户ID自动填充标识
     */
    private final boolean deletedById;
    /**
     * 是否为删除操作用户名自动填充标识
     */
    private final boolean deletedByName;
    /**
     * 是否为逻辑删除标识
     */
    private final boolean logicDelete;
    /**
     * 已删除标识值
     */
    private final Object deletedValue;
    /**
     * 未删除标识值
     */
    private final Object undeletedValue;
    /**
     * 审计类型
     */
    private final int auditType;
    /**
     * 审计策略列表
     */
    private final Set<Integer> auditPolicies;

    public AuditMeta(boolean version, boolean multiTenant, boolean createdDate, boolean createdById,
                     boolean createdByName, boolean lastModifiedDate, boolean lastModifiedById,
                     boolean lastModifiedByName, boolean deletedDate, boolean deletedById, boolean deletedByName,
                     boolean logicDelete, Object deletedValue, Object undeletedValue, int auditType,
                     Set<Integer> auditPolicies) {
        this.version = version;
        this.multiTenant = multiTenant;
        this.createdDate = createdDate;
        this.createdById = createdById;
        this.createdByName = createdByName;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedById = lastModifiedById;
        this.lastModifiedByName = lastModifiedByName;
        this.deletedDate = deletedDate;
        this.deletedById = deletedById;
        this.deletedByName = deletedByName;
        this.logicDelete = logicDelete;
        this.deletedValue = deletedValue;
        this.undeletedValue = undeletedValue;
        this.auditType = auditType;
        this.auditPolicies = auditPolicies;
    }

    /**
     * 检查当前字段是否存在保存操作审计
     * @return boolean
     */
    public boolean insertedAuditable() {
        return this.auditPolicies.contains(0);
    }

    /**
     * 检查当前字段是否存在更新操作审计
     * @return boolean
     */
    public boolean modifiedAuditable() {
        return this.auditPolicies.contains(1);
    }

    /**
     * 检查当前字段是否存在删除操作审计
     * @return boolean
     */
    public boolean deletedAuditable() {
        return this.auditPolicies.contains(2);
    }

    public boolean matchingId() {
        return this.createdById || this.lastModifiedById || this.deletedById;
    }

    public boolean matchingName() {
        return this.createdByName || this.lastModifiedByName || this.deletedByName;
    }

    public boolean matchingDate() {
        return this.createdDate || this.lastModifiedDate || this.deletedDate;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isMultiTenant() {
        return multiTenant;
    }

    public boolean isCreatedDate() {
        return createdDate;
    }

    public boolean isCreatedById() {
        return createdById;
    }

    public boolean isCreatedByName() {
        return createdByName;
    }

    public boolean isLastModifiedDate() {
        return lastModifiedDate;
    }

    public boolean isLastModifiedById() {
        return lastModifiedById;
    }

    public boolean isLastModifiedByName() {
        return lastModifiedByName;
    }

    public boolean isDeletedDate() {
        return deletedDate;
    }

    public boolean isDeletedById() {
        return deletedById;
    }

    public boolean isDeletedByName() {
        return deletedByName;
    }

    public boolean isLogicDelete() {
        return logicDelete;
    }

    public Object getDeletedValue() {
        return deletedValue;
    }

    public Object getUndeletedValue() {
        return undeletedValue;
    }

    public int getAuditType() {
        return auditType;
    }

    public Set<Integer> getAuditPolicies() {
        return auditPolicies;
    }
}
