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

import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.convert.converter.PropertyConverter;

/**
 * 比较条件
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public interface Compare<T, Chain extends Compare<T, Chain>> extends PropertyConverter<T> {

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
    default <V> Chain propEq(final Property<T, V> property, final V value) {
        return propEq(property, value, Slot.AND);
    }

    /**
     * 等于
     * @param property {@link Property}
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain propEq(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain propEq(final String property, final Object value) {
        return propEq(property, value, Slot.AND);
    }

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain propEq(final String property, final Object value, final Slot slot);

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
    default <V> Chain propNe(final Property<T, V> property, final V value) {
        return propNe(property, value, Slot.AND);
    }

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain propNe(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain propNe(final String property, final Object value) {
        return propNe(property, value, Slot.AND);
    }

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain propNe(final String property, final Object value, final Slot slot);


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
    default <V> Chain propGt(final Property<T, V> property, final V value) {
        return propGt(property, value, Slot.AND);
    }

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain propGt(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain propGt(final String property, final Object value) {
        return propGt(property, value, Slot.AND);
    }

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain propGt(final String property, final Object value, final Slot slot);

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
    default <V> Chain propGe(final Property<T, V> property, final V value) {
        return propGe(property, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain propGe(final Property<T, V> property, final V value, final Slot slot);

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain propGe(final String property, final Object value) {
        return propGe(property, value, Slot.AND);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain propGe(final String property, final Object value, final Slot slot);

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
    // endregion

    // region Less than or equal to
    // endregion

    // endregion

    // region Multiple properties

    // endregion
}
