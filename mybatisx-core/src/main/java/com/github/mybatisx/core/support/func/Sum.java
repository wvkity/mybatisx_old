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
package com.github.mybatisx.core.support.func;

import com.github.mybatisx.Objects;
import com.github.mybatisx.support.criteria.Criteria;

/**
 * {@link Func#SUM}聚合函数
 * @author wvkity
 * @created 2021-04-30
 * @since 1.0.0
 */
public class Sum extends AbstractFunction {

    private static final long serialVersionUID = -5495106757200906114L;

    public Sum(Criteria<?> criteria, String column, String alias) {
        this.criteria = criteria;
        this.column = column;
        this.alias = alias;
        this.func = Func.SUM;
    }

    public Sum(Criteria<?> criteria, String column, String alias, boolean distinct) {
        this(criteria, column, alias, null, distinct);
    }

    public Sum(Criteria<?> criteria, String column, String alias, Integer scale) {
        this(criteria, column, alias, scale, false);
    }

    public Sum(Criteria<?> criteria, String column, String alias, Integer scale, boolean distinct) {
        this.criteria = criteria;
        this.column = column;
        this.alias = alias;
        this.scale = scale;
        this.distinct = distinct;
        this.func = Func.SUM;
    }

    public Sum(String tableAlias, String column, String alias) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = alias;
        this.func = Func.SUM;
    }

    public Sum(String tableAlias, String column, String alias, boolean distinct) {
        this(tableAlias, column, alias, null, distinct);
    }

    public Sum(String tableAlias, String column, String alias, Integer scale) {
        this(tableAlias, column, alias, scale, false);
    }

    public Sum(String tableAlias, String column, String alias, Integer scale, boolean distinct) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = alias;
        this.scale = scale;
        this.distinct = distinct;
        this.func = Func.SUM;
    }

    public static final class Builder {
        private Criteria<?> criteria;
        private String column;
        private String alias;
        private Integer scale;
        private boolean distinct;
        private String tableAlias;

        private Builder() {
        }

        public Builder criteria(Criteria<?> criteria) {
            this.criteria = criteria;
            return this;
        }

        public Builder column(String column) {
            this.column = column;
            return this;
        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder scale(Integer scale) {
            this.scale = scale;
            return this;
        }

        public Builder distinct(boolean distinct) {
            this.distinct = distinct;
            return this;
        }

        public Builder tableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
            return this;
        }

        public Sum build() {
            if (Objects.isNotBlank(tableAlias)) {
                return new Sum(tableAlias, column, alias, scale, distinct);
            }
            return new Sum(criteria, column, alias, scale, distinct);
        }

        public static Builder create() {
            return new Builder();
        }
    }

}
