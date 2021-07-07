package com.wvkity.mybatis.core.criteria.query;

/**
 * 通用联表查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public interface GenericForeignQueryCriteria<T, C extends GenericForeignQueryCriteria<T, C>>
    extends GenericQueryCriteria<T, C>, CommonForeignQueryCriteria<T, C>, LambdaForeignQueryCriteria<T, C> {

}
