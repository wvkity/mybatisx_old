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
package com.github.result.model;

import com.github.result.Types;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wvkity
 * @created 2021-02-15
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public interface Aware extends Serializable {

    /**
     * 获取值
     * @param key 键
     * @return 值
     */
    Object getObject(final Object key);

    /**
     * 获取值
     * @param key   键
     * @param clazz 值类
     * @param <T>   值类型
     * @return 值
     */
    <T> T get(final Object key, final Class<T> clazz);

    /**
     * 获取数组值
     * @param key 键
     * @param <T> 值类型
     * @return 数组
     */
    default <T> T[] getArray(final Object key) {
        final Object value = this.getObject(key);
        if (value != null && value.getClass().isArray()) {
            return (T[]) value;
        }
        return null;
    }

    /**
     * 获取{@link Set}
     * @param key 键
     * @param <T> 值类型
     * @return {@link Set}
     */
    default <T> Set<T> getSet(final Object key) {
        final Object value = this.getObject(key);
        if (value != null && Set.class.isAssignableFrom(value.getClass())) {
            return (Set<T>) value;
        }
        return null;
    }

    /**
     * 获取{@link List}
     * @param key 键
     * @param <T> 值类型
     * @return {@link List}
     */
    default <T> List<T> getList(final Object key) {
        final Object value = this.getObject(key);
        if (value != null && List.class.isAssignableFrom(value.getClass())) {
            return (List<T>) value;
        }
        return null;
    }

    /**
     * 获取{@link Collection}
     * @param key 键
     * @param <T> 值类型
     * @return {@link Collection}
     */
    default <T> Collection<T> getCollection(final Object key) {
        final Object value = this.getObject(key);
        if (value != null && Collection.class.isAssignableFrom(value.getClass())) {
            return (Collection<T>) value;
        }
        return null;
    }

    /**
     * 获取{@link Map}
     * @param key 键
     * @return {@link Map}
     */
    default Map<Object, Object> getMap(final Object key) {
        final Object value = this.getObject(key);
        if (value != null && Map.class.isAssignableFrom(value.getClass())) {
            return (Map<Object, Object>) value;
        }
        return null;
    }

    /**
     * 获取字符串值
     * @param key 键
     * @return {@link String}
     */
    default String getString(final Object key) {
        return Types.toString(this.getObject(key));
    }

    /**
     * 获取字符值
     * @param key 键
     * @return {@link Character}
     */
    default Character getChar(final Object key) {
        return Types.toChar(this.getObject(key));
    }

    /**
     * 获取布尔值
     * @param key 键
     * @return {@link Boolean}
     */
    default Boolean getBoolean(final Object key) {
        return Types.toBoolean(this.getObject(key));
    }

    /**
     * 获取布尔值
     * @param key 键
     * @return boolean
     */
    default boolean getBooleanValue(final Object key) {
        final Boolean value = this.getBoolean(key);
        return value != null && value;
    }

    /**
     * 获取短整型值
     * @param key 键
     * @return {@link Short}
     */
    default Short getShort(final Object key) {
        return Types.toShort(this.getObject(key));
    }

    /**
     * 获取短整型值
     * @param key 键
     * @return short
     */
    default short getShortValue(final Object key) {
        final Short value = this.getShort(key);
        return value == null ? 0 : value;
    }

    /**
     * 获取整型值
     * @param key 键
     * @return {@link Integer}
     */
    default Integer getInt(final Object key) {
        return Types.toInt(this.getObject(key));
    }

    /**
     * 获取整型值
     * @param key 键
     * @return int
     */
    default int getIntValue(final Object key) {
        final Integer value = this.getInt(key);
        return value == null ? 0 : value;
    }

    /**
     * 获取长整型值
     * @param key 键
     * @return {@link Long}
     */
    default Long getLong(final Object key) {
        return Types.toLong(this.getObject(key));
    }

    /**
     * 获取长整型值
     * @param key 键
     * @return long
     */
    default Long getLongValue(final Object key) {
        final Long value = this.getLong(key);
        return value == null ? 0L : value;
    }

    /**
     * 获取单精度浮点值
     * @param key 键
     * @return {@link Float}
     */
    default Float getFloat(final Object key) {
        return Types.toFloat(key);
    }

    /**
     * 获取单精度浮点值
     * @param key 键
     * @return float
     */
    default Float getFloatValue(final Object key) {
        final Float value = this.getFloat(key);
        return value == null ? 0.0F : value;
    }

    /**
     * 获取双精度浮点值
     * @param key 键
     * @return {@link Double}
     */
    default Double getDouble(final Object key) {
        return Types.toDouble(key);
    }

    /**
     * 获取双精度浮点值
     * @param key 键
     * @return double
     */
    default double getDoubleValue(final Object key) {
        final Double value = this.getDouble(key);
        return value == null ? 0.0D : value;
    }

}
