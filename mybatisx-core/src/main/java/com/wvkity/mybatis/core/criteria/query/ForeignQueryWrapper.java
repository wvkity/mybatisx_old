package com.wvkity.mybatis.core.criteria.query;

/**
 * 联表查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface ForeignQueryWrapper<T, C extends ForeignQueryWrapper<T, C>> extends QueryWrapper<T, C> {
}
