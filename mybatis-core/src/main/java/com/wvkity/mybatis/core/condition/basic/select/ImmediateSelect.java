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
package com.wvkity.mybatis.core.condition.basic.select;

import com.wvkity.mybatis.core.condition.basic.builder.AbstractSelectBuilder;
import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * 基础查询列
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class ImmediateSelect extends AbstractSelect<String> {

    private static final long serialVersionUID = 7438668171970613439L;

    public ImmediateSelect(Criteria<?> criteria, String column, String tableAlias, String alias) {
        this.criteria = criteria;
        this.column = column;
        this.tableAlias = tableAlias;
        this.alias = alias;
    }

    @Override
    public String getColumn() {
        return this.column;
    }

    @Override
    public String getSegment(boolean isQuery) {
        final String tabAlias = Objects.isBlank(this.tableAlias) ?
            (Objects.nonNull(criteria) ? criteria.as() : null) : this.tableAlias;
        return Scripts.convertToSelectArg(tabAlias, this.column, isQuery ? this.alias : null);
    }

    public static ImmediateSelect.Builder create() {
        return new ImmediateSelect.Builder();
    }

    public static final class Builder extends AbstractSelectBuilder<ImmediateSelect, String> {

        private Builder() {
        }

        @Override
        public ImmediateSelect build() {
            if (Objects.isNotBlank(this.column)) {
                return new ImmediateSelect(this.criteria, this.column, this.tableAlias, alias);
            }
            return null;
        }
    }
}
