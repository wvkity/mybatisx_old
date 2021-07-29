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
package com.github.mybatisx.core.criteria;

import com.github.mybatisx.Objects;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.core.expr.ImdBetween;
import com.github.mybatisx.core.expr.ImdEqual;
import com.github.mybatisx.core.expr.ImdGreaterThan;
import com.github.mybatisx.core.expr.ImdGreaterThanOrEqual;
import com.github.mybatisx.core.expr.ImdIn;
import com.github.mybatisx.core.expr.ImdLessThan;
import com.github.mybatisx.core.expr.ImdLessThanOrEqual;
import com.github.mybatisx.core.expr.ImdLike;
import com.github.mybatisx.core.expr.ImdNotBetween;
import com.github.mybatisx.core.expr.ImdNotEqual;
import com.github.mybatisx.core.expr.ImdNotIn;
import com.github.mybatisx.core.expr.ImdNotLike;
import com.github.mybatisx.core.expr.ImdNotNull;
import com.github.mybatisx.core.expr.ImdNull;
import com.github.mybatisx.core.expr.StdBetween;
import com.github.mybatisx.core.expr.StdEqual;
import com.github.mybatisx.core.expr.StdGreaterThan;
import com.github.mybatisx.core.expr.StdGreaterThanOrEqual;
import com.github.mybatisx.core.expr.StdIn;
import com.github.mybatisx.core.expr.StdLessThan;
import com.github.mybatisx.core.expr.StdLessThanOrEqual;
import com.github.mybatisx.core.expr.StdLike;
import com.github.mybatisx.core.expr.StdNotBetween;
import com.github.mybatisx.core.expr.StdNotEqual;
import com.github.mybatisx.core.expr.StdNotIn;
import com.github.mybatisx.core.expr.StdNotLike;
import com.github.mybatisx.core.expr.StdNotNull;
import com.github.mybatisx.core.expr.StdNull;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.support.constant.Like;
import com.github.mybatisx.support.constant.Slot;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 条件表达式工具类
 * @author wvkity
 * @created 2021-07-28
 * @since 1.0.0
 */
public final class Restrictions {

    private Restrictions() {
    }

    private static <V> boolean early(final V value, final Predicate<V> predicate) {
        return Objects.isNull(predicate) || predicate.test(value);
    }

    // region Equal condition

    /**
     * 等于
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdEqual}
     */
    public static <T, V> StdEqual eq(final ExtCriteria<T> criteria, final Property<T, V> property, final V value) {
        return eq(criteria, property, value, null);
    }

