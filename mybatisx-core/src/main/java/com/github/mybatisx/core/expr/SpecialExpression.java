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
package com.github.mybatisx.core.expr;

import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;

/**
 * 特殊表达式(字段相等)
 * @author wvkity
 * @created 2021-04-13
 * @since 1.0.0
 */
public class SpecialExpression extends AbstractExpression<String> {

    /**
     * {@link Criteria}
     */
    private Criteria<?> otherCriteria;
    /**
     * 表别名
     */
    private String otherTableAlias;
    /**
     * 字段名
     */
    private String otherTarget;

    public SpecialExpression(String tableAlias, String target, Criteria<?> otherCriteria, String otherTarget) {
        this(null, tableAlias, target, otherCriteria, null, otherTarget);
    }

    public SpecialExpression(Criteria<?> criteria, String target, String otherTableAlias, String otherTarget) {
        this(criteria, null, target, null, otherTableAlias, otherTarget);
    }

    public SpecialExpression(String tableAlias, String target, String otherTableAlias, String otherTarget) {
        this(null, tableAlias, target, null, otherTableAlias, otherTarget);
    }

    public SpecialExpression(Criteria<?> criteria, String target, Criteria<?> otherCriteria,
                             String otherTarget) {
        this(criteria, null, target, otherCriteria, null, otherTarget);
    }

    public SpecialExpression(Criteria<?> criteria, String tableAlias, String target, Criteria<?> otherCriteria,
                             String otherTarget) {
        this(criteria, tableAlias, target, otherCriteria, null, otherTarget);
    }

    public SpecialExpression(Criteria<?> criteria, String target, Criteria<?> otherCriteria,
                             String otherTableAlias, String otherTarget) {
        this(criteria, null, target, otherCriteria, otherTableAlias, otherTarget);
    }

    public SpecialExpression(Criteria<?> criteria, String tableAlias, String target, Criteria<?> otherCriteria,
                             String otherTableAlias, String otherTarget) {
        this.criteria = criteria;
        this.tableAlias = tableAlias;
        this.column = target;
        this.otherCriteria = otherCriteria;
        this.otherTableAlias = otherTableAlias;
        this.otherTarget = otherTarget;
        this.matched = Matched.IMMEDIATE;
        this.symbol = Symbol.SPECIAL;
    }

    public Criteria<?> getOtherCriteria() {
        return otherCriteria;
    }

    public String getOtherTableAlias() {
        return otherTableAlias;
    }

    public String getOtherTarget() {
        return otherTarget;
    }

    public SpecialExpression otherCriteria(Criteria<?> otherCriteria) {
        this.otherCriteria = otherCriteria;
        return this;
    }

    public SpecialExpression otherTableAlias(String otherTableAlias) {
        this.otherTableAlias = otherTableAlias;
        return this;
    }

    public SpecialExpression otherTarget(String otherTarget) {
        this.otherTarget = otherTarget;
        return this;
    }
}
