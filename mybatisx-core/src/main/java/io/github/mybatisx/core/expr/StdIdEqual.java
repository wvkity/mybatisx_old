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
package io.github.mybatisx.core.expr;

import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 主键等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class StdIdEqual extends AbstractBasicExpression<Column> {

    private static final long serialVersionUID = -1735853549185563L;

    public StdIdEqual(final Criteria<?> criteria, final Slot slot, final Object value) {
        this.criteria = criteria;
        this.slot = slot;
        this.value = value;
        this.matched = Matched.STANDARD;
        this.symbol = Symbol.EQ;
    }

    public static StdIdEqual.Builder create() {
        return new StdIdEqual.Builder();
    }

    public static final class Builder {
        /**
         * {@link Criteria}
         */
        private Criteria<?> criteria;
        /**
         * {@link Slot}
         */
        private Slot slot;
        /**
         * 值
         */
        private Object value;

        private Builder() {
        }

        public StdIdEqual build() {
            return new StdIdEqual(this.criteria, this.slot, this.value);
        }

        public Builder criteria(Criteria<?> criteria) {
            this.criteria = criteria;
            return this;
        }

        public Builder slot(Slot slot) {
            this.slot = slot;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }
    }
}
