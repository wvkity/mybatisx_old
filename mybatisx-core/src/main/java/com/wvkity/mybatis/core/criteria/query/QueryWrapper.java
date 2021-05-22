package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.core.criteria.CriteriaWrapper;
import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.executor.resultset.EmbedResult;

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
public interface QueryWrapper<T, C extends QueryWrapper<T, C>> extends CriteriaWrapper<T, C>, EmbedResult {

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
     * 设置是否仅仅查询聚合函数
     * @param only 是否
     * @return {@code this}
     */
    C onlyFunc(final boolean only);

    /**
     * 使用属性名作为别名
     * @return {@code this}
     */
    default C usePropAlias() {
        return this.usePropAlias(true);
    }

    /**
     * 设置是否使用属性名作为别名
     * @param used 是否使用
     * @return {@code this}
     */
    C usePropAlias(final boolean used);

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
     * 设置自定义结果集
     * @param resultMap 结果集
     * @return {@code this}
     */
    C resultMap(final String resultMap);

    /**
     * 设置返回值类型
     * @param resultType 返回值类型
     * @return {@code this}
     */
    C resultType(final Class<?> resultType);

    /**
     * 设置map结果中的key值为主键值
     * @return {@code this}
     */
    C mapKey();

    /**
     * 设置map结果中的key值
     * <p>{@code @MapKey("key")}</p>
     * @param mapKey key
     * @return {@code this}
     */
    C mapKey(final String mapKey);

    /**
     * 设置{@link Map}实现类
     * @param mapImplClass {@link Map}实现类
     * @return {@code this}
     */
    @SuppressWarnings("rawtypes")
    C mapType(final Class<? extends Map> mapImplClass);

    /**
     * 指定查询范围
     * @param end 结束位置
     * @return {@code this}
     */
    default C range(final long end) {
        return this.range(1L, end);
    }

    /**
     * 指定查询范围
     * @param start 起始位置
     * @param end   结束位置
     * @return {@code this}
     */
    C range(final long start, final long end);

    /**
     * 指定查询范围
     * @param start 起始页码
     * @param end   结束页码
     * @param size  每页数目
     * @return {@code this}
     */
    C range(final long start, final long end, final long size);

    // endregion

    // region Nested sub query methods

    /**
     * 创建{@link NestedSubQuery}对象
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<T> nestedQuery() {
        return this.nestedQuery(this);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param action {@link Consumer}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<T> nestedQuery(final Consumer<NestedSubQuery<T>> action) {
        return this.nestedQuery(this, action);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param alias 别名
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<T> nestedQuery(final String alias) {
        return this.nestedQuery(this, alias);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param alias  别名
     * @param action {@link Consumer}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<T> nestedQuery(final String alias, final Consumer<NestedSubQuery<T>> action) {
        return this.nestedQuery(this, alias, action);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param query {@link ExtCriteria}
     * @param <S>   实体类型
     * @return {@link NestedSubQuery}
     */
    default <S> NestedSubQuery<S> nestedQuery(final ExtCriteria<S> query) {
        return this.nestedQuery(query, (String) null);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link NestedSubQuery}
     */
    default <S> NestedSubQuery<S> nestedQuery(final ExtCriteria<S> query,
                                              final Consumer<NestedSubQuery<S>> action) {
        return this.nestedQuery(query, null, action);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link NestedSubQuery}
     */
    default <S> NestedSubQuery<S> nestedQuery(final ExtCriteria<S> query,
                                              final BiConsumer<C, NestedSubQuery<S>> action) {
        return this.nestedQuery(query, null, action);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param query {@link ExtCriteria}
     * @param alias 别名
     * @param <S>   实体类型
     * @return {@link NestedSubQuery}
     */
    default <S> NestedSubQuery<S> nestedQuery(final ExtCriteria<S> query, final String alias) {
        return NestedSubQuery.from(query, alias);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link NestedSubQuery}
     */
    default <S> NestedSubQuery<S> nestedQuery(final ExtCriteria<S> query, final String alias,
                                              final Consumer<NestedSubQuery<S>> action) {
        return NestedSubQuery.from(query, alias, action);
    }

    /**
     * 创建{@link NestedSubQuery}对象
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <S>    实体类型
     * @return {@link NestedSubQuery}
     */
    @SuppressWarnings("unchecked")
    default <S> NestedSubQuery<S> nestedQuery(final ExtCriteria<S> query, final String alias,
                                              final BiConsumer<C, NestedSubQuery<S>> action) {
        final NestedSubQuery<S> it = this.nestedQuery(query, alias);
        if (action != null) {
            action.accept((C) this, it);
        }
        return it;
    }

    // endregion

}
