/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.mybatisx.core.criteria.update;

import io.github.mybatisx.Objects;
import io.github.mybatisx.core.criteria.support.CommonCriteriaWrapper;

import java.util.Map;

/**
 * 基础条件/更新接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface CommonUpdateCriteria<T, C extends CommonUpdateCriteria<T, C>> extends UCriteria<T, C>,
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
