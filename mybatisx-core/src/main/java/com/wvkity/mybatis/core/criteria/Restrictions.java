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

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.expr.ImmediateEqual;
import com.wvkity.mybatis.core.expr.ImmediateGreaterThan;
import com.wvkity.mybatis.core.expr.ImmediateGreaterThanOrEqual;
import com.wvkity.mybatis.core.expr.ImmediateIn;
import com.wvkity.mybatis.core.expr.ImmediateLessThan;
import com.wvkity.mybatis.core.expr.ImmediateLessThanOrEqual;
import com.wvkity.mybatis.core.expr.ImmediateNotEqual;
import com.wvkity.mybatis.core.expr.ImmediateNotIn;
import com.wvkity.mybatis.core.expr.ImmediateNotNull;
import com.wvkity.mybatis.core.expr.ImmediateNull;
import com.wvkity.mybatis.core.expr.StandardEqual;
import com.wvkity.mybatis.core.expr.StandardGreaterThan;
import com.wvkity.mybatis.core.expr.StandardGreaterThanOrEqual;
import com.wvkity.mybatis.core.expr.StandardIdEqual;
import com.wvkity.mybatis.core.expr.StandardIn;
import com.wvkity.mybatis.core.expr.StandardLessThan;
import com.wvkity.mybatis.core.expr.StandardLessThanOrEqual;
import com.wvkity.mybatis.core.expr.StandardNotEqual;
import com.wvkity.mybatis.core.expr.StandardNotIn;
import com.wvkity.mybatis.core.expr.StandardNotNull;
import com.wvkity.mybatis.core.expr.StandardNull;
import com.wvkity.mybatis.core.property.PropertiesMappingCache;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Collection;

/**
 * 条件工具
 * @author wvkity
 * @created 2021-05-03
 * @since 1.0.0
 */
public final class Restrictions {
    private Restrictions() {
    }

    static String convert(final Property<?, ?> property) {
        return PropertiesMappingCache.methodToProperty(PropertiesMappingCache.parse(property).getImplMethodName());
    }

    // region Compare conditions

    // region Id equal condition

    /**
     * ID等于
     * @param criteria {@link Criteria}
     * @param value    值
     * @return {@link StandardIdEqual}
     */
    public static StandardIdEqual idEq(final Criteria<?> criteria, final Object value) {
        return idEq(Slot.AND, criteria, value);
    }

    /**
     * ID等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param value    值
     * @return {@link StandardIdEqual}
     */
    public static StandardIdEqual idEq(final Slot slot, final Criteria<?> criteria, final Object value) {
        return new StandardIdEqual(criteria, slot, value);
    }

    // endregion

    // region Equal condition

