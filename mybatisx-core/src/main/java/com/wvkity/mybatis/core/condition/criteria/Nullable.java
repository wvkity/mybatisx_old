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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.core.property.Property;

/**
 * 空值条件
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface Nullable<T, Chain extends Nullable<T, Chain>> {

    /**
     * IS NULL
     * @param property 属性
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain isNull(final Property<T, V> property) {
        return isNull(Slot.AND, property);
    }

    /**
     * IS NULL
     * @param slot     {@link Slot}
     * @param property 属性
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain isNull(final Slot slot, final Property<T, V> property);

    /**
     * IS NULL
     * @param property 属性
     * @return {@link Chain}
     */
    default Chain isNull(final String property) {
        return isNull(Slot.AND, property);
    }

    /**
     * IS NULL
     * @param property 属性
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain isNull(final Slot slot, final String property);

    /**
     * IS NULL
     * @param column 字段
     * @return {@link Chain}
     */
    default Chain colIsNull(final String column) {
        return colIsNull(Slot.AND, column);
    }

    /**
     * IS NULL
     * @param slot   {@link Slot}
     * @param column 字段
     * @return {@link Chain}
     */
    Chain colIsNull(final Slot slot, final String column);

    /**
     * IS NOT NULL
     * @param property 属性
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notNull(final Property<T, V> property) {
        return notNull(Slot.AND, property);
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param property 属性
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notNull(final Slot slot, final Property<T, V> property);

    /**
     * IS NOT NULL
     * @param property 属性
     * @return {@link Chain}
     */
    default Chain notNull(final String property) {
        return notNull(Slot.AND, property);
    }

    /**
     * IS NOT NULL
     * @param slot     {@link Slot}
     * @param property 属性
     * @return {@link Chain}
     */
    Chain notNull(final Slot slot, final String property);

    /**
     * IS NOT NULL
     * @param column 字段
     * @return {@link Chain}
     */
    default Chain colNotNull(final String column) {
        return colNotNull(Slot.AND, column);
    }

    /**
     * IS NOT NULL
     * @param slot   {@link Slot}
     * @param column 字段
     * @return {@link Chain}
     */
    Chain colNotNull(final Slot slot, final String column);

}
