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
package io.github.mybatisx.core.support.func;

import io.github.mybatisx.Objects;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * {@link Func#MIN}聚合函数
 * @author wvkity
 * @created 2021-05-01
 * @since 1.0.0
 */
public class Min extends AbstractFunction {

    private static final long serialVersionUID = -727573337960282476L;

    public Min(Criteria<?> criteria, String column, String alias) {
        this(criteria, column, alias, null);
    }

    public Min(Criteria<?> criteria, String column, String alias, Integer scale) {
        this.criteria = criteria;
        this.column = column;
        this.alias = alias;
        this.scale = scale;
        this.func = Func.MIN;
    }

    public Min(String tableAlias, String column, String alias) {
        this(tableAlias, column, alias, null);
    }

    public Min(String tableAlias, String column, String alias, Integer scale) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = alias;
        this.scale = scale;
        this.func = Func.MIN;
    }

    public static final class Builder {
        private Criteria<?> criteria;
        private String column;
        private String alias;
        private Integer scale;
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

        public Builder tableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
            return this;
        }

        public Min build() {
            if (Objects.isNotBlank(tableAlias)) {
                return new Min(tableAlias, column, alias, scale);
            }
            return new Min(criteria, column, alias, scale);
        }

        public static Builder create() {
            return new Builder();
        }
    }

}
