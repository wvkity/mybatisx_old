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
import com.wvkity.mybatis.core.condition.expression.builder.AbstractExprBuilder;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.metadata.Column;

import java.util.Optional;

/**
 * 小于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class StandardLessThan extends AbstractExpression<Column> {

    private static final long serialVersionUID = -112105594246363693L;

    public StandardLessThan(Criteria<?> criteria, Column column, Slot slot, Object value) {
        this.criteria = criteria;
        this.fragment = column;
        this.slot = slot;
        this.symbol = Symbol.LT;
        this.value = value;
    }

    public static StandardLessThan.Builder create() {
        return new StandardLessThan.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<StandardLessThan, Column> {

        private Builder() {
        }

        @Override
        public StandardLessThan build() {
            return Optional.ofNullable(this.getRealColumn()).map(it ->
                new StandardLessThan(this.criteria, it, this.slot, this.value)).orElse(null);
        }
    }
}
