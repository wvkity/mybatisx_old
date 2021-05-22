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
package com.wvkity.mybatis.core.expr;

import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.expr.Expression;

/**
 * EXISTS条件表达式
 * @author wvkity
 * @created 2021-05-02
 * @since 1.0.0
 */
public class ExistsExpression extends AbstractExpression<String> {

    private static final long serialVersionUID = -7798546725462282249L;

    public ExistsExpression(ExtCriteria<?> criteria, Slot slot) {
        this(criteria, false, slot);
    }

    public ExistsExpression(ExtCriteria<?> criteria, boolean not, Slot slot) {
        this.criteria = criteria;
        this.slot = slot;
        this.symbol = not ? Symbol.NOT_EXISTS : Symbol.EXISTS;
        this.matched = Matched.EXISTS;
    }

    @Override
    public Expression setIfNecessary(Criteria<?> criteria) {
        return this;
    }
}
