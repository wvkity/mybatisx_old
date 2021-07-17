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
package com.github.mybatisx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 基本数据类型工具
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public final class BasicDataTypeRegistry {

    private BasicDataTypeRegistry() {
    }

    /**
     * 基本数据类型及其包装类型缓存
     */
    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPE_CACHE;

    static {
        PRIMITIVE_WRAPPER_TYPE_CACHE = new HashSet<>();
        PRIMITIVE_WRAPPER_TYPE_CACHE.addAll(Arrays.asList(
            Boolean.class, boolean.class, Byte.class, byte.class, Character.class, char.class,
            Double.class, double.class, Float.class, float.class, Integer.class, int.class,
            Long.class, long.class, Short.class, short.class
        ));
    }

    /**
     * 检查指定对象的类型是否为基本类型或包装类型
     * @param target 指定对象
     * @return boolean
     */
    public static boolean isPrimitiveOrWrapper(final Object target) {
        return target != null && isPrimitiveOrWrapper(target.getClass());
    }

    /**
     * 检查指定类是否为基本类型或包装类型
     * @param clazz 指定类
     * @return boolean
     */
    public static boolean isPrimitiveOrWrapper(final Class<?> clazz) {
        return clazz != null && (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_CACHE.contains(clazz));
    }
}
