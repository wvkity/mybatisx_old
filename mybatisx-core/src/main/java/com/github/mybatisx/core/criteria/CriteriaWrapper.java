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
package com.github.mybatisx.core.criteria;

import com.github.mybatisx.Objects;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.support.expr.Expression;

import java.util.List;
import java.util.function.Function;

/**
 * 条件包装容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface CriteriaWrapper<T, C extends CriteriaWrapper<T, C>> extends ExtCriteria<T> {

    // region Nesting condition

    /**
     * {@inheritDoc}
     */
    @Override
    C setVersion(final Object value);

    /**
     * and嵌套条件
     * @param expressions 条件列表
     * @return {@code this}
     */
    default C and(final Expression<?>... expressions) {
        return and(false, expressions);
    }

    /**
     * and嵌套条件
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C and(final List<Expression<?>> expressions) {
        return and(false, expressions);
    }

    /**
     * and嵌套条件
     * @param expressions 条件列表
     * @param not         是否添加NOT
     * @return {@code this}
     */
    default C and(final boolean not, final Expression<?>... expressions) {
        return and(not, Objects.asList(expressions));
    }

    /**
     * and嵌套条件
     * @param not         是否添加NOT
     * @param expressions 条件集合
     * @return {@code this}
     */
    C and(final boolean not, final List<Expression<?>> expressions);

    /**
     * and嵌套条件
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C and(final Criteria<?> criteria, final Expression<?>... expressions) {
        return and(false, criteria, expressions);
    }

    /**
     * and嵌套条件
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C and(final Criteria<?> criteria, final List<Expression<?>> expressions) {
        return and(false, criteria, expressions);
    }

    /**
     * and嵌套条件
     * @param not         是否添加NOT
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C and(final boolean not, final Criteria<?> criteria, final Expression<?>... expressions) {
        return and(not, criteria, Objects.asList(expressions));
    }

    /**
     * and嵌套条件
     * @param not         是否添加NOT
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    C and(final boolean not, final Criteria<?> criteria, final List<Expression<?>> expressions);

    /**
     * and嵌套条件
     * @param apply {@link Function}
     * @return {@code this}
     */
    default C and(final Function<C, C> apply) {
        return and(false, apply);
    }

    /**
     * and嵌套条件
     * @param not   是否添加not
     * @param apply {@link Function}
     * @return {@code this}
     */
    C and(final boolean not, final Function<C, C> apply);

    /**
     * or嵌套条件
     * @param expressions 条件列表
     * @return {@code this}
     */
    default C or(final Expression<?>... expressions) {
        return or(false, expressions);
    }

    /**
     * or嵌套条件
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C or(final List<Expression<?>> expressions) {
        return or(false, expressions);
    }

    /**
     * or嵌套条件
     * @param expressions 条件列表
     * @param not         是否添加NOT
     * @return {@code this}
     */
    default C or(final boolean not, final Expression<?>... expressions) {
        return or(not, Objects.asList(expressions));
    }

    /**
     * or嵌套条件
     * @param not         是否添加NOT
     * @param expressions 条件集合
     * @return {@code this}
     */
    C or(final boolean not, final List<Expression<?>> expressions);

    /**
     * or嵌套条件
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C or(final Criteria<?> criteria, final Expression<?>... expressions) {
        return or(false, criteria, expressions);
    }

    /**
     * or嵌套条件
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C or(final Criteria<?> criteria, final List<Expression<?>> expressions) {
        return or(false, criteria, expressions);
    }

    /**
     * or嵌套条件
     * @param not         是否添加NOT
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    default C or(final boolean not, final Criteria<?> criteria, final Expression<?>... expressions) {
        return or(not, criteria, Objects.asList(expressions));
    }

    /**
     * or嵌套条件
     * @param not         是否添加NOT
     * @param criteria    {@link Criteria}
     * @param expressions 条件集合
     * @return {@code this}
     */
    C or(final boolean not, final Criteria<?> criteria, final List<Expression<?>> expressions);

    /**
     * or嵌套条件
     * @param apply {@link Function}
     * @return {@code this}
     */
    default C or(final Function<C, C> apply) {
        return or(false, apply);
    }

    /**
     * or嵌套条件
     * @param not   是否添加not
     * @param apply {@link Function}
     * @return {@code this}
     */
    C or(final boolean not, final Function<C, C> apply);

    // endregion
}
