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

import com.github.mybatisx.Objects;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.support.constant.Slot;

import java.util.Collection;

/**
 * 范围条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface LambdaRange<T, C extends LambdaRange<T, C>> extends SlotSymbol<T, C> {

    // region In condition

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default <V> C in(final Property<T, V> property, final V... values) {
        return in(this.getSlot(), property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default <V> C in(final Slot slot, final Property<T, V> property, final V... values) {
        return in(slot, property, Objects.asList(values));
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C in(final Property<T, V> property, final Collection<V> values) {
        return in(this.getSlot(), property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C in(final Slot slot, final Property<T, V> property, final Collection<V> values);

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C in(final String property, final Object... values) {
        return in(this.getSlot(), property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C in(final Slot slot, final String property, final Object... values) {
        return in(slot, property, Objects.asList(values));
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C in(final String property, final Collection<?> values) {
        return in(this.getSlot(), property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C in(final Slot slot, final String property, final Collection<?> values);

    /**
     * IN
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    default C inq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.inq(this.getSlot(), property, query);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    C inq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query);

    /**
     * IN
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    default C inq(final String property, final ExtCriteria<?> query) {
        return this.inq(this.getSlot(), property, query);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    C inq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Not in condition

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default <V> C notIn(final Property<T, V> property, final V... values) {
        return notIn(this.getSlot(), property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    @SuppressWarnings({"unchecked"})
    default <V> C notIn(final Slot slot, final Property<T, V> property, final V... values) {
        return notIn(slot, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notIn(final Property<T, V> property, final Collection<V> values) {
        return notIn(this.getSlot(), property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C notIn(final Slot slot, final Property<T, V> property, final Collection<V> values);

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C notIn(final String property, final Object... values) {
        return notIn(this.getSlot(), property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C notIn(final Slot slot, final String property, final Object... values) {
        return notIn(slot, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C notIn(final String property, final Collection<?> values) {
        return notIn(this.getSlot(), property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C notIn(final Slot slot, final String property, final Collection<?> values);

    /**
     * NOT IN
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    default C notInq(final Property<T, ?> property, final ExtCriteria<?> query) {
        return this.notInq(this.getSlot(), property, query);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    C notInq(final Slot slot, final Property<T, ?> property, final ExtCriteria<?> query);

    /**
     * NOT IN
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    default C notInq(final String property, final ExtCriteria<?> query) {
        return this.notInq(this.getSlot(), property, query);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param query    {@link ExtCriteria}
     * @return {@code this}
     */
    C notInq(final Slot slot, final String property, final ExtCriteria<?> query);

    // endregion

    // region Between condition

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C between(final Property<T, V> property, final V begin, final V end) {
        return this.between(this.getSlot(), property, begin, end);
    }

    /**
     * Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C between(final Slot slot, final Property<T, V> property, final V begin, final V end);

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@code this}
     */
    default C between(final String property, final Object begin, final Object end) {
        return this.between(this.getSlot(), property, begin, end);
    }

    /**
     * Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@code this}
     */
    C between(final Slot slot, final String property, final Object begin, final Object end);

    // endregion

    // region Not between condition

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notBetween(final Property<T, V> property, final V begin, final V end) {
        return this.notBetween(this.getSlot(), property, begin, end);
    }

    /**
     * Not Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C notBetween(final Slot slot, final Property<T, V> property, final V begin, final V end);

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@code this}
     */
    default C notBetween(final String property, final Object begin, final Object end) {
        return this.notBetween(this.getSlot(), property, begin, end);
    }

    /**
     * Not Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@code this}
     */
    C notBetween(final Slot slot, final String property, final Object begin, final Object end);

    // endregion
}
