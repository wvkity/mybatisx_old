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
package io.github.mybatisx.core.expr;

import io.github.mybatisx.Objects;
import io.github.mybatisx.RegexMatcher;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 纯SQL
 * @author wvkity
 * @created 2021-01-16
 * @since 1.0.0
 */
public class NativeExpression extends AbstractExpression<String> {

    private static final long serialVersionUID = 1802248293107856419L;

    /**
     * 条件片段
     */
    private final String criterion;

    public NativeExpression(String criterion) {
        this.criterion = criterion;
        this.matched = Matched.IMMEDIATE;
    }

    public NativeExpression(String criterion, final Slot slot) {
        this.criterion = criterion;
        this.matched = Matched.IMMEDIATE;
        this.slot = slot;
    }

    public String getCriterion() {
        if (RegexMatcher.startWithAndOr(this.criterion)) {
            return this.criterion;
        }
        return Objects.isNull(this.slot) ? Slot.AND.getSegment() :
            this.slot.getSegment() + Constants.SPACE + this.criterion;
    }

    public static NativeExpression of(final String criterion) {
        if (Objects.isNotBlank(criterion)) {
            return new NativeExpression(criterion);
        }
        return null;
    }

    @Override
    public NativeExpression setIfNecessary(Criteria<?> criteria) {
        return this;
    }
}
