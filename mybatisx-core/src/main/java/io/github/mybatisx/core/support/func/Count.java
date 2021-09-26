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
 * {@link Func#COUNT}聚合函数
 * @author wvkity
 * @created 2021-04-28
 * @since 1.0.0
 */
public class Count extends AbstractFunction {

    private static final long serialVersionUID = -4201871015277809690L;

    public Count(Criteria<?> criteria, String column, String alias, boolean distinct) {
        this.criteria = criteria;
        this.column = column;
        this.alias = alias;
        this.distinct = distinct;
        this.func = Func.COUNT;
    }

    public Count(String tableAlias, String column, String alias, boolean distinct) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = alias;
        this.distinct = distinct;
        this.func = Func.COUNT;
    }

    public static final class Builder {
        private Criteria<?> criteria;
        private String column;
        private String alias;
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

        public Builder distinct(boolean distinct) {
            this.distinct = distinct;
            return this;
        }

        public Builder tableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
            return this;
        }

        public Count build() {
            if (Objects.isNotBlank(tableAlias)) {
                return new Count(tableAlias, column, alias, distinct);
            }
            return new Count(criteria, column, alias, distinct);
        }

        public static Builder create() {
            return new Builder();
        }
    }
}
