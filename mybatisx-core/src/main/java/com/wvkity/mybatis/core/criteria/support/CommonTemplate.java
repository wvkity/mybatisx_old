package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.basic.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.expr.TemplateMatch;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface CommonTemplate<T, C extends CommonTemplate<T, C>> {

    /**
     * 模板条件
     * @param template 模板
     * @param value    值
     * @return {@code this}
     */
    default C tpl(final String template, final Object value) {
        return this.tpl(Slot.NONE, template, null, TemplateMatch.SINGLE, value, null, null);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param value    值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Object value) {
        return this.tpl(slot, template, null, TemplateMatch.SINGLE, value, null, null);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final Object value) {
        return colTpl(Slot.NONE, template, column, value);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@code this}
     */
    default C colTpl(final Slot slot, final String template, final String column, final Object value) {
        return tpl(slot, template, column, TemplateMatch.SINGLE, value, null, null);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final Object... values) {
        return tpl(template, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final Collection<Object> values) {
        return this.tpl(Slot.NONE, template, null, null, null, values, null);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Object... values) {
        return tpl(slot, template, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Collection<Object> values) {
        return this.tpl(slot, template, null, TemplateMatch.MAP, null, null, values);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final Object... values) {
        return colTpl(Slot.NONE, template, column, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final Slot slot, final String template, final String column, final Object... values) {
        return colTpl(slot, template, column, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final Collection<Object> values) {
        return colTpl(Slot.NONE, template, column, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final Slot slot, final String template, final String column, final Collection<Object> values) {
        return this.tpl(slot, template, column, null, null, values, null);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C tpl(final String template, final String k1, final Object v1, final String k2, final Object v2) {
        return tpl(template, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C tpl(final String template, final String k1, final Object v1,
                  final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(template, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final String template, final Map<String, Object> values) {
        return this.tpl(Slot.NONE, template, values);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param slot     {@link Slot}
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final String k1, final Object v1,
                  final String k2, final Object v2) {
        return tpl(slot, template, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final String k1, final Object v1,
                  final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(slot, template, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param values   多个值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Map<String, Object> values) {
        return this.tpl(slot, template, null, TemplateMatch.MAP, null, null, values);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final String k1, final Object v1,
                     final String k2, final Object v2) {
        return colTpl(Slot.NONE, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@code this}
     */
    default C colTpl(final Slot slot, final String template, final String column, final String k1, final Object v1,
                     final String k2, final Object v2) {
        return colTpl(slot, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final String k1, final Object v1,
                     final String k2, final Object v2, final String k3, final Object v3) {
        return colTpl(Slot.NONE, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@code this}
     */
    default C colTpl(final Slot slot, final String template, final String column, final String k1, final Object v1,
                     final String k2, final Object v2, final String k3, final Object v3) {
        return colTpl(slot, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final Map<String, Object> values) {
        return colTpl(Slot.NONE, template, column, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final Slot slot, final String template, final String column, final Map<String, Object> values) {
        return this.tpl(slot, template, column, TemplateMatch.MAP, null, null, values);
    }

    /**
     * 模板条件
     * @param slot       {@link Slot}
     * @param template   模板
     * @param column     字段
     * @param match      {@link TemplateMatch}
     * @param value      值
     * @param listValues 多个值
     * @param mapValues  多个值
     * @return {@code this}
     */
    C tpl(Slot slot, String template, String column, TemplateMatch match, Object value,
          Collection<Object> listValues, Map<String, Object> mapValues);

}
