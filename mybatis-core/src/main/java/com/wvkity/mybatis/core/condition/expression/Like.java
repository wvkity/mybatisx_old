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

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.condition.expression.builder.AbstractFuzzyExprBuilder;
import com.wvkity.mybatis.core.constant.LikeMode;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.metadata.Column;

import java.util.Optional;

/**
 * Like模糊匹配条件表达式
 * @author wvkity
 * @created 2021-01-08
 * @since 1.0.0
 */
public class Like extends AbstractFuzzyExpression {

    private static final long serialVersionUID = -7938299272126062419L;

    public Like(Criteria<?> criteria, Column column, LikeMode mode, Character escape, Slot slot, Object value) {
        this.criteria = criteria;
        this.target = column;
        this.mode = mode;
        this.escape = escape;
        this.slot = slot;
        this.value = value;
        this.symbol = Symbol.LIKE;
    }

    public static Like.Builder create() {
        return new Like.Builder();
    }

    public static final class Builder extends AbstractFuzzyExprBuilder<Like> {

        private Builder() {
        }

        @Override
        public Like build() {
            return Optional.ofNullable(this.getRealColumn()).map(it ->
                new Like(this.criteria, it, this.mode, this.escape, this.slot, this.value)).orElse(null);
        }
    }
}
