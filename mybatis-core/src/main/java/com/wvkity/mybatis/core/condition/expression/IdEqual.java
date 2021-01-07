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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.handler.TableHandler;
import com.wvkity.mybatis.core.metadata.Column;

import java.util.Optional;

/**
 * 主键等于条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public class IdEqual extends AbstractColumnExpression {

    private static final long serialVersionUID = -1735853549185563L;

    public IdEqual(final Criteria<?> criteria, final Column id, final Slot slot, final Object value) {
        this.criteria = criteria;
        this.target = id;
        this.slot = slot;
        this.value = value;
    }

    public static IdEqual.Builder create() {
        return new IdEqual.Builder();
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

        public IdEqual build() {
            return Optional.ofNullable(TableHandler.getId(this.criteria.getEntityClass()))
                .map(it -> new IdEqual(this.criteria, it, this.slot, this.value)).orElse(null);
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
