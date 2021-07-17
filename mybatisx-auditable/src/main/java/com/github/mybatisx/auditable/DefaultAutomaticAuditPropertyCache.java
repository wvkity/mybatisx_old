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
package com.github.mybatisx.auditable;

import com.github.mybatisx.auditable.annotation.CreatedById;
import com.github.mybatisx.auditable.annotation.CreatedByName;
import com.github.mybatisx.auditable.annotation.CreatedDate;
import com.github.mybatisx.auditable.annotation.DeletedById;
import com.github.mybatisx.auditable.annotation.DeletedByName;
import com.github.mybatisx.auditable.annotation.DeletedDate;
import com.github.mybatisx.auditable.annotation.LastModifiedById;
import com.github.mybatisx.auditable.annotation.LastModifiedByName;
import com.github.mybatisx.auditable.annotation.LastModifiedDate;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 默认自动审计属性
 * @author wvkity
 * @created 2021-07-14
 * @since 1.0.0
 */
public final class DefaultAutomaticAuditPropertyCache {

    private DefaultAutomaticAuditPropertyCache() {
    }

    private static final Set<String> PROPS_C_ID = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_C_NAME = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_C_DATE = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_U_ID = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_U_NAME = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_U_DATE = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_D_ID = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_D_NAME = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_D_DATE = new CopyOnWriteArraySet<>();
    private static final Set<String> PROPS_CACHE = new CopyOnWriteArraySet<>();
    private static final Map<String, Class<? extends Annotation>> PROP_ANT_CACHE = new ConcurrentHashMap<>();

    static {
        PROPS_C_ID.addAll(Arrays.asList("createdById", "createdUserId"));
        PROPS_C_NAME.addAll(Arrays.asList("createdByName", "createdUserName"));
        PROPS_C_DATE.addAll(Arrays.asList("createdTime", "createdDate", "createdDateTime", "gmtCreated"));
        PROPS_U_ID.addAll(Arrays.asList("updatedById", "lastModifiedById", "lastModifiedUserId"));
        PROPS_U_NAME.addAll(Arrays.asList("updatedByName", "lastModifiedByName", "lastModifiedUserName"));
        PROPS_U_DATE.addAll(Arrays.asList("updatedTime", "updatedDate", "updatedDateTime", "gmtLastModifiedDate"));
        PROPS_D_ID.addAll(Arrays.asList("deletedById", "deletedUserId"));
        PROPS_D_NAME.addAll(Arrays.asList("deletedByName", "deletedUserName"));
        PROPS_D_DATE.addAll(Arrays.asList("deletedTime", "deletedDate", "deletedDateTime", "gmtDeleted"));
        cache(CreatedById.class, PROPS_C_ID);
        cache(CreatedByName.class, PROPS_C_NAME);
        cache(CreatedDate.class, PROPS_C_DATE);
        cache(LastModifiedById.class, PROPS_U_ID);
        cache(LastModifiedByName.class, PROPS_U_NAME);
        cache(LastModifiedDate.class, PROPS_U_DATE);
        cache(DeletedById.class, PROPS_D_ID);
        cache(DeletedByName.class, PROPS_D_NAME);
        cache(DeletedDate.class, PROPS_D_DATE);
    }

    private static void cache(final Class<? extends Annotation> target, Iterable<String> iterable) {
        if (iterable != null && target != null) {
            for (String prop : iterable) {
                if (!contains(prop)) {
                    PROP_ANT_CACHE.putIfAbsent(prop, target);
                    PROPS_CACHE.add(prop);
                }
            }
        }
    }

    private static boolean isNotEmpty(final String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 检查属性是否已存在
     * @param property 属性名
     * @return boolean
     */
    public static boolean contains(final String property) {
        return isNotEmpty(property) && PROPS_CACHE.contains(property);
    }

    /**
     * 根据属性获取审计注解类
     * @param property 属性
     * @return 审计注解类
     */
    public static Class<? extends Annotation> get(final String property) {
        if (isNotEmpty(property)) {
            return PROP_ANT_CACHE.get(property);
        }
        return null;
    }

    /**
     * 新增审计属性
     * @param target     审计注解类
     * @param properties 属性列表
     */
    public static void put(final Class<? extends Annotation> target, final String... properties) {
        put(target, Arrays.asList(properties));
    }

    /**
     * 新增审计属性
     * @param target   审计注解类
     * @param iterable 属性列表
     */
    public static void put(final Class<? extends Annotation> target, final Iterable<String> iterable) {
        cache(target, iterable);
    }

}
