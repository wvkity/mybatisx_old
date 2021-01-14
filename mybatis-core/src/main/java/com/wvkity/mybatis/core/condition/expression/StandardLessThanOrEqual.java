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
 * 小于或等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class StandardLessThanOrEqual extends AbstractExpression<Column> {

    private static final long serialVersionUID = 1562680877348547293L;

    public StandardLessThanOrEqual(Criteria<?> criteria, Column column, Slot slot, Object value) {
        this.criteria = criteria;
        this.fragment = column;
        this.slot = slot;
        this.symbol = Symbol.LE;
        this.value = value;
    }

    public static StandardLessThanOrEqual.Builder create() {
        return new StandardLessThanOrEqual.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<StandardLessThanOrEqual, Column> {

        private Builder() {
        }

        @Override
        public StandardLessThanOrEqual build() {
            return Optional.ofNullable(this.getRealColumn()).map(it ->
                new StandardLessThanOrEqual(this.criteria, it, this.slot, this.value)).orElse(null);
        }
    }
}
