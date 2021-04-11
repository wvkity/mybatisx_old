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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.core.property.Property;

import java.util.Map;

/**
 * 基础条件
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public interface Compare<T, Chain extends Compare<T, Chain>> {

    // region Single property

    // region Equal

    /**
     * 主键等于
     * @param value 值
     * @return {@link Chain}
     */
    default Chain idEq(final Object value) {
        return idEq(Slot.AND, value);
    }

    /**
     * 主键等于
     * @param slot  {@link Slot}
     * @param value 值
     * @return {@link Chain}
     */
    Chain idEq(final Slot slot, final Object value);

    /**
     * 等于
     * @param property {@link Property}
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain eq(final Property<T, V> property, final V value) {
        return eq(Slot.AND, property, value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property {@link Property}
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain eq(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain eq(final String property, final Object value) {
        return eq(Slot.AND, property, value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain eq(final Slot slot, final String property, final Object value);

    /**
     * 等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colEq(final String column, final Object value) {
        return colEq(Slot.AND, column, value);
    }

    /**
     * 等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    Chain colEq(final Slot slot, final String column, final Object value);

    // endregion

    // region Not equal to

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain ne(final Property<T, V> property, final V value) {
        return ne(Slot.AND, property, value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain ne(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain ne(final String property, final Object value) {
        return ne(Slot.AND, property, value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain ne(final Slot slot, final String property, final Object value);


    /**
     * 不等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNe(final String column, final Object value) {
        return colNe(Slot.AND, column, value);
    }

    /**
     * 不等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    Chain colNe(final Slot slot, final String column, final Object value);

    // endregion

    // region Greater than

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain gt(final Property<T, V> property, final V value) {
        return gt(Slot.AND, property, value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain gt(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain gt(final String property, final Object value) {
        return gt(Slot.AND, property, value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain gt(final Slot slot, final String property, final Object value);

    /**
     * 大于
     * @param column 属性
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colGt(final String column, final Object value) {
        return colGt(Slot.AND, column, value);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param column 属性
     * @param value  值
     * @return {@link Chain}
     */
    Chain colGt(final Slot slot, final String column, final Object value);

    // endregion

    // region Greater than or equal to

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain ge(final Property<T, V> property, final V value) {
        return ge(Slot.AND, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain ge(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain ge(final String property, final Object value) {
        return ge(Slot.AND, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain ge(final Slot slot, final String property, final Object value);

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colGe(final String column, final Object value) {
        return colGe(Slot.AND, column, value);
    }

    /**
     * 大于或等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    Chain colGe(final Slot slot, final String column, final Object value);

    // endregion

    // region Less than

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain lt(final Property<T, V> property, final V value) {
        return lt(Slot.AND, property, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain lt(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain lt(final String property, final Object value) {
        return lt(Slot.AND, property, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain lt(final Slot slot, final String property, final Object value);

    /**
     * 小于
     * @param column 属性
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLt(final String column, final Object value) {
        return colLt(Slot.AND, column, value);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param column 属性
     * @param value  值
     * @return {@link Chain}
     */
    Chain colLt(final Slot slot, final String column, final Object value);

    // endregion

    // region Less than or equal to

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain le(final Property<T, V> property, final V value) {
        return le(Slot.AND, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain le(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain le(final String property, final Object value) {
        return le(Slot.AND, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain le(final Slot slot, final String property, final Object value);

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLe(final String column, final Object value) {
        return colLe(Slot.AND, column, value);
    }

    /**
     * 大于或等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    Chain colLe(final Slot slot, final String column, final Object value);

    // endregion

    // endregion

    // region Multiple properties

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @return {@link Chain}
     */
    default <V1, V2> Chain eq(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2) {
        return eq(Slot.AND, p1, v1, p2, v2);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @return {@link Chain}
     */
    default <V1, V2> Chain eq(final Slot slot, final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2,
                              final V2 v2) {
        return this.eq(slot, p1, v1).eq(slot, p2, v2);
    }

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @param <V3> 属性3类型
     * @return {@link Chain}
     */
    default <V1, V2, V3> Chain eq(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2,
                                  final Property<T, V3> p3, final V3 v3) {
        return eq(Slot.AND, p1, v1, p2, v2, p3, v3);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @param <V3> 属性3类型
     * @return {@link Chain}
     */
    default <V1, V2, V3> Chain eq(final Slot slot, final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2,
                                  final V2 v2, final Property<T, V3> p3, final V3 v3) {
        return this.eq(slot, p1, v1).eq(slot, p2, v2).eq(slot, p3, v3);
    }


    /**
     * 等于
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @return {@link Chain}
     */
    default Chain eq(final String p1, final Object v1, final String p2, final Object v2) {
        return eq(Slot.AND, p1, v1, p2, v2);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @return {@link Chain}
     */
    default Chain eq(final Slot slot, final String p1, final Object v1, final String p2, final Object v2) {
        return this.eq(slot, p1, v1).eq(slot, p2, v2);
    }

    /**
     * 等于
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @param p3 属性3
     * @param v3 属性3对应值
     * @return {@link Chain}
     */
    default Chain eq(final String p1, final Object v1, final String p2, final Object v2,
                     final String p3, final Object v3) {
        return eq(Slot.AND, p1, v1, p2, v2, p3, v3);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @return {@link Chain}
     */
    default Chain eq(final Slot slot, final String p1, final Object v1, final String p2, final Object v2,
                     final String p3, final Object v3) {
        return this.eq(slot, p1, v1).eq(slot, p2, v2).eq(slot, p3, v3);
    }


    /**
     * 等于
     * @param properties 属性-值集合
     * @return {@link Chain}
     */
    default Chain eq(final Map<String, Object> properties) {
        return eq(Slot.AND, properties);
    }

    /**
     * 等于
     * @param properties 属性-值集合
     * @param slot       {@link Slot}
     * @return {@link Chain}
     */
    Chain eq(final Slot slot, final Map<String, Object> properties);

    /**
     * 等于
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @return {@link Chain}
     */
    default Chain colEq(final String c1, final Object v1, final String c2, final Object v2) {
        return this.colEq(Slot.AND, c1, v1, c2, v2);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param c1   字段1
     * @param v1   字段1对应值
     * @param c2   字段2
     * @param v2   字段2对应值
     * @return {@link Chain}
     */
    default Chain colEq(final Slot slot, final String c1, final Object v1, final String c2, final Object v2) {
        return this.colEq(slot, c1, v1).colEq(slot, c2, v2);
    }

    /**
     * 等于
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @param c3 字段3
     * @param v3 字段3对应值
     * @return {@link Chain}
     */
    default Chain colEq(final String c1, final Object v1, final String c2,
                        final Object v2, final String c3, final Object v3) {
        return this.colEq(Slot.AND, c1, v1, c2, v2, c3, v3);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param c1   字段1
     * @param v1   字段1对应值
     * @param c2   字段2
     * @param v2   字段2对应值
     * @param c3   字段3
     * @param v3   字段3对应值
     * @return {@link Chain}
     */
    default Chain colEq(final Slot slot, final String c1, final Object v1, final String c2,
                        final Object v2, final String c3, final Object v3) {
        return this.colEq(slot, c1, v1).colEq(slot, c2, v2).colEq(slot, c3, v3);
    }

    /**
     * 等于
     * @param columns 字段-值集合
     * @return {@link Chain}
     */
    default Chain colEq(final Map<String, Object> columns) {
        return colEq(Slot.AND, columns);
    }

    /**
     * 等于
     * @param slot    {@link Slot}
     * @param columns 字段-值集合
     * @return {@link Chain}
     */
    Chain colEq(final Slot slot, final Map<String, Object> columns);

    // endregion
}
