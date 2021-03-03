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
    private final boolean tenement;
    /**
     * 是否为保存操作时间自动填充标识
     */
    private final boolean createdDate;
    /**
     * 是否为保存操作用户ID自动填充标识
     */
    private final boolean createdUserId;
    /**
     * 是否为保存操作用户名自动填充标识
     */
    private final boolean createdUserName;
    /**
     * 是否为更新操作时间自动填充标识
     */
    private final boolean lastModifiedDate;
    /**
     * 是否为更新操作用户ID自动填充标识
     */
    private final boolean lastModifiedUserId;
    /**
     * 是否为更新操作用户名自动填充标识
     */
    private final boolean lastModifiedUserName;
    /**
     * 是否为删除操作时间自动填充标识
     */
    private final boolean logicDeletedDate;
    /**
     * 是否为删除操作用户ID自动填充标识
     */
    private final boolean logicDeletedUserId;
    /**
     * 是否为删除操作用户名自动填充标识
     */
    private final boolean logicDeletedUserName;
    /**
     * 是否为逻辑删除标识
     */
    private final boolean logicalDelete;
    /**
     * 已删除标识值
     */
    private final Object deletedValue;
    /**
     * 未删除标识值
     */
    private final Object undeletedValue;

    public Auditor(boolean version, boolean tenement, boolean createdDate,
                   boolean createdUserId, boolean createdUserName,
                   boolean lastModifiedDate, boolean lastModifiedUserId, boolean lastModifiedUserName,
                   boolean logicDeletedDate, boolean logicDeletedUserId, boolean logicDeletedUserName,
                   boolean logicalDelete, Object deletedValue, Object undeletedValue) {
        this.version = version;
        this.tenement = tenement;
        this.createdDate = createdDate;
        this.createdUserId = createdUserId;
        this.createdUserName = createdUserName;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedUserId = lastModifiedUserId;
        this.lastModifiedUserName = lastModifiedUserName;
        this.logicDeletedDate = logicDeletedDate;
        this.logicDeletedUserId = logicDeletedUserId;
        this.logicDeletedUserName = logicDeletedUserName;
        this.logicalDelete = logicalDelete;
        this.deletedValue = deletedValue;
        this.undeletedValue = undeletedValue;
    }

    /**
     * 检查当前字段是否存在保存操作审计
     * @return boolean
     */
    public boolean insertedAuditable() {
        return this.createdDate || this.createdUserId || this.createdUserName;
    }

    /**
     * 检查当前字段是否存在更新操作审计
     * @return boolean
     */
    public boolean modifiedAuditable() {
        return this.lastModifiedDate || this.lastModifiedUserId || this.lastModifiedUserName;
    }

    /**
     * 检查当前字段是否存在删除操作审计
     * @return boolean
     */
    public boolean deletedAuditable() {
        return this.logicDeletedDate || this.logicDeletedUserId || this.logicDeletedUserName;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isTenement() {
        return tenement;
    }

    public boolean isCreatedDate() {
        return createdDate;
    }

    public boolean isCreatedUserId() {
        return createdUserId;
    }

    public boolean isCreatedUserName() {
        return createdUserName;
    }

    public boolean isLastModifiedDate() {
        return lastModifiedDate;
    }

    public boolean isLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public boolean isLastModifiedUserName() {
        return lastModifiedUserName;
    }

    public boolean isLogicDeletedDate() {
        return logicDeletedDate;
    }

    public boolean isLogicDeletedUserId() {
        return logicDeletedUserId;
    }

    public boolean isLogicDeletedUserName() {
        return logicDeletedUserName;
    }

    public boolean isLogicalDelete() {
        return logicalDelete;
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
            ", tenement=" + tenement +
            ", createdDate=" + createdDate +
            ", createdUserId=" + createdUserId +
            ", createdUserName=" + createdUserName +
            ", lastModifiedDate=" + lastModifiedDate +
            ", lastModifiedUserId=" + lastModifiedUserId +
            ", lastModifiedUserName=" + lastModifiedUserName +
            ", logicDeletedDate=" + logicDeletedDate +
            ", logicDeletedUserId=" + logicDeletedUserId +
            ", logicDeletedUserName=" + logicDeletedUserName +
            ", logicalDelete=" + logicalDelete +
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
            tenement == auditor.tenement &&
            createdDate == auditor.createdDate &&
            createdUserId == auditor.createdUserId &&
            createdUserName == auditor.createdUserName &&
            lastModifiedDate == auditor.lastModifiedDate &&
            lastModifiedUserId == auditor.lastModifiedUserId &&
            lastModifiedUserName == auditor.lastModifiedUserName &&
            logicDeletedDate == auditor.logicDeletedDate &&
            logicDeletedUserId == auditor.logicDeletedUserId &&
            logicDeletedUserName == auditor.logicDeletedUserName &&
            logicalDelete == auditor.logicalDelete &&
            Objects.equals(deletedValue, auditor.deletedValue) &&
            Objects.equals(undeletedValue, auditor.undeletedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, tenement, createdDate,
            createdUserId, createdUserName, lastModifiedDate, lastModifiedUserId,
            lastModifiedUserName, logicDeletedDate, logicDeletedUserId, logicDeletedUserName,
            logicalDelete, deletedValue, undeletedValue);
    }
}
