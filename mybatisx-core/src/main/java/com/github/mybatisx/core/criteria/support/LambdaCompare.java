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
import java.util.function.Predicate;

/**
 * 基础比较条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
interface LambdaCompare<T, C extends LambdaCompare<T, C>> extends SlotSymbol<T, C> {

    // region Equal to condition

    /**
     * 主键等于
     * @param value 值
     * @param <V>   属性值类型
     * @return {@code this}
     */
    default <V> C idEq(final V value) {
        return this.idEq(this.getSlot(), value);
    }


    /**
     * 主键等于
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C idEq(final V value, final Predicate<V> predicate) {
        return this.idEq(this.getSlot(), value, predicate);
    }

    /**
     * 主键等于
     * @param slot  {@link Slot}
     * @param value 值
     * @param <V>   属性值类型
     * @return {@code this}
     */
    default <V> C idEq(final Slot slot, final V value) {
        return this.idEq(slot, value, null);
    }

    /**
     * 主键等于
     * @param slot      {@link Slot}
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C idEq(final Slot slot, final V value, final Predicate<V> predicate);

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C eq(final Property<T, V> property, final V value) {
        System.out.println("property: " + property.toProp());
        return this.eq(this.getSlot(), property, value);
    }

    /**
     * 等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C eq(final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return this.eq(this.getSlot(), property, value, predicate);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C eq(final Slot slot, final Property<T, V> property, final V value) {
        return this.eq(slot, property, value, null);
    }

    /**
     * 等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C eq(final Slot slot, final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return this.eq(slot, property.toProp(), value, predicate);
    }

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C eq(final String property, final Object value) {
        return this.eq(this.getSlot(), property, value);
    }

    /**
     * 等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C eq(final String property, final V value, final Predicate<V> predicate) {
        return this.eq(this.getSlot(), property, value, predicate);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C eq(final Slot slot, final String property, final Object value) {
        return this.eq(slot, property, value, null);
    }

    /**
     * 等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C eq(final Slot slot, final String property, final V value, final Predicate<V> predicate);

    /**
     * 等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C eqq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.eqq(this.getSlot(), property, query);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C eqq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.eqq(slot, property.toProp(), query);
    }

    /**
     * 等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C eqq(final String property, final ExtCriteria<?> query) {
        return this.eqq(this.getSlot(), property, query);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C eqq(final Slot slot, final String property, final ExtCriteria<?> query);

    /**
     * 等于
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param <V1> 属性1类型
     * @param <V2> 属性2类型
     * @return {@code this}
     */
    default <V1, V2> C eq(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2) {
        return eq(this.getSlot(), p1, v1, p2, v2);
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
     * @return {@code this}
     */
    default <V1, V2> C eq(final Slot slot, final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2,
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
     * @return {@code this}
     */
    default <V1, V2, V3> C eq(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2,
                              final Property<T, V3> p3, final V3 v3) {
        return eq(this.getSlot(), p1, v1, p2, v2, p3, v3);
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
     * @return {@code this}
     */
    default <V1, V2, V3> C eq(final Slot slot, final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2,
                              final V2 v2, final Property<T, V3> p3, final V3 v3) {
        return this.eq(slot, p1, v1).eq(slot, p2, v2).eq(slot, p3, v3);
    }

    /**
     * 等于
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @return {@code this}
     */
    default C eq(final String p1, final Object v1, final String p2, final Object v2) {
        return eq(this.getSlot(), p1, v1, p2, v2);
    }

    /**
     * 等于
     * @param slot {@link Slot}
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @return {@code this}
     */
    default C eq(final Slot slot, final String p1, final Object v1, final String p2, final Object v2) {
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
     * @return {@code this}
     */
    default C eq(final String p1, final Object v1, final String p2, final Object v2,
                 final String p3, final Object v3) {
        return eq(this.getSlot(), p1, v1, p2, v2, p3, v3);
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
     * @return {@code this}
     */
    default C eq(final Slot slot, final String p1, final Object v1, final String p2, final Object v2,
                 final String p3, final Object v3) {
        return this.eq(slot, p1, v1).eq(slot, p2, v2).eq(slot, p3, v3);
    }

    /**
     * 等于
     * @param properties 属性-值集合
     * @return {@code this}
     */
    default C eq(final Map<String, Object> properties) {
        return eq(this.getSlot(), properties);
    }

    /**
     * 等于
     * @param properties 属性-值集合
     * @param slot       {@link Slot}
     * @return {@code this}
     */
    C eq(final Slot slot, final Map<String, Object> properties);

    // endregion

    // region Not equal to condition

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C ne(final Property<T, V> property, final V value) {
        return ne(this.getSlot(), property, value);
    }

    /**
     * 不等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C ne(final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return this.ne(this.getSlot(), property, value, predicate);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C ne(final Slot slot, final Property<T, V> property, final V value) {
        return this.ne(slot, property.toProp(), value);
    }

    /**
     * 不等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C ne(final Slot slot, final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return this.ne(slot, property.toProp(), value, predicate);
    }

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C ne(final String property, final Object value) {
        return ne(this.getSlot(), property, value);
    }

    /**
     * 不等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C ne(final String property, final V value, final Predicate<V> predicate) {
        return this.ne(this.getSlot(), property, value, predicate);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C ne(final Slot slot, final String property, final Object value) {
        return this.ne(slot, property, value, null);
    }

    /**
     * 不等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C ne(final Slot slot, final String property, final V value, final Predicate<V> predicate);

    /**
     * 不等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C neq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.neq(this.getSlot(), property, query);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C neq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.neq(slot, property.toProp(), query);
    }

    /**
     * 不等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C neq(final String property, final ExtCriteria<?> query) {
        return this.neq(this.getSlot(), property, query);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C neq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Greater than condition

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C gt(final Property<T, V> property, final V value) {
        return gt(this.getSlot(), property, value);
    }

    /**
     * 大于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C gt(final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return this.gt(this.getSlot(), property, value, predicate);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C gt(final Slot slot, final Property<T, V> property, final V value) {
        return this.gt(slot, property, value, null);
    }

    /**
     * 大于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C gt(final Slot slot, final Property<T, V> property, final V value, final Predicate<V> predicate) {
        return this.gt(slot, property.toProp(), value, predicate);
    }

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C gt(final String property, final Object value) {
        return gt(this.getSlot(), property, value);
    }

    /**
     * 大于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C gt(final String property, final V value, final Predicate<V> predicate) {
        return this.gt(this.getSlot(), property, value, predicate);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C gt(final Slot slot, final String property, final Object value) {
        return this.gt(slot, property, value, null);
    }

    /**
     * 大于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C gt(final Slot slot, final String property, final V value, final Predicate<V> predicate);

    /**
     * 大于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C gtq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.gtq(this.getSlot(), property, query);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C gtq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.gtq(slot, property.toProp(), query);
    }

    /**
     * 大于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C gtq(final String property, final ExtCriteria<?> query) {
        return this.gtq(this.getSlot(), property, query);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C gtq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Greater than or equal to condition

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C ge(final Property<T, V> property, final V value) {
        return ge(this.getSlot(), property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C ge(final Slot slot, final Property<T, V> property, final V value) {
        return this.ge(slot, property, value, null);
    }

    /**
     * 大于或等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C ge(final Property<T, ?> property, final V value, final Predicate<V> predicate) {
        return this.ge(this.getSlot(), property, value, predicate);
    }

    /**
     * 大于或等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C ge(final Slot slot, final Property<T, ?> property, final V value, final Predicate<V> predicate) {
        return this.ge(slot, property.toProp(), value, predicate);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C ge(final String property, final Object value) {
        return ge(this.getSlot(), property, value);
    }

    /**
     * 大于或等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C ge(final String property, final V value, final Predicate<V> predicate) {
        return this.ge(this.getSlot(), property, value, predicate);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C ge(final Slot slot, final String property, final Object value) {
        return this.ge(slot, property, value, null);
    }

    /**
     * 大于或等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C ge(final Slot slot, final String property, final V value, final Predicate<V> predicate);

    /**
     * 大于或等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C geq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.geq(this.getSlot(), property, query);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C geq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.geq(slot, property.toProp(), query);
    }

    /**
     * 大于或等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C geq(final String property, final ExtCriteria<?> query) {
        return this.geq(this.getSlot(), property, query);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C geq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Less than condition

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C lt(final Property<T, V> property, final V value) {
        return lt(this.getSlot(), property, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C lt(final Slot slot, final Property<T, V> property, final V value) {
        return this.lt(slot, property, value, null);
    }

    /**
     * 小于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C lt(final Property<T, ?> property, final V value, final Predicate<V> predicate) {
        return this.lt(this.getSlot(), property, value, predicate);
    }

    /**
     * 小于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C lt(final Slot slot, final Property<T, ?> property, final V value, final Predicate<V> predicate) {
        return this.lt(slot, property.toProp(), value, predicate);
    }

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C lt(final String property, final Object value) {
        return lt(this.getSlot(), property, value);
    }

    /**
     * 小于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C lt(final String property, final V value, final Predicate<V> predicate) {
        return this.lt(this.getSlot(), property, value, predicate);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C lt(final Slot slot, final String property, final Object value) {
        return this.lt(slot, property, value, null);
    }

    /**
     * 小于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C lt(final Slot slot, final String property, final V value, final Predicate<V> predicate);

    /**
     * 小于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C ltq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.ltq(this.getSlot(), property, query);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C ltq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.ltq(slot, property.toProp(), query);
    }

    /**
     * 小于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C ltq(final String property, final ExtCriteria<?> query) {
        return this.ltq(this.getSlot(), property, query);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C ltq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Less than or equal to condition

    /**
     * 小于或等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C le(final Property<T, V> property, final V value) {
        return le(this.getSlot(), property, value);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性值类型
     * @return {@code this}
     */
    default <V> C le(final Slot slot, final Property<T, V> property, final V value) {
        return this.le(slot, property, value, null);
    }

    /**
     * 小于或等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C le(final Property<T, ?> property, final V value, final Predicate<V> predicate) {
        return this.le(this.getSlot(), property, value, predicate);
    }

    /**
     * 小于或等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C le(final Slot slot, final Property<T, ?> property, final V value, final Predicate<V> predicate) {
        return this.le(slot, property.toProp(), value, predicate);
    }

    /**
     * 小于或等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C le(final String property, final Object value) {
        return le(this.getSlot(), property, value);
    }

    /**
     * 小于或等于
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    default <V> C le(final String property, final V value, final Predicate<V> predicate) {
        return this.le(this.getSlot(), property, value, predicate);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C le(final Slot slot, final String property, final Object value) {
        return this.le(slot, property, value, null);
    }

    /**
     * 小于或等于
     * @param slot      {@link Slot}
     * @param property  属性
     * @param value     值
     * @param predicate {@link Predicate}
     * @param <V>       属性值类型
     * @return {@code this}
     */
    <V> C le(final Slot slot, final String property, final V value, final Predicate<V> predicate);

    /**
     * 小于或等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C leq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.leq(this.getSlot(), property, query);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C leq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.leq(slot, property.toProp(), query);
    }

    /**
     * 小于或等于
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    default C leq(final String property, final ExtCriteria<?> query) {
        return this.leq(this.getSlot(), property, query);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}(查询条件对象)
     * @return {@code this}
     */
    C leq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Column equal to condition

    /**
     * 字段相等
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty {@link Property}
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> C ce(final ExtCriteria<E> otherCriteria, final Property<E, ?> otherProperty) {
        return this.ce(otherCriteria, otherProperty.toProp());
    }

    /**
     * 字段相等
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty 属性
     * @return {@code this}
     */
    C ce(final ExtCriteria<?> otherCriteria, final String otherProperty);

    /**
     * 字段相等
     * @param property      属性
     * @param otherCriteria {@link ExtCriteria}
     * @return {@code this}
     */
    default C ce(final Property<T, ?> property, final ExtCriteria<?> otherCriteria) {
        return this.ce(property.toProp(), otherCriteria);
    }

    /**
     * 字段相等
     * @param property      属性
     * @param otherCriteria {@link ExtCriteria}
     * @return {@code this}
     */
    C ce(final String property, final ExtCriteria<?> otherCriteria);

    /**
     * 字段相等
     * @param property      {@link Property}
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty {@link Property}
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> C ce(final Property<T, ?> property, final ExtCriteria<E> otherCriteria,
                     final Property<E, ?> otherProperty) {
        return this.ce(property, otherCriteria, otherProperty.toProp());
    }

    /**
     * 字段相等
     * @param property      {@link Property}
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty 属性
     * @return {@code this}
     */
    default C ce(final Property<T, ?> property, final ExtCriteria<?> otherCriteria, final String otherProperty) {
        return this.ce(property.toProp(), otherCriteria, otherProperty);
    }

    /**
     * 字段相等
     * @param property      属性
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty {@link Property}
     * @param <E>           实体类型
     * @return {@code this}
     */
    default <E> C ce(final String property, final ExtCriteria<E> otherCriteria, final Property<E, ?> otherProperty) {
        return this.ce(property, otherCriteria, otherProperty.toProp());
    }

    /**
     * 字段相等
     * @param property      属性
     * @param otherCriteria {@link ExtCriteria}
     * @param otherProperty 属性
     * @return {@code this}
     */
    C ce(final String property, final ExtCriteria<?> otherCriteria, final String otherProperty);

    /**
     * 字段相等
     * @param otherCriteria {@link ExtCriteria}
     * @param otherColumn   字段名
     * @return {@code this}
     */
    C ceWith(final ExtCriteria<?> otherCriteria, final String otherColumn);

    /**
     * 字段相等
     * @param property      {@link Property}
     * @param otherCriteria {@link ExtCriteria}
     * @param otherColumn   字段名
     * @return {@code this}
     */
    default C ceWith(final Property<T, ?> property, final ExtCriteria<?> otherCriteria, final String otherColumn) {
        return this.ceWith(property.toProp(), otherCriteria, otherColumn);
    }

    /**
     * 字段相等
     * @param property      属性
     * @param otherCriteria {@link ExtCriteria}
     * @param otherColumn   字段名
     * @return {@code this}
     */
    C ceWith(final String property, final ExtCriteria<?> otherCriteria, final String otherColumn);

    // endregion

}
