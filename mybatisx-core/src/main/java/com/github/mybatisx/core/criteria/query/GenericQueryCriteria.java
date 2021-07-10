package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.core.criteria.support.GenericCriteriaWrapper;

/**
 * 通用条件/查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface GenericQueryCriteria<T, C extends GenericQueryCriteria<T, C>> extends GenericCriteriaWrapper<T, C>,
    CommonQueryCriteria<T, C>, LambdaQueryCriteria<T, C>, MixinWrapper<T, C>, HavingWrapper<T, C> {

}
