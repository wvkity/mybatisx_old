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
import com.wvkity.mybatis.core.condition.expression.builder.AbstractFuzzyExprBuilder;
import com.wvkity.mybatis.core.constant.LikeMode;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * Like模糊匹配条件表达式
 * @author wvkity
 * @created 2021-01-08
 * @since 1.0.0
 */
public class StandardLike extends AbstractFuzzyExpression<String> {

    private static final long serialVersionUID = -7938299272126062419L;

    public StandardLike(Criteria<?> criteria, String property, LikeMode mode, Character escape, Slot slot, Object value) {
        this.criteria = criteria;
        this.target = property;
        this.mode = mode;
        this.escape = escape;
        this.slot = slot;
        this.value = value;
        this.symbol = Symbol.LIKE;
        this.matched = Matched.STANDARD;
    }

    public static StandardLike.Builder create() {
        return new StandardLike.Builder();
    }

    public static final class Builder extends AbstractFuzzyExprBuilder<StandardLike, String> {

        private Builder() {
        }

        @Override
        public StandardLike build() {
            if (Objects.isNotBlank(this.target)) {
                return new StandardLike(this.criteria, this.target, this.mode, this.escape, this.slot, this.value);
            }
            return null;
        }
    }
}
