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
package com.wvkity.mybatis.core.data.audit;

import com.wvkity.mybatis.core.immutable.ImmutableLinkedSet;
import com.wvkity.mybatis.core.immutable.ImmutableSet;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认自动审计属性
 * @author wvkity
 * @created 2020-10-14
 * @since 1.0.0
 */
public final class DefaultAuditProperties {

    private DefaultAuditProperties() {
    }

    /**
     * 审计属性缓存
     */
    private static final Map<AuditStrategy, Map<AuditMatching, Set<String>>> AUDIT_PROPERTIES_CACHE =
        new ConcurrentHashMap<>();
    /**
     * 创建人ID属性集合
     */
    private static final Set<String> CREATED_USER_ID_PROPERTIES;
    /**
     * 创建人名称属性集合
     */
    private static final Set<String> CREATED_USER_NAME_PROPERTIES;
    /**
     * 创建时间属性集合
     */
    private static final Set<String> CREATED_TIME_PROPERTIES;
    /**
     * 更新人ID属性集合
     */
    private static final Set<String> MODIFIED_USER_ID_PROPERTIES;
    /**
     * 更新人名称属性集合
     */
    private static final Set<String> MODIFIED_USER_NAME_PROPERTIES;
    /**
     * 更新时间属性集合
     */
    private static final Set<String> MODIFIED_TIME_PROPERTIES;
    /**
     * 删除人ID属性集合
     */
    private static final Set<String> DELETED_USER_ID_PROPERTIES;
    /**
     * 删除人名称属性集合
     */
    private static final Set<String> DELETED_USER_NAME_PROPERTIES;
    /**
     * 删除时间属性集合
     */
    private static final Set<String> DELETED_TIME_PROPERTIES;

    static {
        CREATED_USER_ID_PROPERTIES = ImmutableLinkedSet.construct("createUserId", "createdUserId", "createById", "createdById");
        CREATED_USER_NAME_PROPERTIES = ImmutableLinkedSet.construct("createUserName", "createdUserName", "createBy", "createdBy");
        CREATED_TIME_PROPERTIES = ImmutableLinkedSet.construct("gmtCreate", "gmtCreated", "createTime", "createdTime");
        MODIFIED_USER_ID_PROPERTIES = ImmutableLinkedSet.construct("modifiedUserId", "lastModifiedUserId", "updateById", "updatedById");
        MODIFIED_USER_NAME_PROPERTIES = ImmutableLinkedSet.construct("modifiedUserName", "lastModifiedUserName", "updateBy", "updatedBy");
        MODIFIED_TIME_PROPERTIES = ImmutableLinkedSet.construct("gmtModified", "gmtLastModified", "modifiedTime", "lastModifiedTime", "updateTime", "updatedTime");
        DELETED_USER_ID_PROPERTIES = ImmutableLinkedSet.construct("deleteUserId", "deletedUserId", "delUserId", "delById", "deletedById");
        DELETED_USER_NAME_PROPERTIES = ImmutableLinkedSet.construct("deleteUserName", "deletedUserName", "delUserName", "delBy", "deletedBy");
        DELETED_TIME_PROPERTIES = ImmutableLinkedSet.construct("gmtDeleted", "gmtDelete", "deletedTime", "deleteTime", "delTime");
        final Map<AuditMatching, Set<String>> createdMap = new ConcurrentHashMap<>();
        createdMap.put(AuditMatching.ID, CREATED_USER_ID_PROPERTIES);
        createdMap.put(AuditMatching.NAME, CREATED_USER_NAME_PROPERTIES);
        createdMap.put(AuditMatching.TIME, CREATED_TIME_PROPERTIES);
        final Map<AuditMatching, Set<String>> modifiedMap = new ConcurrentHashMap<>();
        modifiedMap.put(AuditMatching.ID, MODIFIED_USER_ID_PROPERTIES);
        modifiedMap.put(AuditMatching.NAME, MODIFIED_USER_NAME_PROPERTIES);
        modifiedMap.put(AuditMatching.TIME, MODIFIED_TIME_PROPERTIES);
        final Map<AuditMatching, Set<String>> deletedMap = new ConcurrentHashMap<>();
        deletedMap.put(AuditMatching.ID, DELETED_USER_ID_PROPERTIES);
        deletedMap.put(AuditMatching.NAME, DELETED_USER_NAME_PROPERTIES);
        deletedMap.put(AuditMatching.TIME, DELETED_TIME_PROPERTIES);
        AUDIT_PROPERTIES_CACHE.put(AuditStrategy.INSERTED, createdMap);
        AUDIT_PROPERTIES_CACHE.put(AuditStrategy.MODIFIED, modifiedMap);
        AUDIT_PROPERTIES_CACHE.put(AuditStrategy.DELETED, deletedMap);
    }

