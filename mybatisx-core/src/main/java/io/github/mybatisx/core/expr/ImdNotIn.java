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
package io.github.mybatisx.core.expr;

import io.github.mybatisx.Objects;
import io.github.mybatisx.core.expr.builder.AbstractRangeExprBuilder;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

import java.util.Collection;

/**
 * NOT IN条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class ImdNotIn extends AbstractInExpression<String> {

    private static final long serialVersionUID = 893533913883915181L;

    public ImdNotIn(Criteria<?> criteria, String column, Slot slot, Collection<?> values) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.NOT_IN;
        this.matched = Matched.IMMEDIATE;
    }

    public ImdNotIn(String alias, String column, Slot slot, Collection<?> values) {
        this.alias = alias;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.NOT_IN;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImdNotIn.Builder create() {
        return new ImdNotIn.Builder();
    }

    public static final class Builder extends AbstractRangeExprBuilder<ImdNotIn, String, Builder> {

        private Builder() {
        }

        @Override
        public ImdNotIn build() {
            if (Objects.isNotBlank(this.target)) {
                final ImdNotIn it = new ImdNotIn(this.criteria, this.target, this.slot, this.values);
                it.alias(this.alias);
                return it;
            }
            return null;
        }
    }
}
