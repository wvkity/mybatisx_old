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

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.core.sql.DefaultQuerySqlManager;
import com.github.mybatisx.support.constant.Join;
import com.github.mybatisx.core.convert.DefaultConditionConverter;
import com.github.mybatisx.core.convert.DefaultParameterConverter;
import com.github.mybatisx.core.criteria.ExtCriteria;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 联表查询条件容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public class Foreign<T> extends AbstractCommonForeignQueryCriteria<T, Foreign<T>> {

    private static final long serialVersionUID = 4874419211919783871L;

    private Foreign() {
    }

    public Foreign(ExtCriteria<?> master, Class<T> entity, Join join) {
        this(master, entity, null, join);
    }

    public Foreign(ExtCriteria<?> master, Class<T> entity, String alias, Join join) {
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
    protected Foreign<T> newInstance() {
        final Foreign<T> it = new Foreign<>();
        it.master = this.master;
        it.join = this.join;
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link Foreign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param join   {@link Join}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link Foreign}
     */
    public static <T, M extends ExtCriteria<?>> Foreign<T> from(final M master, final Class<T> entity,
                                                                final Join join) {
        return Foreign.from(master, entity, null, join);
    }

    /**
     * 创建{@link Foreign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link Foreign}
     */
    public static <T, M extends ExtCriteria<?>> Foreign<T> from(final M master, final Class<T> entity,
                                                                final String alias, final Join join) {
        return new Foreign<>(master, entity, alias, join);
    }

    /**
     * 创建{@link Foreign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link Foreign}
     */
    public static <T, M extends ExtCriteria<?>> Foreign<T> from(final M master, final Class<T> entity,
                                                                final Join join,
                                                                final Consumer<Foreign<T>> action) {
        return Foreign.from(master, entity, null, join, action);
    }

    /**
     * 创建{@link Foreign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link Foreign}
     */
    public static <T, M extends ExtCriteria<?>> Foreign<T> from(final M master, final Class<T> entity,
                                                                final String alias, final Join join,
                                                                final Consumer<Foreign<T>> action) {
        final Foreign<T> it = Foreign.from(master, entity, alias, join);
        if (action != null) {
            action.accept(it);
        }
        return it;
    }

    /**
     * 创建{@link Foreign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link Foreign}
     */
    public static <T, M extends ExtCriteria<?>> Foreign<T> from(final M master, final Class<T> entity,
                                                                final Join join,
                                                                final BiConsumer<M, Foreign<T>> action) {
        return Foreign.from(master, entity, null, join, action);
    }

    /**
     * 创建{@link Foreign}
     * @param master {@link ExtCriteria}
     * @param entity 实体类
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <T>    实体类型
     * @param <M>    主条件对象
     * @return {@link Foreign}
     */
    public static <T, M extends ExtCriteria<?>> Foreign<T> from(final M master, final Class<T> entity,
                                                                final String alias, final Join join,
                                                                final BiConsumer<M, Foreign<T>> action) {
        final Foreign<T> it = Foreign.from(master, entity, alias, join);
        if (action != null) {
            action.accept(master, it);
        }
        return it;
    }

}
