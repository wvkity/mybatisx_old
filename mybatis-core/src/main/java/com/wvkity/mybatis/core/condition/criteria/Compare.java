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
        return idEq(value, Slot.AND);
    }

    /**
     * 主键等于
     * @param value 值
     * @param slot  {@link Slot}
     * @return {@link Chain}
     */
    Chain idEq(final Object value, final Slot slot);

    /**
     * 等于
     * @param property {@link Property}
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain eq(final Property<T, V> property, final V value) {
        return eq(property, value, Slot.AND);
    }

    /**
     * 等于
     * @param property {@link Property}
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain eq(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain eq(final String property, final Object value) {
        return eq(property, value, Slot.AND);
    }

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain eq(final String property, final Object value, final Slot slot);

    /**
     * 等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colEq(final String column, final Object value) {
        return colEq(column, value, Slot.AND);
    }

    /**
     * 等于
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colEq(final String column, final Object value, final Slot slot);

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
        return ne(property, value, Slot.AND);
    }

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain ne(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain ne(final String property, final Object value) {
        return ne(property, value, Slot.AND);
    }

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain ne(final String property, final Object value, final Slot slot);


    /**
     * 不等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNe(final String column, final Object value) {
        return colNe(column, value, Slot.AND);
    }

    /**
     * 不等于
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colNe(final String column, final Object value, final Slot slot);

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
        return gt(property, value, Slot.AND);
    }

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain gt(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain gt(final String property, final Object value) {
        return gt(property, value, Slot.AND);
    }

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain gt(final String property, final Object value, final Slot slot);

    /**
     * 大于
     * @param column 属性
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colGt(final String column, final Object value) {
        return colGt(column, value, Slot.AND);
    }

    /**
     * 大于
     * @param column 属性
     * @param value  值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colGt(final String column, final Object value, final Slot slot);

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
        return ge(property, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain ge(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain ge(final String property, final Object value) {
        return ge(property, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain ge(final String property, final Object value, final Slot slot);

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colGe(final String column, final Object value) {
        return colGe(column, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colGe(final String column, final Object value, final Slot slot);

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
        return lt(property, value, Slot.AND);
    }

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain lt(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain lt(final String property, final Object value) {
        return lt(property, value, Slot.AND);
    }

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain lt(final String property, final Object value, final Slot slot);

    /**
     * 小于
     * @param column 属性
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLt(final String column, final Object value) {
        return colLt(column, value, Slot.AND);
    }

    /**
     * 大于
     * @param column 属性
     * @param value  值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colLt(final String column, final Object value, final Slot slot);

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
        return le(property, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain le(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain le(final String property, final Object value) {
        return le(property, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain le(final String property, final Object value, final Slot slot);

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLe(final String column, final Object value) {
        return colLe(column, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colLe(final String column, final Object value, final Slot slot);

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
        return eq(p1, v1, p2, v2, Slot.AND);
    }

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param slot {@link Slot}
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @return {@link Chain}
     */
    default <V1, V2> Chain eq(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2,
                              final Slot slot) {
        return this.eq(p1, v1, slot).eq(p2, v2, slot);
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
        return eq(p1, v1, p2, v2, p3, v3, Slot.AND);
    }

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @param slot {@link Slot}
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @param <V3> 属性3类型
     * @return {@link Chain}
     */
    default <V1, V2, V3> Chain eq(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2,
                                  final Property<T, V3> p3, final V3 v3, final Slot slot) {
        return this.eq(p1, v1, slot).eq(p2, v2, slot).eq(p3, v3, slot);
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
        return eq(p1, v1, p2, v2, Slot.AND);
    }

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param slot {@link Slot}
     * @return {@link Chain}
     */
    default Chain eq(final String p1, final Object v1, final String p2, final Object v2, final Slot slot) {
        return this.eq(p1, v1, slot).eq(p2, v2, slot);
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
        return eq(p1, v1, p2, v2, p3, v3, Slot.AND);
    }

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @param slot {@link Slot}
     * @return {@link Chain}
     */
    default Chain eq(final String p1, final Object v1, final String p2, final Object v2,
                     final String p3, final Object v3, final Slot slot) {
        return this.eq(p1, v1, slot).eq(p2, v2, slot).eq(p3, v3, slot);
    }


    /**
     * 等于
     * @param properties 属性-值集合
     * @return {@link Chain}
     */
    default Chain eq(final Map<String, Object> properties) {
        return eq(properties, Slot.AND);
    }

    /**
     * 等于
     * @param properties 属性-值集合
     * @param slot       {@link Slot}
     * @return {@link Chain}
     */
    Chain eq(final Map<String, Object> properties, final Slot slot);

    /**
     * 等于
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @return {@link Chain}
     */
    default Chain colEq(final String c1, final Object v1, final String c2, final Object v2) {
        return this.colEq(c1, v1, c2, v2, Slot.AND);
    }

    /**
     * 等于
     * @param c1   字段1
     * @param v1   字段1对应值
     * @param c2   字段2
     * @param v2   字段2对应值
     * @param slot {@link Slot}
     * @return {@link Chain}
     */
    default Chain colEq(final String c1, final Object v1, final String c2, final Object v2, final Slot slot) {
        return this.colEq(c1, v1, slot).colEq(c2, v2, slot);
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
        return this.colEq(c1, v1, c2, v2, c3, v3, Slot.AND);
    }

    /**
     * 等于
     * @param c1   字段1
     * @param v1   字段1对应值
     * @param c2   字段2
     * @param v2   字段2对应值
     * @param c3   字段3
     * @param v3   字段3对应值
     * @param slot {@link Slot}
     * @return {@link Chain}
     */
    default Chain colEq(final String c1, final Object v1, final String c2,
                        final Object v2, final String c3, final Object v3, final Slot slot) {
        return this.colEq(c1, v1, slot).colEq(c2, v2, slot).colEq(c3, v3, slot);
    }

    /**
     * 等于
     * @param properties 字段-值集合
     * @return {@link Chain}
     */
    default Chain colEq(final Map<String, Object> properties) {
        return colEq(properties, Slot.AND);
    }

    /**
     * 等于
     * @param properties 字段-值集合
     * @param slot       {@link Slot}
     * @return {@link Chain}
     */
    Chain colEq(final Map<String, Object> properties, final Slot slot);

    // endregion
}
