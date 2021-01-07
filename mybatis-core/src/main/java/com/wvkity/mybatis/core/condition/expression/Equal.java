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
import com.wvkity.mybatis.core.condition.expression.builder.AbstractColumnExprBuilder;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.metadata.Column;

import java.util.Optional;

/**
 * 等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class Equal extends BasicExpression {

    private static final long serialVersionUID = -2049663458291206062L;

    public Equal(Criteria<?> criteria, Column column, Slot slot, Object value) {
        super(criteria, column, Symbol.EQ, slot, value);
    }

    public static Equal.Builder create() {
        return new Equal.Builder();
    }

    public static final class Builder extends AbstractColumnExprBuilder<Equal> {

        private Builder() {
        }

        @Override
        public Equal build() {
            return Optional.ofNullable(this.getRealColumn()).map(it ->
                new Equal(this.criteria, it, this.slot, this.value)).orElse(null);
        }

    }
}
