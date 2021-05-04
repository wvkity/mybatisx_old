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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.core.property.Property;

import java.util.Collection;

/**
 * 范围条件
 * @param <T> 泛型类
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public interface RangeWrapper<T, Chain extends RangeWrapper<T, Chain>> {

    // region In

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default <V> Chain in(final Property<T, V> property, final V... values) {
        return in(Slot.AND, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default <V> Chain in(final Slot slot, final Property<T, V> property, final V... values) {
        return in(slot, property, Objects.asList(values));
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain in(final Property<T, V> property, final Collection<V> values) {
        return in(Slot.AND, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain in(final Slot slot, final Property<T, V> property, final Collection<V> values);

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain in(final String property, final Object... values) {
        return in(Slot.AND, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain in(final Slot slot, final String property, final Object... values) {
        return in(slot, property, Objects.asList(values));
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain in(final String property, final Collection<?> values) {
        return in(Slot.AND, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain in(final Slot slot, final String property, final Collection<?> values);

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colIn(final String column, final Object... values) {
        return colIn(Slot.AND, column, values);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colIn(final Slot slot, final String column, final Object... values) {
        return colIn(slot, column, Objects.asList(values));
    }

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colIn(final String column, final Collection<?> values) {
        return colIn(Slot.AND, column, values);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    Chain colIn(final Slot slot, final String column, final Collection<?> values);

    // endregion

    // region Not in

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default <V> Chain notIn(final Property<T, V> property, final V... values) {
        return notIn(Slot.AND, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default <V> Chain notIn(final Slot slot, final Property<T, V> property, final V... values) {
        return notIn(slot, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notIn(final Property<T, V> property, final Collection<V> values) {
        return notIn(Slot.AND, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notIn(final Slot slot, final Property<T, V> property, final Collection<V> values);

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain notIn(final String property, final Object... values) {
        return notIn(Slot.AND, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain notIn(final Slot slot, final String property, final Object... values) {
        return notIn(slot, property, Objects.asList(values));
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain notIn(final String property, final Collection<?> values) {
        return notIn(Slot.AND, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain notIn(final Slot slot, final String property, final Collection<?> values);

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colNotIn(final String column, final Object... values) {
        return colNotIn(Slot.AND, column, values);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colNotIn(final Slot slot, final String column, final Object... values) {
        return colNotIn(column, Objects.asList(values), slot);
    }

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colNotIn(final String column, final Collection<?> values) {
        return colNotIn(Slot.AND, column, values);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    Chain colNotIn(final Slot slot, final String column, final Collection<?> values);

    // endregion

    // region Between

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain between(final Property<T, V> property, final V begin, final V end) {
        return between(Slot.AND, property, begin, end);
    }

    /**
     * Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain between(final Slot slot, final Property<T, V> property, final V begin, final V end);

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@link Chain}
     */
    default Chain between(final String property, final Object begin, final Object end) {
        return between(Slot.AND, property, begin, end);
    }

    /**
     * Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@link Chain}
     */
    Chain between(final Slot slot, final String property, final Object begin, final Object end);

    /**
     * Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@link Chain}
     */
    default Chain colBetween(final String column, final Object begin, final Object end) {
        return colBetween(Slot.AND, column, begin, end);
    }

    /**
     * Between
     * @param slot   {@link Slot}
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@link Chain}
     */
    Chain colBetween(final Slot slot, final String column, final Object begin, final Object end);

    // endregion

    // region Not between

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notBetween(final Property<T, V> property, final V begin, final V end) {
        return notBetween(Slot.AND, property, begin, end);
    }

    /**
     * Not Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notBetween(final Slot slot, final Property<T, V> property, final V begin, final V end);

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@link Chain}
     */
    default Chain notBetween(final String property, final Object begin, final Object end) {
        return notBetween(Slot.AND, property, begin, end);
    }

    /**
     * Not Between
     * @param slot     {@link Slot}
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@link Chain}
     */
    Chain notBetween(final Slot slot, final String property, final Object begin, final Object end);

    /**
     * Not Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@link Chain}
     */
    default Chain colNotBetween(final String column, final Object begin, final Object end) {
        return colNotBetween(Slot.AND, column, begin, end);
    }

    /**
     * Not Between
     * @param slot   {@link Slot}
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@link Chain}
     */
    Chain colNotBetween(final Slot slot, final String column, final Object begin, final Object end);

    // endregion
}
