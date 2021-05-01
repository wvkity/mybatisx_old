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
import com.wvkity.mybatis.support.constant.Join;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 联表条件包装接口
 * @param <M>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-04-13
 * @since 1.0.0
 */
public interface ForeignWrapper<M, Chain extends ForeignWrapper<M, Chain>> {

    // region Inner join

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity) {
        return this.join(entity, Join.INNER);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Consumer<Foreign<M, S>> action) {
        return this.join(entity, Join.INNER, action);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity,
                                   final BiConsumer<AbstractCriteria<M>, Foreign<M, S>> action) {
        return this.join(entity, Join.INNER, action);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final String alias) {
        return this.join(entity, Join.INNER, alias);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final String alias,
                                   final Consumer<Foreign<M, S>> action) {
        return this.join(entity, Join.INNER, alias, action);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final String alias,
                                   final BiConsumer<AbstractCriteria<M>, Foreign<M, S>> action) {
        return this.join(entity, Join.INNER, alias, action);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity      实体类
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Expression... expressions) {
        return this.join(entity, Objects.asList(expressions));
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity      实体类
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Collection<Expression> expressions) {
        return this.join(entity, Join.INNER, expressions);
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity      实体类
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final String alias, final Expression... expressions) {
        return this.join(entity, alias, Objects.asList(expressions));
    }

    /**
     * 创建联表条件({@link Join#INNER})
     * @param entity      实体类
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final String alias,
                                   final Collection<Expression> expressions) {
        return this.join(entity, Join.INNER, alias, expressions);
    }

    // endregion

    // region Left join

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity) {
        return this.join(entity, Join.LEFT);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final Consumer<Foreign<M, S>> action) {
        return this.join(entity, Join.LEFT, action);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity,
                                       final BiConsumer<AbstractCriteria<M>, Foreign<M, S>> action) {
        return this.join(entity, Join.LEFT, action);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final String alias) {
        return this.join(entity, Join.LEFT, alias);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final String alias,
                                       final Consumer<Foreign<M, S>> action) {
        return this.join(entity, Join.LEFT, alias, action);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final String alias,
                                       final BiConsumer<AbstractCriteria<M>, Foreign<M, S>> action) {
        return this.join(entity, Join.LEFT, alias, action);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity      实体类
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final Expression... expressions) {
        return this.leftJoin(entity, Objects.asList(expressions));
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity      实体类
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final Collection<Expression> expressions) {
        return this.join(entity, Join.LEFT, expressions);
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity      实体类
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final String alias, final Expression... expressions) {
        return this.leftJoin(entity, alias, Objects.asList(expressions));
    }

    /**
     * 创建联表条件({@link Join#LEFT})
     * @param entity      实体类
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> leftJoin(final Class<S> entity, final String alias,
                                       final Collection<Expression> expressions) {
        return this.join(entity, Join.LEFT, alias, expressions);
    }

    // endregion

    /**
     * 创建联表条件
     * @param entity 实体类
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join) {
        return this.join(entity, join, (String) null);
    }

    /**
     * 创建联表条件
     * @param entity 实体类
     * @param join   {@link Join}
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    <S> Foreign<M, S> join(final Class<S> entity, final Join join, final String alias);

    /**
     * 创建联表条件
     * @param entity 实体类
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final Consumer<Foreign<M, S>> action) {
        return this.join(entity, join, null, action);
    }

    /**
     * 创建联表条件
     * @param entity 实体类
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join,
                                   final BiConsumer<AbstractCriteria<M>, Foreign<M, S>> action) {
        return this.join(entity, join, null, action);
    }

    /**
     * 创建联表条件
     * @param entity 实体类
     * @param join   {@link Join}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final String alias,
                                   final Consumer<Foreign<M, S>> action) {
        final Foreign<M, S> instance = this.join(entity, join, alias);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 创建联表条件
     * @param entity 实体类
     * @param join   {@link Join}
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final String alias,
                                   final BiConsumer<AbstractCriteria<M>, Foreign<M, S>> action) {
        final Foreign<M, S> instance = this.join(entity, join, alias);
        if (action != null) {
            action.accept(instance.getMaster(), instance);
        }
        return instance;
    }

    /**
     * 创建联表条件
     * @param entity      实体类
     * @param join        {@link Join}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final Expression... expressions) {
        return this.join(entity, join, Objects.asList(expressions));
    }

    /**
     * 创建联表条件
     * @param entity      实体类
     * @param join        {@link Join}
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final Collection<Expression> expressions) {
        return this.join(entity, join, null, expressions);
    }

    /**
     * 创建联表条件
     * @param entity      实体类
     * @param join        {@link Join}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final String alias,
                                   final Expression... expressions) {
        return this.join(entity, join, alias, Objects.asList(expressions));
    }

    /**
     * 创建联表条件
     * @param entity      实体类
     * @param join        {@link Join}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @param <S>         实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<M, S> join(final Class<S> entity, final Join join, final String alias,
                                   final Collection<Expression> expressions) {
        final Foreign<M, S> foreign = this.join(entity, join, alias);
        foreign.where(expressions);
        return foreign;
    }

}
