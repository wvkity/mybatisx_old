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
import com.github.mybatisx.core.criteria.query.GenericQuery;
import com.github.mybatisx.core.criteria.query.LambdaQuery;
import com.github.mybatisx.core.criteria.query.Query;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 创查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface QueryCriteriaBuilder<T, C extends QueryCriteriaBuilder<T, C>> extends ExtCriteria<T> {

    // region Common sub query

    /**
     * 创建{@link Query}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link Query}
     */
    default <S> Query<S> newQuery(final Class<S> entity) {
        return this.newQuery(entity, (String) null);
    }

    /**
     * 创建{@link Query}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link Query}
     */
    default <S> Query<S> newQuery(final Class<S> entity, final String alias) {
        return Query.from(this, entity, alias);
    }

    /**
     * 创建{@link Query}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Query}
     */
    default <S> Query<S> newQuery(final Class<S> entity, final Consumer<Query<S>> action) {
        return this.newQuery(entity, null, action);
    }

    /**
     * 创建{@link Query}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Query}
     */
    default <S> Query<S> newQuery(final Class<S> entity, final String alias,
                                  final Consumer<Query<S>> action) {
        return Query.from(this, entity, alias, action);
    }

    /**
     * 创建{@link Query}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Query}
     */
    default <S> Query<S> newQuery(final Class<S> entity, final BiConsumer<C, Query<S>> action) {
        return this.newQuery(entity, null, action);
    }

    /**
     * 创建{@link Query}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Query}
     */
    @SuppressWarnings("unchecked")
    default <S> Query<S> newQuery(final Class<S> entity, final String alias,
                                  final BiConsumer<C, Query<S>> action) {
        final Query<S> it = this.newQuery(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

    // region Lambda sub query

    /**
     * 创建{@link LambdaQuery}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link LambdaQuery}
     */
    default <S> LambdaQuery<S> newLambdaQuery(final Class<S> entity) {
        return this.newLambdaQuery(entity, (String) null);
    }

    /**
     * 创建{@link LambdaQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link LambdaQuery}
     */
    default <S> LambdaQuery<S> newLambdaQuery(final Class<S> entity, final String alias) {
        return LambdaQuery.from(this, entity, alias);
    }

    /**
     * 创建{@link LambdaQuery}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaQuery}
     */
    default <S> LambdaQuery<S> newLambdaQuery(final Class<S> entity,
                                              final Consumer<LambdaQuery<S>> action) {
        return this.newLambdaQuery(entity, null, action);
    }

    /**
     * 创建{@link LambdaQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaQuery}
     */
    default <S> LambdaQuery<S> newLambdaQuery(final Class<S> entity, final String alias,
                                              final Consumer<LambdaQuery<S>> action) {
        return LambdaQuery.from(this, entity, alias, action);
    }

    /**
     * 创建{@link LambdaQuery}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaQuery}
     */
    default <S> LambdaQuery<S> newLambdaQuery(final Class<S> entity,
                                              final BiConsumer<C, LambdaQuery<S>> action) {
        return this.newLambdaQuery(entity, null, action);
    }

    /**
     * 创建{@link LambdaQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> LambdaQuery<S> newLambdaQuery(final Class<S> entity, final String alias,
                                              final BiConsumer<C, LambdaQuery<S>> action) {
        final LambdaQuery<S> it = this.newLambdaQuery(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

    // region Generic sub query

    /**
     * 创建{@link GenericQuery}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link GenericQuery}
     */
    default <S> GenericQuery<S> newGenericQuery(final Class<S> entity) {
        return this.newGenericQuery(entity, (String) null);
    }

    /**
     * 创建{@link GenericQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link GenericQuery}
     */
    default <S> GenericQuery<S> newGenericQuery(final Class<S> entity, final String alias) {
        return GenericQuery.from(this, entity, alias);
    }

    /**
     * 创建{@link GenericQuery}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericQuery}
     */
    default <S> GenericQuery<S> newGenericQuery(final Class<S> entity,
                                                final Consumer<GenericQuery<S>> action) {
        return this.newGenericQuery(entity, null, action);
    }

    /**
     * 创建{@link GenericQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericQuery}
     */
    default <S> GenericQuery<S> newGenericQuery(final Class<S> entity, final String alias,
                                                final Consumer<GenericQuery<S>> action) {
        return GenericQuery.from(this, entity, alias, action);
    }

    /**
     * 创建{@link GenericQuery}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericQuery}
     */
    default <S> GenericQuery<S> newGenericQuery(final Class<S> entity,
                                                final BiConsumer<C, GenericQuery<S>> action) {
        return this.newGenericQuery(entity, null, action);
    }

    /**
     * 创建{@link GenericQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> GenericQuery<S> newGenericQuery(final Class<S> entity, final String alias,
                                                final BiConsumer<C, GenericQuery<S>> action) {
        final GenericQuery<S> it = this.newGenericQuery(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

}
