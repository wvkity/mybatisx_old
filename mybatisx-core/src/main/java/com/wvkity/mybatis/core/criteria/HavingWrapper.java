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

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.basic.func.Function;
import com.wvkity.mybatis.core.basic.having.Having;
import com.wvkity.mybatis.core.basic.having.MultiComparator;
import com.wvkity.mybatis.core.basic.having.NativeHaving;
import com.wvkity.mybatis.core.basic.having.SingleComparator;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分组筛选接口
 * @author wvkity
 * @created 2021-04-29
 * @see TemplateWrapper
 * @see com.wvkity.mybatis.core.utils.Placeholders
 * @since 1.0.0
 */
public interface HavingWrapper<T, Chain extends HavingWrapper<T, Chain>> {

    // region Single having methods

    /**
     * 添加{@link Having}
     * @param alias 聚合函数别名
     * @param value 参数值
     * @return {@link Chain}
     */
    default Chain having(final String alias, final Object value) {
        return this.having(Slot.AND, alias, value);
    }

    /**
     * 添加{@link Having}
     * @param slot  {@link Slot}
     * @param alias 聚合函数别名
     * @param value 参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final String alias, final Object value);

    /**
     * 添加{@link Having}
     * @param function {@link Function}
     * @param value    参数值
     * @return {@link Chain}
     */
    default Chain having(final Function function, final Object value) {
        return this.having(Slot.AND, function, value);
    }

    /**
     * 添加{@link Having}
     * @param slot     {@link Slot}
     * @param function {@link Function}
     * @param value    参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final Function function, final Object value);

    /**
     * 添加{@link Having}
     * @param alias      聚合函数别名
     * @param comparator {@link SingleComparator}
     * @param value      参数值
     * @return {@link Chain}
     */
    default Chain having(final String alias, final SingleComparator comparator, final Object value) {
        return this.having(Slot.AND, alias, comparator, value);
    }

    /**
     * 添加{@link Having}
     * @param slot       {@link Slot}
     * @param alias      聚合函数别名
     * @param comparator {@link SingleComparator}
     * @param value      参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final String alias, final SingleComparator comparator, final Object value);

    /**
     * 添加{@link Having}
     * @param function   {@link Function}
     * @param comparator {@link SingleComparator}
     * @param value      参数值
     * @return {@link Chain}
     */
    default Chain having(final Function function, final SingleComparator comparator, final Object value) {
        return this.having(Slot.AND, function, comparator, value);
    }

    /**
     * 添加{@link Having}
     * @param slot       {@link Slot}
     * @param function   {@link Function}
     * @param comparator {@link SingleComparator}
     * @param value      参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final Function function, final SingleComparator comparator, final Object value);

    // endregion

    // region Multi having methods

    /**
     * 添加{@link Having}
     * @param alias 聚合函数别名
     * @param first 参数值
     * @param last  参数值
     * @return {@link Chain}
     */
    default Chain having(final String alias, final Object first, final Object last) {
        return this.having(Slot.AND, alias, first, last);
    }

    /**
     * 添加{@link Having}
     * @param slot  {@link Slot}
     * @param alias 聚合函数别名
     * @param first 参数值
     * @param last  参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final String alias, final Object first, final Object last);

    /**
     * 添加{@link Having}
     * @param function {@link Function}
     * @param first    参数值
     * @param last     参数值
     * @return {@link Chain}
     */
    default Chain having(final Function function, final Object first, final Object last) {
        return this.having(Slot.AND, function, first, last);
    }

    /**
     * 添加{@link Having}
     * @param slot     {@link Slot}
     * @param function {@link Function}
     * @param first    参数值
     * @param last     参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final Function function, final Object first, final Object last);

    /**
     * 添加{@link Having}
     * @param alias      聚合函数别名
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@link Chain}
     */
    default Chain having(final String alias, final MultiComparator comparator,
                         final Object first, final Object last) {
        return this.having(Slot.AND, alias, comparator, first, last);
    }

    /**
     * 添加{@link Having}
     * @param slot       {@link Slot}
     * @param alias      聚合函数别名
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final String alias, final MultiComparator comparator,
                 final Object first, final Object last);

    /**
     * 添加{@link Having}
     * @param function   {@link Function}
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@link Chain}
     */
    default Chain having(final Function function, final MultiComparator comparator,
                         final Object first, final Object last) {
        return this.having(Slot.AND, function, comparator, first, last);
    }

    /**
     * 添加{@link Having}
     * @param slot       {@link Slot}
     * @param function   {@link Function}
     * @param comparator {@link MultiComparator}
     * @param first      参数值
     * @param last       参数值
     * @return {@link Chain}
     */
    Chain having(final Slot slot, final Function function, final MultiComparator comparator,
                 final Object first, final Object last);

    // endregion

    // region Native having methods

    /**
     * 添加{@link Having}
     * <pre>
     *     <ul>
     *         <li>建议使用下列方法代替本方法</li>
     *         <li>{@link #nativeHaving(String, Object)}</li>
     *         <li>{@link #nativeHaving(String, Object...)}</li>
     *         <li>{@link #nativeHaving(String, List)}</li>
     *         <li>{@link #nativeHaving(String, Map)}</li>
     *     </ul>
     * </pre>
     * @param having having子句
     * @return {@link Chain}
     */
    default Chain nativeHaving(final String having) {
        return this.having(new NativeHaving(having));
    }

    /**
     * 添加{@link Having}
     * @param func  聚合函数语句
     * @param value 参数值
     * @return {@link Chain}
     */
    Chain nativeHaving(final String func, final Object value);

    /**
     * 添加{@link Having}
     * @param func   聚合函数语句
     * @param values 参数值列表
     * @return {@link Chain}
     */
    default Chain nativeHaving(final String func, final Object... values) {
        return this.nativeHaving(func, Objects.asList(values));
    }

    /**
     * 添加{@link Having}
     * @param func   聚合函数语句
     * @param values 参数值列表
     * @return {@link Chain}
     */
    Chain nativeHaving(final String func, final List<Object> values);

    /**
     * 添加{@link Having}
     * @param func   聚合函数语句
     * @param values 参数值列表
     * @return {@link Chain}
     */
    Chain nativeHaving(final String func, final Map<String, Object> values);

    // endregion

    /**
     * 添加{@link Having}
     * @param having {@link Having}
     * @return {@link Chain}
     */
    Chain having(final Having having);

    /**
     * 添加多个{@link Having}
     * @param havingArray {@link Having}列表
     * @return {@link Chain}
     */
    default Chain having(final Having... havingArray) {
        return this.having(Objects.asList(havingArray));
    }

    /**
     * 添加多个{@link Having}
     * @param havingList {@link Having}列表
     * @return {@link Chain}
     */
    Chain having(final Collection<Having> havingList);

}
