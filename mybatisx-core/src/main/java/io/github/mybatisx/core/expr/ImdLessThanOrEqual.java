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
import io.github.mybatisx.core.expr.builder.AbstractBasicExprBuilder;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 小于或等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class ImdLessThanOrEqual extends AbstractBasicExpression<String> {

    private static final long serialVersionUID = -4660945123417961930L;

    public ImdLessThanOrEqual(Criteria<?> criteria, String column, Slot slot, Object value) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.LE;
        this.value = value;
    }

    public ImdLessThanOrEqual(String alias, String column, Slot slot, Object value) {
        this.alias = alias;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.LE;
        this.value = value;
    }

    public static ImdLessThanOrEqual.Builder create() {
        return new ImdLessThanOrEqual.Builder();
    }

    public static final class Builder extends AbstractBasicExprBuilder<ImdLessThanOrEqual, String, Builder> {

        private Builder() {
        }

        @Override
        public ImdLessThanOrEqual build() {
            if (Objects.isNotBlank(this.target)) {
                final ImdLessThanOrEqual it = new ImdLessThanOrEqual(this.criteria, this.target, this.slot,
                    this.value);
                it.alias(this.alias);
                return it;
            }
            return null;
        }


    }
}
