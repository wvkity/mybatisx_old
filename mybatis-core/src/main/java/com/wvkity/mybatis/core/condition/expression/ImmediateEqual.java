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
import com.wvkity.mybatis.core.utils.Objects;

/**
 * 等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class ImmediateEqual extends AbstractExpression<String> {

    private static final long serialVersionUID = -6607630163120844012L;

    public ImmediateEqual(Criteria<?> criteria, String column, Slot slot, Object value) {
        this.criteria = criteria;
        this.fragment = column;
        this.slot = slot;
        this.symbol = Symbol.EQ;
        this.value = value;
    }

    public static ImmediateEqual.Builder create() {
        return new ImmediateEqual.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<ImmediateEqual, String> {

        private Builder() {
        }

        @Override
        public ImmediateEqual build() {
            if (Objects.isNotBlank(this.column)) {
                return new ImmediateEqual(this.criteria, this.column, this.slot, this.value);
            }
            return null;
        }

    }
}
