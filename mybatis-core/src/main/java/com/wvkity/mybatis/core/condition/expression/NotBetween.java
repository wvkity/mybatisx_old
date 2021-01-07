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
import com.wvkity.mybatis.core.metadata.Column;

import java.util.Optional;

/**
 * Not between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class NotBetween extends AbstractBetweenExpression {

    private static final long serialVersionUID = -5107081321447906415L;

    public NotBetween(Criteria<?> criteria, Column column, Slot slot, Object begin, Object end) {
        this.criteria = criteria;
        this.target = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_BETWEEN;
        this.begin = begin;
        this.end = end;
    }

    public static NotBetween.Builder create() {
        return new NotBetween.Builder();
    }

    public static final class Builder extends AbstractBetweenExprBuilder<NotBetween> {

        private Builder() {
        }

        @Override
        public NotBetween build() {
            return Optional.ofNullable(this.getRealColumn()).map(it ->
                new NotBetween(this.criteria, it, this.slot, this.begin, this.end)).orElse(null);
        }
    }
}
