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
package com.github.mybatisx.core.expr;

import com.github.mybatisx.Objects;
import com.github.mybatisx.core.expr.builder.AbstractFuzzyExprBuilder;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Like;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

/**
 * Not Like模糊匹配条件表达式
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class ImdNotLike extends AbstractLikeExpression<String> {

        private static final long serialVersionUID = -2947044664048234865L;

    public ImdNotLike(Criteria<?> criteria, String column, Like like,
                      Character escape, Slot slot, Object value) {
        this.criteria = criteria;
        this.column = column;
        this.like = like;
        this.escape = escape;
        this.slot = slot;
        this.value = value;
        this.symbol = Symbol.LIKE;
        this.matched = Matched.IMMEDIATE;
    }

    public ImdNotLike(String alias, String column, Like like,
                      Character escape, Slot slot, Object value) {
        this.tableAlias = alias;
        this.column = column;
        this.like = like;
        this.escape = escape;
        this.slot = slot;
        this.value = value;
        this.symbol = Symbol.LIKE;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImdNotLike.Builder create() {
        return new ImdNotLike.Builder();
    }

    public static final class Builder extends AbstractFuzzyExprBuilder<ImdNotLike, String, Builder> {

        private Builder() {
        }

        @Override
        public ImdNotLike build() {
            if (Objects.isNotBlank(this.target)) {
                return new ImdNotLike(this.criteria, this.target, this.like, this.escape, this.slot, this.value);
            }
            return null;
        }
    }
}
