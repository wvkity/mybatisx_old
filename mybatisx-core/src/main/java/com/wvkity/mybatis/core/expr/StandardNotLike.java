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
 * Not Like模糊匹配条件表达式
 * @author wvkity
 * @created 2021-01-08
 * @since 1.0.0
 */
public class StandardNotLike extends AbstractFuzzyExpression<String> {

    private static final long serialVersionUID = 5881853809637635749L;

    public StandardNotLike(Criteria<?> criteria, String property, Like mode,
                           Character escape, Slot slot, Object value) {
        this.criteria = criteria;
        this.target = property;
        this.like = mode;
        this.escape = escape;
        this.slot = slot;
        this.value = value;
        this.symbol = Symbol.NOT_LIKE;
        this.matched = Matched.STANDARD;
    }

    public static StandardNotLike.Builder create() {
        return new StandardNotLike.Builder();
    }

    public static final class Builder extends AbstractFuzzyExprBuilder<StandardNotLike, String> {

        private Builder() {
        }

        @Override
        public StandardNotLike build() {
            if (Objects.isNotBlank(this.target)) {
                return new StandardNotLike(this.criteria, this.target, this.like, this.escape, this.slot, this.value);
            }
            return null;
        }
    }
}
