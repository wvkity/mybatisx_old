package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.core.criteria.support.LambdaCriteriaWrapper;

/**
 * 基础条件/查询接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface LambdaQueryCriteria<T, C extends LambdaQueryCriteria<T, C>> extends LambdaQueryWrapper<T, C>,
    LambdaCriteriaWrapper<T, C> {
}
