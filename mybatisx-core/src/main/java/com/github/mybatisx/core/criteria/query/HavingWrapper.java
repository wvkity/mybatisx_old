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
package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.Objects;
import com.github.mybatisx.core.criteria.support.SlotSymbol;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.core.support.having.Having;
import com.github.mybatisx.core.support.having.MultiComparator;
import com.github.mybatisx.core.support.having.SingleComparator;
import com.github.mybatisx.support.constant.Slot;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分组筛选
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-10
 * @since 1.0.0
 */
interface HavingWrapper<T, C extends HavingWrapper<T, C>> extends SlotSymbol<T, C> {

    // region Single having methods

    /**
     * 分组筛选
     * @param funcAlias 聚合函数别名
     * @param value     值
     * @return {@code this}
     */
    default C having(final String funcAlias, final Object value) {
        return this.having(this.getSlot(), funcAlias, value);
    }

    /**
     * 分组筛选
     * @param slot      {@link Slot}
     * @param funcAlias 聚合函数别名
     * @param value     值
     * @return {@code this}
     */
    default C having(final Slot slot, final String funcAlias, final Object value) {
        return this.having(slot, funcAlias, SingleComparator.EQ, value);
    }

    /**
     * 分组筛选
     * @param function {@link Function}
     * @param value    值
     * @return {@code this}
     */
    default C having(final Function function, final Object value) {
        return this.having(this.getSlot(), function, SingleComparator.EQ, value);
    }

    /**
     * 分组筛选
     * @param slot     {@link Slot}
     * @param function {@link Function}
     * @param value    值
     * @return {@code this}
     */
    default C having(final Slot slot, final Function function, final Object value) {
        return this.having(slot, function, SingleComparator.EQ, value);
    }

    /**
     * 分组筛选
     * @param funcAlias  聚合函数别名
     * @param comparator {@link SingleComparator}
     * @param value      值
     * @return {@code this}
     */
    default C having(final String funcAlias, final SingleComparator comparator, final Object value) {
        return this.having(this.getSlot(), funcAlias, comparator, value);
    }

    /**
     * 分组筛选
     * @param slot       {@link Slot}
     * @param funcAlias  聚合函数别名
     * @param comparator {@link SingleComparator}
     * @param value      值
     * @return {@code this}
     */
    C having(final Slot slot, final String funcAlias, final SingleComparator comparator, final Object value);

    /**
     * 分组筛选
     * @param function   {@link Function}
     * @param comparator {@link SingleComparator}
     * @param value      值
     * @return {@code this}
     */
    default C having(final Function function, final SingleComparator comparator, final Object value) {
        return this.having(this.getSlot(), function, comparator, value);
    }

    /**
     * 分组筛选
     * @param slot       {@link Slot}
     * @param function   {@link Function}
     * @param comparator {@link SingleComparator}
     * @param value      值
     * @return {@code this}
     */
    C having(final Slot slot, final Function function, final SingleComparator comparator, final Object value);

    // endregion

    // region Multi having methods

    /**
     * 分组筛选
     * @param funcAlias 聚合函数别名
     * @param first     参数值
     * @param last      参数值
     * @return {@code this}
     */
    default C having(final String funcAlias, final Object first, final Object last) {
        return this.having(this.getSlot(), funcAlias, first, last);
    }

    /**
     * 分组筛选
     * @param slot      {@link Slot}
     * @param funcAlias 聚合函数别名
     * @param first     参数值
     * @param last      参数值
     * @return {@code this}
     */
    default C having(final Slot slot, final String funcAlias, final Object first, final Object last) {
        return this.having(slot, funcAlias, MultiComparator.GE_AND_LE, first, last);
    }

    /**
     * 分组筛选
     * @param function {@link Function}
     * @param first    参数值
     * @param last     参数值
     * @return {@code this}
     */
    default C having(final Function function, final Object first, final Object last) {
        return this.having(this.getSlot(), function, first, last);
    }

    /**
     * 分组筛选
     * @param slot     {@link Slot}
     * @param function {@link Function}
     * @param first    参数值
     * @param last     参数值
     * @return {@code this}
     */
    default C having(final Slot slot, final Function function, final Object first, final Object last) {
        return this.having(slot, function, MultiComparator.GE_AND_LE, first, last);
    }

    /**
     * 分组筛选
     * @param funcAlias  聚合函数别名
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@code this}
     */
    default C having(final String funcAlias, final MultiComparator comparator,
                     final Object first, final Object last) {
        return this.having(this.getSlot(), funcAlias, comparator, first, last);
    }

    /**
     * 分组筛选
     * @param slot       {@link Slot}
     * @param funcAlias  聚合函数别名
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@code this}
     */
    C having(final Slot slot, final String funcAlias, final MultiComparator comparator,
             final Object first, final Object last);

    /**
     * 分组筛选
     * @param function   {@link Function}
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@code this}
     */
    default C having(final Function function, final MultiComparator comparator,
                     final Object first, final Object last) {
        return this.having(this.getSlot(), function, comparator, first, last);
    }

    /**
     * 分组筛选
     * @param slot       {@link Slot}
     * @param function   {@link Function}
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@code this}
     */
    C having(final Slot slot, final Function function, final MultiComparator comparator,
             final Object first, final Object last);

    // endregion

    // region Native having methods

    /**
     * 分组筛选
     * @param havingBody 分组语句
     * @return {@code this}
     */
    C nativeHaving(final String havingBody);

    /**
     * 分组筛选
     * @param havingBody 分组语句
     * @param value      参数
     * @return {@code this}
     */
    C nativeHaving(final String havingBody, final Object value);

    /**
     * 分组筛选
     * @param havingBody 分组语句
     * @param args       参数列表
     * @return {@code this}
     */
    default C nativeHaving(final String havingBody, final Object... args) {
        return this.nativeHaving(havingBody, Objects.asList(args));
    }

    /**
     * 分组筛选
     * @param havingBody 分组语句
     * @param args       参数列表
     * @return {@code this}
     */
    C nativeHaving(final String havingBody, final List<Object> args);

    /**
     * 分组筛选
     * @param havingBody 分组语句
     * @param args       参数列表
     * @return {@code this}
     */
    C nativeHaving(final String havingBody, final Map<String, Object> args);

    // endregion

    /**
     * 分组筛选
     * @param having {@link Having}
     * @return {@code this}
     */
    C having(final Having having);

    /**
     * 分组筛选
     * @param havingList {@link Having}列表
     * @return {@code this}
     */
    C having(final Collection<Having> havingList);

}
