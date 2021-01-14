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
import com.wvkity.mybatis.core.condition.expression.builder.AbstractBetweenExprBuilder;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * Not between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class ImmediateNotBetween extends AbstractBetweenExpression<String> {

    private static final long serialVersionUID = -3337167513113927777L;

    public ImmediateNotBetween(Criteria<?> criteria, String column, Slot slot, Object begin, Object end) {
        this.criteria = criteria;
        this.fragment = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_BETWEEN;
        this.begin = begin;
        this.end = end;
    }

    public static ImmediateNotBetween.Builder create() {
        return new ImmediateNotBetween.Builder();
    }

    public static final class Builder extends AbstractBetweenExprBuilder<ImmediateNotBetween, String> {

        private Builder(){}

        @Override
        public ImmediateNotBetween build() {
            if (Objects.isNotBlank(this.column)) {
                return new ImmediateNotBetween(this.criteria, this.column, this.slot, this.begin, this.end);
            }
            return null;
        }
    }
}
