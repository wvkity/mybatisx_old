package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.support.constant.Join;

/**
 * 联表查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface ForeignQueryWrapper<T, C extends ForeignQueryWrapper<T, C>> extends QueryWrapper<T, C> {

    /**
     * 获取主条件对象
     * @return {@link ExtCriteria}
     */
    ExtCriteria<?> getMaster();

    /**
     * 抓取联表查询字段
     * @return {@code this}
     */
    default C fetch() {
        return this.fetch(true);
    }

    /**
     * 设置是否抓取联表查询字段
     * @param fetch 是否抓取
     * @return {@code this}
     */
    C fetch(final boolean fetch);

    /**
     * 加入容器
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C join() {
        ((QueryWrapper<T, C>) this.getMaster()).foreign(this);
        return (C) this;
    }

}
