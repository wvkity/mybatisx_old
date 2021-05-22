package com.wvkity.mybatis.core.criteria.update;

import com.wvkity.mybatis.core.criteria.CriteriaWrapper;

/**
 * 更新接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface UpdateWrapper<T, C extends UpdateWrapper<T, C>> extends CriteriaWrapper<T, C> {

    /**
     * 获取更新语句片段
     * @return 更新SQL片段
     */
    String getUpdateSegment();

}
