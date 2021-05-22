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
package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.criteria.Category;

import java.util.function.Consumer;

/**
 * 通用条件/查询容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public class GenericQuery<T> extends AbstractGenericQueryCriteria<T, GenericQuery<T>> {

    private static final long serialVersionUID = 1798091974002021171L;

    private GenericQuery() {
    }

    public GenericQuery(Class<T> entity) {
        this(entity, null);
    }

    public GenericQuery(Class<T> entity, String alias) {
        this.entityClass = entity;
        this.initialize(alias, Category.QUERY);
        this.useAlias.set(Objects.isNotBlank(alias));
    }

    @Override
    protected GenericQuery<T> newInstance() {
        final GenericQuery<T> it = new GenericQuery<>();
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link GenericQuery}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link GenericQuery}
     */
    public static <T> GenericQuery<T> from(final Class<T> entity) {
        return GenericQuery.from(entity, null, null);
    }

    /**
     * 创建{@link GenericQuery}
     * @param entity 实体类
     * @param alias  别名
     * @param <T>    实体类型
     * @return {@link GenericQuery}
     */
    public static <T> GenericQuery<T> from(final Class<T> entity, final String alias) {
        return GenericQuery.from(entity, alias, null);
    }

    /**
     * 创建{@link GenericQuery}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link GenericQuery}
     */
    public static <T> GenericQuery<T> from(final Class<T> entity, final Consumer<GenericQuery<T>> action) {
        return GenericQuery.from(entity, null, action);
    }

    /**
     * 创建{@link GenericQuery}
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link GenericQuery}
     */
    public static <T> GenericQuery<T> from(final Class<T> entity, final String alias,
                                           final Consumer<GenericQuery<T>> action) {
        final GenericQuery<T> it = new GenericQuery<>(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
