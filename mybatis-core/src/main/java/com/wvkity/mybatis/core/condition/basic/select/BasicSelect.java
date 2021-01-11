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

import com.wvkity.mybatis.core.condition.basic.builder.AbstractColumnSelectBuilder;
import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Optional;

/**
 * 基础查询字段
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class BasicSelect extends AbstractSelect<Column> {

    private static final long serialVersionUID = -4005247598064881575L;

    public BasicSelect(Criteria<?> criteria, Column column, String tableAlias, String alias) {
        this.criteria = criteria;
        this.column = column;
        this.tableAlias = tableAlias;
        this.alias = alias;
    }

    /**
     * 获取属性名
     * @return 属性名
     */
    public String getProperty() {
        return this.column.getProperty();
    }

    @Override
    public String getColumn() {
        return this.column.getColumn();
    }

    @Override
    public String getSegment(boolean isQuery) {
        final String tabAlias = Objects.isBlank(this.tableAlias) ? this.criteria.as() : this.tableAlias;
        if (Objects.isNotBlank(this.alias)) {
            return Scripts.convertToSelectArg(tabAlias, this.column.getColumn(), this.alias);
        } else {
            return Scripts.convertToSelectArg(tabAlias, column, null, isQuery && this.criteria.isUsePropertyAsAlias());
        }
    }

    public static BasicSelect.Builder create() {
        return new BasicSelect.Builder();
    }

    public static final class Builder extends AbstractColumnSelectBuilder<BasicSelect> {

        private Builder() {
        }

        @Override
        public BasicSelect build() {
            return Optional.ofNullable(this.getColumn()).map(it ->
                new BasicSelect(this.criteria, it, this.tableAlias, this.alias)).orElse(null);
        }
    }
}