    /**
     * 获取默认自动审计属性
     * @param strategy {@link AuditStrategy}
     * @param matching {@link AuditMatching}
     * @return 属性集合
     */
    public static Set<String> get(final AuditStrategy strategy, final AuditMatching matching) {
        return Optional.ofNullable(AUDIT_PROPERTIES_CACHE.get(strategy))
            .map(it -> Optional.ofNullable(it.get(matching)).orElseGet(ImmutableSet::of)).orElseGet(ImmutableSet::of);
    }

    /**
     * 获取默认{@link AuditMatching#ID ID}类型自动审计属性
     * @param strategy {@link AuditStrategy}
     * @return 属性集合
     */
    public static Set<String> getPropertiesWithId(final AuditStrategy strategy) {
        return get(strategy, AuditMatching.ID);
    }

    /**
     * 获取默认{@link AuditMatching#NAME NAME}类型自动审计属性
     * @param strategy {@link AuditStrategy}
     * @return 属性集合
     */
    public static Set<String> getPropertiesWithName(final AuditStrategy strategy) {
        return get(strategy, AuditMatching.NAME);
    }

    /**
     * 获取默认{@link AuditMatching#TIME TIME}类型自动审计属性
     * @param strategy {@link AuditStrategy}
     * @return 属性集合
     */
    public static Set<String> getPropertiesWithTime(final AuditStrategy strategy) {
        return get(strategy, AuditMatching.TIME);
    }

    /**
     * 合并审计属性
     * @param strategy   {@link AuditStrategy}
     * @param matching   {@link AuditMatching}
     * @param properties 属性集合
     * @return boolean
     */
    public static boolean merge(final AuditStrategy strategy, final AuditMatching matching,
                                final String... properties) {
        if (Objects.isEmpty(properties)) {
            return false;
        } else {
            final List<String> props = Arrays.asList(properties);
            return merge(strategy, matching, props.subList(0, props.size()));
        }
    }

