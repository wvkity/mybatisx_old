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

import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.expr.builder.AbstractBetweenExprBuilder;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * Not between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class StandardNotBetween extends AbstractBetweenExpression<Column> {

    private static final long serialVersionUID = -5107081321447906415L;

    public StandardNotBetween(Criteria<?> criteria, Column column, Slot slot, Object begin, Object end) {
        this.criteria = criteria;
        this.column = column;
        this.slot = slot;
        this.symbol = Symbol.NOT_BETWEEN;
        this.matched = Matched.STANDARD;
        this.begin = begin;
        this.end = end;
    }

    public static StandardNotBetween.Builder create() {
        return new StandardNotBetween.Builder();
    }

    public static final class Builder extends AbstractBetweenExprBuilder<StandardNotBetween, Column> {

        private Builder() {
        }

        @Override
        public StandardNotBetween build() {
            if (Objects.nonNull(this.target)) {
                return new StandardNotBetween(this.criteria, this.target, this.slot, this.begin, this.end);
            }
            return null;
        }
    }
}
