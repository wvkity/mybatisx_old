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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.support.constant.Like;
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
public interface FuzzyWrapper<T, Chain extends FuzzyWrapper<T, Chain>> {

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
        return like(Slot.AND, property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeLeft(final Slot slot, final Property<T, V> property, final V value) {
        return like(slot, property, value, Like.END, null);
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
        return like(Slot.AND, property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeLeft(final Slot slot, final Property<T, V> property, final V value, final Character escape) {
        return like(slot, property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeLeft(final String property, final Object value) {
        return like(Slot.AND, property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeLeft(final Slot slot, final String property, final Object value) {
        return like(slot, property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeLeft(final String property, final Object value, final Character escape) {
        return like(Slot.AND, property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeLeft(final Slot slot, final String property, final Object value, final Character escape) {
        return like(slot, property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLikeLeft(final String column, final Object value) {
        return colLike(Slot.AND, column, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLikeLeft(final Slot slot, final String column, final Object value) {
        return colLike(slot, column, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colLikeLeft(final String column, final Object value, final Character escape) {
        return colLike(Slot.AND, column, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    default Chain colLikeLeft(final Slot slot, final String column, final Object value, final Character escape) {
        return colLike(slot, column, value, Like.END, escape);
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
        return like(Slot.AND, property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeRight(final Slot slot, final Property<T, V> property, final V value) {
        return like(slot, property, value, Like.START, null);
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
        return like(Slot.AND, property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeRight(final Slot slot, final Property<T, V> property, final V value, final Character escape) {
        return like(slot, property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeRight(final String property, final Object value) {
        return like(Slot.AND, property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeRight(final Slot slot, final String property, final Object value) {
        return like(slot, property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeRight(final String property, final Object value, final Character escape) {
        return like(Slot.AND, property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeRight(final Slot slot, final String property, final Object value, final Character escape) {
        return like(slot, property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLikeRight(final String column, final Object value) {
        return colLike(Slot.AND, column, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLikeRight(final Slot slot, final String column, final Object value) {
        return colLike(slot, column, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colLikeRight(final String column, final Object value, final Character escape) {
        return colLike(Slot.AND, column, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    default Chain colLikeRight(final Slot slot, final String column, final Object value, final Character escape) {
        return colLike(slot, column, value, Like.START, escape);
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
        return like(Slot.AND, property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeAny(final Slot slot, final Property<T, V> property, final V value) {
        return like(slot, property, value, Like.ANYWHERE, null);
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
        return like(Slot.AND, property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain likeAny(final Slot slot, final Property<T, V> property, final V value, final Character escape) {
        return like(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeAny(final String property, final Object value) {
        return like(Slot.AND, property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain likeAny(final Slot slot, final String property, final Object value) {
        return like(slot, property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeAny(final String property, final Object value, final Character escape) {
        return like(Slot.AND, property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain likeAny(final Slot slot, final String property, final Object value, final Character escape) {
        return like(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLikeAny(final String column, final Object value) {
        return colLike(Slot.AND, column, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colLikeAny(final Slot slot, final String column, final Object value) {
        return colLike(slot, column, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colLikeAny(final String column, final Object value, final Character escape) {
        return colLike(Slot.AND, column, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colLikeAny(final Slot slot, final String column, final Object value, final Character escape) {
        return colLike(slot, column, value, Like.ANYWHERE, escape);
    }

    // endregion

    // region Basic like

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain like(final Property<T, V> property, final V value, final Like like) {
        return like(Slot.AND, property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain like(final Slot slot, final Property<T, V> property, final V value, final Like like) {
        return like(slot, property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain like(final Property<T, V> property, final V value, final Like like, final Character escape) {
        return like(Slot.AND, property, value, like, escape);
    }

    /**
     * like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain like(final Slot slot, final Property<T, V> property, final V value, final Like like,
                   final Character escape);

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@link Chain}
     */
    default Chain like(final String property, final Object value, final Like like) {
        return like(Slot.AND, property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@link Chain}
     */
    default Chain like(final Slot slot, final String property, final Object value, final Like like) {
        return like(slot, property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain like(final String property, final Object value, final Like like,
                       final Character escape) {
        return like(Slot.AND, property, value, like, escape);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain like(final Slot slot, final String property, final Object value, final Like like,
               final Character escape);

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@link Chain}
     */
    default Chain colLike(final String column, final Object value, final Like like) {
        return colLike(Slot.AND, column, value, like, null);
    }

    /**
     * like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@link Chain}
     */
    default Chain colLike(final Slot slot, final String column, final Object value, final Like like) {
        return colLike(slot, column, value, like, null);
    }

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colLike(final String column, final Object value, final Like like,
                          final Character escape) {
        return colLike(Slot.AND, column, value, like, escape);
    }

    /**
     * like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@link Chain}
     */
    Chain colLike(final Slot slot, final String column, final Object value, final Like like,
                  final Character escape);

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
        return notLike(Slot.AND, property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeLeft(final Slot slot, final Property<T, V> property, final V value) {
        return notLike(slot, property, value, Like.END, null);
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
        return notLike(Slot.AND, property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeLeft(final Slot slot, final Property<T, V> property, final V value,
                                  final Character escape) {
        return notLike(slot, property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final String property, final Object value) {
        return notLike(Slot.AND, property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final Slot slot, final String property, final Object value) {
        return notLike(slot, property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final String property, final Object value, final Character escape) {
        return notLike(Slot.AND, property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeLeft(final Slot slot, final String property, final Object value, final Character escape) {
        return notLike(slot, property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNotLikeLeft(final String column, final Object value) {
        return colNotLike(Slot.AND, column, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNotLikeLeft(final Slot slot, final String column, final Object value) {
        return colNotLike(slot, column, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLikeLeft(final String column, final Object value, final Character escape) {
        return colNotLike(Slot.AND, column, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLikeLeft(final Slot slot, final String column, final Object value, final Character escape) {
        return colNotLike(slot, column, value, Like.END, escape);
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
        return notLike(Slot.AND, property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeRight(final Slot slot, final Property<T, V> property, final V value) {
        return notLike(slot, property, value, Like.START, null);
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
        return notLike(Slot.AND, property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeRight(final Slot slot, final Property<T, V> property, final V value,
                                   final Character escape) {
        return notLike(slot, property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeRight(final String property, final Object value) {
        return notLike(Slot.AND, property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeRight(final Slot slot, final String property, final Object value) {
        return notLike(slot, property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeRight(final String property, final Object value, final Character escape) {
        return notLike(Slot.AND, property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeRight(final Slot slot, final String property, final Object value, final Character escape) {
        return notLike(slot, property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNotLikeRight(final String column, final Object value) {
        return colNotLike(Slot.AND, column, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNotLikeRight(final Slot slot, final String column, final Object value) {
        return colNotLike(slot, column, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLikeRight(final String column, final Object value, final Character escape) {
        return colNotLike(Slot.AND, column, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLikeRight(final Slot slot, final String column, final Object value, final Character escape) {
        return colNotLike(slot, column, value, Like.START, escape);
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
        return notLike(Slot.AND, property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeAny(final Slot slot, final Property<T, V> property, final V value) {
        return notLike(slot, property, value, Like.ANYWHERE, null);
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
        return notLike(Slot.AND, property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLikeAny(final Slot slot, final Property<T, V> property, final V value,
                                 final Character escape) {
        return notLike(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeAny(final String property, final Object value) {
        return notLike(Slot.AND, property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain notLikeAny(final Slot slot, final String property, final Object value) {
        return notLike(slot, property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeAny(final String property, final Object value, final Character escape) {
        return notLike(Slot.AND, property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLikeAny(final Slot slot, final String property, final Object value, final Character escape) {
        return notLike(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNotLikeAny(final String column, final Object value) {
        return colNotLike(Slot.AND, column, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@link Chain}
     */
    default Chain colNotLikeAny(final Slot slot, final String column, final Object value) {
        return colNotLike(slot, column, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLikeAny(final String column, final Object value, final Character escape) {
        return colNotLike(Slot.AND, column, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLikeAny(final Slot slot, final String column, final Object value, final Character escape) {
        return colNotLike(slot, column, value, Like.ANYWHERE, escape);
    }

    // endregion

    // region Basic not like

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLike(final Property<T, V> property, final V value, final Like like) {
        return notLike(Slot.AND, property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLike(final Slot slot, final Property<T, V> property, final V value, final Like like) {
        return notLike(slot, property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notLike(final Property<T, V> property, final V value, final Like like,
                              final Character escape) {
        return notLike(Slot.AND, property, value, like, escape);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notLike(final Slot slot, final Property<T, V> property, final V value, final Like like,
                      final Character escape);

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@link Chain}
     */
    default Chain notLike(final String property, final Object value, final Like like) {
        return notLike(Slot.AND, property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@link Chain}
     */
    default Chain notLike(final Slot slot, final String property, final Object value, final Like like) {
        return notLike(slot, property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @return {@link Chain}
     */
    default Chain notLike(final String property, final Object value, final Like like, final Character escape) {
        return notLike(Slot.AND, property, value, like, escape);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @return {@link Chain}
     */
    Chain notLike(final Slot slot, final String property, final Object value, final Like like,
                  final Character escape);

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@link Chain}
     */
    default Chain colNotLike(final String column, final Object value, final Like like) {
        return colNotLike(Slot.AND, column, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@link Chain}
     */
    default Chain colNotLike(final Slot slot, final String column, final Object value, final Like like) {
        return colNotLike(slot, column, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@link Chain}
     */
    default Chain colNotLike(final String column, final Object value, final Like like, final Character escape) {
        return colNotLike(Slot.AND, column, value, like, escape);
    }

    /**
     * not like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@link Chain}
     */
    Chain colNotLike(final Slot slot, final String column, final Object value, final Like like,
                     final Character escape);

    // endregion

    // endregion

}