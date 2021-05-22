package com.wvkity.mybatis.core.criteria.query;

/**
 * 基础查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface CommonQueryWrapper<T, C extends CommonQueryWrapper<T, C>> extends QueryWrapper<T, C>,
    CommonSelect<T, C> {
}
