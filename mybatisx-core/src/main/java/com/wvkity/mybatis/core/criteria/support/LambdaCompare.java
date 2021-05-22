package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Map;

/**
 * 基础比较条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
interface LambdaCompare<T, C extends LambdaCompare<T, C>> {

    // region Equal to condition

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C eq(final Property<T, V> property, final V value) {
        return this.eq(Slot.AND, property, value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C eq(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C eq(final String property, final Object value) {
        return this.eq(Slot.AND, property, value);
    }

    /**
     * 等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C eq(final Slot slot, final String property, final Object value);

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
        return eq(Slot.AND, p1, v1, p2, v2);
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
        return eq(Slot.AND, p1, v1, p2, v2, p3, v3);
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
        return eq(Slot.AND, p1, v1, p2, v2);
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
        return eq(Slot.AND, p1, v1, p2, v2, p3, v3);
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
        return eq(Slot.AND, properties);
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
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C ne(final Property<T, V> property, final V value) {
        return ne(Slot.AND, property, value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C ne(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 不等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C ne(final String property, final Object value) {
        return ne(Slot.AND, property, value);
    }

    /**
     * 不等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C ne(final Slot slot, final String property, final Object value);

    // endregion

    // region Greater than condition

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C gt(final Property<T, V> property, final V value) {
        return gt(Slot.AND, property, value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C gt(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 大于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C gt(final String property, final Object value) {
        return gt(Slot.AND, property, value);
    }

    /**
     * 大于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C gt(final Slot slot, final String property, final Object value);

    // endregion

    // region Greater than or equal to condition

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C ge(final Property<T, V> property, final V value) {
        return ge(Slot.AND, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C ge(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 大于或等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C ge(final String property, final Object value) {
        return ge(Slot.AND, property, value);
    }

    /**
     * 大于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C ge(final Slot slot, final String property, final Object value);

    // endregion

    // region Less than condition

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C lt(final Property<T, V> property, final V value) {
        return lt(Slot.AND, property, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C lt(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 小于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C lt(final String property, final Object value) {
        return lt(Slot.AND, property, value);
    }

    /**
     * 小于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C lt(final Slot slot, final String property, final Object value);

    // endregion

    // region Less than or equal to condition

    /**
     * 小于或等于
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C le(final Property<T, V> property, final V value) {
        return le(Slot.AND, property, value);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C le(final Slot slot, final Property<T, V> property, final V value);

    /**
     * 小于或等于
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C le(final String property, final Object value) {
        return le(Slot.AND, property, value);
    }

    /**
     * 小于或等于
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C le(final Slot slot, final String property, final Object value);

    // endregion
}