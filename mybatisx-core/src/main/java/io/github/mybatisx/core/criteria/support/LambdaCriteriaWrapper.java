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
package io.github.mybatisx.core.criteria.support;

import io.github.mybatisx.core.criteria.CriteriaWrapper;
import io.github.mybatisx.core.property.Property;
import io.github.mybatisx.support.constant.Slot;

/**
 * 基础条件接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 值类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface LambdaCriteriaWrapper<T, C extends LambdaCriteriaWrapper<T, C>> extends CriteriaWrapper<T, C>,
    LambdaCompare<T, C>, LambdaRange<T, C>, LambdaLike<T, C>, LambdaTemplate<T, C>, OtherCondition<T, C>,
    QueryCriteriaBuilder<T, C> {

    /**
     * IS NULL
     * @param property 属性
     * @return {@code this}
     */
    default C isNull(final Property<T, ?> property) {
        return this.isNull(this.getSlot(), property);
    }

    /**
     * IS NULL
     * @param slot     {@link Slot}
     * @param property 属性
     * @return {@code this}
     */
    default C isNull(final Slot slot, final Property<T, ?> property) {
        return this.isNull(slot, property.toProp());
    }

    /**
     * IS NULL
     * @param property 属性
     * @return {@code this}
     */
    default C isNull(final String property) {
        return this.isNull(this.getSlot(), property);
    }

    /**
     * IS NULL
     * @param property 属性
     * @param slot     {@link Slot}
     * @return {@code this}
     */
    C isNull(final Slot slot, final String property);

    /**
     * IS NOT NULL
     * @param property 属性
     * @return {@code this}
     */
    default C notNull(final Property<T, ?> property) {
        return this.notNull(this.getSlot(), property);
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param property 属性
     * @return {@code this}
     */
    default C notNull(final Slot slot, final Property<T, ?> property) {
        return this.notNull(slot, property.toProp());
    }

    /**
     * IS NOT NULL
     * @param property 属性
     * @return {@code this}
     */
    default C notNull(final String property) {
        return this.notNull(this.getSlot(), property);
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param property 属性
     * @return {@code this}
     */
    C notNull(final Slot slot, final String property);

}
