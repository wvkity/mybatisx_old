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
import com.github.mybatisx.basic.metadata.Column;
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
public class StandardNotIn extends AbstractInExpression<Column> {

    private static final long serialVersionUID = 2448932045855706827L;

    public StandardNotIn(Criteria<?> criteria, Column column, Slot slot, Collection<?> values) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.NOT_IN;
        this.matched = Matched.STANDARD;
    }

    public static StandardNotIn.Builder create() {
        return new StandardNotIn.Builder();
    }

    public static final class Builder extends AbstractRangeExprBuilder<StandardNotIn, Column> {

        private Builder() {
        }

        @Override
        public StandardNotIn build() {
            if (Objects.nonNull(this.target)) {
                return new StandardNotIn(this.criteria, this.target, this.slot, this.values);
            }
            return null;
        }
    }
}
