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
package com.github.mybatisx.core.condition;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.jsql.parser.SqlParser;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

/**
 * exists条件
 * @author wvkity
 * @created 2021-05-02
 * @since 1.0.0
 */
public class ExistsCondition implements Criterion {

    private static final long serialVersionUID = -8139298333029727904L;
    private final SqlParser parser;
    private final Criteria<?> criteria;
    private final Symbol symbol;
    private final Slot slot;

    public ExistsCondition(Criteria<?> criteria, Symbol symbol, Slot slot) {
        this.parser = new SqlParser();
        this.criteria = criteria;
        this.symbol = symbol;
        this.slot = slot;
    }

    @Override
    public String getSegment() {
        return this.slot.getSegment() + Constants.SPACE + this.symbol.getSegment() + Constants.SPACE +
            Constants.BRACKET_OPEN + this.parser.smartExistsParse(this.criteria.getSegment()) + Constants.BRACKET_CLOSE;
    }
}
