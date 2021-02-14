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
package com.wvkity.mybatis.core.condition.basic.order;

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.List;

/**
 * 排序
 * @author wvkity
 * @created 2021-01-13
 * @since 1.0.0
 */
public class StandardOrder extends AbstractOrder<String> {

    private static final long serialVersionUID = 1158993136517541109L;

    private StandardOrder(boolean ascending, final List<String> columns) {
        this.ascending = ascending;
        this.addAll(columns);
    }

    private StandardOrder(String alias, boolean ascending, final List<String> columns) {
        this.ascending = ascending;
        this.tableAlias = alias;
        this.addAll(columns);
    }

    private StandardOrder(Criteria<?> criteria, boolean ascending, final List<String> columns) {
        this.criteria = criteria;
        this.ascending = ascending;
        this.addAll(columns);
    }

    @Override
    public String toString(String target) {
        return target;
    }

    public StandardOrder alias(final String alias) {
        this.tableAlias = alias;
        return this;
    }

    // region Static methods

    // region Asc sort methods

    /**
     * 升序
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder asc(final String... columns) {
        return asc(Objects.asList(columns));
    }

    /**
     * 升序
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder asc(final List<String> columns) {
        return sort(true, columns);
    }

    /**
     * 升序
     * @param criteria {@link Criteria}
     * @param columns  字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder asc(final Criteria<?> criteria, final String... columns) {
        return asc(criteria, Objects.asList(columns));
    }

    /**
     * 升序
     * @param criteria {@link Criteria}
     * @param columns  字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder asc(final Criteria<?> criteria, final List<String> columns) {
        return sort(criteria, true, columns);
    }

    /**
     * 升序
     * @param alias   表别名
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder ascWithAlias(final String alias, final String... columns) {
        return ascWithAlias(alias, Objects.asList(columns));
    }

    /**
     * 升序
     * @param alias   表别名
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder ascWithAlias(final String alias, final List<String> columns) {
        return sort(alias, true, columns);
    }

    // endregion


    // region Desc sort methods

    /**
     * 降序
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder desc(final String... columns) {
        return desc(Objects.asList(columns));
    }

    /**
     * 降序
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder desc(final List<String> columns) {
        return sort(false, columns);
    }

    /**
     * 降序
     * @param criteria {@link Criteria}
     * @param columns  字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder desc(final Criteria<?> criteria, final String... columns) {
        return desc(criteria, Objects.asList(columns));
    }

    /**
     * 降序
     * @param criteria {@link Criteria}
     * @param columns  字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder desc(final Criteria<?> criteria, final List<String> columns) {
        return sort(criteria, false, columns);
    }

    /**
     * 降序
     * @param alias   表别名
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder descWithAlias(final String alias, final String... columns) {
        return descWithAlias(alias, Objects.asList(columns));
    }

    /**
     * 降序
     * @param alias   表别名
     * @param columns 字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder descWithAlias(final String alias, final List<String> columns) {
        return sort(alias, false, columns);
    }

    // endregion

    // region Sort methods

    /**
     * 排序
     * @param ascending 是否升序
     * @param columns   字段列表
     * @return {@link StandardOrder}
     */
    public static StandardOrder sort(final boolean ascending, final String... columns) {
        return sort(ascending, Objects.asList(columns));
    }

    /**
     * 排序
     * @param ascending 是否升序
     * @param columns   字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder sort(final boolean ascending, final List<String> columns) {
        if (Objects.isEmpty(columns)) {
            return null;
        }
        return new StandardOrder(ascending, columns);
    }

    /**
     * 排序
     * @param criteria  {@link Criteria}
     * @param ascending 是否升序
     * @param columns   字段列表
     * @return {@link StandardOrder}
     */
    public static StandardOrder sort(final Criteria<?> criteria, final boolean ascending, final String... columns) {
        return sort(criteria, ascending, Objects.asList(columns));
    }

    /**
     * 排序
     * @param criteria  {@link Criteria}
     * @param ascending 是否升序
     * @param columns   字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder sort(final Criteria<?> criteria, final boolean ascending, final List<String> columns) {
        if (Objects.isEmpty(columns)) {
            return null;
        }
        return new StandardOrder(criteria, ascending, columns);
    }

    /**
     * 排序
     * @param alias     表别名
     * @param ascending 是否升序
     * @param columns   字段列表
     * @return {@link StandardOrder}
     */
    public static StandardOrder sort(final String alias, final boolean ascending, final String... columns) {
        return sort(alias, ascending, Objects.asList(columns));
    }

    /**
     * 排序
     * @param alias     表别名
     * @param ascending 是否升序
     * @param columns   字段集合
     * @return {@link StandardOrder}
     */
    public static StandardOrder sort(final String alias, final boolean ascending, final List<String> columns) {
        if (Objects.isEmpty(columns)) {
            return null;
        }
        return new StandardOrder(alias, ascending, columns);
    }

    // endregion

    // endregion
}
