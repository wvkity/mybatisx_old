package com.github.mybatisx.core.criteria.update;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.support.CommonCriteriaWrapper;

import java.util.Map;

/**
 * 基础条件/更新接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface CommonUpdateCriteria<T, C extends CommonUpdateCriteria<T, C>> extends UpdateWrapper<T, C>,
    CommonCriteriaWrapper<T, C> {

    /**
     * 更新字段值
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    C colSet(final String column, final Object value);

    /**
     * 更新字段值
     * @param column 字段
     * @param value  值
     * @return {@code this}
     */
    C colSetIfAbsent(final String column, final Object value);

    /**
     * 更新字段值
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @return {@code this}
     */
    default C colSet(final String c1, final Object v1, final String c2, final Object v2) {
        return this.colSet(c1, v1).colSet(c2, v2);
    }

    /**
     * 更新字段值
     * @param c1 字段1
     * @param v1 字段1对应值
     * @param c2 字段2
     * @param v2 字段2对应值
     * @param c3 字段3
     * @param v3 字段3对应值
     * @return {@code this}
     */
    default C colSet(final String c1, final Object v1, final String c2, final Object v2, final String c3,
                     final Object v3) {
        return this.colSet(c1, v1).colSet(c2, v2).colSet(c3, v3);
    }

    /**
     * 更新字段值
     * @param columns 字段-值集合
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colSet(final Map<String, Object> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, Object> it : columns.entrySet()) {
                this.colSet(it.getKey(), it.getValue());
            }
        }
        return (C) this;
    }
}
