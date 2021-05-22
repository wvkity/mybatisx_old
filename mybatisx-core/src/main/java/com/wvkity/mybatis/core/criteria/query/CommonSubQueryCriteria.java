package com.wvkity.mybatis.core.criteria.query;

/**
 * 子查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface CommonSubQueryCriteria<T, C extends CommonSubQueryCriteria<T, C>> extends SubQueryWrapper<T, C>,
    CommonQueryCriteria<T, C> {
}
