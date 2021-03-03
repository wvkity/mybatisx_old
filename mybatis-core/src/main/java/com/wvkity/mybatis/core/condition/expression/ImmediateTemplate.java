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

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.condition.basic.Matched;
import com.wvkity.mybatis.support.condition.criteria.Criteria;
import com.wvkity.mybatis.core.condition.expression.builder.AbstractTemplateExprBuilder;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件表达式
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public class ImmediateTemplate extends AbstractTemplateExpression<String> {

    private static final long serialVersionUID = -415830760117182971L;

    public ImmediateTemplate(Criteria<?> criteria, String column, String template, TemplateMatch match, Slot slot,
                             Object value, Collection<Object> listValues, Map<String, Object> mapValues) {
        this.criteria = criteria;
        this.target = column;
        this.template = template;
        this.match = match;
        this.slot = slot;
        this.value = value;
        this.listValues = listValues;
        this.mapValues = mapValues;
        this.matched = Matched.IMMEDIATE;
    }

    public static ImmediateTemplate.Builder create() {
        return new ImmediateTemplate.Builder();
    }

    public static final class Builder extends AbstractTemplateExprBuilder<ImmediateTemplate, String> {

        public Builder() {
        }

        @Override
        public ImmediateTemplate build() {
            if (Objects.isNotBlank(this.template)) {
                return new ImmediateTemplate(this.criteria, this.target, this.template, this.match, this.slot,
                    this.value, this.listValues, this.mapValues);
            }
            return null;
        }
    }

}
