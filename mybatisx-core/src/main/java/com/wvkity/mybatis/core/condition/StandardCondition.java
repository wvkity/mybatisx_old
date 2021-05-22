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
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Optional;

/**
 * 条件
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public class StandardCondition implements Criterion {

    private static final long serialVersionUID = 5051695488350975635L;
    protected final Criteria<?> criteria;
    protected final String alias;
    protected final String column;
    protected final String fragment;

    public StandardCondition(Criteria<?> criteria, String alias, String column, String fragment) {
        this.criteria = criteria;
        this.alias = alias;
        this.column = column;
        this.fragment = fragment;
    }

    protected Optional<Criteria<?>> optional() {
        return Optional.ofNullable(this.criteria);
    }

    protected String getAlias() {
        return Objects.nonNull(this.alias) ? this.alias : this.optional().map(Criteria::as).orElse(Constants.EMPTY);
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder(30);
        final String as = this.getAlias();
        if (Objects.isNotBlank(as)) {
            builder.append(as).append(Constants.DOT);
        }
        if (Objects.isNotBlank(this.column)) {
            builder.append(this.column);
        }
        final String rc = builder.toString();
        final String template = this.fragment;
        if (template.contains(Constants.DEF_STR_COLUMN_PH)) {
            return template.replaceAll(Constants.DEF_STR_COLUMN_PH, rc);
        } else if (template.contains(Constants.DEF_STR_PH)) {
            return String.format(template, rc);
        }
        return template;
    }
}
