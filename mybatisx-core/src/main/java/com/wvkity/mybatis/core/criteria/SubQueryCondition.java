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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.jsql.parser.SqlParser;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 子查询条件
 * @author wvkity
 * @created 2021-05-05
 * @since 1.0.0
 */
public class SubQueryCondition implements Criterion {

    private static final long serialVersionUID = 636512444753930058L;

    protected final Criteria<?> criteria;
    protected final String tableAlias;
    protected final String column;
    protected final Slot slot;
    protected final Symbol symbol;
    protected final String query;

    public SubQueryCondition(Criteria<?> criteria, String tableAlias, String column, Slot slot, Symbol symbol,
                             String query) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.slot = slot;
        this.symbol = symbol;
        this.query = query;
    }

    /**
     * 获取表别名
     * @return 表别名
     */
    String getAlias() {
        return Objects.nonNull(tableAlias) ? tableAlias : Objects.nonNull(criteria) ? criteria.as() : null;
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder(100);
        if (this.slot != null) {
            builder.append(symbol.getSegment()).append(Constants.SPACE);
        }
        final String alias = this.getAlias();
        if (Objects.isNotBlank(alias)) {
            builder.append(alias).append(Constants.DOT);
        }
        builder.append(column).append(Constants.SPACE).append(this.symbol.getSegment());
        builder.append(Constants.SPACE).append(Constants.BRACKET_OPEN);
        if (this.symbol == Symbol.IN || this.symbol == Symbol.NOT_IN) {
            builder.append(this.query);
        } else {
            final SqlParser parser = new SqlParser();
            builder.append(parser.smartRemoveOrderBy(this.query));
        }
        return builder.append(Constants.BRACKET_CLOSE).toString();
    }
}
