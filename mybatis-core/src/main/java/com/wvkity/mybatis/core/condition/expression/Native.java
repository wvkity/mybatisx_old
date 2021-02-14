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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.condition.basic.Matched;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * 纯SQL
 * @author wvkity
 * @created 2021-01-16
 * @since 1.0.0
 */
public class Native extends AbstractExpression<String> {

    private static final long serialVersionUID = 1802248293107856419L;

    /**
     * 条件片段
     */
    private final String criterion;

    public Native(String criterion) {
        this.criterion = criterion;
        this.matched = Matched.IMMEDIATE;
    }

    public String getCriterion() {
        return criterion;
    }

    public static Native of(final String criterion) {
        if (Objects.isNotBlank(criterion)) {
            return new Native(criterion);
        }
        return null;
    }
}