    /**
     * 等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIdEqual}
     */
    public static <T, V> StandardEqual eq(final Criteria<T> criteria, final Property<T, V> property, final V value) {
        return eq(criteria, convert(property), value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIdEqual}
     */
    public static <T, V> StandardEqual eq(final Slot slot, final Criteria<T> criteria,
                                          final Property<T, V> property, final V value) {
        return eq(slot, criteria, convert(property), value);
    }

    /**
     * 等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardIdEqual}
     */
    public static StandardEqual eq(final Criteria<?> criteria, final String property, final Object value) {
        return eq(Slot.AND, criteria, property, value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardIdEqual}
     */
    public static StandardEqual eq(final Slot slot, final Criteria<?> criteria,
                                   final String property, final Object value) {
        return new StandardEqual(criteria, property, slot, value);
    }

    /**
     * 等于
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateEqual}
     */
    public static ImmediateEqual colEq(final Criteria<?> criteria, final String column, final Object value) {
        return colEq(Slot.AND, criteria, column, value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateEqual}
     */
    public static ImmediateEqual colEq(final Slot slot, final Criteria<?> criteria,
                                       final String column, final Object value) {
        return new ImmediateEqual(criteria, column, slot, value);
    }

    /**
     * 等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateEqual}
     */
    public static ImmediateEqual colEq(final String alias, final String column, final Object value) {
        return colEq(Slot.AND, alias, column, value);
    }

    /**
     * 等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateEqual}
     */
    public static ImmediateEqual colEq(final Slot slot, final String alias, final String column, final Object value) {
        return new ImmediateEqual(alias, column, slot, value);
    }

    // endregion

    // region Not equal condition

    /**
     * 不等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotEqual}
     */
    public static <T, V> StandardNotEqual ne(final Criteria<T> criteria, final Property<T, V> property, final V value) {
        return ne(criteria, convert(property), value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotEqual}
     */
    public static <T, V> StandardNotEqual ne(final Slot slot, final Criteria<T> criteria,
                                             final Property<T, V> property, final V value) {
        return ne(slot, criteria, convert(property), value);
    }

    /**
     * 不等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardNotEqual}
     */
    public static StandardNotEqual ne(final Criteria<?> criteria, final String property, final Object value) {
        return ne(Slot.AND, criteria, property, value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardNotEqual}
     */
    public static StandardNotEqual ne(final Slot slot, final Criteria<?> criteria,
                                      final String property, final Object value) {
        return new StandardNotEqual(criteria, property, slot, value);
    }

    /**
     * 不等于
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateNotEqual}
     */
    public static ImmediateNotEqual colNe(final Criteria<?> criteria, final String column, final Object value) {
        return colNe(Slot.AND, criteria, column, value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateNotEqual}
     */
    public static ImmediateNotEqual colNe(final Slot slot, final Criteria<?> criteria,
                                          final String column, final Object value) {
        return new ImmediateNotEqual(criteria, column, slot, value);
    }

    /**
     * 不等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateNotEqual}
     */
    public static ImmediateNotEqual colNe(final String alias, final String column, final Object value) {
        return colNe(Slot.AND, alias, column, value);
    }

    /**
     * 不等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateNotEqual}
     */
    public static ImmediateNotEqual colNe(final Slot slot, final String alias, final String column,
                                          final Object value) {
        return new ImmediateNotEqual(alias, column, slot, value);
    }

    // endregion

    // region Less than condition

    /**
     * 小于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardLessThan}
     */
    public static <T, V> StandardLessThan lt(final Criteria<T> criteria,
                                             final Property<T, V> property, final V value) {
        return lt(criteria, convert(property), value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardLessThan}
     */
    public static <T, V> StandardLessThan lt(final Slot slot, final Criteria<T> criteria,
                                             final Property<T, V> property, final V value) {
        return lt(slot, criteria, convert(property), value);
    }

    /**
     * 小于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardLessThan}
     */
    public static StandardLessThan lt(final Criteria<?> criteria,
                                      final String property, final Object value) {
        return lt(Slot.AND, criteria, property, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardLessThan}
     */
    public static StandardLessThan lt(final Slot slot, final Criteria<?> criteria,
                                      final String property, final Object value) {
        return new StandardLessThan(criteria, property, slot, value);
    }

    /**
     * 小于
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link StandardLessThan}
     */
    public static ImmediateLessThan colLt(final Criteria<?> criteria, final String column, final Object value) {
        return colLt(Slot.AND, criteria, column, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link StandardLessThan}
     */
    public static ImmediateLessThan colLt(final Slot slot, final Criteria<?> criteria,
                                          final String column, final Object value) {
        return new ImmediateLessThan(criteria, column, slot, value);
    }

    /**
     * 小于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link StandardLessThan}
     */
    public static ImmediateLessThan colLt(final String alias, final String column, final Object value) {
        return colLt(Slot.AND, alias, column, value);
    }

    /**
     * 小于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link StandardLessThan}
     */
    public static ImmediateLessThan colLt(final Slot slot, final String alias,
                                          final String column, final Object value) {
        return new ImmediateLessThan(alias, column, slot, value);
    }

    // endregion

    // region Less than or equal to

    /**
     * 小于或等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardLessThanOrEqual}
     */
    public static <T, V> StandardLessThanOrEqual le(final Criteria<T> criteria,
                                                    final Property<T, V> property, final V value) {
        return le(criteria, convert(property), value);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardLessThanOrEqual}
     */
    public static <T, V> StandardLessThanOrEqual le(final Slot slot, final Criteria<T> criteria,
                                                    final Property<T, V> property, final V value) {
        return le(slot, criteria, convert(property), value);
    }

    /**
     * 小于或等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardLessThanOrEqual}
     */
    public static StandardLessThanOrEqual le(final Criteria<?> criteria,
                                             final String property, final Object value) {
        return le(Slot.AND, criteria, property, value);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardLessThanOrEqual}
     */
    public static StandardLessThanOrEqual le(final Slot slot, final Criteria<?> criteria,
                                             final String property, final Object value) {
        return new StandardLessThanOrEqual(criteria, property, slot, value);
    }

    /**
     * 小于或等于
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateLessThanOrEqual}
     */
    public static ImmediateLessThanOrEqual colLe(final Criteria<?> criteria, final String column, final Object value) {
        return colLe(Slot.AND, criteria, column, value);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateLessThanOrEqual}
     */
    public static ImmediateLessThanOrEqual colLe(final Slot slot, final Criteria<?> criteria,
                                                 final String column, final Object value) {
        return new ImmediateLessThanOrEqual(criteria, column, slot, value);
    }

    /**
     * 小于或等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateLessThanOrEqual}
     */
    public static ImmediateLessThanOrEqual colLe(final String alias, final String column, final Object value) {
        return colLe(Slot.AND, alias, column, value);
    }

    /**
     * 小于或等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateLessThanOrEqual}
     */
    public static ImmediateLessThanOrEqual colLe(final Slot slot, final String alias,
                                                 final String column, final Object value) {
        return new ImmediateLessThanOrEqual(alias, column, slot, value);
    }

    // endregion

    // region Greater than

    /**
     * 大于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardGreaterThan}
     */
    public static <T, V> StandardGreaterThan gt(final Criteria<T> criteria,
                                                final Property<T, V> property, final V value) {
        return gt(criteria, convert(property), value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardGreaterThan}
     */
    public static <T, V> StandardGreaterThan gt(final Slot slot, final Criteria<T> criteria,
                                                final Property<T, V> property, final V value) {
        return gt(slot, criteria, convert(property), value);
    }

    /**
     * 大于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardGreaterThan}
     */
    public static StandardGreaterThan gt(final Criteria<?> criteria,
                                         final String property, final Object value) {
        return gt(Slot.AND, criteria, property, value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardGreaterThan}
     */
    public static StandardGreaterThan gt(final Slot slot, final Criteria<?> criteria,
                                         final String property, final Object value) {
        return new StandardGreaterThan(criteria, property, slot, value);
    }

    /**
     * 大于
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateGreaterThan}
     */
    public static ImmediateGreaterThan colGt(final Criteria<?> criteria, final String column, final Object value) {
        return colGt(Slot.AND, criteria, column, value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateGreaterThan}
     */
    public static ImmediateGreaterThan colGt(final Slot slot, final Criteria<?> criteria,
                                             final String column, final Object value) {
        return new ImmediateGreaterThan(criteria, column, slot, value);
    }

    /**
     * 大于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateGreaterThan}
     */
    public static ImmediateGreaterThan colGt(final String alias, final String column, final Object value) {
        return colGt(Slot.AND, alias, column, value);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateGreaterThan}
     */
    public static ImmediateGreaterThan colGt(final Slot slot, final String alias,
                                             final String column, final Object value) {
        return new ImmediateGreaterThan(alias, column, slot, value);
    }

    // endregion

    // region Greater than or equal to

    /**
     * 大于或等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardGreaterThanOrEqual}
     */
    public static <T, V> StandardGreaterThanOrEqual ge(final Criteria<T> criteria,
                                                       final Property<T, V> property, final V value) {
        return ge(criteria, convert(property), value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardGreaterThanOrEqual}
     */
    public static <T, V> StandardGreaterThanOrEqual ge(final Slot slot, final Criteria<T> criteria,
                                                       final Property<T, V> property, final V value) {
        return ge(slot, criteria, convert(property), value);
    }

    /**
     * 大于或等于
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardGreaterThanOrEqual}
     */
    public static StandardGreaterThanOrEqual ge(final Criteria<?> criteria,
                                                final String property, final Object value) {
        return ge(Slot.AND, criteria, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param value    值
     * @return {@link StandardGreaterThanOrEqual}
     */
    public static StandardGreaterThanOrEqual ge(final Slot slot, final Criteria<?> criteria,
                                                final String property, final Object value) {
        return new StandardGreaterThanOrEqual(criteria, property, slot, value);
    }

    /**
     * 大于或等于
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateGreaterThanOrEqual}
     */
    public static ImmediateGreaterThanOrEqual colGe(final Criteria<?> criteria, final String column,
                                                    final Object value) {
        return colGe(Slot.AND, criteria, column, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param value    值
     * @return {@link ImmediateLessThanOrEqual}
     */
    public static ImmediateGreaterThanOrEqual colGe(final Slot slot, final Criteria<?> criteria,
                                                    final String column, final Object value) {
        return new ImmediateGreaterThanOrEqual(criteria, column, slot, value);
    }

    /**
     * 大于或等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateGreaterThanOrEqual}
     */
    public static ImmediateGreaterThanOrEqual colGe(final String alias, final String column, final Object value) {
        return colGe(Slot.AND, alias, column, value);
    }

    /**
     * 大于或等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @return {@link ImmediateGreaterThanOrEqual}
     */
    public static ImmediateGreaterThanOrEqual colGe(final Slot slot, final String alias,
                                                    final String column, final Object value) {
        return new ImmediateGreaterThanOrEqual(alias, column, slot, value);
    }

    // endregion

    // endregion

    // region Nullable conditions

    // region Is null condition

    /**
     * is null
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param <T>      实体类型
     * @return {@link StandardNull}
     */
    public static <T> StandardNull isNull(final Criteria<T> criteria, final Property<T, ?> property) {
        return isNull(criteria, convert(property));
    }

    /**
     * is null
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param <T>      实体类型
     * @return {@link StandardNull}
     */
    public static <T> StandardNull isNull(final Slot slot, final Criteria<T> criteria, final Property<T, ?> property) {
        return isNull(slot, criteria, convert(property));
    }

    /**
     * is null
     * @param criteria {@link Criteria}
     * @param property 属性
     * @return {@link StandardNull}
     */
    public static StandardNull isNull(final Criteria<?> criteria, final String property) {
        return isNull(Slot.AND, criteria, property);
    }

    /**
     * is null
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @return {@link StandardNull}
     */
    public static StandardNull isNull(final Slot slot, final Criteria<?> criteria, final String property) {
        return new StandardNull(criteria, property, slot);
    }

    /**
     * is null
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @return {@link ImmediateNull}
     */
    public static ImmediateNull colIsNull(final Criteria<?> criteria, final String column) {
        return colIsNull(Slot.AND, criteria, column);
    }

    /**
     * is null
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @return {@link ImmediateNull}
     */
    public static ImmediateNull colIsNull(final Slot slot, final Criteria<?> criteria, final String column) {
        return new ImmediateNull(criteria, column, slot);
    }

    /**
     * is null
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImmediateNull}
     */
    public static ImmediateNull colIsNull(final String alias, final String column) {
        return colIsNull(Slot.AND, alias, column);
    }

    /**
     * is null
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImmediateNull}
     */
    public static ImmediateNull colIsNull(final Slot slot, final String alias, final String column) {
        return new ImmediateNull(alias, column, slot);
    }

    // endregion

    // region Not is null condition

    /**
     * not is null
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param <T>      实体类型
     * @return {@link StandardNotNull}
     */
    public static <T> StandardNotNull notNull(final Criteria<T> criteria, final Property<T, ?> property) {
        return notNull(criteria, convert(property));
    }

    /**
     * not is null
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param <T>      实体类型
     * @return {@link StandardNotNull}
     */
    public static <T> StandardNotNull notNull(final Slot slot, final Criteria<T> criteria,
                                              final Property<T, ?> property) {
        return notNull(slot, criteria, convert(property));
    }

    /**
     * not is null
     * @param criteria {@link Criteria}
     * @param property 属性
     * @return {@link StandardNotNull}
     */
    public static StandardNotNull notNull(final Criteria<?> criteria, final String property) {
        return notNull(Slot.AND, criteria, property);
    }

    /**
     * not is null
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @return {@link StandardNotNull}
     */
    public static StandardNotNull notNull(final Slot slot, final Criteria<?> criteria, final String property) {
        return new StandardNotNull(criteria, property, slot);
    }

    /**
     * not is null
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @return {@link ImmediateNotNull}
     */
    public static ImmediateNotNull colNotNull(final Criteria<?> criteria, final String column) {
        return colNotNull(Slot.AND, criteria, column);
    }

    /**
     * not is null
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @return {@link ImmediateNotNull}
     */
    public static ImmediateNotNull colNotNull(final Slot slot, final Criteria<?> criteria, final String column) {
        return new ImmediateNotNull(criteria, column, slot);
    }

    /**
     * not is null
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImmediateNotNull}
     */
    public static ImmediateNotNull colNotNull(final String alias, final String column) {
        return colNotNull(Slot.AND, alias, column);
    }

    /**
     * not is null
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImmediateNotNull}
     */
    public static ImmediateNotNull colNotNull(final Slot slot, final String alias, final String column) {
        return new ImmediateNotNull(alias, column, slot);
    }

    // endregion

    // endregion

    // region Range conditions

    // region In conditions

    /**
     * in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIn}
     */
    @SafeVarargs
    public static <T, V> StandardIn in(final Criteria<T> criteria, final Property<T, V> property,
                                       final V... values) {
        return in(criteria, convert(property), Objects.asList(values));
    }

    /**
     * in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIn}
     */
    public static <T, V> StandardIn in(final Criteria<T> criteria, final Property<T, V> property,
                                       final Collection<V> values) {
        return in(criteria, convert(property), values);
    }

    /**
     * in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIn}
     */
    @SafeVarargs
    public static <T, V> StandardIn in(final Slot slot, final Criteria<T> criteria,
                                       final Property<T, V> property, final V... values) {
        return in(slot, criteria, convert(property), Objects.asList(values));
    }

    /**
     * in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIn}
     */
    public static <T, V> StandardIn in(final Slot slot, final Criteria<T> criteria,
                                       final Property<T, V> property, final Collection<V> values) {
        return in(slot, criteria, convert(property), values);
    }

    /**
     * in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @return {@link StandardIn}
     */
    public static StandardIn in(final Criteria<?> criteria, final String property, final Object... values) {
        return in(criteria, property, Objects.asList(values));
    }

    /**
     * in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @return {@link StandardIn}
     */
    public static StandardIn in(final Criteria<?> criteria, final String property, final Collection<?> values) {
        return in(Slot.AND, criteria, property, values);
    }

    /**
     * in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @return {@link StandardIn}
     */
    public static StandardIn in(final Slot slot, final Criteria<?> criteria,
                                final String property, final Object... values) {
        return in(slot, criteria, property, Objects.asList(values));
    }

    /**
     * in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardIn}
     */
    public static <T, V> StandardIn in(final Slot slot, final Criteria<?> criteria,
                                       final String property, final Collection<?> values) {
        return new StandardIn(criteria, property, slot, values);
    }

    /**
     * in
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final Criteria<?> criteria,
                                    final String column, final Object... values) {
        return colIn(Slot.AND, criteria, column, Objects.asList(values));
    }

    /**
     * in
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final Criteria<?> criteria,
                                    final String column, final Collection<?> values) {
        return colIn(Slot.AND, criteria, column, values);
    }

    /**
     * in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final Slot slot, final Criteria<?> criteria,
                                    final String column, final Object... values) {
        return colIn(slot, criteria, column, Objects.asList(values));
    }

    /**
     * in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final Slot slot, final Criteria<?> criteria,
                                    final String column, final Collection<?> values) {
        return new ImmediateIn(criteria, column, slot, values);
    }

    /**
     * in
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final String alias,
                                    final String column, final Object... values) {
        return colIn(Slot.AND, alias, column, Objects.asList(values));
    }

    /**
     * in
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final String alias,
                                    final String column, final Collection<?> values) {
        return colIn(Slot.AND, alias, column, values);
    }

    /**
     * in
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final Slot slot, final String alias,
                                    final String column, final Object... values) {
        return colIn(slot, alias, column, Objects.asList(values));
    }

    /**
     * in
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateIn}
     */
    public static ImmediateIn colIn(final Slot slot, final String alias,
                                    final String column, final Collection<?> values) {
        return new ImmediateIn(alias, column, slot, values);
    }

    // endregion

    // region Not in condition

    /**
     * not in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotIn}
     */
    @SafeVarargs
    public static <T, V> StandardNotIn notIn(final Criteria<T> criteria, final Property<T, V> property,
                                       final V... values) {
        return notIn(criteria, convert(property), Objects.asList(values));
    }

    /**
     * not in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotIn}
     */
    public static <T, V> StandardNotIn notIn(final Criteria<T> criteria, final Property<T, V> property,
                                       final Collection<V> values) {
        return notIn(criteria, convert(property), values);
    }

    /**
     * not in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotIn}
     */
    @SafeVarargs
    public static <T, V> StandardNotIn notIn(final Slot slot, final Criteria<T> criteria,
                                       final Property<T, V> property, final V... values) {
        return notIn(slot, criteria, convert(property), Objects.asList(values));
    }

    /**
     * not in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotIn}
     */
    public static <T, V> StandardNotIn notIn(final Slot slot, final Criteria<T> criteria,
                                       final Property<T, V> property, final Collection<V> values) {
        return notIn(slot, criteria, convert(property), values);
    }

    /**
     * in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @return {@link StandardNotIn}
     */
    public static StandardNotIn notIn(final Criteria<?> criteria, final String property, final Object... values) {
        return notIn(criteria, property, Objects.asList(values));
    }

    /**
     * not in
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @return {@link StandardNotIn}
     */
    public static StandardNotIn notIn(final Criteria<?> criteria, final String property, final Collection<?> values) {
        return notIn(Slot.AND, criteria, property, values);
    }

    /**
     * not in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @return {@link StandardNotIn}
     */
    public static StandardNotIn notIn(final Slot slot, final Criteria<?> criteria,
                                final String property, final Object... values) {
        return notIn(slot, criteria, property, Objects.asList(values));
    }

    /**
     * not in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StandardNotIn}
     */
    public static <T, V> StandardNotIn notIn(final Slot slot, final Criteria<?> criteria,
                                       final String property, final Collection<?> values) {
        return new StandardNotIn(criteria, property, slot, values);
    }

    /**
     * not in
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final Criteria<?> criteria,
                                    final String column, final Object... values) {
        return colNotIn(Slot.AND, criteria, column, Objects.asList(values));
    }

    /**
     * not in
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final Criteria<?> criteria,
                                    final String column, final Collection<?> values) {
        return colNotIn(Slot.AND, criteria, column, values);
    }

    /**
     * not in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final Slot slot, final Criteria<?> criteria,
                                    final String column, final Object... values) {
        return colNotIn(slot, criteria, column, Objects.asList(values));
    }

    /**
     * not in
     * @param slot     {@link Slot}
     * @param criteria {@link Criteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final Slot slot, final Criteria<?> criteria,
                                    final String column, final Collection<?> values) {
        return new ImmediateNotIn(criteria, column, slot, values);
    }

    /**
     * not in
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final String alias,
                                    final String column, final Object... values) {
        return colNotIn(Slot.AND, alias, column, Objects.asList(values));
    }

    /**
     * not in
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final String alias,
                                    final String column, final Collection<?> values) {
        return colNotIn(Slot.AND, alias, column, values);
    }

    /**
     * not in
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final Slot slot, final String alias,
                                    final String column, final Object... values) {
        return colNotIn(slot, alias, column, Objects.asList(values));
    }

    /**
     * not in
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImmediateNotIn}
     */
    public static ImmediateNotIn colNotIn(final Slot slot, final String alias,
                                    final String column, final Collection<?> values) {
        return new ImmediateNotIn(alias, column, slot, values);
    }

    // endregion

    // endregion


}
