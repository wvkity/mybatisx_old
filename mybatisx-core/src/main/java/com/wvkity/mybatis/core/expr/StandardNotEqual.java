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
package com.wvkity.mybatis.core.expr;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.core.expr.builder.AbstractBasicExprBuilder;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;

/**
 * 不等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class StandardNotEqual extends AbstractBasicExpression<String> {

    private static final long serialVersionUID = -8879802952196072736L;

    public StandardNotEqual(Criteria<?> criteria, String property, Slot slot, Object value) {
        this.criteria = criteria;
        this.target = property;
        this.slot = slot;
        this.symbol = Symbol.NE;
        this.matched = Matched.STANDARD;
        this.value = value;
    }

    public static StandardNotEqual.Builder create() {
        return new StandardNotEqual.Builder();
    }

    public static final class Builder extends AbstractBasicExprBuilder<StandardNotEqual, String> {

        private Builder() {
        }

        @Override
        public StandardNotEqual build() {
            if (Objects.isNotBlank(this.target)) {
                return new StandardNotEqual(this.criteria, this.target, this.slot, this.value);
            }
            return null;
        }
    }
}
