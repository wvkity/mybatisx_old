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
package com.wvkity.mybatis.basic.metadata;

import java.util.Objects;

/**
 * 审计信息
 * @author wvkity
 * @created 2020-10-05
 * @since 1.0.0
 */
public class Auditor {

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

    public Auditor(boolean version, boolean multiTenant, boolean createdDate,
                   boolean createdById, boolean createdByName,
                   boolean lastModifiedDate, boolean lastModifiedById, boolean lastModifiedByName,
                   boolean deletedDate, boolean deletedById, boolean deletedByName,
                   boolean logicDelete, Object deletedValue, Object undeletedValue) {
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
    }

    /**
     * 检查当前字段是否存在保存操作审计
     * @return boolean
     */
    public boolean insertedAuditable() {
        return this.createdDate || this.createdById || this.createdByName;
    }

    /**
     * 检查当前字段是否存在更新操作审计
     * @return boolean
     */
    public boolean modifiedAuditable() {
        return this.lastModifiedDate || this.lastModifiedById || this.lastModifiedByName;
    }

    /**
     * 检查当前字段是否存在删除操作审计
     * @return boolean
     */
    public boolean deletedAuditable() {
        return this.deletedDate || this.deletedById || this.deletedByName;
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

    @Override
    public String toString() {
        return "Auditor{" +
            "version=" + version +
            ", multiTenant=" + multiTenant +
            ", createdDate=" + createdDate +
            ", createdById=" + createdById +
            ", createdByName=" + createdByName +
            ", lastModifiedDate=" + lastModifiedDate +
            ", lastModifiedById=" + lastModifiedById +
            ", lastModifiedByName=" + lastModifiedByName +
            ", deletedDate=" + deletedDate +
            ", deletedById=" + deletedById +
            ", deletedByName=" + deletedByName +
            ", logicDelete=" + logicDelete +
            ", deletedValue=" + deletedValue +
            ", undeletedValue=" + undeletedValue +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auditor)) return false;
        Auditor auditor = (Auditor) o;
        return version == auditor.version &&
            multiTenant == auditor.multiTenant &&
            createdDate == auditor.createdDate &&
            createdById == auditor.createdById &&
            createdByName == auditor.createdByName &&
            lastModifiedDate == auditor.lastModifiedDate &&
            lastModifiedById == auditor.lastModifiedById &&
            lastModifiedByName == auditor.lastModifiedByName &&
            deletedDate == auditor.deletedDate &&
            deletedById == auditor.deletedById &&
            deletedByName == auditor.deletedByName &&
            logicDelete == auditor.logicDelete &&
            Objects.equals(deletedValue, auditor.deletedValue) &&
            Objects.equals(undeletedValue, auditor.undeletedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, multiTenant, createdDate,
            createdById, createdByName, lastModifiedDate, lastModifiedById,
            lastModifiedByName, deletedDate, deletedById, deletedByName,
            logicDelete, deletedValue, undeletedValue);
    }
}
