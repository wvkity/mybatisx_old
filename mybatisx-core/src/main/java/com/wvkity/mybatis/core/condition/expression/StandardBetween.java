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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.condition.basic.Matched;
import com.wvkity.mybatis.support.condition.criteria.Criteria;
import com.wvkity.mybatis.core.condition.expression.builder.AbstractBetweenExprBuilder;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;

/**
 * Between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class StandardBetween extends AbstractBetweenExpression<String> {

    private static final long serialVersionUID = -5335171321951491035L;

    public StandardBetween(Criteria<?> criteria, String property, Slot slot, Object begin, Object end) {
        this.criteria = criteria;
        this.target = property;
        this.slot = slot;
        this.symbol = Symbol.BETWEEN;
        this.matched = Matched.STANDARD;
        this.begin = begin;
        this.end = end;
    }

    public static StandardBetween.Builder create() {
        return new StandardBetween.Builder();
    }

    public static final class Builder extends AbstractBetweenExprBuilder<StandardBetween, String> {

        private Builder() {
        }

        @Override
        public StandardBetween build() {
            if (Objects.isNotBlank(this.target)) {
                return new StandardBetween(this.criteria, this.target, this.slot, this.begin, this.end);
            }
            return null;
        }
    }
}
