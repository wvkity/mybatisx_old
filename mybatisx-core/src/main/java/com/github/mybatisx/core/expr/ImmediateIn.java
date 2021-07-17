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

import com.github.mybatisx.Objects;
import com.github.mybatisx.core.expr.builder.AbstractRangeExprBuilder;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

import java.util.Collection;

/**
 * IN条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class ImmediateIn extends AbstractInExpression<String> {

    private static final long serialVersionUID = 6345569461265134215L;

    public ImmediateIn(Criteria<?> criteria, String column, Slot slot, Collection<?> values) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.IN;
        this.matched = Matched.IMMEDIATE;
    }

    public ImmediateIn(String alias, String property, Slot slot, Collection<?> values) {
        this.tableAlias = alias;
        this.column = property;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.IN;
        this.matched = Matched.STANDARD;
    }

    public static ImmediateIn.Builder create() {
        return new ImmediateIn.Builder();
    }

    public static final class Builder extends AbstractRangeExprBuilder<ImmediateIn, String> {

        private Builder() {
        }

        @Override
        public ImmediateIn build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImmediateIn(this.criteria, this.target, this.slot, this.values);
            }
            return null;
        }
    }
}
