package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.support.constant.Slot;

import java.util.Collection;

/**
 * 范围条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface CommonRange<T, C extends CommonRange<T, C>> {

    // region In condition

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colIn(final String column, final Object... values) {
        return colIn(Slot.AND, column, values);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colIn(final Slot slot, final String column, final Object... values) {
        return colIn(slot, column, Objects.asList(values));
    }

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colIn(final String column, final Collection<?> values) {
        return colIn(Slot.AND, column, values);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    C colIn(final Slot slot, final String column, final Collection<?> values);

    // endregion

    // region Not in condition

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colNotIn(final String column, final Object... values) {
        return colNotIn(Slot.AND, column, values);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colNotIn(final Slot slot, final String column, final Object... values) {
        return colNotIn(column, Objects.asList(values), slot);
    }

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colNotIn(final String column, final Collection<?> values) {
        return colNotIn(Slot.AND, column, values);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    C colNotIn(final Slot slot, final String column, final Collection<?> values);

    // endregion

    // region Between

    /**
     * Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@code this}
     */
    default C colBetween(final String column, final Object begin, final Object end) {
        return colBetween(Slot.AND, column, begin, end);
    }

    /**
     * Between
     * @param slot   {@link Slot}
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@code this}
     */
    C colBetween(final Slot slot, final String column, final Object begin, final Object end);

    // endregion

    // region Not between

    /**
     * Not Between
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@code this}
     */
    default C colNotBetween(final String column, final Object begin, final Object end) {
        return colNotBetween(Slot.AND, column, begin, end);
    }

    /**
     * Not Between
     * @param slot   {@link Slot}
     * @param column 字段
     * @param begin  开始值
     * @param end    结束值
     * @return {@code this}
     */
    C colNotBetween(final Slot slot, final String column, final Object begin, final Object end);

    // endregion
}
