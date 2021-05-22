package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.core.criteria.query.GenericSubQuery;
import com.wvkity.mybatis.core.criteria.query.LambdaSubQuery;
import com.wvkity.mybatis.core.criteria.query.SubQuery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 创建子查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface SubCriteriaWrapper<T, C extends SubCriteriaWrapper<T, C>> extends ExtCriteria<T> {

    // region Common sub query

    /**
     * 创建{@link SubQuery}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity) {
        return this.newQuery(entity, (String) null);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias) {
        return SubQuery.from(this, entity, alias);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final Consumer<SubQuery<S>> action) {
        return this.newQuery(entity, null, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias, final Consumer<SubQuery<S>> action) {
        return SubQuery.from(this, entity, alias, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    default <S> SubQuery<S> newQuery(final Class<S> entity, final BiConsumer<C, SubQuery<S>> action) {
        return this.newQuery(entity, null, action);
    }

    /**
     * 创建{@link SubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link SubQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> SubQuery<S> newQuery(final Class<S> entity, final String alias,
                                     final BiConsumer<C, SubQuery<S>> action) {
        final SubQuery<S> it = this.newQuery(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

    // region Lambda sub query

    /**
     * 创建{@link LambdaSubQuery}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link LambdaSubQuery}
     */
    default <S> LambdaSubQuery<S> newLambdaQuery(final Class<S> entity) {
        return this.newLambdaQuery(entity, (String) null);
    }

    /**
     * 创建{@link LambdaSubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link LambdaSubQuery}
     */
    default <S> LambdaSubQuery<S> newLambdaQuery(final Class<S> entity, final String alias) {
        return LambdaSubQuery.from(this, entity, alias);
    }

    /**
     * 创建{@link LambdaSubQuery}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaSubQuery}
     */
    default <S> LambdaSubQuery<S> newLambdaQuery(final Class<S> entity, final Consumer<LambdaSubQuery<S>> action) {
        return this.newLambdaQuery(entity, null, action);
    }

    /**
     * 创建{@link LambdaSubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link LambdaSubQuery}
     */
    default <S> LambdaSubQuery<S> newLambdaQuery(final Class<S> entity, final String alias,
                                                 final Consumer<LambdaSubQuery<S>> action) {
        return LambdaSubQuery.from(this, entity, alias, action);
    }

    /**
     * 创建{@link LambdaSubQuery}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaSubQuery}
     */
    default <S> LambdaSubQuery<S> newLambdaQuery(final Class<S> entity,
                                                 final BiConsumer<C, LambdaSubQuery<S>> action) {
        return this.newLambdaQuery(entity, null, action);
    }

    /**
     * 创建{@link LambdaSubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link LambdaSubQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> LambdaSubQuery<S> newLambdaQuery(final Class<S> entity, final String alias,
                                                 final BiConsumer<C, LambdaSubQuery<S>> action) {
        final LambdaSubQuery<S> it = this.newLambdaQuery(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

    // region Generic sub query

    /**
     * 创建{@link GenericSubQuery}对象
     * @param entity 实体类
     * @param <S>    实体类型
     * @return {@link GenericSubQuery}
     */
    default <S> GenericSubQuery<S> newGenericQuery(final Class<S> entity) {
        return this.newGenericQuery(entity, (String) null);
    }

    /**
     * 创建{@link GenericSubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param <S>    实体类型
     * @return {@link GenericSubQuery}
     */
    default <S> GenericSubQuery<S> newGenericQuery(final Class<S> entity, final String alias) {
        return GenericSubQuery.from(this, entity, alias);
    }

    /**
     * 创建{@link GenericSubQuery}对象
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericSubQuery}
     */
    default <S> GenericSubQuery<S> newGenericQuery(final Class<S> entity,
                                                   final Consumer<GenericSubQuery<S>> action) {
        return this.newGenericQuery(entity, null, action);
    }

    /**
     * 创建{@link GenericSubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link GenericSubQuery}
     */
    default <S> GenericSubQuery<S> newGenericQuery(final Class<S> entity, final String alias,
                                                   final Consumer<GenericSubQuery<S>> action) {
        return GenericSubQuery.from(this, entity, alias, action);
    }

    /**
     * 创建{@link GenericSubQuery}对象
     * @param entity 实体类
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericSubQuery}
     */
    default <S> GenericSubQuery<S> newGenericQuery(final Class<S> entity,
                                                   final BiConsumer<C, GenericSubQuery<S>> action) {
        return this.newGenericQuery(entity, null, action);
    }

    /**
     * 创建{@link GenericSubQuery}对象
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link BiConsumer}
     * @param <S>    实体类型
     * @return {@link GenericSubQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> GenericSubQuery<S> newGenericQuery(final Class<S> entity, final String alias,
                                                   final BiConsumer<C, GenericSubQuery<S>> action) {
        final GenericSubQuery<S> it = this.newGenericQuery(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

}
