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
import com.wvkity.mybatis.core.expr.builder.AbstractFuzzyExprBuilder;
import com.wvkity.mybatis.support.constant.Like;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;

/**
 * Like模糊匹配条件表达式
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class ImmediateLike extends AbstractFuzzyExpression<String> {

    private static final long serialVersionUID = 437732395610721845L;

    public ImmediateLike(Criteria<?> criteria, String column, Like like,
                         Character escape, Slot slot, Object value) {
        this.criteria = criteria;
        this.target = column;
        this.like = like;
        this.escape = escape;
        this.slot = slot;
        this.value = value;
        this.symbol = Symbol.LIKE;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImmediateLike.Builder create() {
        return new ImmediateLike.Builder();
    }

    public static final class Builder extends AbstractFuzzyExprBuilder<ImmediateLike, String> {

        private Builder() {
        }

        @Override
        public ImmediateLike build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImmediateLike(this.criteria, this.target, this.like, this.escape, this.slot, this.value);
            }
            return null;
        }
    }
}