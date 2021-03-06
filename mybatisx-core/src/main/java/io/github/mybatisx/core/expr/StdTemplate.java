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
import io.github.mybatisx.PlaceholderPattern;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.core.expr.builder.AbstractTemplateExprBuilder;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件表达式
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public class StdTemplate extends AbstractTemplateExpression<Column> {

    private static final long serialVersionUID = -3342020710178444452L;

    public StdTemplate(Criteria<?> criteria, Column column, String template, PlaceholderPattern pattern,
                       Slot slot, Object value, Collection<Object> listValues, Map<String, Object> mapValues) {
        this.criteria = criteria;
        this.column = column;
        this.template = template;
        this.pattern = pattern;
        this.slot = slot;
        this.value = value;
        this.listValues = listValues;
        this.mapValues = mapValues;
        this.matched = Matched.STANDARD;
        this.symbol = Symbol.TPL;
    }

    public static StdTemplate.Builder create() {
        return new StdTemplate.Builder();
    }

    public static final class Builder extends AbstractTemplateExprBuilder<StdTemplate, Column, Builder> {

        public Builder() {
        }

        @Override
        public StdTemplate build() {
            if (Objects.isNotBlank(this.template) && Objects.nonNull(this.target)) {
                return new StdTemplate(this.criteria, this.target, this.template, this.pattern,
                    this.slot, this.value, this.listValues, this.mapValues);
            }
            return null;
        }
    }

}
