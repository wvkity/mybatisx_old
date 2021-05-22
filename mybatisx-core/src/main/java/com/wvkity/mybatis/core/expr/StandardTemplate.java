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

import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.core.expr.builder.AbstractTemplateExprBuilder;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件表达式
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public class StandardTemplate extends AbstractTemplateExpression<Column> {

    private static final long serialVersionUID = -3342020710178444452L;

    public StandardTemplate(Criteria<?> criteria, Column column, String template, TemplateMatch match, Slot slot,
                            Object value, Collection<Object> listValues, Map<String, Object> mapValues) {
        this.criteria = criteria;
        this.column = column;
        this.template = template;
        this.match = match;
        this.slot = slot;
        this.value = value;
        this.listValues = listValues;
        this.mapValues = mapValues;
        this.matched = Matched.STANDARD;
        this.symbol = Symbol.TPL;
    }

    public static StandardTemplate.Builder create() {
        return new StandardTemplate.Builder();
    }

    public static final class Builder extends AbstractTemplateExprBuilder<StandardTemplate, Column> {

        public Builder() {
        }

        @Override
        public StandardTemplate build() {
            if (Objects.isNotBlank(this.template) && Objects.nonNull(this.target)) {
                return new StandardTemplate(this.criteria, this.target, this.template, this.match,
                    this.slot, this.value, this.listValues, this.mapValues);
            }
            return null;
        }
    }

}
