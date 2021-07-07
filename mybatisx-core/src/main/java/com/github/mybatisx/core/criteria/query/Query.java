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
package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.convert.DefaultConditionConverter;
import com.github.mybatisx.core.convert.DefaultParameterConverter;
import com.github.mybatisx.core.criteria.Category;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.sql.DefaultQuerySqlManager;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 基础条件/查询容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public class Query<T> extends AbstractCommonQueryCriteria<T, Query<T>> {

    private static final long serialVersionUID = 3873857821804301136L;

    private Query() {
    }

    public Query(Class<T> entity) {
        this(entity, null);
    }

    public Query(Class<T> entity, String alias) {
        this.entityClass = entity;
        this.initialize(alias, Category.QUERY);
        this.useAlias.set(Objects.isNotBlank(alias));
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    public Query(ExtCriteria<?> criteria, Class<T> entity) {
        this(criteria, entity, null);
    }

    public Query(ExtCriteria<?> criteria, Class<T> entity, String alias) {
        this.entityClass = entity;
        this.clone(criteria.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = this.genDefTabAlias();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    @Override
    protected Query<T> newInstance() {
        final Query<T> it = new Query<>();
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link Query}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entity) {
        return Query.from(entity, null, null);
    }

    /**
     * 创建{@link Query}
     * @param entity 实体类
     * @param alias  别名
     * @param <T>    实体类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entity, final String alias) {
        return Query.from(entity, alias, null);
    }

    /**
     * 创建{@link Query}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entity, final Consumer<Query<T>> action) {
        return Query.from(entity, null, action);
    }

    /**
     * 创建{@link Query}
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link Query}
     */
    public static <T> Query<T> from(final Class<T> entity, final String alias, final Consumer<Query<T>> action) {
        final Query<T> it = new Query<>(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

    /**
     * 创建{@link Query}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link Query}
     */
    public static <E, T> Query<T> from(final ExtCriteria<E> criteria, final Class<T> entity) {
        return Query.from(criteria, entity, (String) null);
    }

    /**
     * 创建{@link Query}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param alias    别名
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link Query}
     */
    public static <E, T> Query<T> from(final ExtCriteria<E> criteria, final Class<T> entity, final String alias) {
        return new Query<>(criteria, entity, alias);
    }

    /**
     * 创建{@link Query}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param action   {@link Consumer}
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link Query}
     */
    public static <E, T> Query<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                       final Consumer<Query<T>> action) {
        return Query.from(criteria, entity, null, action);
    }

    /**
     * 创建{@link Query}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link Query}
     */
    public static <E, T> Query<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                       final String alias, final Consumer<Query<T>> action) {
        final Query<T> it = Query.from(criteria, entity, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
