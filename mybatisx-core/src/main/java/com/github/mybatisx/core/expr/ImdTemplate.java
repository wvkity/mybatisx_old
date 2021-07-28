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
import com.github.mybatisx.PlaceholderPattern;
import com.github.mybatisx.core.expr.builder.AbstractTemplateExprBuilder;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.criteria.Criteria;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件表达式
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public class ImdTemplate extends AbstractTemplateExpression<String> {

    private static final long serialVersionUID = -415830760117182971L;

    public ImdTemplate(Criteria<?> criteria, String column, String template, PlaceholderPattern pattern,
                       Slot slot, Object value, Collection<Object> listValues, Map<String, Object> mapValues) {
        this.criteria = criteria;
        this.column = column;
        this.template = template;
        this.pattern = pattern;
        this.slot = slot;
        this.value = value;
        this.listValues = listValues;
        this.mapValues = mapValues;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImdTemplate.Builder create() {
        return new ImdTemplate.Builder();
    }

    public static final class Builder extends AbstractTemplateExprBuilder<ImdTemplate, String> {

        public Builder() {
        }

        @Override
        public ImdTemplate build() {
            if (Objects.isNotBlank(this.template)) {
                return new ImdTemplate(this.criteria, this.target, this.template, this.pattern, this.slot,
                    this.value, this.listValues, this.mapValues);
            }
            return null;
        }
    }

}
