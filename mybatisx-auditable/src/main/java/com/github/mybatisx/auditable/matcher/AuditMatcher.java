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
package com.github.mybatisx.auditable.matcher;

import com.github.mybatisx.auditable.AuditPolicy;
import com.github.mybatisx.auditable.AuditType;

import java.util.Set;

/**
 * 审计匹配器
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public interface AuditMatcher {

    /**
     * 是否可匹配
     * @return boolean
     */
    boolean canMatches();

    /**
     * 获取审计类型
     * @return {@link AuditType}
     */
    default AuditType getType() {
        return null;
    }

    /**
     * 是否为保存操作用户ID自动填充标识
     * @return boolean
     */
    default boolean isCreatedById() {
        return false;
    }

    /**
     * 是否为保存操作用户名自动填充标识
     * @return boolean
     */
    default boolean isCreatedByName() {
        return false;
    }

    /**
     * 是否为保存操作时间自动填充标识
     * @return boolean
     */
    default boolean isCreatedDate() {
        return false;
    }

    /**
     * 是否为更新操作用户ID自动填充标识
     * @return boolean
     */
    default boolean isLastModifiedById() {
        return false;
    }

    /**
     * 是否为更新操作用户名自动填充标识
     * @return boolean
     */
    default boolean isLastModifiedByName() {
        return false;
    }

    /**
     * 是否为更新操作时间自动填充标识
     * @return boolean
     */
    default boolean isLastModifiedDate() {
        return false;
    }

    /**
     * 是否为更新操作用户ID自动填充标识
     * @return boolean
     */
    default boolean isDeletedById() {
        return false;
    }

    /**
     * 是否为更新操作用户名自动填充标识
     * @return boolean
     */
    default boolean isDeletedByName() {
        return false;
    }

    /**
     * 是否为更新操作时间自动填充标识
     * @return boolean
     */
    default boolean isDeletedDate() {
        return false;
    }

    /**
     * 获取审计策略
     * @return {@link AuditPolicy}列表
     */
    default Set<AuditPolicy> getPolicies() {
        return null;
    }

}
