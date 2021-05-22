package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;

/**
 * 范围条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface LambdaRange<T, C extends LambdaRange<T, C>> {

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
        return in(Slot.AND, property, values);
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
        return in(Slot.AND, property, values);
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
        return in(Slot.AND, property, values);
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
        return in(Slot.AND, property, values);
    }

    /**
     * IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C in(final Slot slot, final String property, final Collection<?> values);

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
        return notIn(Slot.AND, property, values);
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
        return notIn(Slot.AND, property, values);
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
        return notIn(Slot.AND, property, values);
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
        return notIn(Slot.AND, property, values);
    }

    /**
     * NOT IN
     * @param slot     {@link Slot}
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C notIn(final Slot slot, final String property, final Collection<?> values);

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
        return this.between(Slot.AND, property, begin, end);
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
        return this.between(Slot.AND, property, begin, end);
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
        return this.notBetween(Slot.AND, property, begin, end);
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
        return this.notBetween(Slot.AND, property, begin, end);
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
