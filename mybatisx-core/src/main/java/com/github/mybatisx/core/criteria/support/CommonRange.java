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

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.ExtCriteria;
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
interface CommonRange<T, C extends CommonRange<T, C>> extends SlotSymbol<T, C> {

    // region In condition

    /**
     * IN
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colIn(final String column, final Object... values) {
        return colIn(this.getSlot(), column, values);
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
        return colIn(this.getSlot(), column, values);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    C colIn(final Slot slot, final String column, final Collection<?> values);

    /**
     * IN
     * @param column 字段
     * @param query  {@link ExtCriteria}
     * @return {@code this}
     */
    default C colInq(final String column, final ExtCriteria<?> query) {
        return this.colInq(this.getSlot(), column, query);
    }

    /**
     * IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param query  {@link ExtCriteria}
     * @return {@code this}
     */
    C colInq(final Slot slot, final String column, final ExtCriteria<?> query);

    // endregion

    // region Not in condition

    /**
     * NOT IN
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    default C colNotIn(final String column, final Object... values) {
        return colNotIn(this.getSlot(), column, values);
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
        return colNotIn(this.getSlot(), column, values);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param values 多个值
     * @return {@code this}
     */
    C colNotIn(final Slot slot, final String column, final Collection<?> values);

    /**
     * NOT IN
     * @param column 字段
     * @param query  {@link ExtCriteria}
     * @return {@code this}
     */
    default C colNotInq(final String column, final ExtCriteria<?> query) {
        return this.colNotInq(this.getSlot(), column, query);
    }

    /**
     * NOT IN
     * @param slot   {@link Slot}
     * @param column 字段
     * @param query  {@link ExtCriteria}
     * @return {@code this}
     */
    C colNotInq(final Slot slot, final String column, final ExtCriteria<?> query);

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
        return colBetween(this.getSlot(), column, begin, end);
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
        return colNotBetween(this.getSlot(), column, begin, end);
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
