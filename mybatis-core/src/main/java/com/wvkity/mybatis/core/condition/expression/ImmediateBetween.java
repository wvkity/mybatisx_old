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
import com.wvkity.mybatis.core.condition.expression.builder.AbstractImmediateBetweenExprBuilder;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * Between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class ImmediateBetween extends AbstractImmediateBetweenExpression {

    private static final long serialVersionUID = 2476462797500501083L;

    public ImmediateBetween(Criteria<?> criteria, String column, Slot slot, Object begin, Object end) {
        this.criteria = criteria;
        this.target = column;
        this.slot = slot;
        this.symbol = Symbol.BETWEEN;
        this.begin = begin;
        this.end = end;
    }

    public static ImmediateBetween.Builder create() {
        return new ImmediateBetween.Builder();
    }

    public static final class Builder extends AbstractImmediateBetweenExprBuilder<ImmediateBetween> {

        private Builder(){}

        @Override
        public ImmediateBetween build() {
            if (Objects.isNotBlank(this.column)) {
                return new ImmediateBetween(this.criteria, this.column, this.slot, this.begin, this.end);
            }
            return null;
        }
    }
}
