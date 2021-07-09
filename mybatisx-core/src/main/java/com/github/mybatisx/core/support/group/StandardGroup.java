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
package com.github.mybatisx.core.support.group;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.support.criteria.Criteria;

import java.util.Collection;

/**
 * 分组
 * @author wvkity
 * @created 2021-01-29
 * @since 1.0.0
 */
public class StandardGroup extends AbstractGroup<String> implements Group {

    private static final long serialVersionUID = 3367191253546128146L;

    private StandardGroup(Collection<String> columns) {
        this.addAll(columns);
    }

    private StandardGroup(String alias, Collection<String> columns) {
        this.tableAlias = alias;
        this.addAll(columns);
    }

    private StandardGroup(Criteria<?> criteria, Collection<String> columns) {
        this.criteria = criteria;
        this.addAll(columns);
    }

    // region Static methods

    /**
     * 分组
     * @param columns 字段列表
     * @return {@link StandardGroup}
     */
    public static StandardGroup group(final String... columns) {
        return group(Objects.asNotNullList(columns));
    }

    /**
     * 分组
     * @param columns 字段列表
     * @return {@link StandardGroup}
     */
    public static StandardGroup group(final Collection<String> columns) {
        if (Objects.isNotNullElement(columns)) {
            return new StandardGroup(columns);
        }
        return null;
    }

    /**
     * 分组
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link StandardGroup}
     */
    public static StandardGroup group(final String alias, final String... columns) {
        return groupWithAlias(alias, Objects.asNotNullList(columns));
    }

    /**
     * 分组
     * @param alias   表别名
     * @param columns 字段列表
     * @return {@link StandardGroup}
     */
    public static StandardGroup groupWithAlias(final String alias, final Collection<String> columns) {
        if (Objects.isNotNullElement(columns)) {
            return new StandardGroup(alias, columns);
        }
        return null;
    }

    /**
     * 分组
     * @param criteria {@link Criteria}
     * @param columns  字段列表
     * @return {@link StandardGroup}
     */
    public static StandardGroup group(final Criteria<?> criteria, final String... columns) {
        return group(criteria, Objects.asNotNullList(columns));
    }

    /**
     * 分组
     * @param criteria {@link Criteria}
     * @param columns  字段列表
     * @return {@link StandardGroup}
     */
    public static StandardGroup group(final Criteria<?> criteria, final Collection<String> columns) {
        if (Objects.isNotNullElement(columns)) {
            return new StandardGroup(criteria, columns);
        }
        return null;
    }

    // endregion
}
