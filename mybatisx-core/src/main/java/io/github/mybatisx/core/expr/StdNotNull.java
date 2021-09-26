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
import io.github.mybatisx.basic.metadata.Column;
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
public class StdNotNull extends AbstractNullableExpression<Column> {

    private static final long serialVersionUID = 8443593657388476364L;

    public StdNotNull(Criteria<?> criteria, Column column, Slot slot) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_NULL;
        this.matched = Matched.STANDARD;
    }

    public static StdNotNull.Builder create() {
        return new StdNotNull.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<StdNotNull, Column, Builder> {

        private Builder() {
        }

        @Override
        public StdNotNull build() {
            if (Objects.nonNull(this.target)) {
                return new StdNotNull(this.criteria, this.target, this.slot);
            }
            return null;
        }
    }
}
