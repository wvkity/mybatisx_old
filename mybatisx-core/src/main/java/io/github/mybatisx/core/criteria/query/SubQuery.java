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
package io.github.mybatisx.core.criteria.query;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.convert.DefaultConditionConverter;
import io.github.mybatisx.core.convert.DefaultParameterConverter;
import io.github.mybatisx.core.criteria.ExtCriteria;
import io.github.mybatisx.core.sql.DefaultQuerySqlManager;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 子查询条件容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-20
 * @since 1.0.0
 */
public class SubQuery<T> extends AbstractCommonSubQueryCriteria<T, SubQuery<T>> {

    private static final long serialVersionUID = -7419255092099013737L;

    private SubQuery() {
    }

    public SubQuery(ExtCriteria<T> query) {
        this(query, null);
    }

    public SubQuery(ExtCriteria<T> query, String alias) {
        this.entityClass = query.getEntityClass();
        this.refQuery = query;
        this.refCriteria = query;
        this.clone(query.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = this.genDefTabAlias();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.sqlManager = new DefaultQuerySqlManager(this, this.refQuery, this.foreignSet, this.fragmentManager);
    }

    @Override
    protected SubQuery<T> newInstance() {
        final SubQuery<T> it = new SubQuery<>();
        it.refCriteria = this.refCriteria;
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link SubQuery}
     * @param query {@link ExtCriteria}
     * @param <T>      实体类型
     * @return {@link SubQuery}
     */
    public static <T> SubQuery<T> from(final ExtCriteria<T> query) {
        return SubQuery.from(query, (String) null);
    }

    /**
     * 创建{@link SubQuery}
     * @param query {@link ExtCriteria}
     * @param alias    别名
     * @param <T>      实体类型
     * @return {@link SubQuery}
     */
    public static <T> SubQuery<T> from(final ExtCriteria<T> query, final String alias) {
        return new SubQuery<>(query, alias);
    }

    /**
     * 创建{@link SubQuery}
     * @param query {@link ExtCriteria}
     * @param action   {@link Consumer}
     * @param <T>      实体类型
     * @return {@link SubQuery}
     */
    public static <T> SubQuery<T> from(final ExtCriteria<T> query,
                                       final Consumer<SubQuery<T>> action) {
        return SubQuery.from(query, null, action);
    }

    /**
     * 创建{@link SubQuery}
     * @param query {@link ExtCriteria}
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <T>      实体类型
     * @return {@link SubQuery}
     */
    public static <T> SubQuery<T> from(final ExtCriteria<T> query, final String alias,
                                       final Consumer<SubQuery<T>> action) {
        final SubQuery<T> it = SubQuery.from(query, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
