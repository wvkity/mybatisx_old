/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
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
package com.wvkity.mybatis.core.expr;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.core.expr.builder.AbstractExprBuilder;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;

/**
 * NULL条件表达式
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class ImmediateNotNull extends AbstractNullableExpression<String> {

    private static final long serialVersionUID = 3206146356985090856L;

    public ImmediateNotNull(Criteria<?> criteria, String column, Slot slot) {
        this.criteria = criteria;
        this.target = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_NULL;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImmediateNotNull.Builder create() {
        return new ImmediateNotNull.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<ImmediateNotNull, String> {

        private Builder() {
        }

        @Override
        public ImmediateNotNull build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImmediateNotNull(this.criteria, this.target, this.slot);
            }
            return null;
        }
    }
}
