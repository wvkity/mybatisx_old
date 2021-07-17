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
import com.github.mybatisx.PlaceholderPattern;
import com.github.mybatisx.immutable.ImmutableLinkedMap;
import com.github.mybatisx.support.constant.Slot;

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
interface CommonTemplate<T, C extends CommonTemplate<T, C>> extends SlotSymbol<T, C> {

    /**
     * 模板条件
     * @param template 模板
     * @param value    值
     * @return {@code this}
     */
    default C tpl(final String template, final Object value) {
        return this.tpl(this.getSlot(), template, null, PlaceholderPattern.SINGLE, value, null, null);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param value    值
     * @return {@code this}
     */
    default C tpl(final Slot slot, final String template, final Object value) {
        return this.tpl(slot, template, null, PlaceholderPattern.SINGLE, value, null, null);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final Object value) {
        return colTpl(this.getSlot(), template, column, value);
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
        return tpl(slot, template, column, PlaceholderPattern.SINGLE, value, null, null);
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
        return this.tpl(this.getSlot(), template, null, null, null, values, null);
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
        return this.tpl(slot, template, null, PlaceholderPattern.MAP, null, null, values);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@code this}
     */
    default C colTpl(final String template, final String column, final Object... values) {
        return colTpl(this.getSlot(), template, column, Objects.asList(values));
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
        return colTpl(this.getSlot(), template, column, values);
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
        return this.tpl(this.getSlot(), template, values);
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
        return this.tpl(slot, template, null, PlaceholderPattern.MAP, null, null, values);
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
        return colTpl(this.getSlot(), template, column, ImmutableLinkedMap.of(k1, v1, k2, v2));
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
        return colTpl(this.getSlot(), template, column, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
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
        return colTpl(this.getSlot(), template, column, values);
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
        return this.tpl(slot, template, column, PlaceholderPattern.MAP, null, null, values);
    }

    /**
     * 模板条件
     * @param slot       {@link Slot}
     * @param template   模板
     * @param column     字段
     * @param pattern      {@link PlaceholderPattern}
     * @param value      值
     * @param listValues 多个值
     * @param mapValues  多个值
     * @return {@code this}
     */
    C tpl(Slot slot, String template, String column, PlaceholderPattern pattern, Object value,
          Collection<Object> listValues, Map<String, Object> mapValues);

}
