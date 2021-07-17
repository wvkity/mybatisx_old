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
import com.github.mybatisx.core.convert.DefaultConditionConverter;
import com.github.mybatisx.core.convert.DefaultParameterConverter;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.sql.DefaultQuerySqlManager;
import com.github.mybatisx.support.constant.Join;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 子外联表查询条件容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public class SubForeign<T> extends AbstractSubForeignQueryCriteria<T, SubForeign<T>> {

    private static final long serialVersionUID = 6031750774691037687L;

    private SubForeign() {
    }

    public SubForeign(ExtCriteria<?> master, ExtCriteria<T> query, String alias, Join join) {
        this.master = master;
        this.refQuery = query;
        this.join = join;
        this.clone(master.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = this.genDefTabAlias();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    @Override
    protected SubForeign<T> newInstance() {
        final SubForeign<T> it = new SubForeign<>();
        it.join = join;
        it.master = master;
        it.refQuery = refQuery;
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link SubForeign}对象
     * @param master {@link ExtCriteria}
     * @param query  {@link ExtCriteria}
     * @param join   {@link Join}
     * @param <T>    实体类型
     * @param <M>    主查询条件对象
     * @return {@link SubForeign}
     */
    public static <T, M extends ExtCriteria<?>> SubForeign<T> from(final M master, final ExtCriteria<T> query,
                                                                   final Join join) {
        return SubForeign.from(master, query, null, join);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param master {@link ExtCriteria}
     * @param query  {@link ExtCriteria}
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @param <M>    主查询条件对象
     * @return {@link SubForeign}
     */
    public static <T, M extends ExtCriteria<?>> SubForeign<T> from(final M master, final ExtCriteria<T> query,
                                                                   final Join join,
                                                                   final Consumer<SubForeign<T>> action) {
        final SubForeign<T> it = SubForeign.from(master, query, join);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

    /**
     * 创建{@link SubForeign}对象
     * @param master {@link ExtCriteria}
     * @param query  {@link ExtCriteria}
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <T>    实体类型
     * @param <M>    主查询条件对象
     * @return {@link SubForeign}
     */
    public static <T, M extends ExtCriteria<?>> SubForeign<T> from(final M master, final ExtCriteria<T> query,
                                                                   final Join join,
                                                                   final BiConsumer<M, SubForeign<T>> action) {
        final SubForeign<T> it = SubForeign.from(master, query, join);
        if (Objects.nonNull(action)) {
            action.accept(master, it);
        }
        return it;
    }

    /**
     * 创建{@link SubForeign}对象
     * @param master {@link ExtCriteria}
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param join   {@link Join}
     * @param <T>    实体类型
     * @param <M>    主查询条件对象
     * @return {@link SubForeign}
     */
    public static <T, M extends ExtCriteria<?>> SubForeign<T> from(final M master, final ExtCriteria<T> query,
                                                                   final String alias, final Join join) {
        return new SubForeign<>(master, query, alias, join);
    }

    /**
     * 创建{@link SubForeign}对象
     * @param master {@link ExtCriteria}
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @param <M>    主查询条件对象
     * @return {@link SubForeign}
     */
    public static <T, M extends ExtCriteria<?>> SubForeign<T> from(final M master, final ExtCriteria<T> query,
                                                                   final String alias, final Join join,
                                                                   final Consumer<SubForeign<T>> action) {
        final SubForeign<T> it = SubForeign.from(master, query, alias, join);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

    /**
     * 创建{@link SubForeign}对象
     * @param master {@link ExtCriteria}
     * @param query  {@link ExtCriteria}
     * @param alias  别名
     * @param join   {@link Join}
     * @param action {@link BiConsumer}
     * @param <T>    实体类型
     * @param <M>    主查询条件对象
     * @return {@link SubForeign}
     */
    public static <T, M extends ExtCriteria<?>> SubForeign<T> from(final M master, final ExtCriteria<T> query,
                                                                   final String alias, final Join join,
                                                                   final BiConsumer<M, SubForeign<T>> action) {
        final SubForeign<T> it = SubForeign.from(master, query, alias, join);
        if (Objects.nonNull(action)) {
            action.accept(master, it);
        }
        return it;
    }

}
