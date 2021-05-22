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
 * 嵌套子查询条件容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-20
 * @since 1.0.0
 */
public class NestedSubQuery<T> extends AbstractNestedSubQueryCriteria<T, NestedSubQuery<T>> {

    private static final long serialVersionUID = -7419255092099013737L;

    private NestedSubQuery() {
    }

    public NestedSubQuery(ExtCriteria<T> criteria) {
        this(criteria, null);
    }

    public NestedSubQuery(ExtCriteria<T> criteria, String alias) {
        this.entityClass = criteria.getEntityClass();
        this.refCriteria = criteria;
        this.clone(criteria.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
    }

    @Override
    protected NestedSubQuery<T> newInstance() {
        final NestedSubQuery<T> it = new NestedSubQuery<>();
        it.refCriteria = this.refCriteria;
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link NestedSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param <T>      实体类型
     * @return {@link NestedSubQuery}
     */
    public static <T> NestedSubQuery<T> from(final ExtCriteria<T> criteria) {
        return NestedSubQuery.from(criteria, (String) null);
    }

    /**
     * 创建{@link NestedSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param alias    别名
     * @param <T>      实体类型
     * @return {@link NestedSubQuery}
     */
    public static <T> NestedSubQuery<T> from(final ExtCriteria<T> criteria, final String alias) {
        return new NestedSubQuery<>(criteria, alias);
    }

    /**
     * 创建{@link NestedSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param action   {@link Consumer}
     * @param <T>      实体类型
     * @return {@link NestedSubQuery}
     */
    public static <T> NestedSubQuery<T> from(final ExtCriteria<T> criteria,
                                             final Consumer<NestedSubQuery<T>> action) {
        return NestedSubQuery.from(criteria, null, action);
    }

    /**
     * 创建{@link NestedSubQuery}
     * @param criteria {@link ExtCriteria}
     * @param alias    别名
     * @param action   {@link Consumer}
     * @param <T>      实体类型
     * @return {@link NestedSubQuery}
     */
    public static <T> NestedSubQuery<T> from(final ExtCriteria<T> criteria, final String alias,
                                             final Consumer<NestedSubQuery<T>> action) {
        final NestedSubQuery<T> it = NestedSubQuery.from(criteria, alias);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
