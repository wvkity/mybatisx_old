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
package com.wvkity.mybatis.core.support.func;

import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * {@link AggFunc#MAX}聚合函数
 * @author wvkity
 * @created 2021-05-01
 * @since 1.0.0
 */
public class Max extends AbstractFunction {

    private static final long serialVersionUID = -727573337960282476L;

    public Max(Criteria<?> criteria, String column, String alias) {
        this(criteria, column, alias, null);
    }

    public Max(Criteria<?> criteria, String column, String alias, Integer scale) {
        this.criteria = criteria;
        this.column = column;
        this.alias = alias;
        this.scale = scale;
        this.func = AggFunc.MAX;
    }

    public Max(String tableAlias, String column, String alias) {
        this(tableAlias, column, alias, null);
    }

    public Max(String tableAlias, String column, String alias, Integer scale) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = alias;
        this.scale = scale;
        this.func = AggFunc.MAX;
    }
}
