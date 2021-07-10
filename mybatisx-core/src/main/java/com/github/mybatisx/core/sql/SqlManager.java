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
package com.github.mybatisx.core.sql;

import com.github.mybatisx.basic.constant.Constants;

/**
 * SQL管理器
 * @author wvkity
 * @created 2021-06-21
 * @since 1.0.0
 */
public interface SqlManager {

    /**
     * 完整SQL语句
     * @return SQL语句
     */
    default String intactString() {
        return this.getWhereSegment();
    }

    /**
     * 获取条件SQL语句
     * @return 条件语句
     */
    default String getWhereSegment() {
        return this.getWhereSegment(true, true, Constants.NULL);
    }

    /**
     * 获取条件片段
     * @param self 是否自身
     * @param appendWhere 是否拼接where
     * @param groupByReplacement 分组替换语句
     * @return 条件语句
     */
    String getWhereSegment(final boolean self, final boolean appendWhere, final String groupByReplacement);

    /**
     * 获取查询字段语句
     * @return 查询字段语句
     */
    default String getSelectSegment() {
        return this.getSelectSegment(true);
    }

    /**
     * 获取查询字段语句
     * @param self 自己本身
     * @return 查询字段语句
     */
    String getSelectSegment(final boolean self);

    /**
     * 获取查询字段字SQL语句
     * @return 查询字段SQL语句
     */
    String getSelectString();

    /**
     * 获取分组SQL语句
     * @return 分组语句
     */
    String getGroupSegment();

    /**
     * 获取更新SQL语句
     * @return 更新SQL语句
     */
    String getUpdateSegment();
}
