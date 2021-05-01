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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.core.property.Property;

import java.util.Map;

/**
 * 更新包装器
 * @author wvkity
 * @created 2021-03-21
 * @since 1.0.0
 */
public interface UpdateWrapper<T, Chain extends UpdateWrapper<T, Chain>> {

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link Chain}
     */
    <V> Chain set(final Property<T, V> property, final V value);

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link Chain}
     */
    <V> Chain setIfAbsent(final Property<T, V> property, final V value);

    /**
     * 更新字段值
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param <V1> 值类型
     * @param <V2> 值类型
     * @return {@link Chain}
     */
    default <V1, V2> Chain set(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2) {
        return this.set(p1, v1).set(p2, v2);
    }

    /**
     * 更新字段值
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @param <V1> 值类型
     * @param <V2> 值类型
     * @param <V3> 值类型
     * @return {@link Chain}
     */
    default <V1, V2, V3> Chain set(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2,
                                   final V2 v2, final Property<T, V3> p3, final V3 v3) {
        return this.set(p1, v1).set(p2, v2).set(p3, v3);
    }

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain set(final String property, final Object value);

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain setIfAbsent(final String property, final Object value);

    /**
     * 更新字段值
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @return {@link Chain}
     */
    default <V1, V2, V3> Chain set(final String p1, final Object v1, final String p2, final Object v2) {
        return this.set(p1, v1).set(p2, v2);
    }

    /**
     * 更新字段值
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @param p3 属性3
     * @param v3 属性3对应值
     * @return {@link Chain}
     */
    default <V1, V2, V3> Chain set(final String p1, final Object v1, final String p2,
                                   final Object v2, final String p3, final Object v3) {
        return this.set(p1, v1).set(p2, v2).set(p3, v3);
    }

    /**
     * 更新字段值
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    Chain colSet(final String column, final Object value);

    /**
     * 更新字段值
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    Chain colSetIfAbsent(final String column, final Object value);

    /**
     * 更新字段值
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @return {@link Chain}
     */
    default Chain colSet(final String c1, final Object v1, final String c2, final Object v2) {
        return this.colSet(c1, v1).colSet(c2, v2);
    }

    /**
     * 更新字段值
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @param c3 字段3
     * @param v3 字段3对应值
     * @return {@link Chain}
     */
    default Chain colSet(final String c1, final Object v1, final String c2, final Object v2, final String c3,
                         final Object v3) {
        return this.colSet(c1, v1).colSet(c2, v2).colSet(c3, v3);
    }

    /**
     * 更新字段值
     * @param columns 字段-值集合
     * @return {@link Chain}
     */
    Chain colSet(final Map<String, Object> columns);

    /**
     * 获取更新语句片段
     * @return 更新SQL片段
     */
    String getUpdateSegment();

}
