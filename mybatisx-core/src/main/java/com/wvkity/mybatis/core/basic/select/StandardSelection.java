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
package com.wvkity.mybatis.core.basic.select;

import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 查询字段
 * @author wvkity
 * @created 2021-01-26
 * @since 1.0.0
 */
public class StandardSelection extends AbstractSelection<String> {

    private static final long serialVersionUID = 4437700150371514743L;

    public StandardSelection(Criteria<?> criteria, String column, String columnAlias, Matched matched) {
        this.criteria = criteria;
        this.column = column;
        this.alias = columnAlias;
        this.matched = matched;
    }

    public StandardSelection(Criteria<?> criteria, String tableAlias, String column,
                             String columnAlias, Matched matched) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = columnAlias;
        this.matched = matched;
    }

    public StandardSelection(Criteria<?> criteria, String tableAlias, String column,
                             String columnAlias, String property, Matched matched) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.property = property;
        this.alias = columnAlias;
        this.matched = matched;
    }

    public StandardSelection(Criteria<?> criteria, String tableAlias, String column, String columnAlias,
                             String property, String refProp, Matched matched) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.property = property;
        this.alias = columnAlias;
        this.refProp = refProp;
        this.matched = matched;
    }

    @Override
    public String getColumn() {
        return this.column;
    }

}
