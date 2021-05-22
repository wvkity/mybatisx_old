package com.wvkity.mybatis.core.criteria.query;

/**
 * 子外联表查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public interface SubForeignQueryWrapper<T, C extends SubForeignQueryWrapper<T, C>> extends CommonForeignQueryCriteria<T, C> {
}
