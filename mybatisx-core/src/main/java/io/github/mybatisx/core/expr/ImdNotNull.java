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
package io.github.mybatisx.core.expr;

import io.github.mybatisx.Objects;
import io.github.mybatisx.core.expr.builder.AbstractExprBuilder;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * NOT NULL条件表达式
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class ImdNotNull extends AbstractNullableExpression<String> {

    private static final long serialVersionUID = 3206146356985090856L;

    public ImdNotNull(Criteria<?> criteria, String column, Slot slot) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_NULL;
        this.matched = Matched.IMMEDIATE;
    }

    public ImdNotNull(String alias, String column, Slot slot) {
        this.alias = alias;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_NULL;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImdNotNull.Builder create() {
        return new ImdNotNull.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<ImdNotNull, String, Builder> {

        private Builder() {
        }

        @Override
        public ImdNotNull build() {
            if (Objects.isNotBlank(this.target)) {
                final ImdNotNull it = new ImdNotNull(this.criteria, this.target, this.slot);
                it.alias(this.alias);
                return it;
            }
            return null;
        }
    }
}
