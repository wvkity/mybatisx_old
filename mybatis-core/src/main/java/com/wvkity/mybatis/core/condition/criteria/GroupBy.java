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

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.condition.basic.group.Group;
import com.wvkity.mybatis.core.property.Property;

import java.util.Collection;

/**
 * 分组
 * @param <T>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-31
 * @since 1.0.0
 */
public interface GroupBy<T, Chain extends GroupBy<T, Chain>> {

    /**
     * 分组
     * @param property 属性
     * @return {@link Chain}
     */
    Chain group(final Property<T, ?> property);

    /**
     * 分组
     * @param properties 属性列表
     * @return {@link Chain}
     */
    @SuppressWarnings("unchecked")
    Chain group(final Property<T, ?>... properties);

    /**
     * 分组
     * @param property 属性
     * @return {@link Chain}
     */
    Chain group(final String property);

    /**
     * 分组
     * @param properties 属性列表
     * @return {@link Chain}
     */
    default Chain group(final String... properties) {
        return this.group(Objects.asList(properties));
    }

    /**
     * 分组
     * @param properties 属性列表
     * @return {@link Chain}
     */
    Chain group(final Collection<String> properties);

    /**
     * 分组
     * @param column 字段
     * @return {@link Chain}
     */
    Chain colGroup(final String column);

    /**
     * 分组
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain colGroup(final String... columns) {
        return this.colGroup(Objects.asList(columns));
    }

    /**
     * 分组
     * @param columns 字段列表
     * @return {@link Chain}
     */
    Chain colGroup(final Collection<String> columns);

    /**
     * 分组
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain groupOnly(final String... columns) {
        return this.groupOnly(Objects.asList(columns));
    }

    /**
     * 分组
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain groupOnly(final Collection<String> columns) {
        return this.groupWithAlias(null, columns);
    }

    /**
     * 分组
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain groupWithAlias(final String alias, final String... columns) {
        return this.groupWithAlias(alias, Objects.asList(columns));
    }

    /**
     * 分组
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link Chain}
     */
    Chain groupWithAlias(final String alias, final Collection<String> columns);

    /**
     * 分组
     * @param group {@link Group}
     * @return {@link Chain}
     */
    Chain groupBy(final Group group);

    /**
     * 分组
     * @param groups {@link Group}列表
     * @return {@link Chain}
     */
    default Chain groupBy(final Group... groups) {
        return groupBy(Objects.asList(groups));
    }

    /**
     * 分组
     * @param groups {@link Group}列表
     * @return {@link Chain}
     */
    Chain groupBy(final Collection<Group> groups);
}