    /**
     * 合并审计属性
     * @param strategy   {@link AuditStrategy}
     * @param matching   {@link AuditMatching}
     * @param properties 属性集合
     * @return boolean
     */
    public static boolean merge(final AuditStrategy strategy, final AuditMatching matching,
                                final Collection<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            final Set<String> old = get(strategy, matching);
            if (Objects.isNotEmpty(old)) {
                final Set<String> props = new LinkedHashSet<>(old);
                props.addAll(properties);
                return put(strategy, matching, ImmutableLinkedSet.of(props));
            } else {
                return put(strategy, matching, ImmutableLinkedSet.of(properties));
            }
        }
        return false;
    }

    /**
     * 覆盖自动审计属性
     * @param strategy   {@link AuditStrategy}
     * @param properties 属性集合
     * @return boolean
     */
    public static boolean put(final AuditStrategy strategy, final Map<AuditMatching, Set<String>> properties) {
        if (strategy != null && Objects.nonNull(properties)) {
            AUDIT_PROPERTIES_CACHE.put(strategy, properties);
            return true;
        }
        return false;
    }

    /**
     * 覆盖自动审计属性
     * @param strategy   {@link AuditStrategy}
     * @param matching   {@link AuditMatching}
     * @param properties 属性集合
     * @return boolean
     */
    public static boolean put(final AuditStrategy strategy, final AuditMatching matching,
                              final Set<String> properties) {
        if (strategy != null && Objects.nonNull(properties)) {
            final Map<AuditMatching, Set<String>> cache =
                AUDIT_PROPERTIES_CACHE.computeIfAbsent(strategy, k -> new ConcurrentHashMap<>());
            cache.put(matching, ImmutableLinkedSet.of(properties));
            return true;
        }
        return false;
    }

    /**
     * 检查指定属性是否匹配指定类型的属性
     * @param strategy {@link AuditStrategy}
     * @param matching {@link AuditMatching}
     * @param property 属性
     * @return boolean
     */
    public static boolean matching(final AuditStrategy strategy, final AuditMatching matching,
                                   final String property) {
        return Optional.ofNullable(get(strategy, matching)).map(it -> it.contains(property)).orElse(false);
    }

    /**
     * 检查指定属性是否匹配{@link AuditMatching#ID ID}类型属性
     * @param strategy {@link AuditStrategy}
     * @param property 属性
     * @return boolean
     */
    public static boolean matchingWithId(final AuditStrategy strategy, final String property) {
        return matching(strategy, AuditMatching.ID, property);
    }

    /**
     * 检查指定属性是否匹配{@link AuditMatching#NAME NAME}类型属性
     * @param strategy {@link AuditStrategy}
     * @param property 属性
     * @return boolean
     */
    public static boolean matchingWithName(final AuditStrategy strategy, final String property) {
        return matching(strategy, AuditMatching.NAME, property);
    }

    /**
     * 检查指定属性是否匹配{@link AuditMatching#TIME TIME}类型属性
     * @param strategy {@link AuditStrategy}
     * @param property 属性
     * @return boolean
     */
    public static boolean matchingWithTime(final AuditStrategy strategy, final String property) {
        return matching(strategy, AuditMatching.TIME, property);
    }

    /**
     * 获取默认创建人ID属性集合
     * @return 属性集合
     */
    public static Set<String> getCreatedIdProperties() {
        return CREATED_USER_ID_PROPERTIES;
    }

    /**
     * 获取默认创建人名称属性集合
     * @return 属性集合
     */
    public static Set<String> getCreatedNameProperties() {
        return CREATED_USER_NAME_PROPERTIES;
    }

    /**
     * 获取默认创建时间属性集合
     * @return 属性集合
     */
    public static Set<String> getCreatedTimeProperties() {
        return CREATED_TIME_PROPERTIES;
    }

    /**
     * 获取默认更新人ID属性集合
     * @return 属性集合
     */
    public static Set<String> getModifiedIdProperties() {
        return MODIFIED_USER_ID_PROPERTIES;
    }

    /**
     * 获取默认更新人名称属性集合
     * @return 属性集合
     */
    public static Set<String> getModifiedNameProperties() {
        return MODIFIED_USER_NAME_PROPERTIES;
    }

    /**
     * 获取默认更新时间属性集合
     * @return 属性集合
     */
    public static Set<String> getModifiedTimeProperties() {
        return MODIFIED_TIME_PROPERTIES;
    }

    /**
     * 获取默认删除人ID属性集合
     * @return 属性集合
     */
    public static Set<String> getDeletedIdProperties() {
        return DELETED_USER_ID_PROPERTIES;
    }

    /**
     * 获取默认删除人名称属性集合
     * @return 属性集合
     */
    public static Set<String> getDeletedNameProperties() {
        return DELETED_USER_NAME_PROPERTIES;
    }

    /**
     * 获取默认删除时间属性集合
     * @return 属性集合
     */
    public static Set<String> getDeletedTimeProperties() {
        return DELETED_TIME_PROPERTIES;
    }
}
