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
import com.github.mybatisx.core.expr.builder.AbstractBasicExprBuilder;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

/**
 * 大于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class ImdGreaterThan extends AbstractBasicExpression<String> {

    private static final long serialVersionUID = -6450458243911624554L;

    public ImdGreaterThan(Criteria<?> criteria, String column, Slot slot, Object value) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.GT;
        this.matched = Matched.IMMEDIATE;
        this.value = value;
    }

    public ImdGreaterThan(String alias, String column, Slot slot, Object value) {
        this.tableAlias = alias;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.GT;
        this.matched = Matched.IMMEDIATE;
        this.value = value;
    }

    public static ImdGreaterThan.Builder create() {
        return new ImdGreaterThan.Builder();
    }

    public static final class Builder extends AbstractBasicExprBuilder<ImdGreaterThan, String, Builder> {

        private Builder() {
        }

        @Override
        public ImdGreaterThan build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImdGreaterThan(this.criteria, this.target, this.slot, this.value);
            }
            return null;
        }


    }
}
