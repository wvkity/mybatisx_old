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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 条件
 * @author wvkity
 * @created 2021-01-23
 * @since 1.0.0
 */
public class Condition implements Criterion {

    private static final long serialVersionUID = 5235217961745776908L;
    /**
     * 字段默认占位符
     */
    public static final String DEF_PLACEHOLDER_COLUMN = "(?<!\\\\):@";
    /**
     * 条件包装对象
     */
    protected final Criteria<?> criteria;
    /**
     * 表别名
     */
    protected final String tableAlias;
    /**
     * 字段名
     */
    protected final String column;
    /**
     * SQL碎片
     */
    protected final String fragment;

    public Condition(Criteria<?> criteria, String tableAlias, String column, String fragment) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.fragment = fragment;
    }

    /**
     * 获取表别名
     * @return 表别名
     */
    String getAlias() {
        return Objects.nonNull(tableAlias) ? tableAlias : Objects.nonNull(criteria) ? criteria.as() : null;
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder();
        final String alias = this.getAlias();
        if (Objects.isNotBlank(alias)) {
            builder.append(alias).append(Constants.DOT);
        }
        if (Objects.isNotBlank(this.column)) {
            builder.append(column);
        }
        final String realColumn = builder.toString();
        final String template = this.fragment;
        if (template.contains(Constants.DEF_STR_COLUMN_PH)) {
            return template.replaceAll(DEF_PLACEHOLDER_COLUMN, realColumn);
        } else if (template.contains(Constants.DEF_STR_PH)) {
            return String.format(template, realColumn);
        }
        return template;
    }
}
