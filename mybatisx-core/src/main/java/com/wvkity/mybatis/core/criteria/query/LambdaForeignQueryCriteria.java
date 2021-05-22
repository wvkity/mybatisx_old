package com.wvkity.mybatis.core.criteria.query;

/**
 * 联表查询条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public interface LambdaForeignQueryCriteria<T, C extends LambdaForeignQueryCriteria<T, C>> extends
    LambdaQueryCriteria<T, C>, ForeignQueryWrapper<T, C> {
}
