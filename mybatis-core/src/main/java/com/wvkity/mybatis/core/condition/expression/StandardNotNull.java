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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.condition.basic.Matched;
import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.condition.expression.builder.AbstractExprBuilder;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * NOT NULL条件表达式
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class StandardNotNull extends AbstractNullableExpression<String> {

    private static final long serialVersionUID = 8443593657388476364L;

    public StandardNotNull(Criteria<?> criteria, String property, Slot slot) {
        this.criteria = criteria;
        this.target = property;
        this.slot = slot;
        this.symbol = Symbol.NOT_NULL;
        this.matched = Matched.STANDARD;
    }

    public static StandardNotNull.Builder create() {
        return new StandardNotNull.Builder();
    }

    public static final class Builder extends AbstractExprBuilder<StandardNotNull, String> {

        private Builder() {
        }

        @Override
        public StandardNotNull build() {
            if (Objects.isNotBlank(this.target)) {
                return new StandardNotNull(this.criteria, this.target, this.slot);
            }
            return null;
        }
    }
}
