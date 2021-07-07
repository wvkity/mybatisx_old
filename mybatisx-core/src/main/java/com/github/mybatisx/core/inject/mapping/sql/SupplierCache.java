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
package com.github.mybatisx.core.inject.mapping.sql;

import com.github.mybatisx.basic.reflect.Reflections;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.support.inject.mapping.sql.Supplier;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * {@link Supplier}缓存
 * @author wvkity
 * @created 2020-10-21
 * @since 1.0.0
 */
public final class SupplierCache {

    private SupplierCache() {
    }

    /**
     * {@link Supplier}缓存
     */
    private static final Map<String, Class<? extends Supplier>> SUPPLIER_CLASS_CACHE =
        Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * 根据指定类获取{@link Supplier}类
     * @param clazz 指定类
     * @return {@link Supplier}类
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends Supplier> getTarget(final Class<?> clazz) {
        return Optional.ofNullable(clazz).map(it -> {
            final String key = it.getName();
            final Class<? extends Supplier> target = SUPPLIER_CLASS_CACHE.get(key);
            if (Objects.isNull(target)) {
                final Class<?> objectClass = Reflections.getGenericClass(it, 0);
                if (Supplier.class.isAssignableFrom(objectClass)) {
                    final Class<? extends Supplier> supplier = (Class<? extends Supplier>) objectClass;
                    SUPPLIER_CLASS_CACHE.putIfAbsent(key, supplier);
                    return SUPPLIER_CLASS_CACHE.getOrDefault(key, supplier);
                }
            }
            return target;
        }).orElse(null);
    }

    /**
     * 根据指定类获取泛型{@link Supplier}类，通过反射创建{@link Supplier}对象
     * @param args  参数列表
     * @param clazz 指定类
     * @return {@link Supplier}对象
     */
    public static Supplier newInstance(final Class<?> clazz, Object... args) {
        final Class<? extends Supplier> supplierClass = getTarget(clazz);
        return Optional.ofNullable(supplierClass).map(it -> {
            try {
                return Reflections.newInstance(it, args);
            } catch (Exception ignore) {
                // ignore
            }
            return null;
        }).orElse(null);
    }
}
