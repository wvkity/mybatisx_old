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

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.CriteriaWrapper;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.executor.resultset.EmbedResult;
import com.github.mybatisx.plugin.paging.RangeFetch;
import com.github.mybatisx.support.constant.Join;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface QueryWrapper<T, C extends QueryWrapper<T, C>> extends CriteriaWrapper<T, C>,
    EmbedResult, RangeFetch {

    // region Extension methods

    /**
     * 设置引用属性
     * @param reference 引用属性
     * @return {@code this}
     */
    C reference(final String reference);

    /**
     * 设置去重
     * @return {@code this}
     */
    default C distinct() {
        return this.distinct(true);
    }

    /**
     * 设置是否去重
     * @param distinct 是否去重
     * @return {@code this}
     */
    C distinct(final boolean distinct);

    /**
     * 设置查询是否包含聚合函数
     * @param contains 是否
     * @return {@code this}
     */
    C containsFunc(final boolean contains);

    /**
     * 仅仅查询聚合函数
     * @return {@code this}
     */
    default C onlyFunc() {
        return this.onlyFunc(true);
    }

    /**
     * 设置是否仅仅查询聚合函数
     * @param only 是否
     * @return {@code this}
     */
    C onlyFunc(final boolean only);

    /**
     * 获取聚合函数
     * @param alias 聚合函数别名
     * @return {@link Function}
     */
    Function getFunction(final String alias);

    /**
     * 设置使用表别名
     * @return {@code this}
     */
    default C useTabAlias() {
        return this.useTabAlias(true);
    }

    /**
     * 设置是否使用表别名
     * @param using 是否使用
     * @return {@code this}
     */
    C useTabAlias(boolean using);

    /**
     * 使用属性名作为字段别名
     * @return {@code this}
     */
    default C usePropAlias() {
        return this.usePropAlias(true);
    }

    /**
     * 设置是否使用属性名作为字段别名
     * @param using 是否使用
     * @return {@code this}
     */
    C usePropAlias(final boolean using);

    /**
     * 添加保持排序注释(避免优化sql时被干掉)
     * @return {@code this}
     */
    default C keepOrderBy() {
        return this.keepOrderBy(true);
    }

    /**
     * 设置是否添加保持排序注释(避免优化sql时被干掉)
     * @param keep 是否保存
     * @return {@code this}
     */
    C keepOrderBy(boolean keep);

    /**
     * {@inheritDoc}
     */
    @Override
    C resultMap(final String resultMap);

    /**
     * {@inheritDoc}
     */
    @Override
    C resultType(final Class<?> resultType);

    /**
     * 设置map结果中的key值为主键值
     * @return {@code this}
     */
    C mapKey();

    /**
     * {@inheritDoc}
     */
    @Override
    C mapKey(final String mapKey);

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    C mapType(final Class<? extends Map> mapImplClass);

    /**
     * 指定查询范围
     * @param rowEnd 结束位置
     * @return {@code this}
     */
    default C rangeWithRow(final long rowEnd) {
        return this.rangeWithRow(1L, rowEnd);
    }

    /**
     * 指定查询范围
     * @param rowStart 起始位置
     * @param rowEnd   结束位置
     * @return {@code this}
     */
    C rangeWithRow(final long rowStart, final long rowEnd);

    /**
     * 指定查询范围
     * @param pageEnd 结束页码
     * @return {@code this}
     */
    default C rangeWithPage(final long pageEnd) {
        return this.rangeWithPage(1L, pageEnd, 20L);
    }

    /**
     * 指定查询范围
     * @param pageStart 起始页码
     * @param pageEnd   结束页码
     * @return {@code this}
     */
    default C rangeWithPage(final long pageStart, final long pageEnd) {
        return this.rangeWithPage(pageStart, pageEnd, 20L);
    }

    /**
     * 指定查询范围
     * @param pageStart 起始页码
     * @param pageEnd   结束页码
     * @param pageSize  每页数目
     * @return {@code this}
     */
    C rangeWithPage(final long pageStart, final long pageEnd, final long pageSize);

    // endregion

    // region Nested sub query methods

    /**
     * 创建{@link SubQuery}对象
     * @return {@link SubQuery}
     */
    default SubQuery<T> newSubQuery() {
        return this.newSubQuery(this);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param action {@link Consumer}
     * @return {@link SubQuery}
     */
    default SubQuery<T> newSubQuery(final Consumer<SubQuery<T>> action) {
        return this.newSubQuery(this, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param alias 别名
     * @return {@link SubQuery}
     */
    default SubQuery<T> newSubQuery(final String alias) {
        return this.newSubQuery(this, alias);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param alias  别名
     * @param action {@link Consumer}
     * @return {@link SubQuery}
     */
    default SubQuery<T> newSubQuery(final String alias, final Consumer<SubQuery<T>> action) {
        return this.newSubQuery(this, alias, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param query {@link ExtCriteria}
     * @param <S>   实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newSubQuery(final ExtCriteria<S> query) {
        return this.newSubQuery(query, (String) null);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newSubQuery(final ExtCriteria<S> query,
                                        final Consumer<SubQuery<S>> action) {
        return this.newSubQuery(query, null, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newSubQuery(final ExtCriteria<S> query,
                                        final BiConsumer<C, SubQuery<S>> action) {
        return this.newSubQuery(query, null, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param query {@link ExtCriteria}
     * @param alias 别名
     * @param <S>   实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newSubQuery(final ExtCriteria<S> query, final String alias) {
        return SubQuery.from(query, alias);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newSubQuery(final ExtCriteria<S> query, final String alias,
                                        final Consumer<SubQuery<S>> action) {
        return SubQuery.from(query, alias, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> SubQuery<S> newSubQuery(final ExtCriteria<S> query, final String alias,
                                        final BiConsumer<C, SubQuery<S>> action) {
        final SubQuery<S> it = this.newSubQuery(query, alias);
        if (action != null) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

    // region Foreign methods

    // region Common foreign methods

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity) {
        return this.join(entity, Join.INNER);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final String alias) {
        return this.join(entity, alias, Join.INNER);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final Consumer<Foreign<S>> action) {
        return this.join(entity, null, Join.INNER, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final BiConsumer<C, Foreign<S>> action) {
        return this.join(entity, null, Join.INNER, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final String alias,
                                final Consumer<Foreign<S>> action) {
        return this.join(entity, alias, Join.INNER, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final String alias,
                                final BiConsumer<C, Foreign<S>> action) {
        return this.join(entity, alias, Join.INNER, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> leftJoin(final Class<S> entity) {
        return this.join(entity, Join.LEFT);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> leftJoin(final Class<S> entity, final String alias) {
        return this.join(entity, alias, Join.LEFT);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> leftJoin(final Class<S> entity, final Consumer<Foreign<S>> action) {
        return this.join(entity, null, Join.LEFT, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> leftJoin(final Class<S> entity, final BiConsumer<C, Foreign<S>> action) {
        return this.join(entity, null, Join.LEFT, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> leftJoin(final Class<S> entity, final String alias,
                                    final Consumer<Foreign<S>> action) {
        return this.join(entity, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> leftJoin(final Class<S> entity, final String alias,
                                    final BiConsumer<C, Foreign<S>> action) {
        return this.join(entity, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final Join join) {
        final Foreign<S> it = Foreign.from(this, entity, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final String alias, final Join join) {
        final Foreign<S> it = Foreign.from(this, entity, alias, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    default <S> Foreign<S> join(final Class<S> entity, final String alias, final Join join,
                                final Consumer<Foreign<S>> action) {
        final Foreign<S> it = Foreign.from(this, entity, alias, join, action);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link Foreign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link Foreign}
     */
    @SuppressWarnings("unchecked")
    default <S> Foreign<S> join(final Class<S> entity, final String alias, final Join join,
                                final BiConsumer<C, Foreign<S>> action) {
        final Foreign<S> it = Foreign.from(this, entity, alias, join);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        this.foreign(it);
        return it;
    }

    // endregion

    // region Lambda foreign methods

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity) {
        return this.joinWithLambda(entity, Join.INNER);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final String alias) {
        return this.joinWithLambda(entity, alias, Join.INNER);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity,
                                                final Consumer<LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, null, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity,
                                                final BiConsumer<C, LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, null, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final String alias,
                                                final Consumer<LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, alias, Join.INNER, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final String alias,
                                                final BiConsumer<C, LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, alias, Join.INNER, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> leftJoinWithLambda(final Class<S> entity) {
        return this.joinWithLambda(entity, Join.LEFT);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> leftJoinWithLambda(final Class<S> entity, final String alias) {
        return this.joinWithLambda(entity, alias, Join.LEFT);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> leftJoinWithLambda(final Class<S> entity,
                                                    final Consumer<LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, null, Join.LEFT, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> leftJoinWithLambda(final Class<S> entity,
                                                    final BiConsumer<C, LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, null, Join.LEFT, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> leftJoinWithLambda(final Class<S> entity, final String alias,
                                                    final Consumer<LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> leftJoinWithLambda(final Class<S> entity, final String alias,
                                                    final BiConsumer<C, LambdaForeign<S>> action) {
        return this.joinWithLambda(entity, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final Join join) {
        final LambdaForeign<S> it = LambdaForeign.from(this, entity, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final String alias, final Join join) {
        final LambdaForeign<S> it = LambdaForeign.from(this, entity, alias, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final String alias, final Join join,
                                                final Consumer<LambdaForeign<S>> action) {
        final LambdaForeign<S> it = LambdaForeign.from(this, entity, alias, join, action);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link LambdaForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaForeign}
     */
    @SuppressWarnings("unchecked")
    default <S> LambdaForeign<S> joinWithLambda(final Class<S> entity, final String alias, final Join join,
                                                final BiConsumer<C, LambdaForeign<S>> action) {
        final LambdaForeign<S> it = LambdaForeign.from(this, entity, alias, join);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        this.foreign(it);
        return it;
    }

    // endregion

    // region Generic foreign methods

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity) {
        return this.joinWithGeneric(entity, Join.INNER);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final String alias) {
        return this.joinWithGeneric(entity, alias, Join.INNER);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity,
                                                  final Consumer<GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, null, Join.INNER, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity,
                                                  final BiConsumer<C, GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, null, Join.INNER, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final String alias,
                                                  final Consumer<GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, alias, Join.INNER, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final String alias,
                                                  final BiConsumer<C, GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, alias, Join.INNER, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> leftJoinWithGeneric(final Class<S> entity) {
        return this.joinWithGeneric(entity, Join.LEFT);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> leftJoinWithGeneric(final Class<S> entity, final String alias) {
        return this.joinWithGeneric(entity, alias, Join.LEFT);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> leftJoinWithGeneric(final Class<S> entity,
                                                      final Consumer<GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, null, Join.LEFT, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> leftJoinWithGeneric(final Class<S> entity,
                                                      final BiConsumer<C, GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, null, Join.LEFT, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> leftJoinWithGeneric(final Class<S> entity, final String alias,
                                                      final Consumer<GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> leftJoinWithGeneric(final Class<S> entity, final String alias,
                                                      final BiConsumer<C, GenericForeign<S>> action) {
        return this.joinWithGeneric(entity, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final Join join) {
        final GenericForeign<S> it = GenericForeign.from(this, entity, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final String alias, final Join join) {
        final GenericForeign<S> it = GenericForeign.from(this, entity, alias, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final String alias, final Join join,
                                                  final Consumer<GenericForeign<S>> action) {
        final GenericForeign<S> it = GenericForeign.from(this, entity, alias, join, action);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link GenericForeign}对象
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericForeign}
     */
    @SuppressWarnings("unchecked")
    default <S> GenericForeign<S> joinWithGeneric(final Class<S> entity, final String alias, final Join join,
                                                  final BiConsumer<C, GenericForeign<S>> action) {
        final GenericForeign<S> it = GenericForeign.from(this, entity, alias, join);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        this.foreign(it);
        return it;
    }

    // endregion

    // region Sub foreign methods

    /**
     * 创建{@link SubForeign}对象
     * @param query {@link ExtCriteria}
     * @param <S>   实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query) {
        return this.join(query, Join.INNER);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query {@link ExtCriteria}
     * @param alias 别名
     * @param <S>   实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final String alias) {
        return this.join(query, alias, Join.INNER);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final Consumer<SubForeign<S>> action) {
        return this.join(query, null, Join.INNER, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final BiConsumer<C, SubForeign<S>> action) {
        return this.join(query, null, Join.INNER, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final String alias,
                                   final Consumer<SubForeign<S>> action) {
        return this.join(query, alias, Join.INNER, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final String alias,
                                   final BiConsumer<C, SubForeign<S>> action) {
        return this.join(query, alias, Join.INNER, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query {@link ExtCriteria}
     * @param <S>   实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> leftJoin(final ExtCriteria<S> query) {
        return this.join(query, Join.LEFT);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query {@link ExtCriteria}
     * @param alias 别名
     * @param <S>   实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> leftJoin(final ExtCriteria<S> query, final String alias) {
        return this.join(query, alias, Join.LEFT);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> leftJoin(final ExtCriteria<S> query, final Consumer<SubForeign<S>> action) {
        return this.join(query, null, Join.LEFT, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> leftJoin(final ExtCriteria<S> query, final BiConsumer<C, SubForeign<S>> action) {
        return this.join(query, null, Join.LEFT, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> leftJoin(final ExtCriteria<S> query, final String alias,
                                       final Consumer<SubForeign<S>> action) {
        return this.join(query, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> leftJoin(final ExtCriteria<S> query, final String alias,
                                       final BiConsumer<C, SubForeign<S>> action) {
        return this.join(query, alias, Join.LEFT, action);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query {@link ExtCriteria}
     * @param join  {@link Join}
     * @param <S>   实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final Join join) {
        return this.join(query, null, join);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query {@link ExtCriteria}
     * @param alias 别名
     * @param join  {@link Join}
     * @param <S>   实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final String alias, final Join join) {
        final SubForeign<S> it = SubForeign.from(this, query, alias, join);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final String alias, final Join join,
                                   final Consumer<SubForeign<S>> action) {
        final SubForeign<S> it = SubForeign.from(this, query, alias, join, action);
        this.foreign(it);
        return it;
    }

    /**
     * 创建{@link SubForeign}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubForeign}
     */
    @SuppressWarnings("unchecked")
    default <S> SubForeign<S> join(final ExtCriteria<S> query, final String alias, final Join join,
                                   final BiConsumer<C, SubForeign<S>> action) {
        final SubForeign<S> it = SubForeign.from(this, query, alias, join);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        this.foreign(it);
        return it;
    }

    // endregion

    /**
     * 添加联表查询条件对象
     * @param query {@link ExtCriteria}
     * @return {@code this}
     */
    C foreign(final ExtCriteria<?> query);

    // endregion

}
