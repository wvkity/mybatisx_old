package com.github.mybatisx.core.criteria.update;

import com.github.mybatisx.core.criteria.support.GenericCriteriaWrapper;

/**
 * 通用条件/更新接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface GenericUpdateCriteria<T, C extends GenericUpdateCriteria<T, C>> extends GenericCriteriaWrapper<T, C>,
    CommonUpdateCriteria<T, C>, LambdaUpdateCriteria<T, C> {

}
