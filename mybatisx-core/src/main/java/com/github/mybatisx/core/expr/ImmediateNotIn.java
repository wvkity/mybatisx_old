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
package com.github.mybatisx.core.expr;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.expr.builder.AbstractRangeExprBuilder;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

import java.util.Collection;

/**
 * NOT IN条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class ImmediateNotIn extends AbstractInExpression<String> {

    private static final long serialVersionUID = 893533913883915181L;

    public ImmediateNotIn(Criteria<?> criteria, String column, Slot slot, Collection<?> values) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.NOT_IN;
        this.matched = Matched.IMMEDIATE;
    }

    public ImmediateNotIn(String alias, String column, Slot slot, Collection<?> values) {
        this.tableAlias = alias;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.NOT_IN;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImmediateNotIn.Builder create() {
        return new ImmediateNotIn.Builder();
    }

    public static final class Builder extends AbstractRangeExprBuilder<ImmediateNotIn, String> {

        private Builder() {
        }

        @Override
        public ImmediateNotIn build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImmediateNotIn(this.criteria, this.target, this.slot, this.values);
            }
            return null;
        }
    }
}