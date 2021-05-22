package com.wvkity.mybatis.core.criteria.query;

/**
 * 子查询接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface LambdaSubQueryCriteria<T, C extends LambdaSubQueryCriteria<T, C>> extends SubQueryWrapper<T, C>,
    LambdaQueryCriteria<T, C> {
}
