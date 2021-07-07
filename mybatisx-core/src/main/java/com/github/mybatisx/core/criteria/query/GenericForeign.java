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
import com.github.mybatisx.core.criteria.sql.DefaultQuerySqlManager;
import com.github.mybatisx.support.constant.Join;
import com.github.mybatisx.core.convert.DefaultConditionConverter;
import com.github.mybatisx.core.convert.DefaultParameterConverter;
import com.github.mybatisx.core.criteria.ExtCriteria;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 通用联表查询条件容器
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public class GenericForeign<T> extends AbstractGenericForeignQueryCriteria<T, GenericForeign<T>> {

    private static final long serialVersionUID = -7861575646844467150L;

    private GenericForeign() {
    }

    public GenericForeign(ExtCriteria<?> master, Class<T> entity, Join join) {
        this(master, entity, null, join);
    }

    public GenericForeign(ExtCriteria<?> master, Class<T> entity, String alias, Join join) {
        this.master = master;
        this.entityClass = entity;
        this.join = join;
        this.clone(master.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = this.genDefTabAlias();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    @Override
    protected GenericForeign<T> newInstance() {
        final GenericForeign<T> it = new GenericForeign<>();
        it.master = this.master;
        it.join = this.join;
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link GenericForeign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param join   {@link Join}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link GenericForeign}
     */
    public static <T, M extends ExtCriteria<?>> GenericForeign<T> from(final M master, final Class<T> entity,
                                                                       final Join join) {
        return GenericForeign.from(master, entity, null, join);
    }

    /**
     * 创建{@link GenericForeign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link GenericForeign}
     */
    public static <T, M extends ExtCriteria<?>> GenericForeign<T> from(final M master, final Class<T> entity,
                                                                       final String alias, final Join join) {
        return new GenericForeign<>(master, entity, alias, join);
    }

    /**
     * 创建{@link GenericForeign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link GenericForeign}
     */
    public static <T, M extends ExtCriteria<?>> GenericForeign<T> from(final M master, final Class<T> entity,
                                                                       final Join join,
                                                                       final Consumer<GenericForeign<T>> action) {
        return GenericForeign.from(master, entity, null, join, action);
    }

    /**
     * 创建{@link GenericForeign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link GenericForeign}
     */
    public static <T, M extends ExtCriteria<?>> GenericForeign<T> from(final M master, final Class<T> entity,
                                                                       final String alias, final Join join,
                                                                       final Consumer<GenericForeign<T>> action) {
        final GenericForeign<T> it = GenericForeign.from(master, entity, alias, join);
        if (action != null) {
            action.accept(it);
        }
        return it;
    }

    /**
     * 创建{@link GenericForeign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link GenericForeign}
     */
    public static <T, M extends ExtCriteria<?>> GenericForeign<T> from(final M master, final Class<T> entity,
                                                                       final Join join,
                                                                       final BiConsumer<M, GenericForeign<T>> action) {
        return GenericForeign.from(master, entity, null, join, action);
    }

    /**
     * 创建{@link GenericForeign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link GenericForeign}
     */
    public static <T, M extends ExtCriteria<?>> GenericForeign<T> from(final M master, final Class<T> entity,
                                                                       final String alias, final Join join,
                                                                       final BiConsumer<M, GenericForeign<T>> action) {
        final GenericForeign<T> it = GenericForeign.from(master, entity, alias, join);
        if (action != null) {
            action.accept(master, it);
        }
        return it;
    }

}
