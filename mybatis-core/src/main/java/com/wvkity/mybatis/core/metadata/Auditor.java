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
package com.wvkity.mybatis.core.metadata;

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
    private final boolean createdTime;
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
    private final boolean modifiedTime;
    /**
     * 是否为更新操作用户ID自动填充标识
     */
    private final boolean modifiedUserId;
    /**
     * 是否为更新操作用户名自动填充标识
     */
    private final boolean modifiedUserName;
    /**
     * 是否为删除操作时间自动填充标识
     */
    private final boolean deletedTime;
    /**
     * 是否为删除操作用户ID自动填充标识
     */
    private final boolean deletedUserId;
    /**
     * 是否为删除操作用户名自动填充标识
     */
    private final boolean deletedUserName;
    /**
     * 是否为逻辑删除标识
     */
    private final boolean logicalDeletion;
    /**
     * 已删除标识值
     */
    private final Object deletedValue;
    /**
     * 未删除标识值
     */
    private final Object undeletedValue;

    public Auditor(boolean version, boolean tenement, boolean createdTime,
                   boolean createdUserId, boolean createdUserName,
                   boolean modifiedTime, boolean modifiedUserId, boolean modifiedUserName,
                   boolean deletedTime, boolean deletedUserId, boolean deletedUserName,
                   boolean logicalDeletion, Object deletedValue, Object undeletedValue) {
        this.version = version;
        this.tenement = tenement;
        this.createdTime = createdTime;
        this.createdUserId = createdUserId;
        this.createdUserName = createdUserName;
        this.modifiedTime = modifiedTime;
        this.modifiedUserId = modifiedUserId;
        this.modifiedUserName = modifiedUserName;
        this.deletedTime = deletedTime;
        this.deletedUserId = deletedUserId;
        this.deletedUserName = deletedUserName;
        this.logicalDeletion = logicalDeletion;
        this.deletedValue = deletedValue;
        this.undeletedValue = undeletedValue;
    }

    /**
     * 检查当前字段是否存在保存操作审计
     * @return boolean
     */
    public boolean insertedAuditable() {
        return this.createdTime || this.createdUserId || this.createdUserName;
    }

    /**
     * 检查当前字段是否存在更新操作审计
     * @return boolean
     */
    public boolean modifiedAuditable() {
        return this.modifiedTime || this.modifiedUserId || this.modifiedUserName;
    }

    /**
     * 检查当前字段是否存在删除操作审计
     * @return boolean
     */
    public boolean deletedAuditable() {
        return this.deletedTime || this.deletedUserId || this.deletedUserName;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isTenement() {
        return tenement;
    }

    public boolean isCreatedTime() {
        return createdTime;
    }

    public boolean isCreatedUserId() {
        return createdUserId;
    }

    public boolean isCreatedUserName() {
        return createdUserName;
    }

    public boolean isModifiedTime() {
        return modifiedTime;
    }

    public boolean isModifiedUserId() {
        return modifiedUserId;
    }

    public boolean isModifiedUserName() {
        return modifiedUserName;
    }

    public boolean isDeletedTime() {
        return deletedTime;
    }

    public boolean isDeletedUserId() {
        return deletedUserId;
    }

    public boolean isDeletedUserName() {
        return deletedUserName;
    }

    public boolean isLogicalDeletion() {
        return logicalDeletion;
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
            ", createdTime=" + createdTime +
            ", createdUserId=" + createdUserId +
            ", createdUserName=" + createdUserName +
            ", modifiedTime=" + modifiedTime +
            ", modifiedUserId=" + modifiedUserId +
            ", modifiedUserName=" + modifiedUserName +
            ", deletedTime=" + deletedTime +
            ", deletedUserId=" + deletedUserId +
            ", deletedUserName=" + deletedUserName +
            ", logicalDeletion=" + logicalDeletion +
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
            createdTime == auditor.createdTime &&
            createdUserId == auditor.createdUserId &&
            createdUserName == auditor.createdUserName &&
            modifiedTime == auditor.modifiedTime &&
            modifiedUserId == auditor.modifiedUserId &&
            modifiedUserName == auditor.modifiedUserName &&
            deletedTime == auditor.deletedTime &&
            deletedUserId == auditor.deletedUserId &&
            deletedUserName == auditor.deletedUserName &&
            logicalDeletion == auditor.logicalDeletion &&
            Objects.equals(deletedValue, auditor.deletedValue) &&
            Objects.equals(undeletedValue, auditor.undeletedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, tenement, createdTime,
            createdUserId, createdUserName, modifiedTime, modifiedUserId,
            modifiedUserName, deletedTime, deletedUserId, deletedUserName,
            logicalDeletion, deletedValue, undeletedValue);
    }
}
