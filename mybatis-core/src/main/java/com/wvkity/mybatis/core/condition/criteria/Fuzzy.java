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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.support.constant.LikeMode;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.core.property.Property;

/**
 * 模糊条件
 * @param <T>     泛型类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-08
 * @since 1.0.0
 */
public interface Fuzzy<T, Chain extends Fuzzy<T, Chain>> {

    // region Like methods

    // region Like left (%arg)

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeLeft(final Property<T, V> property, final V value) {
        return like(property, value, LikeMode.END, null, Slot.AND);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeLeft(final Property<T, V> property, final V value, final Slot slot) {
        return like(property, value, LikeMode.END, null, slot);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeLeft(final Property<T, V> property, final V value, final Character escape) {
        return like(property, value, LikeMode.END, escape, Slot.AND);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeLeft(final Property<T, V> property, final V value, final Character escape, final Slot slot) {
        return like(property, value, LikeMode.END, escape, slot);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeLeft(final String property, final Object value) {
        return like(property, value, LikeMode.END, null, Slot.AND);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain likeLeft(final String property, final Object value, final Slot slot) {
        return like(property, value, LikeMode.END, null, slot);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeLeft(final String property, final Object value, final Character escape) {
        return like(property, value, LikeMode.END, escape, Slot.AND);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain likeLeft(final String property, final Object value, final Character escape, final Slot slot) {
        return like(property, value, LikeMode.END, escape, slot);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeLeft(final String column, final V value) {
        return colLike(column, value, LikeMode.END, null, Slot.AND);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeLeft(final String column, final V value, final Slot slot) {
        return colLike(column, value, LikeMode.END, null, slot);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeLeft(final String column, final V value, final Character escape) {
        return colLike(column, value, LikeMode.END, escape, Slot.AND);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeLeft(final String column, final V value, final Character escape, final Slot slot) {
        return colLike(column, value, LikeMode.END, escape, slot);
    }

    // endregion

    // region Like right (arg%)

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeRight(final Property<T, V> property, final V value) {
        return like(property, value, LikeMode.START, null, Slot.AND);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeRight(final Property<T, V> property, final V value, final Slot slot) {
        return like(property, value, LikeMode.START, null, slot);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeRight(final Property<T, V> property, final V value, final Character escape) {
        return like(property, value, LikeMode.START, escape, Slot.AND);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeRight(final Property<T, V> property, final V value, final Character escape, final Slot slot) {
        return like(property, value, LikeMode.START, escape, slot);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeRight(final String property, final Object value) {
        return like(property, value, LikeMode.START, null, Slot.AND);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain likeRight(final String property, final Object value, final Slot slot) {
        return like(property, value, LikeMode.START, null, slot);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeRight(final String property, final Object value, final Character escape) {
        return like(property, value, LikeMode.START, escape, Slot.AND);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain likeRight(final String property, final Object value, final Character escape, final Slot slot) {
        return like(property, value, LikeMode.START, escape, slot);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeRight(final String column, final V value) {
        return colLike(column, value, LikeMode.START, null, Slot.AND);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeRight(final String column, final V value, final Slot slot) {
        return colLike(column, value, LikeMode.START, null, slot);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeRight(final String column, final V value, final Character escape) {
        return colLike(column, value, LikeMode.START, escape, Slot.AND);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeRight(final String column, final V value, final Character escape, final Slot slot) {
        return colLike(column, value, LikeMode.START, escape, slot);
    }

    // endregion

    // region Like anywhere (%arg%)

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeAny(final Property<T, V> property, final V value) {
        return like(property, value, LikeMode.ANYWHERE, null, Slot.AND);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeAny(final Property<T, V> property, final V value, final Slot slot) {
        return like(property, value, LikeMode.ANYWHERE, null, slot);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeAny(final Property<T, V> property, final V value, final Character escape) {
        return like(property, value, LikeMode.ANYWHERE, escape, Slot.AND);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeAny(final Property<T, V> property, final V value, final Character escape, final Slot slot) {
        return like(property, value, LikeMode.ANYWHERE, escape, slot);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeAny(final String property, final Object value) {
        return like(property, value, LikeMode.ANYWHERE, null, Slot.AND);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain likeAny(final String property, final Object value, final Slot slot) {
        return like(property, value, LikeMode.ANYWHERE, null, slot);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeAny(final String property, final Object value, final Character escape) {
        return like(property, value, LikeMode.ANYWHERE, escape, Slot.AND);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain likeAny(final String property, final Object value, final Character escape, final Slot slot) {
        return like(property, value, LikeMode.ANYWHERE, escape, slot);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeAny(final String column, final V value) {
        return colLike(column, value, LikeMode.ANYWHERE, null, Slot.AND);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeAny(final String column, final V value, final Slot slot) {
        return colLike(column, value, LikeMode.ANYWHERE, null, slot);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeAny(final String column, final V value, final Character escape) {
        return colLike(column, value, LikeMode.ANYWHERE, escape, Slot.AND);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colLikeAny(final String column, final V value, final Character escape, final Slot slot) {
        return colLike(column, value, LikeMode.ANYWHERE, escape, slot);
    }

    // endregion

    // region Basic like

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain like(final Property<T, V> property, final V value, final LikeMode mode) {
        return like(property, value, mode, null, Slot.AND);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain like(final Property<T, V> property, final V value, final LikeMode mode,
                           final Slot slot) {
        return like(property, value, mode, null, slot);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain like(final Property<T, V> property, final V value, final LikeMode mode,
                           final Character escape) {
        return like(property, value, mode, escape, Slot.AND);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain like(final Property<T, V> property, final V value, final LikeMode mode,
                   final Character escape, final Slot slot);

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @return {@link Chain}
     */
    default Chain like(final String property, final Object value, final LikeMode mode) {
        return like(property, value, mode, null, Slot.AND);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain like(final String property, final Object value, final LikeMode mode,
                       final Slot slot) {
        return like(property, value, mode, null, slot);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain like(final String property, final Object value, final LikeMode mode,
                       final Character escape) {
        return like(property, value, mode, escape, Slot.AND);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain like(final String property, final Object value, final LikeMode mode,
               final Character escape, final Slot slot);

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @return {@link Chain}
     */
    default Chain colLike(final String column, final Object value, final LikeMode mode) {
        return colLike(column, value, mode, null, Slot.AND);
    }

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    default Chain colLike(final String column, final Object value, final LikeMode mode,
                          final Slot slot) {
        return colLike(column, value, mode, null, slot);
    }

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colLike(final String column, final Object value, final LikeMode mode,
                          final Character escape) {
        return colLike(column, value, mode, escape, Slot.AND);
    }

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colLike(final String column, final Object value, final LikeMode mode,
                  final Character escape, final Slot slot);

    // endregion

    // endregion

    // region Not like methods

    // region Not like left (%arg)

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeLeft(final Property<T, V> property, final V value) {
        return notLike(property, value, LikeMode.END, null, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeLeft(final Property<T, V> property, final V value, final Slot slot) {
        return notLike(property, value, LikeMode.END, null, slot);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeLeft(final Property<T, V> property, final V value, final Character escape) {
        return notLike(property, value, LikeMode.END, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeLeft(final Property<T, V> property, final V value,
                                  final Character escape, final Slot slot) {
        return notLike(property, value, LikeMode.END, escape, slot);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final String property, final Object value) {
        return notLike(property, value, LikeMode.END, null, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final String property, final Object value, final Slot slot) {
        return notLike(property, value, LikeMode.END, null, slot);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final String property, final Object value, final Character escape) {
        return notLike(property, value, LikeMode.END, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final String property, final Object value, final Character escape, final Slot slot) {
        return notLike(property, value, LikeMode.END, escape, slot);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeLeft(final String column, final V value) {
        return colNotLike(column, value, LikeMode.END, null, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeLeft(final String column, final V value, final Slot slot) {
        return colNotLike(column, value, LikeMode.END, null, slot);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeLeft(final String column, final V value, final Character escape) {
        return colNotLike(column, value, LikeMode.END, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeLeft(final String column, final V value, final Character escape, final Slot slot) {
        return colNotLike(column, value, LikeMode.END, escape, slot);
    }

    // endregion

    // region Not like right (arg%)

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeRight(final Property<T, V> property, final V value) {
        return notLike(property, value, LikeMode.START, null, Slot.AND);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeRight(final Property<T, V> property, final V value, final Slot slot) {
        return notLike(property, value, LikeMode.START, null, slot);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeRight(final Property<T, V> property, final V value, final Character escape) {
        return notLike(property, value, LikeMode.START, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeRight(final Property<T, V> property, final V value,
                                   final Character escape, final Slot slot) {
        return notLike(property, value, LikeMode.START, escape, slot);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeRight(final String property, final Object value) {
        return notLike(property, value, LikeMode.START, null, Slot.AND);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLikeRight(final String property, final Object value, final Slot slot) {
        return notLike(property, value, LikeMode.START, null, slot);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeRight(final String property, final Object value, final Character escape) {
        return notLike(property, value, LikeMode.START, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLikeRight(final String property, final Object value, final Character escape, final Slot slot) {
        return notLike(property, value, LikeMode.START, escape, slot);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeRight(final String column, final V value) {
        return colNotLike(column, value, LikeMode.START, null, Slot.AND);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeRight(final String column, final V value, final Slot slot) {
        return colNotLike(column, value, LikeMode.START, null, slot);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeRight(final String column, final V value, final Character escape) {
        return colNotLike(column, value, LikeMode.START, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeRight(final String column, final V value, final Character escape, final Slot slot) {
        return colNotLike(column, value, LikeMode.START, escape, slot);
    }

    // endregion

    // region Not like anywhere (%arg%)

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeAny(final Property<T, V> property, final V value) {
        return notLike(property, value, LikeMode.ANYWHERE, null, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeAny(final Property<T, V> property, final V value, final Slot slot) {
        return notLike(property, value, LikeMode.ANYWHERE, null, slot);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeAny(final Property<T, V> property, final V value, final Character escape) {
        return notLike(property, value, LikeMode.ANYWHERE, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeAny(final Property<T, V> property, final V value,
                                 final Character escape, final Slot slot) {
        return notLike(property, value, LikeMode.ANYWHERE, escape, slot);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeAny(final String property, final Object value) {
        return notLike(property, value, LikeMode.ANYWHERE, null, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLikeAny(final String property, final Object value, final Slot slot) {
        return notLike(property, value, LikeMode.ANYWHERE, null, slot);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeAny(final String property, final Object value, final Character escape) {
        return notLike(property, value, LikeMode.ANYWHERE, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLikeAny(final String property, final Object value, final Character escape, final Slot slot) {
        return notLike(property, value, LikeMode.ANYWHERE, escape, slot);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeAny(final String column, final V value) {
        return colNotLike(column, value, LikeMode.ANYWHERE, null, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeAny(final String column, final V value, final Slot slot) {
        return colNotLike(column, value, LikeMode.ANYWHERE, null, slot);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeAny(final String column, final V value, final Character escape) {
        return colNotLike(column, value, LikeMode.ANYWHERE, escape, Slot.AND);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @param <V>    属性类型
     * @return {@link Chain}
     */
    default <V> Chain colNotLikeAny(final String column, final V value, final Character escape, final Slot slot) {
        return colNotLike(column, value, LikeMode.ANYWHERE, escape, slot);
    }

    // endregion

    // region Basic not like

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLike(final Property<T, V> property, final V value, final LikeMode mode) {
        return notLike(property, value, mode, null, Slot.AND);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLike(final Property<T, V> property, final V value, final LikeMode mode,
                              final Slot slot) {
        return notLike(property, value, mode, null, slot);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLike(final Property<T, V> property, final V value, final LikeMode mode,
                              final Character escape) {
        return notLike(property, value, mode, escape, Slot.AND);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notLike(final Property<T, V> property, final V value, final LikeMode mode,
                      final Character escape, final Slot slot);

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @return {@link Chain}
     */
    default Chain notLike(final String property, final Object value, final LikeMode mode) {
        return notLike(property, value, mode, null, Slot.AND);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain notLike(final String property, final Object value, final LikeMode mode,
                          final Slot slot) {
        return notLike(property, value, mode, null, slot);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLike(final String property, final Object value, final LikeMode mode,
                          final Character escape) {
        return notLike(property, value, mode, escape, Slot.AND);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param mode     匹配模式
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain notLike(final String property, final Object value, final LikeMode mode,
                  final Character escape, final Slot slot);

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @return {@link Chain}
     */
    default Chain colNotLike(final String column, final Object value, final LikeMode mode) {
        return colNotLike(column, value, mode, null, Slot.AND);
    }

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    default Chain colNotLike(final String column, final Object value, final LikeMode mode,
                             final Slot slot) {
        return colNotLike(column, value, mode, null, slot);
    }

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLike(final String column, final Object value, final LikeMode mode,
                             final Character escape) {
        return colNotLike(column, value, mode, escape, Slot.AND);
    }

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param mode   匹配模式
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colNotLike(final String column, final Object value, final LikeMode mode,
                     final Character escape, final Slot slot);

    // endregion

    // endregion

}
