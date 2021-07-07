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

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.convert.DefaultConditionConverter;
import com.wvkity.mybatis.core.convert.DefaultParameterConverter;
import com.wvkity.mybatis.core.criteria.Category;
import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.core.criteria.sql.DefaultQuerySqlManager;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 基础条件/查询容器(支持lambda语法)
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public class LambdaQuery<T> extends AbstractLambdaQueryCriteria<T, LambdaQuery<T>> {

    private static final long serialVersionUID = -7267472137968222445L;

    private LambdaQuery() {
    }

    public LambdaQuery(Class<T> entity) {
        this(entity, null);
    }

    public LambdaQuery(Class<T> entity, String alias) {
        this.entityClass = entity;
        this.initialize(alias, Category.QUERY);
        this.useAlias.set(Objects.isNotBlank(alias));
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    public LambdaQuery(ExtCriteria<?> criteria, Class<T> entity) {
        this(criteria, entity, null);
    }

    public LambdaQuery(ExtCriteria<?> criteria, Class<T> entity, String alias) {
        this.entityClass = entity;
        this.clone(criteria.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = this.genDefTabAlias();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    @Override
    protected LambdaQuery<T> newInstance() {
        final LambdaQuery<T> it = new LambdaQuery<>();
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link LambdaQuery}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link LambdaQuery}
     */
    public static <T> LambdaQuery<T> from(final Class<T> entity) {
        return LambdaQuery.from(entity, null, null);
    }

    /**
     * 创建{@link LambdaQuery}
     * @param entity 实体类
     * @param alias  别名
     * @param <T>    实体类型
     * @return {@link LambdaQuery}
     */
    public static <T> LambdaQuery<T> from(final Class<T> entity, final String alias) {
        return LambdaQuery.from(entity, alias, null);
    }

    /**
     * 创建{@link LambdaQuery}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link LambdaQuery}
     */
    public static <T> LambdaQuery<T> from(final Class<T> entity, final Consumer<LambdaQuery<T>> action) {
        return LambdaQuery.from(entity, null, action);
    }

    /**
     * 创建{@link LambdaQuery}
     * @param entity 实体类
     * @param alias  别名
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link LambdaQuery}
     */
    public static <T> LambdaQuery<T> from(final Class<T> entity, final String alias,
                                          final Consumer<LambdaQuery<T>> action) {
        final LambdaQuery<T> it = new LambdaQuery<>(entity, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

    /**
     * 创建{@link LambdaQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link LambdaQuery}
     */
    public static <E, T> LambdaQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity) {
        return LambdaQuery.from(criteria, entity, (String) null);
    }

    /**
     * 创建{@link LambdaQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param alias    别名
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link LambdaQuery}
     */
    public static <E, T> LambdaQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                             final String alias) {
        return new LambdaQuery<>(criteria, entity, alias);
    }

    /**
     * 创建{@link LambdaQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param action   {@link Consumer}
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link LambdaQuery}
     */
    public static <E, T> LambdaQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                             final Consumer<LambdaQuery<T>> action) {
        return LambdaQuery.from(criteria, entity, null, action);
    }

    /**
     * 创建{@link LambdaQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link LambdaQuery}
     */
    public static <E, T> LambdaQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                             final String alias, final Consumer<LambdaQuery<T>> action) {
        final LambdaQuery<T> it = LambdaQuery.from(criteria, entity, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

}
