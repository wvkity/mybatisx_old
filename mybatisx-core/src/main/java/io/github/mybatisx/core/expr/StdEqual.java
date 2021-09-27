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
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.core.expr.builder.AbstractBasicExprBuilder;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class StdEqual extends AbstractBasicExpression<Column> {

    private static final long serialVersionUID = -2049663458291206062L;

    public StdEqual(Criteria<?> criteria, Column column, Slot slot, Object value) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.EQ;
        this.matched = Matched.STANDARD;
        this.value = value;
    }

    public static StdEqual.Builder create() {
        return new StdEqual.Builder();
    }

    public static final class Builder extends AbstractBasicExprBuilder<StdEqual, Column, Builder> {

        private Builder() {
        }

        @Override
        public StdEqual build() {
            if (Objects.nonNull(this.target)) {
                return new StdEqual(this.criteria, this.target, this.slot, this.value);
            }
            return null;
        }

    }
}