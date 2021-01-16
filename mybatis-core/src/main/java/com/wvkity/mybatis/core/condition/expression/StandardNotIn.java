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

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.condition.expression.builder.AbstractRangeExprBuilder;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.metadata.Column;

import java.util.Collection;
import java.util.Optional;

/**
 * NOT IN条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class StandardNotIn extends AbstractRangeExpression<Column> {

    private static final long serialVersionUID = 2448932045855706827L;

    public StandardNotIn(Criteria<?> criteria, Column column, Slot slot, Collection<Object> values) {
        this.criteria = criteria;
        this.fragment = column;
        this.slot = slot;
        this.values = values;
        this.symbol = Symbol.NOT_IN;
    }

    public static StandardNotIn.Builder create() {
        return new StandardNotIn.Builder();
    }

    public static final class Builder extends AbstractRangeExprBuilder<StandardNotIn, Column> {

        private Builder() {
        }

        @Override
        public StandardNotIn build() {
            return Optional.ofNullable(this.getRealColumn()).map(it ->
                new StandardNotIn(this.criteria, it, this.slot, this.values)).orElse(null);
        }
    }
}