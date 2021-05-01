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
import com.wvkity.mybatis.support.constant.Join;
import com.wvkity.mybatis.support.expr.Expression;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 子外联表条件包装器
 * @param <M>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-04-18
 * @since 1.0.0
 */
public interface SubForeignWrapper<M, Chain extends SubForeignWrapper<M, Chain>> {

    // region Inner join

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery {@link AbstractSubCriteria}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery) {
        return this.join(subQuery, Join.INNER);
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery {@link AbstractSubCriteria}
     * @param alias    别名
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final String alias) {
        return this.join(subQuery, Join.INNER, alias);
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery {@link AbstractSubCriteria}
     * @param action   {@link Consumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery,
                                      final Consumer<SubForeign<M, S>> action) {
        return this.join(subQuery, Join.INNER, null, action);
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery {@link AbstractSubCriteria}
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery,
                                      final String alias, final Consumer<SubForeign<M, S>> action) {
        return this.join(subQuery, Join.INNER, alias, action);
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery {@link AbstractSubCriteria}
     * @param action   {@link BiConsumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery,
                                      final BiConsumer<AbstractCriteria<M>, SubForeign<M, S>> action) {
        return this.join(subQuery, Join.INNER, null, action);
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery {@link AbstractSubCriteria}
     * @param alias    别名
     * @param action   {@link BiConsumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final String alias,
                                      final BiConsumer<AbstractCriteria<M>, SubForeign<M, S>> action) {
        return this.join(subQuery, Join.INNER, alias, action);
    }


    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery,
                                      final Expression... expressions) {
        return this.join(subQuery, Objects.asList(expressions));
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery,
                                      final Collection<Expression> expressions) {
        return this.join(subQuery, Join.INNER, expressions);
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final String alias,
                                      final Expression... expressions) {
        return this.join(subQuery, alias, Objects.asList(expressions));
    }

    /**
     * 创建子外联表条件({@link Join#INNER})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final String alias,
                                      final Collection<Expression> expressions) {
        return this.join(subQuery, Join.INNER, alias, expressions);
    }

    // endregion

    // region Left join

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery {@link AbstractSubCriteria}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery) {
        return this.join(subQuery, Join.LEFT);
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery {@link AbstractSubCriteria}
     * @param alias    别名
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery, final String alias) {
        return this.join(subQuery, Join.LEFT, alias);
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery {@link AbstractSubCriteria}
     * @param action   {@link Consumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery,
                                          final Consumer<SubForeign<M, S>> action) {
        return this.join(subQuery, Join.LEFT, null, action);
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery {@link AbstractSubCriteria}
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery,
                                          final String alias, final Consumer<SubForeign<M, S>> action) {
        return this.join(subQuery, Join.LEFT, alias, action);
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery {@link AbstractSubCriteria}
     * @param action   {@link BiConsumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery,
                                          final BiConsumer<AbstractCriteria<M>, SubForeign<M, S>> action) {
        return this.join(subQuery, Join.LEFT, null, action);
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery {@link AbstractSubCriteria}
     * @param alias    别名
     * @param action   {@link BiConsumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery, final String alias,
                                          final BiConsumer<AbstractCriteria<M>, SubForeign<M, S>> action) {
        return this.join(subQuery, Join.LEFT, alias, action);
    }


    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery,
                                          final Expression... expressions) {
        return this.leftJoin(subQuery, Objects.asList(expressions));
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery,
                                          final Collection<Expression> expressions) {
        return this.join(subQuery, Join.LEFT, expressions);
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery, final String alias,
                                          final Expression... expressions) {
        return this.leftJoin(subQuery, alias, Objects.asList(expressions));
    }

    /**
     * 创建子外联表条件({@link Join#LEFT})
     * @param subQuery    {@link AbstractSubCriteria}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> leftJoin(final AbstractSubCriteria<S> subQuery, final String alias,
                                          final Collection<Expression> expressions) {
        return this.join(subQuery, Join.LEFT, alias, expressions);
    }

    // endregion

    /**
     * 创建子外联表条件
     * @param subQuery {@link AbstractSubCriteria}
     * @param join     {@link Join}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join) {
        return this.join(subQuery, join, (String) null);
    }

    /**
     * 创建子外联表条件
     * @param subQuery {@link AbstractSubCriteria}
     * @param join     {@link Join}
     * @param alias    别名
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join, final String alias);

    /**
     * 创建子外联表条件
     * @param subQuery {@link AbstractSubCriteria}
     * @param join     {@link Join}
     * @param action   {@link Consumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final Consumer<SubForeign<M, S>> action) {
        return this.join(subQuery, join, null, action);
    }

    /**
     * 创建子外联表条件
     * @param subQuery {@link AbstractSubCriteria}
     * @param join     {@link Join}
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final String alias, final Consumer<SubForeign<M, S>> action) {
        final SubForeign<M, S> instance = this.join(subQuery, join, alias);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 创建子外联表条件
     * @param subQuery {@link AbstractSubCriteria}
     * @param join     {@link Join}
     * @param action   {@link BiConsumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final BiConsumer<AbstractCriteria<M>, SubForeign<M, S>> action) {
        return this.join(subQuery, join, null, action);
    }

    /**
     * 创建子外联表条件
     * @param subQuery {@link AbstractSubCriteria}
     * @param join     {@link Join}
     * @param alias    别名
     * @param action   {@link BiConsumer}
     * @param <S>      实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final String alias,
                                      final BiConsumer<AbstractCriteria<M>, SubForeign<M, S>> action) {
        final SubForeign<M, S> instance = this.join(subQuery, join, alias);
        if (action != null) {
            action.accept(instance.getMaster(), instance);
        }
        return instance;
    }

    /**
     * 创建子外联表条件
     * @param subQuery    {@link AbstractSubCriteria}
     * @param join        {@link Join}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final Expression... expressions) {
        return this.join(subQuery, join, Objects.asList(expressions));
    }

    /**
     * 创建子外联表条件
     * @param subQuery    {@link AbstractSubCriteria}
     * @param join        {@link Join}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final Collection<Expression> expressions) {
        return this.join(subQuery, join, null, expressions);
    }

    /**
     * 创建子外联表条件
     * @param subQuery    {@link AbstractSubCriteria}
     * @param join        {@link Join}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final String alias, final Expression... expressions) {
        return this.join(subQuery, join, alias, Objects.asList(expressions));
    }

    /**
     * 创建子外联表条件
     * @param subQuery    {@link AbstractSubCriteria}
     * @param join        {@link Join}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<M, S> join(final AbstractSubCriteria<S> subQuery, final Join join,
                                      final String alias, final Collection<Expression> expressions) {
        final SubForeign<M, S> instance = this.join(subQuery, join, alias);
        instance.where(expressions);
        return instance;
    }


}
