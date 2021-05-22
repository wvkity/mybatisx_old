package com.wvkity.mybatis.core.criteria.query;

/**
 * 通用子查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-20
 * @since 1.0.0
 */
public interface GenericSubQueryCriteria<T, C extends GenericSubQueryCriteria<T, C>> extends GenericQueryCriteria<T, C>,
    CommonSubQueryCriteria<T, C>, LambdaSubQueryCriteria<T, C> {
}
