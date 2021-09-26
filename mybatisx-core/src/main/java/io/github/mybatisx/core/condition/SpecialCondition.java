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
package io.github.mybatisx.core.condition;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 特殊条件(字段相等)
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public class SpecialCondition implements Criterion {

    private static final long serialVersionUID = -3226684503336166627L;
    private final Criteria<?> criteria;
    private final String tableAlias;
    private final String column;
    private final Criteria<?> otherCriteria;
    private final String otherTableAlias;
    private final String otherColumn;
    private final Symbol symbol;
    private final Slot slot;

    public SpecialCondition(Criteria<?> criteria, String tableAlias, String column, Criteria<?> otherCriteria,
                            String otherTableAlias, String otherColumn, Symbol symbol, Slot slot) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = column;
        this.otherCriteria = otherCriteria;
        this.otherTableAlias = otherTableAlias;
        this.otherColumn = otherColumn;
        this.symbol = Objects.isNull(symbol) ? Symbol.EQ : symbol;
        this.slot = slot;
    }

    protected String getAlias(final Criteria<?> criteria, final String alias) {
        return Objects.isNotBlank(alias) ? alias : Objects.nonNull(criteria) ? criteria.as() : null;
    }

    @Override
    public Symbol getSymbol() {
        return this.symbol;
    }

    @Override
    public String getColumn() {
        return this.column;
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder(60);
        if (Objects.nonNull(this.slot)) {
            builder.append(this.slot.getSegment()).append(Constants.SPACE);
        }
        final String as = this.getAlias(this.criteria, this.tableAlias);
        final String oas = this.getAlias(this.otherCriteria, this.otherTableAlias);
        if (Objects.isNotBlank(as)) {
            builder.append(as).append(Constants.DOT);
        }
        builder.append(this.column).append(Constants.SPACE).append(this.symbol.getSegment()).append(Constants.SPACE);
        if (Objects.isNotBlank(oas)) {
            builder.append(oas).append(Constants.DOT);
        }
        builder.append(this.otherColumn);
        return builder.toString();
    }
}
