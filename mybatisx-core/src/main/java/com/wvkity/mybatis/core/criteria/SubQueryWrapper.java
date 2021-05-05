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
import com.wvkity.mybatis.support.expr.Expression;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 子查询条件包装接口
 * @param <R>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-04-17
 * @since 1.0.0
 */
public interface SubQueryWrapper<R, Chain extends SubQueryWrapper<R, Chain>> {

    /**
     * 创建子查询条件
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity) {
        return this.newQuery(entity, (String) null);
    }

    /**
     * 创建子查询条件
     * @param entity 实体类
     * @param alias  子查询别名
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    <S> SubQuery<S> newQuery(final Class<S> entity, final String alias);

    /**
     * 创建子查询条件
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final Consumer<SubQuery<S>> action) {
        return this.newQuery(entity, null, action);
    }

    /**
     * 创建子查询条件
     * @param entity 实体类
     * @param alias  子查询别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias,
                                     final Consumer<SubQuery<S>> action) {
        final SubQuery<S> instance = this.newQuery(entity, alias);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 创建子查询条件
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity,
                                     final BiConsumer<AbstractCriteria<R>, SubQuery<S>> action) {
        return this.newQuery(entity, null, action);
    }

    /**
     * 创建子查询条件
     * @param entity 实体类
     * @param alias  子查询别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias,
                                     final BiConsumer<AbstractCriteria<R>, SubQuery<S>> action) {
        final SubQuery<S> instance = this.newQuery(entity, alias);
        if (action != null) {
            action.accept(instance.getMaster(), instance);
        }
        return instance;
    }

    /**
     * 创建子查询条件
     * @param entity      实体类
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final Expression... expressions) {
        return this.newQuery(entity, null, expressions);
    }

    /**
     * 创建子查询条件
     * @param entity      实体类
     * @param alias       子查询别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias,
                                     final Expression... expressions) {
        return this.newQuery(entity, alias, Objects.asList(expressions));
    }

    /**
     * 创建子查询条件
     * @param entity      实体类
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final Collection<Expression> expressions) {
        return this.newQuery(entity, null, expressions);
    }

    /**
     * 创建子查询条件
     * @param entity      实体类
     * @param alias       子查询别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias,
                                     final Collection<Expression> expressions) {
        final SubQuery<S> instance = this.newQuery(entity, alias);
        instance.where(expressions);
        return instance;
    }

}
