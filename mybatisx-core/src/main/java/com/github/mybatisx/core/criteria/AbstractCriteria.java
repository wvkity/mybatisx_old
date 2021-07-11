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
package com.github.mybatisx.core.criteria;

import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.condition.Criterion;
import com.github.mybatisx.core.condition.NestedCondition;
import com.github.mybatisx.core.expr.StandardNesting;
import com.github.mybatisx.core.expr.SubQueryExpression;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.support.expr.Expression;
import com.github.mybatisx.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 抽象条件包装容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractCriteria<T, C extends CriteriaWrapper<T, C>> extends AbstractExtCriteria<T>
    implements CriteriaWrapper<T, C> {

    private static final Logger log = LoggerFactory.getLogger(AbstractCriteria.class);

    @SuppressWarnings("unchecked")
    protected final C context = (C) this;

    // region Basic methods

    @Override
    protected C self() {
        return this.context;
    }

    /**
     * 并行
     * <pre>{@code
     *     // Example:
     *     final LambdaQuery<Exam> query = LambdaQuery.from(Exam.class)
     *          .select()
     *          .group("studentId", "period", "subjectId", "grade");
     *     query.having(new Sum(query, "SCORE", null), SingleComparator.GT, 1008);
     *
     *     // Parallel example:
     *     final LambdaQuery<Exam> query = LambdaQuery.from(Exam.class)
     *          .select()
     *          .group("studentId", "period", "subjectId", "grade")
     *          .parallel(it -> it.having(new Sum(it, "SCORE", null), SingleComparator.GT, 1008));
     *
     *     }
     * </pre>
     * @param action {@link Consumer}
     * @return {@code this}
     */
    public C parallel(final Consumer<C> action) {
        if (Objects.nonNull(action)) {
            action.accept(this.context);
        }
        return this.self();
    }

    /**
     * {@code Slot.AND}
     * @return {@code this}
     */
    public C and() {
        this.refSlot.set(Slot.AND);
        return this.self();
    }

    /**
     * {@code Slot.OR}
     * @return {@code this}
     */
    public C or() {
        this.refSlot.set(Slot.OR);
        return this.self();
    }

    @Override
    public C strict(boolean throwing) {
        this.notMatchingWithThrows.set(throwing);
        return this.self();
    }

    @Override
    public boolean isFetch() {
        return this.fetch;
    }

    @Override
    public boolean hasSelect() {
        return this.fragmentManager.hasSelect();
    }

    /**
     * 创建实例(需要自行实现，默认返回null)
     * @return {@link C}
     */
    protected C newInstance() {
        // empty
        return null;
    }

    // endregion

    // region Add condition methods

    /**
     * 添加查询条件
     * @param slot   {@link Slot}
     * @param column {@link Column}
     * @param query  {@link ExtCriteria}
     * @param symbol {@link Symbol}
     * @return {@code this}
     */
    protected C addSubCondition(final Slot slot, final Column column,
                                final ExtCriteria<?> query, final Symbol symbol) {
        if (Objects.nonNull(column)) {
            this.addSubCondition(slot, column.getColumn(), query, symbol);
        }
        return this.self();
    }

    /**
     * 添加查询条件
     * @param slot   {@link Slot}
     * @param column 字段名称
     * @param query  {@link ExtCriteria}
     * @param symbol {@link Symbol}
     * @return {@code this}
     */
    protected C addSubCondition(final Slot slot, final String column,
                                final ExtCriteria<?> query, final Symbol symbol) {
        if (Objects.isNotBlank(column)) {
            Objects.requireNonNull(query, "The query condition object cannot be null.");
            this.where(new SubQueryExpression(this, column, query, slot, symbol));
        }
        return this.self();
    }

    @Override
    public C where(Expression<?> expression) {
        Optional.ofNullable(expression).map(it -> {
            it.setIfNecessary(this);
            return this.conditionConverter.convert(it);
        }).ifPresent(this.fragmentManager::where);
        return this.self();
    }

    @Override
    public C where(Expression<?>... expressions) {
        if (Objects.isEmpty(expressions)) {
            if (expressions.length == 1) {
                return this.where(expressions[0]);
            }
            return this.where(Objects.asList(expressions));
        }
        return this.self();
    }

    @Override
    public C where(Collection<Expression<?>> expressions) {
        if (Objects.isNotEmpty(expressions)) {
            for (Expression<?> it : expressions) {
                this.where(it);
            }
        }
        return this.self();
    }

    // endregion

    // region Nesting condition

    @Override
    public C and(boolean not, List<Expression<?>> expressions) {
        return this.where(new StandardNesting(this, not, Slot.AND, expressions));
    }

    @Override
    public C and(boolean not, Criteria<?> criteria, List<Expression<?>> expressions) {
        return this.where(new StandardNesting(criteria, not, Slot.AND, expressions));
    }

    @Override
    public C and(boolean not, Function<C, C> apply) {
        return this.doIt(apply, true, not);
    }

    @Override
    public C or(boolean not, List<Expression<?>> expressions) {
        return this.where(new StandardNesting(this, not, Slot.OR, expressions));
    }

    @Override
    public C or(boolean not, Criteria<?> criteria, List<Expression<?>> expressions) {
        return this.where(new StandardNesting(criteria, not, Slot.OR, expressions));
    }

    @Override
    public C or(boolean not, Function<C, C> apply) {
        return this.doIt(apply, false, not);
    }

    C doIt(final Function<C, C> apply, final boolean and, final boolean not) {
        if (Objects.nonNull(apply)) {
            final C ctx = this.newInstance();
            if (Objects.nonNull(ctx)) {
                final AbstractCriteria<?, ?> instance = (AbstractCriteria<?, ?>) apply.apply(ctx);
                final List<Criterion> conditions = instance.fragmentManager.getConditions();
                if (Objects.isNotEmpty(conditions)) {
                    this.fragmentManager.where(new NestedCondition(not, and ? Slot.AND : Slot.OR, conditions));
                }
            } else {
                log.warn("The `newInstance` method must be overridden({}).", this);
            }
        }
        return this.context;
    }

    // endregion

    // region Extension methods

    /**
     * 更新字段值
     * @param column {@link Column}
     * @param value  值
     * @return {@code this}
     */
    protected C setOfUpdate(final Column column, final Object value) {
        if (Objects.nonNull(column)) {
            if (column.isUpdatable()) {
                this.updateProperties.put(column.getProperty(), column.getColumn());
                this.updateColumnsOfWrap.put(column, value);
                this.updateColumns.add(column.getColumn().toUpperCase(Locale.ENGLISH));
            } else {
                final Table table = TableHelper.getTable(this.entityClass);
                log.warn("The \"" + column.getColumn() + "\" column in the \"" + table.getName()
                    + "(" + this.entityClass.getName() + ")\" table does not support updates and is automatically " +
                    "ignored by the system.");
            }
        }
        return this.context;
    }

    /**
     * 更新字段值
     * @param column 字段名称
     * @param value  值
     * @return {@code this}
     */
    protected C setOfUpdate(final String column, final Object value) {
        if (Objects.isNotBlank(column)) {
            final Column col = this.toColumnOfOrg(column);
            if (Objects.nonNull(col)) {
                this.setOfUpdate(col, value);
            } else {
                this.updateColumns.add(column.toUpperCase(Locale.ENGLISH));
                this.updateColumnsOfOrg.put(column, value);
            }
        }
        return this.context;
    }

    // endregion
}
