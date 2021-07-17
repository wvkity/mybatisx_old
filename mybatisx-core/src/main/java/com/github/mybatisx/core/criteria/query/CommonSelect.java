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
package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.Objects;
import com.github.mybatisx.support.criteria.Criteria;

import java.util.Map;

/**
 * 查询字段接口
 * @param <T> 实体类型
 * @param <C> 子类
 * @author wvkity
 * @created 2021-05-15
 * @since 1.0.0
 */
interface CommonSelect<T, C extends CommonSelect<T, C>> extends Criteria<T> {

    /**
     * 查询字段
     * @param column 字段名
     * @return {@code this}
     */
    default C colSelect(final String column) {
        return this.colSelect(column, null);
    }

    /**
     * 查询字段
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    C colSelect(final String column, final String alias);

    /**
     * 查询字段
     * @param columns 字段集合({@code Map<别名, 字段名>})
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colSelect(final Map<String, String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, String> it : columns.entrySet()) {
                this.colSelect(it.getValue(), it.getKey());
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param columns 字段列表
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colSelects(final String... columns) {
        if (Objects.isEmpty(columns)) {
            for (String it : columns) {
                this.colSelect(it);
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param c1  字段1
     * @param as1 字段1对应别名
     * @param c2  字段2
     * @param as2 字段2对应别名
     * @return {@code this}
     */
    default C colSelect(final String c1, final String as1, final String c2, final String as2) {
        return this.colSelect(c1, as1).colSelect(c2, as2);
    }

    /**
     * 查询字段
     * @param c1  字段1
     * @param as1 字段1对应别名
     * @param c2  字段2
     * @param as2 字段2对应别名
     * @param c3  字段3
     * @param as3 字段3对应别名
     * @return {@code this}
     */
    default C colSelect(final String c1, final String as1, final String c2,
                        final String as2, final String c3, final String as3) {
        return this.colSelect(c1, as1).colSelect(c2, as2).colSelect(c3, as3);
    }

    /**
     * 忽略查询字段
     * @param column 字段名
     * @return {@code this}
     */
    C colIgnore(final String column);

    /**
     * 忽略查询字段
     * @param columns 字段列表
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colIgnores(final String... columns) {
        if (!Objects.isEmpty(columns)) {
            for (String it : columns) {
                this.colIgnore(it);
            }
        }
        return (C) this;
    }
}
