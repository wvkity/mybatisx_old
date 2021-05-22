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
import com.wvkity.mybatis.core.criteria.ExtCriteria;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 通用子查询条件容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-20
 * @since 1.0.0
 */
public class GenericSubQuery<T> extends AbstractGenericSubQueryCriteria<T, GenericSubQuery<T>> {

    private static final long serialVersionUID = 1240531896119632232L;

    private GenericSubQuery() {
    }

    public GenericSubQuery(ExtCriteria<?> criteria, Class<T> entity) {
        this(criteria, entity, null);
    }

    public GenericSubQuery(ExtCriteria<?> criteria, Class<T> entity, String alias) {
        this.entityClass = entity;
        this.refCriteria = criteria;
        this.clone(criteria.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
    }

    @Override
    protected GenericSubQuery<T> newInstance() {
        final GenericSubQuery<T> it = new GenericSubQuery<>();
        it.refCriteria = this.refCriteria;
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link GenericSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link GenericSubQuery}
     */
    public static <E, T> GenericSubQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity) {
        return GenericSubQuery.from(criteria, entity, (String) null);
    }

    /**
     * 创建{@link GenericSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param alias    别名
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link GenericSubQuery}
     */
    public static <E, T> GenericSubQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                                 final String alias) {
        return new GenericSubQuery<>(criteria, entity, alias);
    }

    /**
     * 创建{@link GenericSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param action   {@link Consumer}
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link GenericSubQuery}
     */
    public static <E, T> GenericSubQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                                 final Consumer<GenericSubQuery<T>> action) {
        return GenericSubQuery.from(criteria, entity, null, action);
    }

    /**
     * 创建{@link GenericSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param entity   实体类
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <E>      实体类型
     * @param <T>      实体类型
     * @return {@link GenericSubQuery}
     */
    public static <E, T> GenericSubQuery<T> from(final ExtCriteria<E> criteria, final Class<T> entity,
                                                 final String alias, final Consumer<GenericSubQuery<T>> action) {
        final GenericSubQuery<T> it = GenericSubQuery.from(criteria, entity, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

}
