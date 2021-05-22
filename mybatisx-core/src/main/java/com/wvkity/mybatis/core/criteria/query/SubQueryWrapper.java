package com.wvkity.mybatis.core.criteria.query;

/**
 * 子查询条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface SubQueryWrapper<T, C extends SubQueryWrapper<T, C>> extends QueryWrapper<T, C> {
}
