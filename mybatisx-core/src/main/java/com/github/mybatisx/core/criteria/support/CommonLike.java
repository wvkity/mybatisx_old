package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.support.constant.Like;
import com.github.mybatisx.support.constant.Slot;

/**
 * like模糊匹配条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface CommonLike<T, C extends CommonLike<T, C>> {

    // region Like condition

    // region Like left (%arg)

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLikeLeft(final String column, final Object value) {
        return this.colLike(Slot.AND, column, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLikeLeft(final Slot slot, final String column, final Object value) {
        return this.colLike(slot, column, value, Like.END, null);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colLikeLeft(final String column, final Object value, final Character escape) {
        return this.colLike(Slot.AND, column, value, Like.END, escape);
    }

    /**
     * like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @return {@code this}
     */
    default C colLikeLeft(final Slot slot, final String column, final Object value, final Character escape) {
        return this.colLike(slot, column, value, Like.END, escape);
    }

    // endregion

    // region Like right (arg%)

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLikeRight(final String column, final Object value) {
        return this.colLike(Slot.AND, column, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLikeRight(final Slot slot, final String column, final Object value) {
        return this.colLike(slot, column, value, Like.START, null);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colLikeRight(final String column, final Object value, final Character escape) {
        return this.colLike(Slot.AND, column, value, Like.START, escape);
    }

    /**
     * like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @param slot   {@link Slot}
     * @return {@code this}
     */
    default C colLikeRight(final Slot slot, final String column, final Object value, final Character escape) {
        return this.colLike(slot, column, value, Like.START, escape);
    }

    // endregion

    // region Like anywhere (%arg%)

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLikeAny(final String column, final Object value) {
        return this.colLike(Slot.AND, column, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colLikeAny(final Slot slot, final String column, final Object value) {
        return this.colLike(slot, column, value, Like.ANYWHERE, null);
    }

    /**
     * like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colLikeAny(final String column, final Object value, final Character escape) {
        return this.colLike(Slot.AND, column, value, Like.ANYWHERE, escape);
    }

    /**
     * like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colLikeAny(final Slot slot, final String column, final Object value, final Character escape) {
        return this.colLike(slot, column, value, Like.ANYWHERE, escape);
    }

    // endregion

    // region Basic like

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@code this}
     */
    default C colLike(final String column, final Object value, final Like like) {
        return this.colLike(Slot.AND, column, value, like, null);
    }

    /**
     * like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@code this}
     */
    default C colLike(final Slot slot, final String column, final Object value, final Like like) {
        return this.colLike(slot, column, value, like, null);
    }

    /**
     * like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colLike(final String column, final Object value, final Like like,
                      final Character escape) {
        return this.colLike(Slot.AND, column, value, like, escape);
    }

    /**
     * like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@code this}
     */
    C colLike(final Slot slot, final String column, final Object value, final Like like,
              final Character escape);

    // endregion

    // endregion

    // region Not like condition

    // region Not like left (%arg)

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNotLikeLeft(final String column, final Object value) {
        return this.colNotLike(Slot.AND, column, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNotLikeLeft(final Slot slot, final String column, final Object value) {
        return this.colNotLike(slot, column, value, Like.END, null);
    }

    /**
     * not like模糊匹配(%arg)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLikeLeft(final String column, final Object value, final Character escape) {
        return this.colNotLike(Slot.AND, column, value, Like.END, escape);
    }

    /**
     * not like模糊匹配(%arg)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLikeLeft(final Slot slot, final String column, final Object value, final Character escape) {
        return this.colNotLike(slot, column, value, Like.END, escape);
    }

    // endregion

    // region Not like right (arg%)

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNotLikeRight(final String column, final Object value) {
        return this.colNotLike(Slot.AND, column, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNotLikeRight(final Slot slot, final String column, final Object value) {
        return this.colNotLike(slot, column, value, Like.START, null);
    }

    /**
     * not like模糊匹配(arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLikeRight(final String column, final Object value, final Character escape) {
        return this.colNotLike(Slot.AND, column, value, Like.START, escape);
    }

    /**
     * not like模糊匹配(arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLikeRight(final Slot slot, final String column, final Object value, final Character escape) {
        return this.colNotLike(slot, column, value, Like.START, escape);
    }

    // endregion

    // region Not like anywhere (%arg%)

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNotLikeAny(final String column, final Object value) {
        return this.colNotLike(Slot.AND, column, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    default C colNotLikeAny(final Slot slot, final String column, final Object value) {
        return this.colNotLike(slot, column, value, Like.ANYWHERE, null);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLikeAny(final String column, final Object value, final Character escape) {
        return this.colNotLike(Slot.AND, column, value, Like.ANYWHERE, escape);
    }

    /**
     * not like模糊匹配(%arg%)
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLikeAny(final Slot slot, final String column, final Object value, final Character escape) {
        return this.colNotLike(slot, column, value, Like.ANYWHERE, escape);
    }

    // endregion

    // region Basic not like

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@code this}
     */
    default C colNotLike(final String column, final Object value, final Like like) {
        return this.colNotLike(Slot.AND, column, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @return {@code this}
     */
    default C colNotLike(final Slot slot, final String column, final Object value, final Like like) {
        return this.colNotLike(slot, column, value, like, null);
    }

    /**
     * not like模糊匹配
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@code this}
     */
    default C colNotLike(final String column, final Object value, final Like like, final Character escape) {
        return this.colNotLike(Slot.AND, column, value, like, escape);
    }

    /**
     * not like模糊匹配
     * @param slot   {@link Slot}
     * @param column 字段
     * @param value  值
     * @param like   匹配模式
     * @param escape 转义字符
     * @return {@code this}
     */
    C colNotLike(final Slot slot, final String column, final Object value, final Like like,
                 final Character escape);

    // endregion

    // endregion

}
