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
import com.github.mybatisx.core.expr.builder.AbstractBetweenExprBuilder;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

/**
 * Not between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class ImmediateNotBetween extends AbstractBetweenExpression<String> {

    private static final long serialVersionUID = -3337167513113927777L;

    public ImmediateNotBetween(Criteria<?> criteria, String column, Slot slot, Object begin, Object end) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_BETWEEN;
        this.matched = Matched.IMMEDIATE;
        this.begin = begin;
        this.end = end;
    }

    public ImmediateNotBetween(String alias, String column, Slot slot, Object begin, Object end) {
        this.tableAlias = alias;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_BETWEEN;
        this.matched = Matched.IMMEDIATE;
        this.begin = begin;
        this.end = end;
    }

    public static ImmediateNotBetween.Builder create() {
        return new ImmediateNotBetween.Builder();
    }

    public static final class Builder extends AbstractBetweenExprBuilder<ImmediateNotBetween, String> {

        private Builder() {
        }

        @Override
        public ImmediateNotBetween build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImmediateNotBetween(this.criteria, this.target, this.slot, this.begin, this.end);
            }
            return null;
        }
    }
}