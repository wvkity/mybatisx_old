/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.wvkity.mybatis.core.condition;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.jsql.parser.SqlParser;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 子查询条件
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public class SubQueryCondition implements Criterion {

    private static final long serialVersionUID = 5568153904171125467L;
    private static final Pattern DEF_REGEX_ORDER_BY = Pattern.compile("^(?i)(.*(\\s+order\\s+by\\s+)(.*))$",
        Pattern.CASE_INSENSITIVE);
    protected final Criteria<?> criteria;
    protected final String alias;
    protected final String column;
    protected final Slot slot;
    protected final Symbol symbol;
    protected final String query;

    public SubQueryCondition(Criteria<?> criteria, String alias, String column, Slot slot, Symbol symbol,
                             String query) {
        this.criteria = criteria;
        this.alias = alias;
        this.column = column;
        this.slot = slot;
        this.symbol = symbol;
        this.query = query;
    }

    protected Optional<Criteria<?>> optional() {
        return Optional.ofNullable(this.criteria);
    }

    protected String getAlias() {
        return Objects.nonNull(this.alias) ? this.alias : optional().map(Criteria::as).orElse(Constants.EMPTY);
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder(145);
        if (this.slot != null) {
            builder.append(symbol.getSegment()).append(Constants.SPACE);
        }
        final String alias = this.getAlias();
        if (Objects.isNotBlank(alias)) {
            builder.append(alias).append(Constants.DOT);
        }
        builder.append(column).append(Constants.SPACE).append(this.symbol.getSegment());
        builder.append(Constants.SPACE).append(Constants.BRACKET_OPEN);
        if (DEF_REGEX_ORDER_BY.matcher(this.query).matches()) {
            final SqlParser parser = new SqlParser();
            builder.append(parser.smartRemoveOrderBy(this.query));
        } else {
            builder.append(this.query);
        }
        return builder.append(Constants.BRACKET_CLOSE).toString();
    }
}
