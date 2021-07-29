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
public class ImdIn extends AbstractInExpression<String> {

    private static final long serialVersionUID = 6345569461265134215L;

    public ImdIn(Criteria<?> criteria, String column, Slot slot, Collection<?> values) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.IN;
        this.matched = Matched.IMMEDIATE;
    }

    public ImdIn(String alias, String property, Slot slot, Collection<?> values) {
        this.alias = alias;
        this.column = property;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.IN;
        this.matched = Matched.STANDARD;
    }

    public static ImdIn.Builder create() {
        return new ImdIn.Builder();
    }

    public static final class Builder extends AbstractRangeExprBuilder<ImdIn, String, Builder> {

        private Builder() {
        }

        @Override
        public ImdIn build() {
            if (Objects.isNotBlank(this.target)) {
                final ImdIn it = new ImdIn(this.criteria, this.target, this.slot, this.values);
                it.alias(this.alias);
                return it;
            }
            return null;
        }
    }
}
