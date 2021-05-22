package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.basic.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface LambdaTemplate<T, C extends LambdaTemplate<T, C>> {

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C tpl(final String template, final Property<T, ?> property, final Object value) {
        return tpl(Slot.NONE, template, property, value);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C tpl(final Slot slot, final String template, final Property<T, ?> property, final Object value);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C tpl(final String template, final String property, final Object value) {
        return tpl(Slot.NONE, template, property, value);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C tpl(final Slot slot, final String template, final String property, final Object value);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final Property<T, ?> property, final Object... values) {
        return tpl(Slot.NONE, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template,
                  final Property<T, ?> property, final Object... values) {
        return tpl(slot, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final Property<T, ?> property, final Collection<Object> values) {
        return tpl(Slot.NONE, template, property, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C tpl(final Slot slot, final String template, final Property<T, ?> property,
          final Collection<Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final String property, final Object... values) {
        return tpl(Slot.NONE, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final String property, final Object... values) {
        return tpl(slot, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final String property, final Collection<Object> values) {
        return tpl(Slot.NONE, template, property, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C tpl(final Slot slot, final String template, final String property, final Collection<Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C tpl(final String template, final Property<T, ?> property, final String k1, final Object v1,
                  final String k2, final Object v2) {
        return tpl(Slot.NONE, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Property<T, ?> property, final String k1,
                  final Object v1, final String k2, final Object v2) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C tpl(final String template, final Property<T, ?> property, final String k1, final Object v1,
                  final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(Slot.NONE, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Property<T, ?> property, final String k1,
                  final Object v1, final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final Property<T, ?> property, final Map<String, Object> values) {
        return tpl(Slot.NONE, template, property, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    C tpl(final Slot slot, final String template, final Property<T, ?> property,
          final Map<String, Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C tpl(final String template, final String property, final String k1, final Object v1,
                  final String k2, final Object v2) {
        return tpl(Slot.NONE, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final String property, final String k1, final Object v1,
                  final String k2, final Object v2) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C tpl(final String template, final String property, final String k1, final Object v1,
                  final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(Slot.NONE, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final String property, final String k1, final Object v1,
                  final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final String property, final Map<String, Object> values) {
        return tpl(Slot.NONE, template, property, values);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @return {@code this}
     */
    C tpl(final Slot slot, final String template, final String property, final Map<String, Object> values);

}
