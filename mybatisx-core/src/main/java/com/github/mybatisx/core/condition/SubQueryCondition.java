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
package com.github.mybatisx.core.condition;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.jsql.parser.SqlParser;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

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
    protected final Criteria<?> query;

    public SubQueryCondition(Criteria<?> criteria, String alias, String column, Slot slot, Symbol symbol,
                             Criteria<?> query) {
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
        if (this.slot != null && this.slot != Slot.NONE) {
            builder.append(this.slot.getSegment()).append(Constants.SPACE);
        }
        final String alias = this.getAlias();
        if (Objects.isNotBlank(alias)) {
            builder.append(alias).append(Constants.DOT);
        }
        builder.append(this.column).append(Constants.SPACE).append(this.symbol.getSegment());
        builder.append(Constants.SPACE).append(Constants.BRACKET_OPEN);
        final String sql = this.query.getSegment();
        if (DEF_REGEX_ORDER_BY.matcher(sql).matches()) {
            final SqlParser parser = new SqlParser();
            builder.append(parser.smartRemoveOrderBy(sql));
        } else {
            builder.append(sql);
        }
        return builder.append(Constants.BRACKET_CLOSE).toString();
    }
}
