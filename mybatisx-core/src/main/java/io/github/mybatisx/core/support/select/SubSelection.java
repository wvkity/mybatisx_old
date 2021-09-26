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
package io.github.mybatisx.core.support.select;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.criteria.ExtCriteria;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 子查询字段
 * @author wvkity
 * @created 2021-04-17
 * @since 1.0.0
 */
public class SubSelection extends AbstractSelection<String> {

    private static final long serialVersionUID = 4307693761233245536L;

    protected Criteria<?> query;

    public SubSelection(ExtCriteria<?> criteria, ExtCriteria<?> query, final String alias) {
        this.criteria = criteria;
        this.query = query;
        this.alias = alias;
        this.matched = Matched.QUERY;
    }

    @Override
    public String getColumn() {
        if (Objects.nonNull(this.query)) {
            return Constants.BRACKET_OPEN + this.query.getSegment() + Constants.BRACKET_CLOSE;
        }
        return null;
    }

}