    /**
     * 等于
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdEqual}
     */
    public static <T, V> StdEqual eq(final ExtCriteria<T> criteria, final Property<T, V> property,
                                     final V value, final Predicate<V> predicate) {
        return eq(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdEqual}
     */
    public static <T, V> StdEqual eq(final Slot slot, final ExtCriteria<T> criteria,
                                     final Property<T, V> property, final V value) {
        return eq(slot, criteria, property, value, null);
    }

    /**
     * 等于
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdEqual}
     */
    public static <V> StdEqual eq(final ExtCriteria<?> criteria, final String property, final V value) {
        return eq(criteria, property, value, null);
    }

    /**
     * 等于
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdEqual}
     */
    public static <V> StdEqual eq(final ExtCriteria<?> criteria, final String property,
                                  final V value, final Predicate<V> predicate) {
        return eq(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdEqual}
     */
    public static <T, V> StdEqual eq(final Slot slot, final ExtCriteria<T> criteria,
                                     final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return eq(slot, criteria, property.toProp(), value, predicate);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdEqual}
     */
    public static <V> StdEqual eq(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                  final V value) {
        return eq(slot, criteria, property, value, null);
    }

    /**
     * 等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdEqual}
     */
    public static <V> StdEqual eq(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                  final V value, final Predicate<V> predicate) {
        if (early(value, predicate)) {
            return StdEqual.create().criteria(criteria).target(criteria.getConverter().convert(property)).slot(slot)
                .value(value).build();
        }
        return null;
    }

    /**
     * 等于
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final ExtCriteria<?> criteria, final String column, final V value) {
        return colEq(criteria.transfer().getSlot(), criteria, column, value);
    }

    /**
     * 等于
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final ExtCriteria<?> criteria, final String column,
                                     final V value, final Predicate<V> predicate) {
        return colEq(criteria.transfer().getSlot(), criteria, column, value, predicate);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final Slot slot, final ExtCriteria<?> criteria,
                                     final String column, final V value) {
        return colEq(slot, criteria, column, value, null);
    }

    /**
     * 等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                     final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdEqual.create().criteria(criteria).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final String alias, final String column, final V value) {
        return colEq(Slot.AND, alias, column, value);
    }

    /**
     * 等于
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final String alias, final String column,
                                     final V value, final Predicate<V> predicate) {
        return colEq(Slot.AND, alias, column, value, predicate);
    }

    /**
     * 等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final Slot slot, final String alias, final String column, final V value) {
        return colEq(slot, alias, column, value, null);
    }

    /**
     * 等于
     * @param slot      {@link Slot}
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdEqual}
     */
    public static <V> ImdEqual colEq(final Slot slot, final String alias, final String column,
                                     final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdEqual.create().alias(alias).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    // endregion

    // region Not equal condition

    /**
     * 不等于
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotEqual}
     */
    public static <T, V> StdNotEqual ne(final ExtCriteria<T> criteria, final Property<T, V> property, final V value) {
        return ne(criteria, property, value, null);
    }

    /**
     * 不等于
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdNotEqual}
     */
    public static <T, V> StdNotEqual ne(final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final V value, final Predicate<V> predicate) {
        return ne(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotEqual}
     */
    public static <T, V> StdNotEqual ne(final Slot slot, final ExtCriteria<T> criteria,
                                        final Property<T, V> property, final V value) {
        return ne(slot, criteria, property, value, null);
    }

    /**
     * 不等于
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdNotEqual}
     */
    public static <V> StdNotEqual ne(final ExtCriteria<?> criteria, final String property, final V value) {
        return ne(criteria, property, value, null);
    }

    /**
     * 不等于
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdNotEqual}
     */
    public static <V> StdNotEqual ne(final ExtCriteria<?> criteria, final String property,
                                     final V value, final Predicate<V> predicate) {
        return ne(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 不等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdNotEqual}
     */
    public static <T, V> StdNotEqual ne(final Slot slot, final ExtCriteria<T> criteria,
                                        final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return ne(slot, criteria, property.toProp(), value, predicate);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdNotEqual}
     */
    public static <V> StdNotEqual ne(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final V value) {
        return ne(slot, criteria, property, value, null);
    }

    /**
     * 不等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdNotEqual}
     */
    public static <V> StdNotEqual ne(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final V value, final Predicate<V> predicate) {
        if (early(value, predicate)) {
            return StdNotEqual.create().criteria(criteria).target(criteria.getConverter().convert(property)).slot(slot)
                .value(value).build();
        }
        return null;
    }

    /**
     * 不等于
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final ExtCriteria<?> criteria, final String column, final V value) {
        return colNe(criteria.transfer().getSlot(), criteria, column, value);
    }

    /**
     * 不等于
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final ExtCriteria<?> criteria, final String column,
                                        final V value, final Predicate<V> predicate) {
        return colNe(criteria.transfer().getSlot(), criteria, column, value, predicate);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final Slot slot, final ExtCriteria<?> criteria,
                                        final String column, final V value) {
        return colNe(slot, criteria, column, value, null);
    }

    /**
     * 不等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                        final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdNotEqual.create().criteria(criteria).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 不等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final String alias, final String column, final V value) {
        return colNe(Slot.AND, alias, column, value);
    }

    /**
     * 不等于
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final String alias, final String column,
                                        final V value, final Predicate<V> predicate) {
        return colNe(Slot.AND, alias, column, value, predicate);
    }

    /**
     * 不等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final Slot slot, final String alias, final String column, final V value) {
        return colNe(slot, alias, column, value, null);
    }

    /**
     * 不等于
     * @param slot      {@link Slot}
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdNotEqual}
     */
    public static <V> ImdNotEqual colNe(final Slot slot, final String alias, final String column,
                                        final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdNotEqual.create().alias(alias).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    // endregion

    // region Greater than condition

    /**
     * 大于
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdGreaterThan}
     */
    public static <T, V> StdGreaterThan gt(final ExtCriteria<T> criteria, final Property<T, V> property,
                                           final V value) {
        return gt(criteria, property, value, null);
    }

    /**
     * 大于
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdGreaterThan}
     */
    public static <T, V> StdGreaterThan gt(final ExtCriteria<T> criteria, final Property<T, V> property,
                                           final V value, final Predicate<V> predicate) {
        return gt(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdGreaterThan}
     */
    public static <T, V> StdGreaterThan gt(final Slot slot, final ExtCriteria<T> criteria,
                                           final Property<T, V> property, final V value) {
        return gt(slot, criteria, property, value, null);
    }

    /**
     * 大于
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdGreaterThan}
     */
    public static <V> StdGreaterThan gt(final ExtCriteria<?> criteria, final String property, final V value) {
        return gt(criteria, property, value, null);
    }

    /**
     * 大于
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdGreaterThan}
     */
    public static <V> StdGreaterThan gt(final ExtCriteria<?> criteria, final String property,
                                        final V value, final Predicate<V> predicate) {
        return gt(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 大于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdGreaterThan}
     */
    public static <T, V> StdGreaterThan gt(final Slot slot, final ExtCriteria<T> criteria,
                                           final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return gt(slot, criteria, property.toProp(), value, predicate);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdGreaterThan}
     */
    public static <V> StdGreaterThan gt(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                        final V value) {
        return gt(slot, criteria, property, value, null);
    }

    /**
     * 大于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdGreaterThan}
     */
    public static <V> StdGreaterThan gt(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                        final V value, final Predicate<V> predicate) {
        if (early(value, predicate)) {
            return StdGreaterThan.create().criteria(criteria).target(criteria.getConverter().convert(property)).slot(slot)
                .value(value).build();
        }
        return null;
    }

    /**
     * 大于
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final ExtCriteria<?> criteria, final String column, final V value) {
        return colGt(criteria.transfer().getSlot(), criteria, column, value);
    }

    /**
     * 大于
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final ExtCriteria<?> criteria, final String column,
                                           final V value, final Predicate<V> predicate) {
        return colGt(criteria.transfer().getSlot(), criteria, column, value, predicate);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final Slot slot, final ExtCriteria<?> criteria,
                                           final String column, final V value) {
        return colGt(slot, criteria, column, value, null);
    }

    /**
     * 大于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                           final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdGreaterThan.create().criteria(criteria).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 大于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final String alias, final String column, final V value) {
        return colGt(Slot.AND, alias, column, value);
    }

    /**
     * 大于
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final String alias, final String column,
                                           final V value, final Predicate<V> predicate) {
        return colGt(Slot.AND, alias, column, value, predicate);
    }

    /**
     * 大于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final Slot slot, final String alias, final String column, final V value) {
        return colGt(slot, alias, column, value, null);
    }

    /**
     * 大于
     * @param slot      {@link Slot}
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdGreaterThan}
     */
    public static <V> ImdGreaterThan colGt(final Slot slot, final String alias, final String column,
                                           final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdGreaterThan.create().alias(alias).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    // endregion

    // region Greater than or equal to condition

    /**
     * 大于等于
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <T, V> StdGreaterThanOrEqual ge(final ExtCriteria<T> criteria, final Property<T, V> property,
                                                  final V value) {
        return ge(criteria, property, value, null);
    }

    /**
     * 大于等于
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <T, V> StdGreaterThanOrEqual ge(final ExtCriteria<T> criteria, final Property<T, V> property,
                                                  final V value, final Predicate<V> predicate) {
        return ge(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 大于等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <T, V> StdGreaterThanOrEqual ge(final Slot slot, final ExtCriteria<T> criteria,
                                                  final Property<T, V> property, final V value) {
        return ge(slot, criteria, property, value, null);
    }

    /**
     * 大于等于
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <V> StdGreaterThanOrEqual ge(final ExtCriteria<?> criteria, final String property, final V value) {
        return ge(criteria, property, value, null);
    }

    /**
     * 大于等于
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <V> StdGreaterThanOrEqual ge(final ExtCriteria<?> criteria, final String property,
                                               final V value, final Predicate<V> predicate) {
        return ge(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 大于等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <T, V> StdGreaterThanOrEqual ge(final Slot slot, final ExtCriteria<T> criteria,
                                                  final Property<T, V> property, final V value,
                                                  final Predicate<V> predicate) {
        return ge(slot, criteria, property.toProp(), value, predicate);
    }

    /**
     * 大于等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <V> StdGreaterThanOrEqual ge(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                               final V value) {
        return ge(slot, criteria, property, value, null);
    }

    /**
     * 大于等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdGreaterThanOrEqual}
     */
    public static <V> StdGreaterThanOrEqual ge(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                               final V value, final Predicate<V> predicate) {
        if (early(value, predicate)) {
            return StdGreaterThanOrEqual.create().criteria(criteria).target(criteria.getConverter().convert(property))
                .slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 大于等于
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdGreaterThanOrEqual}
     */
    public static <V> ImdGreaterThanOrEqual colGe(final ExtCriteria<?> criteria, final String column, final V value) {
        return colGe(criteria.transfer().getSlot(), criteria, column, value);
    }

    /**
     * 大于等于
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdGreaterThanOrEqual}
     */
    public static <V> ImdGreaterThanOrEqual colGe(final ExtCriteria<?> criteria, final String column,
                                                  final V value, final Predicate<V> predicate) {
        return colGe(criteria.transfer().getSlot(), criteria, column, value, predicate);
    }

    /**
     * 大于等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdGreaterThanOrEqual}
     */
    public static <V> ImdGreaterThanOrEqual colGe(final Slot slot, final ExtCriteria<?> criteria,
                                                  final String column, final V value) {
        return colGe(slot, criteria, column, value, null);
    }

    /**
     * 大于等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdGreaterThanOrEqual}
     */
    public static <V> ImdGreaterThanOrEqual colGe(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                                  final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdGreaterThanOrEqual.create().criteria(criteria).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 大于等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colGe(final String alias, final String column, final V value) {
        return colGe(Slot.AND, alias, column, value);
    }

    /**
     * 大于等于
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colGe(final String alias, final String column,
                                               final V value, final Predicate<V> predicate) {
        return colGe(Slot.AND, alias, column, value, predicate);
    }

    /**
     * 大于等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colGe(final Slot slot, final String alias, final String column,
                                               final V value) {
        return colGe(slot, alias, column, value, null);
    }

    /**
     * 大于等于
     * @param slot      {@link Slot}
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colGe(final Slot slot, final String alias, final String column,
                                               final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdLessThanOrEqual.create().alias(alias).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    // endregion

    // region Less than condition

    /**
     * 小于
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdLessThan}
     */
    public static <T, V> StdLessThan lt(final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final V value) {
        return lt(criteria, property, value, null);
    }

    /**
     * 小于
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdLessThan}
     */
    public static <T, V> StdLessThan lt(final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final V value, final Predicate<V> predicate) {
        return lt(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdLessThan}
     */
    public static <T, V> StdLessThan lt(final Slot slot, final ExtCriteria<T> criteria,
                                        final Property<T, V> property, final V value) {
        return lt(slot, criteria, property, value, null);
    }

    /**
     * 小于
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdLessThan}
     */
    public static <V> StdLessThan lt(final ExtCriteria<?> criteria, final String property, final V value) {
        return lt(criteria, property, value, null);
    }

    /**
     * 小于
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdLessThan}
     */
    public static <V> StdLessThan lt(final ExtCriteria<?> criteria, final String property,
                                     final V value, final Predicate<V> predicate) {
        return lt(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 小于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdLessThan}
     */
    public static <T, V> StdLessThan lt(final Slot slot, final ExtCriteria<T> criteria,
                                        final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return lt(slot, criteria, property.toProp(), value, predicate);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdLessThan}
     */
    public static <V> StdLessThan lt(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final V value) {
        return lt(slot, criteria, property, value, null);
    }

    /**
     * 小于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdLessThan}
     */
    public static <V> StdLessThan lt(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final V value, final Predicate<V> predicate) {
        if (early(value, predicate)) {
            return StdLessThan.create().criteria(criteria).target(criteria.getConverter().convert(property)).slot(slot)
                .value(value).build();
        }
        return null;
    }

    /**
     * 小于
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final ExtCriteria<?> criteria, final String column, final V value) {
        return colLt(criteria.transfer().getSlot(), criteria, column, value);
    }

    /**
     * 小于
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final ExtCriteria<?> criteria, final String column,
                                        final V value, final Predicate<V> predicate) {
        return colLt(criteria.transfer().getSlot(), criteria, column, value, predicate);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final Slot slot, final ExtCriteria<?> criteria,
                                        final String column, final V value) {
        return colLt(slot, criteria, column, value, null);
    }

    /**
     * 小于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                        final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdLessThan.create().criteria(criteria).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 小于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final String alias, final String column, final V value) {
        return colLt(Slot.AND, alias, column, value);
    }

    /**
     * 小于
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final String alias, final String column,
                                        final V value, final Predicate<V> predicate) {
        return colLt(Slot.AND, alias, column, value, predicate);
    }

    /**
     * 小于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final Slot slot, final String alias, final String column,
                                        final V value) {
        return colLt(slot, alias, column, value, null);
    }

    /**
     * 小于
     * @param slot      {@link Slot}
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThan}
     */
    public static <V> ImdLessThan colLt(final Slot slot, final String alias, final String column,
                                        final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdLessThan.create().alias(alias).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    // endregion

    // region Less than or equal to condition

    /**
     * 小于等于
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <T, V> StdLessThanOrEqual le(final ExtCriteria<T> criteria, final Property<T, V> property,
                                               final V value) {
        return le(criteria, property, value, null);
    }

    /**
     * 小于等于
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <T, V> StdLessThanOrEqual le(final ExtCriteria<T> criteria, final Property<T, V> property,
                                               final V value, final Predicate<V> predicate) {
        return le(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 小于等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param value    值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <T, V> StdLessThanOrEqual le(final Slot slot, final ExtCriteria<T> criteria,
                                               final Property<T, V> property, final V value) {
        return le(slot, criteria, property, value, null);
    }

    /**
     * 小于等于
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <V> StdLessThanOrEqual le(final ExtCriteria<?> criteria, final String property, final V value) {
        return le(criteria, property, value, null);
    }

    /**
     * 小于等于
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <V> StdLessThanOrEqual le(final ExtCriteria<?> criteria, final String property,
                                            final V value, final Predicate<V> predicate) {
        return le(criteria.transfer().getSlot(), criteria, property, value, predicate);
    }

    /**
     * 小于等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  Lambda属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <T>       实体类型
     * @param <V>       值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <T, V> StdLessThanOrEqual le(final Slot slot, final ExtCriteria<T> criteria,
                                               final Property<T, V> property, final V value,
                                               final Predicate<V> predicate) {
        return le(slot, criteria, property.toProp(), value, predicate);
    }

    /**
     * 小于等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <V> StdLessThanOrEqual le(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                            final V value) {
        return le(slot, criteria, property, value, null);
    }

    /**
     * 小于等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link StdLessThanOrEqual}
     */
    public static <V> StdLessThanOrEqual le(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                            final V value, final Predicate<V> predicate) {
        if (early(value, predicate)) {
            return StdLessThanOrEqual.create().criteria(criteria).target(criteria.getConverter().convert(property))
                .slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 小于等于
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final ExtCriteria<?> criteria, final String column, final V value) {
        return colLe(criteria.transfer().getSlot(), criteria, column, value);
    }

    /**
     * 小于等于
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final ExtCriteria<?> criteria, final String column,
                                               final V value, final Predicate<V> predicate) {
        return colLe(criteria.transfer().getSlot(), criteria, column, value, predicate);
    }

    /**
     * 小于等于
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param value    值
     * @param <V>      值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final Slot slot, final ExtCriteria<?> criteria,
                                               final String column, final V value) {
        return colLe(slot, criteria, column, value, null);
    }

    /**
     * 小于等于
     * @param slot      {@link Slot}
     * @param criteria  {@link ExtCriteria}
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                               final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdLessThanOrEqual.create().criteria(criteria).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    /**
     * 小于等于
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final String alias, final String column, final V value) {
        return colLe(Slot.AND, alias, column, value);
    }

    /**
     * 小于等于
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final String alias, final String column,
                                               final V value, final Predicate<V> predicate) {
        return colLe(Slot.AND, alias, column, value, predicate);
    }

    /**
     * 小于等于
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param value  值
     * @param <V>    值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final Slot slot, final String alias, final String column,
                                               final V value) {
        return colLe(slot, alias, column, value, null);
    }

    /**
     * 小于等于
     * @param slot      {@link Slot}
     * @param alias     表别名
     * @param column    字段名
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       值类型
     * @return {@link ImdLessThanOrEqual}
     */
    public static <V> ImdLessThanOrEqual colLe(final Slot slot, final String alias, final String column,
                                               final V value, final Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && early(value, predicate)) {
            return ImdLessThanOrEqual.create().alias(alias).target(column).slot(slot).value(value).build();
        }
        return null;
    }

    // endregion

    // region Is null condition

    /**
     * IS NULL
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param <T>      实体类型
     * @return {@link StdNull}
     */
    public static <T> StdNull isNull(final ExtCriteria<T> criteria, final Property<T, ?> property) {
        return isNull(criteria.transfer().getSlot(), criteria, property.toProp());
    }

    /**
     * IS NULL
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param <T>      实体类型
     * @return {@link StdNull}
     */
    public static <T> StdNull isNull(final Slot slot, final ExtCriteria<T> criteria, final Property<T, ?> property) {
        return isNull(slot, criteria, property.toProp());
    }

    /**
     * IS NULL
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @return {@link StdNull}
     */
    public static StdNull isNull(final ExtCriteria<?> criteria, final String property) {
        return isNull(criteria.transfer().getSlot(), criteria, property);
    }

    /**
     * IS NULL
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @return {@link StdNull}
     */
    public static StdNull isNull(final Slot slot, final ExtCriteria<?> criteria, final String property) {
        if (Objects.isNotBlank(property)) {
            return StdNull.create().criteria(criteria).target(criteria.getConverter().convert(property)).slot(slot)
                .build();
        }
        return null;
    }

    /**
     * IS NULL
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImdNull}
     */
    public static ImdNull colIsNull(final String alias, final String column) {
        return colIsNull(Slot.AND, alias, column);
    }

    /**
     * IS NULL
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImdNull}
     */
    public static ImdNull colIsNull(final Slot slot, final String alias, final String column) {
        if (Objects.isNotBlank(column)) {
            return ImdNull.create().alias(alias).target(column).slot(slot).build();
        }
        return null;
    }

    /**
     * IS NULL
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @return {@link ImdNull}
     */
    public static ImdNull colIsNull(final ExtCriteria<?> criteria, final String column) {
        return colIsNull(criteria.transfer().getSlot(), criteria, column);
    }

    /**
     * IS NULL
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @return {@link ImdNull}
     */
    public static ImdNull colIsNull(final Slot slot, final ExtCriteria<?> criteria, final String column) {
        if (Objects.isNotBlank(column)) {
            return ImdNull.create().criteria(criteria).target(column).slot(slot).build();
        }
        return null;
    }

    // endregion

    // region Is not null condition

    /**
     * IS NOT NULL
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param <T>      实体类型
     * @return {@link StdNotNull}
     */
    public static <T> StdNotNull notNull(final ExtCriteria<T> criteria, final Property<T, ?> property) {
        return notNull(criteria.transfer().getSlot(), criteria, property.toProp());
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property Lambda属性
     * @param <T>      实体类型
     * @return {@link StdNotNull}
     */
    public static <T> StdNotNull notNull(final Slot slot, final ExtCriteria<T> criteria,
                                         final Property<T, ?> property) {
        return notNull(slot, criteria, property.toProp());
    }

    /**
     * IS NOT NULL
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @return {@link StdNotNull}
     */
    public static StdNotNull notNull(final ExtCriteria<?> criteria, final String property) {
        return notNull(criteria.transfer().getSlot(), criteria, property);
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @return {@link StdNotNull}
     */
    public static StdNotNull notNull(final Slot slot, final ExtCriteria<?> criteria, final String property) {
        if (Objects.isNotBlank(property)) {
            return StdNotNull.create().criteria(criteria).target(criteria.getConverter().convert(property)).slot(slot)
                .build();
        }
        return null;
    }

    /**
     * IS NOT NULL
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImdNotNull}
     */
    public static ImdNotNull colNotNull(final String alias, final String column) {
        return colNotNull(Slot.AND, alias, column);
    }

    /**
     * IS NOT NULL
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @return {@link ImdNotNull}
     */
    public static ImdNotNull colNotNull(final Slot slot, final String alias, final String column) {
        if (Objects.isNotBlank(column)) {
            return ImdNotNull.create().alias(alias).target(column).slot(slot).build();
        }
        return null;
    }

    /**
     * IS NOT NULL
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @return {@link ImdNotNull}
     */
    public static ImdNotNull colNotNull(final ExtCriteria<?> criteria, final String column) {
        return colNotNull(criteria.transfer().getSlot(), criteria, column);
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @return {@link ImdNotNull}
     */
    public static ImdNotNull colNotNull(final Slot slot, final ExtCriteria<?> criteria, final String column) {
        if (Objects.isNotBlank(column)) {
            return ImdNotNull.create().criteria(criteria).target(column).slot(slot).build();
        }
        return null;
    }

    // endregion

    // region In condition

    /**
     * IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdIn}
     */
    @SafeVarargs
    public static <T, V> StdIn in(final ExtCriteria<T> criteria, final Property<T, V> property, final V... values) {
        return in(criteria.transfer().getSlot(), criteria, property, Objects.asList(values));
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdIn}
     */
    @SafeVarargs
    public static <T, V> StdIn in(final Slot slot, final ExtCriteria<T> criteria, final Property<T, V> property,
                                  final V... values) {
        return in(slot, criteria, property, Objects.asList(values));
    }

    /**
     * IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdIn}
     */
    public static <T, V> StdIn in(final ExtCriteria<T> criteria, final Property<T, V> property,
                                  final Collection<V> values) {
        return in(criteria.transfer().getSlot(), criteria, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdIn}
     */
    public static <T, V> StdIn in(final Slot slot, final ExtCriteria<T> criteria, final Property<T, V> property,
                                  final Collection<V> values) {
        return in(slot, criteria, property.toProp(), values);
    }

    /**
     * IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <V>      值类型
     * @return {@link StdIn}
     */
    @SafeVarargs
    public static <V> StdIn in(final ExtCriteria<?> criteria, final String property, final V... values) {
        return in(criteria.transfer().getSlot(), criteria, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <V>      值类型
     * @return {@link StdIn}
     */
    @SafeVarargs
    public static <V> StdIn in(final Slot slot, final ExtCriteria<?> criteria, final String property,
                               final V... values) {
        return in(slot, criteria, property, Objects.asList(values));
    }

    /**
     * IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @return {@link StdIn}
     */
    public static StdIn in(final ExtCriteria<?> criteria, final String property, final Collection<?> values) {
        return in(criteria.transfer().getSlot(), criteria, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @return {@link StdIn}
     */
    public static StdIn in(final Slot slot, final ExtCriteria<?> criteria, final String property,
                           final Collection<?> values) {
        final Column it;
        if (Objects.nonNull((it = criteria.getConverter().convert(property)))) {
            return StdIn.create().criteria(criteria).target(it).slot(slot).values(values).build();
        }
        return null;
    }

    /**
     * IN
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @param <V>      值类型
     * @return {@link ImdIn}
     */
    @SafeVarargs
    public static <V> ImdIn colIn(final ExtCriteria<?> criteria, final String column, final V... values) {
        return colIn(criteria.transfer().getSlot(), criteria, column, Objects.asList(values));
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @param <V>      值类型
     * @return {@link ImdIn}
     */
    @SafeVarargs
    public static <V> ImdIn colIn(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                  final V... values) {
        return colIn(slot, criteria, column, Objects.asList(values));
    }

    /**
     * IN
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImdIn}
     */
    public static ImdIn colIn(final ExtCriteria<?> criteria, final String column, final Collection<?> values) {
        return colIn(criteria.transfer().getSlot(), criteria, column, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImdIn}
     */
    public static ImdIn colIn(final Slot slot, final ExtCriteria<?> criteria, final String column,
                              final Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            return ImdIn.create().criteria(criteria).target(column).slot(slot).values(values).build();
        }
        return null;
    }

    /**
     * IN
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdIn}
     */
    @SafeVarargs
    public static <V> ImdIn colIn(final String alias, final String column, final V... values) {
        return colIn(Slot.AND, alias, column, Objects.asList(values));
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdIn}
     */
    @SafeVarargs
    public static <V> ImdIn colIn(final Slot slot, final String alias, final String column, final V... values) {
        return colIn(slot, alias, column, Objects.asList(values));
    }

    /**
     * IN
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdIn}
     */
    public static ImdIn colIn(final String alias, final String column, final Collection<?> values) {
        return colIn(Slot.AND, alias, column, values);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdIn}
     */
    public static ImdIn colIn(final Slot slot, final String alias, final String column,
                              final Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            return ImdIn.create().alias(alias).target(column).slot(slot).values(values).build();
        }
        return null;
    }

    // endregion

    // region Not in condition

    /**
     * NOT IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotIn}
     */
    @SafeVarargs
    public static <T, V> StdNotIn notIn(final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final V... values) {
        return notIn(criteria.transfer().getSlot(), criteria, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotIn}
     */
    @SafeVarargs
    public static <T, V> StdNotIn notIn(final Slot slot, final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final V... values) {
        return notIn(slot, criteria, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotIn}
     */
    public static <T, V> StdNotIn notIn(final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final Collection<V> values) {
        return notIn(criteria.transfer().getSlot(), criteria, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotIn}
     */
    public static <T, V> StdNotIn notIn(final Slot slot, final ExtCriteria<T> criteria, final Property<T, V> property,
                                        final Collection<V> values) {
        return notIn(slot, criteria, property.toProp(), values);
    }

    /**
     * NOT IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <V>      值类型
     * @return {@link StdNotIn}
     */
    @SafeVarargs
    public static <V> StdNotIn notIn(final ExtCriteria<?> criteria, final String property, final V... values) {
        return notIn(criteria.transfer().getSlot(), criteria, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @param <V>      值类型
     * @return {@link StdNotIn}
     */
    @SafeVarargs
    public static <V> StdNotIn notIn(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final V... values) {
        return notIn(slot, criteria, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @return {@link StdNotIn}
     */
    public static StdNotIn notIn(final ExtCriteria<?> criteria, final String property, final Collection<?> values) {
        return notIn(criteria.transfer().getSlot(), criteria, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param values   值
     * @return {@link StdNotIn}
     */
    public static StdNotIn notIn(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                 final Collection<?> values) {
        final Column it;
        if (Objects.nonNull((it = criteria.getConverter().convert(property)))) {
            return StdNotIn.create().criteria(criteria).target(it).slot(slot).values(values).build();
        }
        return null;
    }

    /**
     * NOT IN
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @param <V>      值类型
     * @return {@link ImdNotIn}
     */
    @SafeVarargs
    public static <V> ImdNotIn colNotIn(final ExtCriteria<?> criteria, final String column, final V... values) {
        return colNotIn(criteria.transfer().getSlot(), criteria, column, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @param <V>      值类型
     * @return {@link ImdNotIn}
     */
    @SafeVarargs
    public static <V> ImdNotIn colNotIn(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                        final V... values) {
        return colNotIn(slot, criteria, column, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImdNotIn}
     */
    public static ImdNotIn colNotIn(final ExtCriteria<?> criteria, final String column, final Collection<?> values) {
        return colNotIn(criteria.transfer().getSlot(), criteria, column, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param values   值
     * @return {@link ImdNotIn}
     */
    public static ImdNotIn colNotIn(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                    final Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            return ImdNotIn.create().criteria(criteria).target(column).slot(slot).values(values).build();
        }
        return null;
    }

    /**
     * NOT IN
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdNotIn}
     */
    @SafeVarargs
    public static <V> ImdNotIn colNotIn(final String alias, final String column, final V... values) {
        return colNotIn(Slot.AND, alias, column, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdNotIn}
     */
    @SafeVarargs
    public static <V> ImdNotIn colNotIn(final Slot slot, final String alias, final String column, final V... values) {
        return colNotIn(slot, alias, column, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdNotIn}
     */
    public static ImdNotIn colNotIn(final String alias, final String column, final Collection<?> values) {
        return colNotIn(Slot.AND, alias, column, values);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param values 值
     * @return {@link ImdNotIn}
     */
    public static ImdNotIn colNotIn(final Slot slot, final String alias, final String column,
                                    final Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            return ImdNotIn.create().alias(alias).target(column).slot(slot).values(values).build();
        }
        return null;
    }

    // endregion

    // region Between condition

    /**
     * BETWEEN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdBetween}
     */
    public static <T, V> StdBetween between(final ExtCriteria<T> criteria, final Property<T, V> property,
                                            final V begin, final V end) {
        return between(criteria.transfer().getSlot(), criteria, property, begin, end);
    }

    /**
     * BETWEEN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdBetween}
     */
    public static <T, V> StdBetween between(final Slot slot, final ExtCriteria<T> criteria,
                                            final Property<T, V> property, final V begin, final V end) {
        return between(slot, criteria, property.toProp(), begin, end);
    }

    /**
     * BETWEEN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link StdBetween}
     */
    public static <V> StdBetween between(final ExtCriteria<?> criteria, final String property,
                                         final V begin, final V end) {
        return between(criteria.transfer().getSlot(), criteria, property, begin, end);
    }

    /**
     * BETWEEN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link StdBetween}
     */
    public static <V> StdBetween between(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                         final V begin, final V end) {
        final Column it;
        if (Objects.nonNull((it = criteria.getConverter().convert(property)))) {
            return StdBetween.create().criteria(criteria).target(it).slot(slot).begin(begin).end(end).build();
        }
        return null;
    }

    /**
     * BETWEEN
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link ImdBetween}
     */
    public static <V> ImdBetween colBetween(final ExtCriteria<?> criteria, final String column,
                                            final V begin, final V end) {
        return colBetween(criteria.transfer().getSlot(), criteria, column, begin, end);
    }

    /**
     * BETWEEN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link ImdBetween}
     */
    public static <V> ImdBetween colBetween(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                            final V begin, final V end) {
        if (Objects.isNotBlank(column)) {
            return ImdBetween.create().criteria(criteria).target(column).slot(slot).begin(begin).end(end).build();
        }
        return null;
    }

    /**
     * BETWEEN
     * @param alias  表别名
     * @param column 字段名
     * @param begin  起始值
     * @param end    结束值
     * @param <V>    值类型
     * @return {@link ImdBetween}
     */
    public static <V> ImdBetween colBetween(final String alias, final String column,
                                            final V begin, final V end) {
        return colBetween(Slot.AND, alias, column, begin, end);
    }

    /**
     * BETWEEN
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param begin  起始值
     * @param end    结束值
     * @param <V>    值类型
     * @return {@link ImdBetween}
     */
    public static <V> ImdBetween colBetween(final Slot slot, final String alias, final String column,
                                            final V begin, final V end) {
        if (Objects.isNotBlank(column)) {
            return ImdBetween.create().alias(alias).target(column).slot(slot).begin(begin).end(end).build();
        }
        return null;
    }

    // endregion

    // region Not between condition

    /**
     * NOT BETWEEN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotBetween}
     */
    public static <T, V> StdNotBetween notBetween(final ExtCriteria<T> criteria, final Property<T, V> property,
                                                  final V begin, final V end) {
        return notBetween(criteria.transfer().getSlot(), criteria, property, begin, end);
    }

    /**
     * NOT BETWEEN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <T>      实体类型
     * @param <V>      值类型
     * @return {@link StdNotBetween}
     */
    public static <T, V> StdNotBetween notBetween(final Slot slot, final ExtCriteria<T> criteria,
                                                  final Property<T, V> property, final V begin, final V end) {
        return notBetween(slot, criteria, property.toProp(), begin, end);
    }

    /**
     * NOT BETWEEN
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link StdNotBetween}
     */
    public static <V> StdNotBetween notBetween(final ExtCriteria<?> criteria, final String property,
                                               final V begin, final V end) {
        return notBetween(criteria.transfer().getSlot(), criteria, property, begin, end);
    }

    /**
     * NOT BETWEEN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link StdNotBetween}
     */
    public static <V> StdNotBetween notBetween(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                               final V begin, final V end) {
        final Column it;
        if (Objects.nonNull((it = criteria.getConverter().convert(property)))) {
            return StdNotBetween.create().criteria(criteria).target(it).slot(slot).begin(begin).end(end).build();
        }
        return null;
    }

    /**
     * NOT BETWEEN
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link ImdNotBetween}
     */
    public static <V> ImdNotBetween colNotBetween(final ExtCriteria<?> criteria, final String column,
                                                  final V begin, final V end) {
        return colNotBetween(criteria.transfer().getSlot(), criteria, column, begin, end);
    }

    /**
     * NOT BETWEEN
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param begin    起始值
     * @param end      结束值
     * @param <V>      值类型
     * @return {@link ImdNotBetween}
     */
    public static <V> ImdNotBetween colNotBetween(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                                  final V begin, final V end) {
        if (Objects.isNotBlank(column)) {
            return ImdNotBetween.create().criteria(criteria).target(column).slot(slot).begin(begin).end(end).build();
        }
        return null;
    }

    /**
     * NOT BETWEEN
     * @param alias  表别名
     * @param column 字段名
     * @param begin  起始值
     * @param end    结束值
     * @param <V>    值类型
     * @return {@link ImdNotBetween}
     */
    public static <V> ImdNotBetween colNotBetween(final String alias, final String column,
                                                  final V begin, final V end) {
        return colNotBetween(Slot.AND, alias, column, begin, end);
    }

    /**
     * NOT BETWEEN
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param begin  起始值
     * @param end    结束值
     * @param <V>    值类型
     * @return {@link ImdNotBetween}
     */
    public static <V> ImdNotBetween colNotBetween(final Slot slot, final String alias, final String column,
                                                  final V begin, final V end) {
        if (Objects.isNotBlank(column)) {
            return ImdNotBetween.create().alias(alias).target(column).slot(slot).begin(begin).end(end).build();
        }
        return null;
    }

    // endregion

    // region Like condition

    /**
     * LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param <T>      实体类型
     * @return {@link StdLike}
     */
    public static <T> StdLike like(final ExtCriteria<T> criteria, final Property<T, String> property,
                                   final Like like, final String value) {
        return like(criteria.transfer().getSlot(), criteria, property, like, value);
    }

    /**
     * LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @param <T>      实体类型
     * @return {@link StdLike}
     */
    public static <T> StdLike like(final ExtCriteria<T> criteria, final Property<T, String> property,
                                   final Like like, final String value, final Character escape) {
        return like(criteria.transfer().getSlot(), criteria, property, like, value, escape);
    }

    /**
     * LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param <T>      实体类型
     * @return {@link StdLike}
     */
    public static <T> StdLike like(final Slot slot, final ExtCriteria<T> criteria, final Property<T, String> property,
                                   final Like like, final String value) {
        return like(slot, criteria, property, like, value, null);
    }

    /**
     * LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @param <T>      实体类型
     * @return {@link StdLike}
     */
    public static <T> StdLike like(final Slot slot, final ExtCriteria<T> criteria, final Property<T, String> property,
                                   final Like like, final String value, final Character escape) {
        return like(slot, criteria, property.toProp(), like, value, escape);
    }

    /**
     * LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @return {@link StdLike}
     */
    public static StdLike like(final ExtCriteria<?> criteria, final String property,
                               final Like like, final String value) {
        return like(criteria.transfer().getSlot(), criteria, property, like, value);
    }

    /**
     * LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link StdLike}
     */
    public static StdLike like(final ExtCriteria<?> criteria, final String property,
                               final Like like, final String value, final Character escape) {
        return like(criteria.transfer().getSlot(), criteria, property, like, value, escape);
    }

    /**
     * LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @return {@link StdLike}
     */
    public static StdLike like(final Slot slot, final ExtCriteria<?> criteria, final String property,
                               final Like like, final String value) {
        return like(slot, criteria, property, like, value, null);
    }

    /**
     * LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link StdLike}
     */
    public static StdLike like(final Slot slot, final ExtCriteria<?> criteria, final String property,
                               final Like like, final String value, final Character escape) {
        final Column it;
        if (Objects.nonNull((it = criteria.getConverter().convert(property)))) {
            return StdLike.create().criteria(criteria).target(it).like(like).slot(slot)
                .value(value).escape(escape).build();
        }
        return null;
    }

    /**
     * LIKE
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final ExtCriteria<?> criteria, final String column,
                                  final Like like, final String value) {
        return colLike(criteria.transfer().getSlot(), criteria, column, like, value);
    }

    /**
     * LIKE
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final ExtCriteria<?> criteria, final String column,
                                  final Like like, final String value, final Character escape) {
        return colLike(criteria.transfer().getSlot(), criteria, column, like, value, escape);
    }

    /**
     * LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                  final Like like, final String value) {
        return colLike(slot, criteria, column, like, value, null);
    }

    /**
     * LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                  final Like like, final String value, final Character escape) {
        if (Objects.isNotBlank(column)) {
            return ImdLike.create().criteria(criteria).target(column).like(like).slot(slot)
                .value(value).escape(escape).build();
        }
        return null;
    }

    /**
     * LIKE
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final String alias, final String column,
                                  final Like like, final String value) {
        return colLike(Slot.AND, alias, column, like, value);
    }

    /**
     * LIKE
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @param escape 转义字符
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final String alias, final String column,
                                  final Like like, final String value, final Character escape) {
        return colLike(Slot.AND, alias, column, like, value, escape);
    }

    /**
     * LIKE
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final Slot slot, final String alias, final String column,
                                  final Like like, final String value) {
        return colLike(slot, alias, column, like, value, null);
    }

    /**
     * LIKE
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @param escape 转义字符
     * @return {@link ImdLike}
     */
    public static ImdLike colLike(final Slot slot, final String alias, final String column,
                                  final Like like, final String value, final Character escape) {
        if (Objects.isNotBlank(column)) {
            return ImdLike.create().alias(alias).target(column).like(like).slot(slot)
                .value(value).escape(escape).build();
        }
        return null;
    }

    // endregion

    // region Not like condition

    /**
     * NOT LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param <T>      实体类型
     * @return {@link StdNotLike}
     */
    public static <T> StdNotLike notLike(final ExtCriteria<T> criteria, final Property<T, String> property,
                                         final Like like, final String value) {
        return notLike(criteria.transfer().getSlot(), criteria, property, like, value);
    }

    /**
     * NOT LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @param <T>      实体类型
     * @return {@link StdNotLike}
     */
    public static <T> StdNotLike notLike(final ExtCriteria<T> criteria, final Property<T, String> property,
                                         final Like like, final String value, final Character escape) {
        return notLike(criteria.transfer().getSlot(), criteria, property, like, value, escape);
    }

    /**
     * NOT LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param <T>      实体类型
     * @return {@link StdNotLike}
     */
    public static <T> StdNotLike notLike(final Slot slot, final ExtCriteria<T> criteria,
                                         final Property<T, String> property, final Like like, final String value) {
        return notLike(slot, criteria, property, like, value, null);
    }

    /**
     * NOT LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @param <T>      实体类型
     * @return {@link StdNotLike}
     */
    public static <T> StdNotLike notLike(final Slot slot, final ExtCriteria<T> criteria,
                                         final Property<T, String> property, final Like like, final String value,
                                         final Character escape) {
        return notLike(slot, criteria, property.toProp(), like, value, escape);
    }

    /**
     * NOT LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @return {@link StdNotLike}
     */
    public static StdNotLike notLike(final ExtCriteria<?> criteria, final String property,
                                     final Like like, final String value) {
        return notLike(criteria.transfer().getSlot(), criteria, property, like, value);
    }

    /**
     * NOT LIKE
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link StdNotLike}
     */
    public static StdNotLike notLike(final ExtCriteria<?> criteria, final String property,
                                     final Like like, final String value, final Character escape) {
        return notLike(criteria.transfer().getSlot(), criteria, property, like, value, escape);
    }

    /**
     * NOT LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @return {@link StdNotLike}
     */
    public static StdNotLike notLike(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final Like like, final String value) {
        return notLike(slot, criteria, property, like, value, null);
    }

    /**
     * NOT LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param property 属性
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link StdNotLike}
     */
    public static StdNotLike notLike(final Slot slot, final ExtCriteria<?> criteria, final String property,
                                     final Like like, final String value, final Character escape) {
        final Column it;
        if (Objects.nonNull((it = criteria.getConverter().convert(property)))) {
            return StdNotLike.create().criteria(criteria).target(it).like(like).slot(slot)
                .value(value).escape(escape).build();
        }
        return null;
    }

    /**
     * NOT LIKE
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final ExtCriteria<?> criteria, final String column,
                                        final Like like, final String value) {
        return colNotLike(criteria.transfer().getSlot(), criteria, column, like, value);
    }

    /**
     * NOT LIKE
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final ExtCriteria<?> criteria, final String column,
                                        final Like like, final String value, final Character escape) {
        return colNotLike(criteria.transfer().getSlot(), criteria, column, like, value, escape);
    }

    /**
     * NOT LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                        final Like like, final String value) {
        return colNotLike(slot, criteria, column, like, value, null);
    }

    /**
     * NOT LIKE
     * @param slot     {@link Slot}
     * @param criteria {@link ExtCriteria}
     * @param column   字段名
     * @param like     {@link Like}
     * @param value    值
     * @param escape   转义字符
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final Slot slot, final ExtCriteria<?> criteria, final String column,
                                        final Like like, final String value, final Character escape) {
        if (Objects.isNotBlank(column)) {
            return ImdNotLike.create().criteria(criteria).target(column).like(like).slot(slot)
                .value(value).escape(escape).build();
        }
        return null;
    }

    /**
     * NOT LIKE
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final String alias, final String column,
                                        final Like like, final String value) {
        return colNotLike(Slot.AND, alias, column, like, value);
    }

    /**
     * NOT LIKE
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @param escape 转义字符
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final String alias, final String column,
                                        final Like like, final String value, final Character escape) {
        return colNotLike(Slot.AND, alias, column, like, value, escape);
    }

    /**
     * NOT LIKE
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final Slot slot, final String alias, final String column,
                                        final Like like, final String value) {
        return colNotLike(slot, alias, column, like, value, null);
    }

    /**
     * NOT LIKE
     * @param slot   {@link Slot}
     * @param alias  表别名
     * @param column 字段名
     * @param like   {@link Like}
     * @param value  值
     * @param escape 转义字符
     * @return {@link ImdNotLike}
     */
    public static ImdNotLike colNotLike(final Slot slot, final String alias, final String column,
                                        final Like like, final String value, final Character escape) {
        if (Objects.isNotBlank(column)) {
            return ImdNotLike.create().alias(alias).target(column).like(like).slot(slot)
                .value(value).escape(escape).build();
        }
        return null;
    }

    // endregion

}
