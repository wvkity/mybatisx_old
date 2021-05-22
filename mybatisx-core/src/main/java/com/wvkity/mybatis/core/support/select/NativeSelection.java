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
package com.wvkity.mybatis.core.support.select;

import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 纯SQL查询字段
 * @author wvkity
 * @created 2021-05-15
 * @since 1.0.0
 */
public class NativeSelection extends AbstractSelection<String> {

    /**
     * 是否为简单字段
     */
    private boolean simple;

    public NativeSelection(String sql, String alias) {
        this(null, sql, alias);
    }

    public NativeSelection(Criteria<?> criteria, String sql, String alias) {
        this.criteria = criteria;
        this.column = sql;
        this.alias = alias;
        this.matched = Matched.IMMEDIATE;
    }

    @Override
    public String getColumn() {
        return this.column;
    }

    @Override
    public String getSegment() {
        return super.getSegment();
    }
}
