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
import com.wvkity.mybatis.core.condition.expression.builder.AbstractBasicExprBuilder;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;

/**
 * 不等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class ImmediateNotEqual extends AbstractBasicExpression<String> {

    private static final long serialVersionUID = -7062776323830921155L;

    public ImmediateNotEqual(Criteria<?> criteria, String column, Slot slot, Object value) {
        this.criteria = criteria;
        this.target = column;
        this.slot = slot;
        this.symbol = Symbol.NE;
        this.matched = Matched.IMMEDIATE;
        this.value = value;
    }

    public static ImmediateNotEqual.Builder create() {
        return new ImmediateNotEqual.Builder();
    }

    public static final class Builder extends AbstractBasicExprBuilder<ImmediateNotEqual, String> {

        private Builder() {
        }

        public ImmediateNotEqual build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImmediateNotEqual(this.criteria, this.target, this.slot, this.value);
            }
            return null;
        }
    }
}
