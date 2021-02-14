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
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;

/**
 * 范围条件
 * @param <T> 泛型类
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public interface Range<T, Chain extends Range<T, Chain>> {

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
        return in(property, Slot.AND, values);
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default <V> Chain in(final Property<T, V> property, final Slot slot, final V... values) {
        return in(property, Objects.asList(values), slot);
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain in(final Property<T, V> property, final Collection<V> values) {
        return in(property, values, Slot.AND);
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain in(final Property<T, V> property, final Collection<V> values, final Slot slot);

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain in(final String property, final Object... values) {
        return in(property, Slot.AND, values);
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain in(final String property, final Slot slot, final Object... values) {
        return in(property, Objects.asList(values), slot);
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain in(final String property, final Collection<Object> values) {
        return in(property, values, Slot.AND);
    }

    /**
     * IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain in(final String property, final Collection<Object> values, final Slot slot);

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colIn(final String column, final Object... values) {
        return colIn(column, Slot.AND, values);
    }

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    default Chain colIn(final String column, final Slot slot, final Object... values) {
        return colIn(column, Objects.asList(values), slot);
    }

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colIn(final String column, final Collection<Object> values) {
        return colIn(column, values, Slot.AND);
    }

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colIn(final String column, final Collection<Object> values, final Slot slot);

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
        return notIn(property, Slot.AND, values);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default <V> Chain notIn(final Property<T, V> property, final Slot slot, final V... values) {
        return notIn(property, Objects.asList(values), slot);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notIn(final Property<T, V> property, final Collection<V> values) {
        return notIn(property, values, Slot.AND);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notIn(final Property<T, V> property, final Collection<V> values, final Slot slot);

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain notIn(final String property, final Object... values) {
        return notIn(property, Slot.AND, values);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notIn(final String property, final Slot slot, final Object... values) {
        return notIn(property, Objects.asList(values), slot);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain notIn(final String property, final Collection<Object> values) {
        return notIn(property, values, Slot.AND);
    }

    /**
     * NOT IN
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain notIn(final String property, final Collection<Object> values, final Slot slot);

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colNotIn(final String column, final Object... values) {
        return colNotIn(column, Slot.AND, values);
    }

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    default Chain colNotIn(final String column, final Slot slot, final Object... values) {
        return colNotIn(column, Objects.asList(values), slot);
    }

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@link Chain}
     */
    default Chain colNotIn(final String column, final Collection<Object> values) {
        return colNotIn(column, values, Slot.AND);
    }

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colNotIn(final String column, final Collection<Object> values, final Slot slot);

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
        return between(property, begin, end, Slot.AND);
    }

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain between(final Property<T, V> property, final V begin, final V end, final Slot slot);

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@link Chain}
     */
    default Chain between(final String property, final Object begin, final Object end) {
        return between(property, begin, end, Slot.AND);
    }

    /**
     * Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain between(final String property, final Object begin, final Object end, final Slot slot);

    /**
     * Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@link Chain}
     */
    default Chain colBetween(final String column, final Object begin, final Object end) {
        return colBetween(column, begin, end, Slot.AND);
    }

    /**
     * Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colBetween(final String column, final Object begin, final Object end, final Slot slot);

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
        return notBetween(property, begin, end, Slot.AND);
    }

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notBetween(final Property<T, V> property, final V begin, final V end, final Slot slot);

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @return {@link Chain}
     */
    default Chain notBetween(final String property, final Object begin, final Object end) {
        return notBetween(property, begin, end, Slot.AND);
    }

    /**
     * Not Between
     * @param property 属性
     * @param begin    开始值
     * @param end      结束值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain notBetween(final String property, final Object begin, final Object end, final Slot slot);

    /**
     * Not Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@link Chain}
     */
    default Chain colNotBetween(final String column, final Object begin, final Object end) {
        return colNotBetween(column, begin, end, Slot.AND);
    }

    /**
     * Not Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colNotBetween(final String column, final Object begin, final Object end, final Slot slot);

    // endregion
}
