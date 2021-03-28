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
package com.wvkity.mybatis.core.auditable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 自动审计属性
 * @author wvkity
 * @created 2021-03-05
 * @since 1.0.0
 */
public class AutomaticAuditableProperties {

    private static final Map<AuditMatching, Map<AuditType, Set<String>>> PROPS_AUDIT_CACHE = new HashMap<>(4);
    private static final Set<String> PROPS_S_ID = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_S_NAME = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_S_DATE = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_U_ID = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_U_NAME = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_U_DATE = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_D_ID = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_D_NAME = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_D_DATE = new CopyOnWriteArraySet<>();

    static {
        PROPS_S_ID.addAll(Arrays.asList("createBy", "createdBy", "createUserId", "createdUserId"));
        PROPS_S_NAME.addAll(Arrays.asList("createByName", "createdByName", "createUserName", "createdUserName"));
        PROPS_S_DATE.addAll(Arrays.asList("createTime", "createdTime", "gmtCreate", "gmtCreated"));
        PROPS_U_ID.addAll(Arrays.asList("updateBy", "updatedBy", "modifiedUserId", "lastModifiedUserId"));
        PROPS_U_NAME.addAll(Arrays.asList("updateByName", "updatedByName", "modifiedUserName", "lastModifiedUserName"));
        PROPS_U_DATE.addAll(Arrays.asList("updateTime", "updatedTime", "gmtModified", "gmtLastModified"));
        PROPS_D_ID.addAll(Arrays.asList("deleteBy", "deletedBy", "deleteUserId", "deletedUserId"));
        PROPS_D_NAME.addAll(Arrays.asList("deleteByName", "deletedByName", "deleteUserName", "deletedUserName"));
        PROPS_D_DATE.addAll(Arrays.asList("deleteTime", "deletedTime", "gmtDelete", "gmtDeleted"));
        final Map<AuditType, Set<String>> saveProps = new HashMap<>(4);
        saveProps.put(AuditType.ID, PROPS_S_ID);
        saveProps.put(AuditType.NAME, PROPS_S_NAME);
        saveProps.put(AuditType.DATE, PROPS_S_DATE);
        final Map<AuditType, Set<String>> updateProps = new HashMap<>(4);
        updateProps.put(AuditType.ID, PROPS_U_ID);
        updateProps.put(AuditType.NAME, PROPS_U_NAME);
        updateProps.put(AuditType.DATE, PROPS_U_DATE);
        final Map<AuditType, Set<String>> deleteProps = new HashMap<>(4);
        deleteProps.put(AuditType.ID, PROPS_D_ID);
        deleteProps.put(AuditType.NAME, PROPS_D_NAME);
        deleteProps.put(AuditType.DATE, PROPS_D_DATE);
        PROPS_AUDIT_CACHE.put(AuditMatching.SAVE, saveProps);
        PROPS_AUDIT_CACHE.put(AuditMatching.UPDATE, updateProps);
        PROPS_AUDIT_CACHE.put(AuditMatching.DELETE, deleteProps);
    }

    public static boolean matches(final AuditMatching matching, final AuditType type, final String property) {
        if (matching == null || type == null || property == null || property.trim().isEmpty()) {
            return false;
        }
        final Set<String> props = PROPS_AUDIT_CACHE.get(matching).get(type);
        return !props.isEmpty() && props.contains(property);
    }

    public static boolean idMatches(final AuditMatching matching, final String property) {
        return matches(matching, AuditType.ID, property);
    }

    public static boolean nameMatches(final AuditMatching matching, final String property) {
        return matches(matching, AuditType.NAME, property);
    }

    public static boolean dateMatches(final AuditMatching matching, final String property) {
        return matches(matching, AuditType.DATE, property);
    }

    public static Set<String> get(final AuditMatching matching, final AuditType type) {
        if (matching == null || type == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(PROPS_AUDIT_CACHE.get(matching).get(type));
    }

    public static void merge(final AuditMatching matching, final AuditType type, final String... properties) {
        if (properties != null && properties.length > 0) {
            merge(matching, type, Arrays.asList(properties));
        }
    }

    public static void merge(final AuditMatching matching, final AuditType type, final Collection<String> properties) {
        if (matching != null && type != null && properties != null && !properties.isEmpty()) {
            final Set<String> props = properties.stream().filter(AutomaticAuditableProperties::isNotEmpty)
                .map(String::trim).collect(Collectors.toSet());
            if (!props.isEmpty()) {
                PROPS_AUDIT_CACHE.get(matching).get(type).addAll(props);
            }
        }
    }

    public static void put(final AuditMatching matching, final AuditType type, final String... properties) {
        if (properties != null && properties.length > 0) {
            final Set<String> props = Arrays.stream(properties).filter(AutomaticAuditableProperties::isNotEmpty)
                .map(String::trim).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
            if (!props.isEmpty()) {
                put(matching, type, props);
            }
        }
    }

    public static void put(final AuditMatching matching, final AuditType type, final Collection<String> properties) {
        if (properties != null && !properties.isEmpty()) {
            final Set<String> props = properties.stream().filter(AutomaticAuditableProperties::isNotEmpty)
                .map(String::trim).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
            if (!props.isEmpty()) {
                put(matching, type, props);
            }
        }
    }

    public static void put(final AuditMatching matching, final AuditType type, final Set<String> properties) {
        if (matching != null && type != null && properties != null && !properties.isEmpty()) {
            PROPS_AUDIT_CACHE.get(matching).put(type, properties);
        }
    }

    private static boolean isNotEmpty(final String value) {
        return value != null && !value.trim().isEmpty();
    }

}
