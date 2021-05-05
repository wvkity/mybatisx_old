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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.utils.Objects;

import java.util.function.Consumer;

/**
 * 查询条件
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public class Query<T> extends AbstractQueryCriteria<T> {

    private static final long serialVersionUID = -3270889922571718841L;

    private Query() {
    }

    public Query(final Class<T> entityClass) {
        this.entityClass = entityClass;
        this.initialize(null, true);
    }

    public Query(final Class<T> entityClass, final String alias) {
        this.entityClass = entityClass;
        this.initialize(alias, true);
        this.useAlias(Objects.isNotBlank(alias));
    }

    @Override
    protected Query<T> newInstance() {
        final Query<T> instance = new Query<>();
        instance.entityClass = this.entityClass;
        instance.clone(this);
        return instance;
    }

    // region Static methods

    /**
     * 创建{@link Query}对象
     * @param entityClass 实体类
     * @param <T>         泛型类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entityClass) {
        return new Query<>(entityClass);
    }

    /**
     * 创建{@link Query}对象
     * @param entityClass 实体类
     * @param action      {@link Consumer}
     * @param <T>         泛型类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entityClass, final Consumer<Query<T>> action) {
        final Query<T> instance = new Query<>(entityClass);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 创建{@link Query}对象
     * @param entityClass 实体类
     * @param alias       别名
     * @param <T>         泛型类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entityClass, final String alias) {
        return new Query<>(entityClass, alias);
    }

    /**
     * 创建{@link Query}对象
     * @param entityClass 实体类
     * @param alias       别名
     * @param action      {@link Consumer}
     * @param <T>         泛型类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entityClass, final String alias, final Consumer<Query<T>> action) {
        final Query<T> instance = new Query<>(entityClass, alias);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 根据实例创建{@link Query}对象
     * @param instance 实例
     * @param <T>      实例类型
     * @return {@link Query}
     */
    @SuppressWarnings("unchecked")
    public static <T> Query<T> from(final T instance) {
        if (Objects.isNull(instance)) {
            throw new NullPointerException("The instance object cannot be null.");
        }
        return Query.from((Class<T>) instance.getClass());
    }

    // endregion
}
