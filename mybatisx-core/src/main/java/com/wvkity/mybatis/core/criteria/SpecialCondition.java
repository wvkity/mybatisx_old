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
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;

/**
 * 特殊条件(字段相等)
 * @author wvkity
 * @created 2021-04-13
 * @since 1.0.0
 */
public class SpecialCondition implements Criterion {

    private static final long serialVersionUID = -2012877193882997286L;
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

    String getAlias(final Criteria<?> criteria, final String alias) {
        return Objects.isNotBlank(alias) ? alias : Objects.nonNull(criteria) ? criteria.as() : null;
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
