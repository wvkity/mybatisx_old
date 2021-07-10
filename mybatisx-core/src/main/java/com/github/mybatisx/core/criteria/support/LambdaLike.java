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

import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.support.constant.Like;
import com.github.mybatisx.support.constant.Slot;

/**
 * like模糊匹配条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface LambdaLike<T, C extends LambdaLike<T, C>> extends SlotSymbol<T, C> {

    // region Like condition

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeLeft(final Property<T, V> property, final V value) {
        return this.like(this.getSlot(), property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeLeft(final Slot slot, final Property<T, V> property, final V value) {
        return this.like(slot, property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeLeft(final Property<T, V> property, final V value, final Character escape) {
        return this.like(this.getSlot(), property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeLeft(final Slot slot, final Property<T, V> property, final V value, final Character escape) {
        return this.like(slot, property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C likeLeft(final String property, final Object value) {
        return this.like(this.getSlot(), property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C likeLeft(final Slot slot, final String property, final Object value) {
        return this.like(slot, property, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C likeLeft(final String property, final Object value, final Character escape) {
        return this.like(this.getSlot(), property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C likeLeft(final Slot slot, final String property, final Object value, final Character escape) {
        return this.like(slot, property, value, Like.END, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeRight(final Property<T, V> property, final V value) {
        return this.like(this.getSlot(), property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeRight(final Slot slot, final Property<T, V> property, final V value) {
        return this.like(slot, property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeRight(final Property<T, V> property, final V value, final Character escape) {
        return this.like(this.getSlot(), property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeRight(final Slot slot, final Property<T, V> property, final V value, final Character escape) {
        return this.like(slot, property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C likeRight(final String property, final Object value) {
        return this.like(this.getSlot(), property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C likeRight(final Slot slot, final String property, final Object value) {
        return this.like(slot, property, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C likeRight(final String property, final Object value, final Character escape) {
        return this.like(this.getSlot(), property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C likeRight(final Slot slot, final String property, final Object value, final Character escape) {
        return this.like(slot, property, value, Like.START, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeAny(final Property<T, V> property, final V value) {
        return this.like(this.getSlot(), property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeAny(final Slot slot, final Property<T, V> property, final V value) {
        return this.like(slot, property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeAny(final Property<T, V> property, final V value, final Character escape) {
        return this.like(this.getSlot(), property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C likeAny(final Slot slot, final Property<T, V> property, final V value, final Character escape) {
        return this.like(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C likeAny(final String property, final Object value) {
        return this.like(this.getSlot(), property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C likeAny(final Slot slot, final String property, final Object value) {
        return this.like(slot, property, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C likeAny(final String property, final Object value, final Character escape) {
        return this.like(this.getSlot(), property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C likeAny(final Slot slot, final String property, final Object value, final Character escape) {
        return this.like(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C like(final Property<T, V> property, final V value, final Like like) {
        return this.like(this.getSlot(), property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C like(final Slot slot, final Property<T, V> property, final V value, final Like like) {
        return this.like(slot, property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C like(final Property<T, V> property, final V value, final Like like, final Character escape) {
        return this.like(this.getSlot(), property, value, like, escape);
    }

    /**
     * like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C like(final Slot slot, final Property<T, V> property, final V value, final Like like,
               final Character escape);

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@code this}
     */
    default C like(final String property, final Object value, final Like like) {
        return this.like(this.getSlot(), property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@code this}
     */
    default C like(final Slot slot, final String property, final Object value, final Like like) {
        return this.like(slot, property, value, like, null);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @return {@code this}
     */
    default C like(final String property, final Object value, final Like like,
                   final Character escape) {
        return this.like(this.getSlot(), property, value, like, escape);
    }

    /**
     * like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param slot     {@link Slot}
     * @return {@code this}
     */
    C like(final Slot slot, final String property, final Object value, final Like like,
           final Character escape);

    // endregion

    // region Not like condition

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeLeft(final Property<T, V> property, final V value) {
        return this.notLike(this.getSlot(), property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeLeft(final Slot slot, final Property<T, V> property, final V value) {
        return this.notLike(slot, property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeLeft(final Property<T, V> property, final V value, final Character escape) {
        return this.notLike(this.getSlot(), property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeLeft(final Slot slot, final Property<T, V> property, final V value,
                              final Character escape) {
        return this.notLike(slot, property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C notLikeLeft(final String property, final Object value) {
        return this.notLike(this.getSlot(), property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C notLikeLeft(final Slot slot, final String property, final Object value) {
        return this.notLike(slot, property, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLikeLeft(final String property, final Object value, final Character escape) {
        return this.notLike(this.getSlot(), property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLikeLeft(final Slot slot, final String property, final Object value, final Character escape) {
        return this.notLike(slot, property, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeRight(final Property<T, V> property, final V value) {
        return this.notLike(this.getSlot(), property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeRight(final Slot slot, final Property<T, V> property, final V value) {
        return this.notLike(slot, property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeRight(final Property<T, V> property, final V value, final Character escape) {
        return this.notLike(this.getSlot(), property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeRight(final Slot slot, final Property<T, V> property, final V value,
                               final Character escape) {
        return this.notLike(slot, property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C notLikeRight(final String property, final Object value) {
        return this.notLike(this.getSlot(), property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C notLikeRight(final Slot slot, final String property, final Object value) {
        return this.notLike(slot, property, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLikeRight(final String property, final Object value, final Character escape) {
        return this.notLike(this.getSlot(), property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLikeRight(final Slot slot, final String property, final Object value, final Character escape) {
        return this.notLike(slot, property, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeAny(final Property<T, V> property, final V value) {
        return this.notLike(this.getSlot(), property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeAny(final Slot slot, final Property<T, V> property, final V value) {
        return this.notLike(slot, property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeAny(final Property<T, V> property, final V value, final Character escape) {
        return this.notLike(this.getSlot(), property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLikeAny(final Slot slot, final Property<T, V> property, final V value,
                             final Character escape) {
        return this.notLike(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C notLikeAny(final String property, final Object value) {
        return this.notLike(this.getSlot(), property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    default C notLikeAny(final Slot slot, final String property, final Object value) {
        return this.notLike(slot, property, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLikeAny(final String property, final Object value, final Character escape) {
        return this.notLike(this.getSlot(), property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLikeAny(final Slot slot, final String property, final Object value, final Character escape) {
        return this.notLike(slot, property, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLike(final Property<T, V> property, final V value, final Like like) {
        return this.notLike(this.getSlot(), property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLike(final Slot slot, final Property<T, V> property, final V value, final Like like) {
        return this.notLike(slot, property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    default <V> C notLike(final Property<T, V> property, final V value, final Like like,
                          final Character escape) {
        return this.notLike(this.getSlot(), property, value, like, escape);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @param <V>      属性类型
     * @return {@code this}
     */
    <V> C notLike(final Slot slot, final Property<T, V> property, final V value, final Like like,
                  final Character escape);

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@code this}
     */
    default C notLike(final String property, final Object value, final Like like) {
        return this.notLike(this.getSlot(), property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @return {@code this}
     */
    default C notLike(final Slot slot, final String property, final Object value, final Like like) {
        return this.notLike(slot, property, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @return {@code this}
     */
    default C notLike(final String property, final Object value, final Like like, final Character escape) {
        return this.notLike(this.getSlot(), property, value, like, escape);
    }

    /**
     * not like模糊匹配
     * @param slot     {@link Slot}
     * @param property 属性
     * @param value    值
     * @param like     匹配模式
     * @param escape   转义字符
     * @return {@code this}
     */
    C notLike(final Slot slot, final String property, final Object value, final Like like,
              final Character escape);

    // endregion
}
