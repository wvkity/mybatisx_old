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

import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.convert.converter.PropertyConverter;

/**
 * 空值条件
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface Nullable<T, Chain extends Nullable<T, Chain>> extends PropertyConverter<T> {

    /**
     * IS NULL
     * @param property 属性
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain isNull(final Property<T, V> property) {
        return isNull(property, Slot.AND);
    }

    /**
     * IS NULL
     * @param property 属性
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain isNull(final Property<T, V> property, final Slot slot);

    /**
     * IS NULL
     * @param property 属性
     * @return {@link Chain}
     */
    default Chain isNull(final String property) {
        return isNull(property, Slot.AND);
    }

    /**
     * IS NULL
     * @param property 属性
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain isNull(final String property, final Slot slot);

    /**
     * IS NULL
     * @param column 字段
     * @return {@link Chain}
     */
    default Chain colIsNull(final String column) {
        return colIsNull(column, Slot.AND);
    }

    /**
     * IS NULL
     * @param column 字段
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colIsNull(final String column, final Slot slot);

    /**
     * IS NOT NULL
     * @param property 属性
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    default <V> Chain notNull(final Property<T, V> property) {
        return notNull(property, Slot.AND);
    }

    /**
     * IS NOT NULL
     * @param property 属性
     * @param slot     {@link Slot}
     * @param <V>      属性类型
     * @return {@link Chain}
     */
    <V> Chain notNull(final Property<T, V> property, final Slot slot);

    /**
     * IS NOT NULL
     * @param property 属性
     * @return {@link Chain}
     */
    default Chain notNull(final String property) {
        return notNull(property, Slot.AND);
    }

    /**
     * IS NOT NULL
     * @param property 属性
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain notNull(final String property, final Slot slot);

    /**
     * IS NOT NULL
     * @param column 字段
     * @return {@link Chain}
     */
    default Chain colNotNull(final String column) {
        return colNotNull(column, Slot.AND);
    }

    /**
     * IS NOT NULL
     * @param column 字段
     * @param slot   {@link Slot}
     * @return {@link Chain}
     */
    Chain colNotNull(final String column, final Slot slot);

}
