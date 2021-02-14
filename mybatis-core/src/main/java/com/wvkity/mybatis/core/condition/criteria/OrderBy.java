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

import com.wvkity.mybatis.core.condition.basic.order.Order;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.List;

/**
 * 排序
 * @param <T>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-28
 * @since 1.0.0
 */
public interface OrderBy<T, Chain extends OrderBy<T, Chain>> {

    // region Asc sort methods

    /**
     * 升序
     * @param property 属性
     * @return {@link Chain}
     */
    Chain asc(final Property<T, ?> property);

    /**
     * 升序
     * @param properties 属性列表
     * @return {@link Chain}
     */
    @SuppressWarnings("unchecked")
    Chain asc(final Property<T, ?>... properties);

    /**
     * 升序
     * @param property 属性
     * @return {@link Chain}
     */
    Chain asc(final String property);

    /**
     * 升序
     * @param properties 属性列表
     * @return {@link Chain}
     */
    default Chain asc(final String... properties) {
        return asc(Objects.asList(properties));
    }

    /**
     * 升序
     * @param properties 属性列表
     * @return {@link Chain}
     */
    Chain asc(final List<String> properties);

    /**
     * 升序
     * @param column 字段名
     * @return {@link Chain}
     */
    Chain colAsc(final String column);

    /**
     * 升序
     * @param columns 字段名列表
     * @return {@link Chain}
     */
    default Chain colAsc(final String... columns) {
        return colAsc(Objects.asList(columns));
    }

    /**
     * 升序
     * @param columns 字段名列表
     * @return {@link Chain}
     */
    Chain colAsc(final List<String> columns);

    /**
     * 升序(不含{@link Criteria}、表别名)
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain ascOnly(final String... columns) {
        return this.ascOnly(Objects.asList(columns));
    }

    /**
     * 升序(不含{@link Criteria}、表别名)
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain ascOnly(final List<String> columns) {
        return this.ascWithAlias(null, columns);
    }

    /**
     * 升序(不含{@link Criteria})
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain ascWithAlias(final String alias, final String... columns) {
        return ascWithAlias(alias, Objects.asList(columns));
    }

    /**
     * 升序(不含{@link Criteria})
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link Chain}
     */
    Chain ascWithAlias(final String alias, final List<String> columns);

    // endregion

    // region Desc sort methods

    /**
     * 降序
     * @param property 属性
     * @return {@link Chain}
     */
    Chain desc(final Property<T, ?> property);

    /**
     * 降序
     * @param properties 属性列表
     * @return {@link Chain}
     */
    @SuppressWarnings("unchecked")
    Chain desc(final Property<T, ?>... properties);

    /**
     * 降序
     * @param property 属性
     * @return {@link Chain}
     */
    Chain desc(final String property);

    /**
     * 降序
     * @param properties 属性列表
     * @return {@link Chain}
     */
    default Chain desc(final String... properties) {
        return desc(Objects.asList(properties));
    }

    /**
     * 降序
     * @param properties 属性列表
     * @return {@link Chain}
     */
    Chain desc(final List<String> properties);

    /**
     * 降序
     * @param column 字段名
     * @return {@link Chain}
     */
    Chain colDesc(final String column);

    /**
     * 降序
     * @param columns 字段名列表
     * @return {@link Chain}
     */
    default Chain colDesc(final String... columns) {
        return colDesc(Objects.asList(columns));
    }

    /**
     * 降序
     * @param columns 字段名列表
     * @return {@link Chain}
     */
    Chain colDesc(final List<String> columns);

    /**
     * 降序(不含{@link Criteria}、表别名)
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain descOnly(final String... columns) {
        return this.descOnly(Objects.asList(columns));
    }

    /**
     * 降序(不含{@link Criteria}、表别名)
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain descOnly(final List<String> columns) {
        return this.descWithAlias(null, columns);
    }

    /**
     * 降序(不含{@link Criteria})
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain descWithAlias(final String alias, final String... columns) {
        return descWithAlias(alias, Objects.asList(columns));
    }

    /**
     * 降序(不含{@link Criteria})
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link Chain}
     */
    Chain descWithAlias(final String alias, final List<String> columns);

    // endregion

    // region Sort methods

    /**
     * 排序
     * @param order {@link Order}
     * @return {@link Chain}
     */
    Chain orderBy(final Order order);

    /**
     * 排序
     * @param orders {@link Order}列表
     * @return {@link Chain}
     */
    default Chain orderBy(final Order... orders) {
        return orderBy(Objects.asList(orders));
    }

    /**
     * 排序
     * @param orders {@link Order}集合
     * @return {@link Chain}
     */
    Chain orderBy(final List<Order> orders);

    // endregion
}
