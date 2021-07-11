/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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

import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.support.select.Selection;

/**
 * 其他查询列
 * @author wvkity
 * @created 2021-07-11
 * @since 1.0.0
 */
interface OtherSelect<T, C extends OtherSelect<T, C>> {

    /**
     * 查询所有
     * @return {@code this}
     */
    C select();

    /**
     * 添加子查询列
     * @param query {@link ExtCriteria}
     * @return {@code this}
     */
    default C select(final ExtCriteria<?> query) {
        return this.select(query, null);
    }

    /**
     * 添加子查询列
     * @param query {@link ExtCriteria}
     * @param alias 别名
     * @return {@code this}
     */
    C select(final ExtCriteria<?> query, final String alias);

    /**
     * 纯SQL查询字段
     * @param sql SQL语句
     * @return {@code this}
     */
    default C nativeSelect(final String sql) {
        return this.nativeSelect(sql, null);
    }

    /**
     * 纯SQL查询字段
     * @param sql   SQL语句
     * @param alias 别名
     * @return {@code this}
     */
    C nativeSelect(final String sql, final String alias);

    /**
     * 查询列
     * @param selection {@link Selection}
     * @return {@code this}
     */
    C select(final Selection selection);

}
