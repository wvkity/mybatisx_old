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
package com.github.mybatisx.core.expr;

import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.core.criteria.ExtCriteria;

/**
 * 子查询条件表达式
 * @author wvkity
 * @created 2021-05-05
 * @since 1.0.0
 */
public class SubQueryExpression extends AbstractExpression<String> {

    private final ExtCriteria<?> query;

    public SubQueryExpression(Criteria<?> criteria, String column, ExtCriteria<?> query) {
        this(criteria, column, query, Slot.AND, Symbol.EQ);
    }

    public SubQueryExpression(Criteria<?> criteria, String column, ExtCriteria<?> query, Symbol symbol) {
        this(criteria, column, query, Slot.AND, symbol);
    }

    public SubQueryExpression(Criteria<?> criteria, String column, ExtCriteria<?> query, Slot slot) {
        this(criteria, column, query, slot, Symbol.EQ);
    }

    public SubQueryExpression(Criteria<?> criteria, String column, ExtCriteria<?> query,
                              Slot slot, Symbol symbol) {
        this.criteria = criteria;
        this.column = column;
        this.query = query;
        this.slot = slot;
        this.symbol = symbol;
        this.matched = Matched.IMMEDIATE;
    }

    public SubQueryExpression(String alias, String column, ExtCriteria<?> query) {
        this(alias, column, query, Slot.AND, Symbol.EQ);
    }

    public SubQueryExpression(String alias, String column, ExtCriteria<?> query, Symbol symbol) {
        this(alias, column, query, Slot.AND, symbol);
    }

    public SubQueryExpression(String alias, String column, ExtCriteria<?> query, Slot slot) {
        this(alias, column, query, slot, Symbol.EQ);
    }

    public SubQueryExpression(String alias, String column, ExtCriteria<?> query,
                              Slot slot, Symbol symbol) {
        this.tableAlias = alias;
        this.column = column;
        this.query = query;
        this.slot = slot;
        this.symbol = symbol;
        this.matched = Matched.IMMEDIATE;
    }

    public ExtCriteria<?> getQuery() {
        return query;
    }
}
