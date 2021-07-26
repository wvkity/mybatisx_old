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
package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.support.constant.Slot;

import java.util.Map;

/**
 * 基础比较条件接口
 * @param <T> 实体类型
 * @param <C> 子类
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
interface CommonCompare<T, C extends CommonCompare<T, C>> extends SlotSymbol<T, C> {

    // region Equal to condition

    /**
     * 等于
     * @param column 字段名
     * @param value  值
     * @return {@code this}
     */
    default C colEq(final String column, final Object value) {
        return this.colEq(this.getSlot(), column, value);
    }

    /**
     * 等于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param value  值
     * @return {@code this}
     */
    C colEq(final Slot slot, final String column, final Object value);

    /**
     * 等于
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C colEqq(final String column, final ExtCriteria<?> query) {
        return this.colEqq(this.getSlot(), column, query);
    }

    /**
     * 等于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C colEqq(final Slot slot, final String column, final ExtCriteria<?> query);

    /**
     * 等于
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @return {@code this}
     */
    default C colEq(final String c1, final Object v1, final String c2, final Object v2) {
        return this.colEq(this.getSlot(), c1, v1, c2, v2);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param c1   字段1
     * @param v1   字段1对应值
     * @param c2   字段2
     * @param v2   字段2对应值
     * @return {@code this}
     */
    default C colEq(final Slot slot, final String c1, final Object v1, final String c2, final Object v2) {
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
     * @return {@code this}
     */
    default C colEq(final String c1, final Object v1, final String c2,
                    final Object v2, final String c3, final Object v3) {
        return this.colEq(this.getSlot(), c1, v1, c2, v2, c3, v3);
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
     * @return {@code this}
     */
    default C colEq(final Slot slot, final String c1, final Object v1, final String c2,
                    final Object v2, final String c3, final Object v3) {
        return this.colEq(slot, c1, v1).colEq(slot, c2, v2).colEq(slot, c3, v3);
    }

    /**
     * 等于
     * @param columns 字段-值集合
     * @return {@code this}
     */
    default C colEq(final Map<String, Object> columns) {
        return colEq(this.getSlot(), columns);
    }

    /**
     * 等于
     * @param slot    {@link Slot}
     * @param columns 字段-值集合
     * @return {@code this}
     */
    C colEq(final Slot slot, final Map<String, Object> columns);

    // endregion

    // region Not equal to condition

    /**
     * 不等于
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNe(final String column, final Object value) {
        return colNe(this.getSlot(), column, value);
    }

    /**
     * 不等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    C colNe(final Slot slot, final String column, final Object value);

    /**
     * 不等于
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C colNeq(final String column, final ExtCriteria<?> query) {
        return this.colNeq(this.getSlot(), column, query);
    }

    /**
     * 不等于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C colNeq(final Slot slot, final String column, final ExtCriteria<?> query);

    // endregion

    // region greater than condition

    /**
     * 大于
     * @param column 属性
     * @param value  值
     * @return {@code this}
     */
    default C colGt(final String column, final Object value) {
        return colGt(this.getSlot(), column, value);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param column 属性
     * @param value  值
     * @return {@code this}
     */
    C colGt(final Slot slot, final String column, final Object value);

    /**
     * 大于
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C colGtq(final String column, final ExtCriteria<?> query) {
        return this.colGtq(this.getSlot(), column, query);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C colGtq(final Slot slot, final String column, final ExtCriteria<?> query);

    // endregion

    // region Greater than or equal to condition

    /**
     * 大于或等于
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colGe(final String column, final Object value) {
        return colGe(this.getSlot(), column, value);
    }

    /**
     * 大于或等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    C colGe(final Slot slot, final String column, final Object value);

    /**
     * 大于或等于
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C colGeq(final String column, final ExtCriteria<?> query) {
        return this.colGeq(this.getSlot(), column, query);
    }

    /**
     * 大于或等于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C colGeq(final Slot slot, final String column, final ExtCriteria<?> query);

    // endregion

    // region Less than condition

    /**
     * 小于
     * @param column 属性
     * @param value  值
     * @return {@code this}
     */
    default C colLt(final String column, final Object value) {
        return colLt(this.getSlot(), column, value);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param column 属性
     * @param value  值
     * @return {@code this}
     */
    C colLt(final Slot slot, final String column, final Object value);

    /**
     * 小于
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C colLtq(final String column, final ExtCriteria<?> query) {
        return this.colLtq(this.getSlot(), column, query);
    }

    /**
     * 小于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C colLtq(final Slot slot, final String column, final ExtCriteria<?> query);

    // endregion

    // region Less than or equal to condition

    /**
     * 小于或等于
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLe(final String column, final Object value) {
        return colLe(this.getSlot(), column, value);
    }

    /**
     * 小于或等于
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    C colLe(final Slot slot, final String column, final Object value);

    /**
     * 小于或等于
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C colLeq(final String column, final ExtCriteria<?> query) {
        return this.colLeq(this.getSlot(), column, query);
    }

    /**
     * 小于或等于
     * @param slot   {@link Slot}
     * @param column 字段名
     * @param query  {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C colLeq(final Slot slot, final String column, final ExtCriteria<?> query);

    // endregion

    // region Column equal to condition

    /**
     * 字段相等
     * @param column        字段名
     * @param otherCriteria {@link ExtCriteria}
     * @param otherColumn   字段名
     * @return {@code this}
     */
    C colCe(final String column, final ExtCriteria<?> otherCriteria, final String otherColumn);

    /**
     * 字段相等
     * @param column        字段名
     * @param otherCriteria {@link ExtCriteria}
     * @return {@code this}
     */
    C colCeWith(final String column, final ExtCriteria<?> otherCriteria);

    /**
     * 字段相等
     * @param column        字段名
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty {@link Property}
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> C colCeWith(final String column, final ExtCriteria<E> otherCriteria,
                            final Property<E, ?> otherProperty) {
        return this.colCeWith(column, otherCriteria, otherCriteria.getConverter().toProperty(otherProperty));
    }

    /**
     * 字段相等
     * @param column        字段名
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty 属性
     * @return {@code this}
     */
    default C colCeWith(final String column, final ExtCriteria<?> otherCriteria, final String otherProperty) {
        return this.colCe(column, otherCriteria, otherCriteria.getConverter().convert(otherProperty).getColumn());
    }

    // endregion

}
