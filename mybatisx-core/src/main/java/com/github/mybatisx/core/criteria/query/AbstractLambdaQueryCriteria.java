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
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.criteria.support.AbstractLambdaCriteria;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.core.support.func.Avg;
import com.github.mybatisx.core.support.func.Count;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.core.support.func.Max;
import com.github.mybatisx.core.support.func.Min;
import com.github.mybatisx.core.support.func.NativeFunction;
import com.github.mybatisx.core.support.func.Sum;
import com.github.mybatisx.core.support.group.Group;
import com.github.mybatisx.core.support.group.NativeGroup;
import com.github.mybatisx.core.support.group.StandardGroup;
import com.github.mybatisx.core.support.having.Having;
import com.github.mybatisx.core.support.having.MultiComparator;
import com.github.mybatisx.core.support.having.MultiHaving;
import com.github.mybatisx.core.support.having.NativeHaving;
import com.github.mybatisx.core.support.having.SingleComparator;
import com.github.mybatisx.core.support.having.SingleHaving;
import com.github.mybatisx.core.support.order.FuncOrder;
import com.github.mybatisx.core.support.order.NativeOrder;
import com.github.mybatisx.core.support.order.Order;
import com.github.mybatisx.core.support.order.StandardOrder;
import com.github.mybatisx.core.support.select.FuncSelection;
import com.github.mybatisx.core.support.select.NativeSelection;
import com.github.mybatisx.core.support.select.Selection;
import com.github.mybatisx.core.support.select.StandardSelection;
import com.github.mybatisx.core.support.select.SubSelection;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 抽象基础条件/查询容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractLambdaQueryCriteria<T, C extends LambdaQueryCriteria<T, C>> extends
    AbstractLambdaCriteria<T, C> implements LambdaQueryCriteria<T, C> {

    // region Basic methods

    @Override
    public C reference(final String reference) {
        this.reference.compareAndSet(Constants.EMPTY, Objects.isBlank(reference) ? Constants.EMPTY : reference);
        return this.self();
    }

    @Override
    public C distinct(final boolean distinct) {
        this.distinct = distinct;
        return this.self();
    }

    @Override
    public C containsFunc(final boolean contains) {
        this.containsFunc = contains;
        return this.self();
    }

    @Override
    public C onlyFunc(final boolean only) {
        this.onlyFunc = only;
        return this.self();
    }

    @Override
    public C useTabAlias(boolean using) {
        this.useAlias.set(using);
        return this.self();
    }

    @Override
    public C usePropAlias(final boolean using) {
        this.propAsAlias = using;
        return this.self();
    }

    @Override
    public C keepOrderBy(boolean keep) {
        this.keepOrderBy = keep;
        return this.self();
    }

    @Override
    public C resultMap(final String resultMap) {
        this.resultMap = resultMap;
        return this.self();
    }

    @Override
    public C resultType(final Class<?> resultType) {
        this.resultType = resultType;
        return this.self();
    }

    @Override
    public C mapKey() {
        return this.self();
    }

    @Override
    public C mapKey(final Property<T, ?> property) {
        final String prop = this.toProperty(property);
        if (this.propAsAlias) {
            return this.mapKey(prop);
        }
        return this.mapKey(this.toColumn(prop));
    }

    @Override
    public C mapKey(final String mapKey) {
        this.mapKey = mapKey;
        return this.self();
    }

    /**
     * 设置map键
     * @param column {@link Column}
     * @return {@code this}
     */
    protected C mapKey(final Column column) {
        if (column != null) {
            this.mapKey(column.getColumn());
        }
        return this.self();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public C mapType(final Class<? extends Map> mapImplClass) {
        this.mapType = mapImplClass;
        return this.self();
    }

    @Override
    public C rangeWithRow(final long rowStart, final long rowEnd) {
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        return this.self();
    }

    @Override
    public C rangeWithPage(final long pageStart, final long pageEnd, final long pageSize) {
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        this.pageSize = pageSize;
        return this.self();
    }

    @Override
    public C foreign(ExtCriteria<?> query) {
        this.addForeign(query);
        return this.self();
    }

    // endregion

    // region Select column methods

    @Override
    public C select() {
        this.fetch = true;
        return this.self();
    }

    @Override
    public C select(ExtCriteria<?> query, String alias) {
        if (Objects.nonNull(query)) {
            this.select(new SubSelection(this, query, alias));
        }
        return this.self();
    }

    @Override
    public C select(Selection selection) {
        this.fragmentManager.select(selection);
        return this.self();
    }

    @Override
    public C nativeSelect(String sql, String alias) {
        if (Objects.isNotBlank(sql)) {
            this.select(new NativeSelection(this, sql, alias));
        }
        return this.self();
    }

    @Override
    public C select(Property<T, ?> property, String alias) {
        return this.select(this.toProperty(property), alias);
    }

    @Override
    public C select(String property, String alias) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.select(new StandardSelection(this, null, column.getColumn(), alias,
                column.getProperty(), Matched.STANDARD));
        }
        return this.self();
    }

    @Override
    public C ignore(Property<T, ?> property) {
        return this.ignore(this.toProperty(property));
    }

    @Override
    public C ignore(String property) {
        this.fragmentManager.exclude(property);
        return this.self();
    }

    // endregion

    // region Aggregation function methods

    @Override
    public C count(String alias) {
        return this.function(new Count(this, "*", alias, false));
    }

    @Override
    public C count(Property<T, ?> property, String alias, boolean distinct) {
        return this.count(this.toProperty(property), alias, distinct);
    }

    @Override
    public C count(String property, String alias, boolean distinct) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.function(new Count(this, column.getColumn(), alias, distinct));
        }
        return this.self();
    }

    @Override
    public C sum(Property<T, ?> property, String alias, Integer scale, boolean distinct) {
        return this.sum(this.toProperty(property), alias, scale, distinct);
    }

    @Override
    public C sum(String property, String alias, Integer scale, boolean distinct) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.function(new Sum(this, column.getColumn(), alias, scale, distinct));
        }
        return this.self();
    }

    @Override
    public C avg(Property<T, ?> property, String alias, Integer scale, boolean distinct) {
        return this.avg(this.toProperty(property), alias, scale, distinct);
    }

    @Override
    public C avg(String property, String alias, Integer scale, boolean distinct) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.function(new Avg(this, column.getColumn(), alias, scale, distinct));
        }
        return this.self();
    }

    @Override
    public C min(Property<T, ?> property, String alias, Integer scale) {
        return this.min(this.toProperty(property), alias, scale);
    }

    @Override
    public C min(String property, String alias, Integer scale) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.function(new Min(this, column.getColumn(), alias, scale));
        }
        return this.self();
    }

    @Override
    public C max(Property<T, ?> property, String alias, Integer scale) {
        return this.max(this.toProperty(property), alias, scale);
    }

    @Override
    public C max(String property, String alias, Integer scale) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.function(new Max(this, column.getColumn(), alias, scale));
        }
        return this.self();
    }

    @Override
    public C func(Property<T, ?> property, String aliasPrefix, Integer scale, boolean distinct) {
        return this.func(this.toProperty(property), aliasPrefix, scale, distinct);
    }

    @Override
    public C func(String property, String aliasPrefix, Integer scale, boolean distinct) {
        Optional.ofNullable(this.toColumn(property)).ifPresent(it ->
            this.genFunctions(this, null, it.getColumn(), aliasPrefix, scale, distinct).stream()
                .filter(Objects::nonNull).forEach(this::function));
        return this.self();
    }

    @Override
    public C nativeFunc(String funcBody, String alias) {
        if (Objects.isNotBlank(funcBody)) {
            this.function(new NativeFunction(this, funcBody, alias));
        }
        return this.self();
    }

    @Override
    public C function(Function function) {
        if (Objects.nonNull(function)) {
            this.select(new FuncSelection(function));
        }
        return this.self();
    }

    // endregion

    // region Group by methods

    @Override
    public C group(Property<T, ?> property) {
        return this.group(this.toProperty(property));
    }

    @Override
    public C group(String property) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.group(StandardGroup.group(this, column.getColumn()));
        }
        return this.self();
    }

    @Override
    public C group(Collection<String> properties) {
        return this.group(StandardGroup.group(this, this.toColumnList(properties)));
    }

    @Override
    public C group(boolean all) {
        this.groupAll = all;
        return this.self();
    }

    @Override
    public C nativeGroup(String groupBody) {
        if (Objects.isNotBlank(groupBody)) {
            this.group(new NativeGroup(groupBody));
        }
        return this.self();
    }

    @Override
    public C group(Group group) {
        this.fragmentManager.groupBy(group);
        return this.self();
    }

    // endregion

    // region Order by methods

    @Override
    public C asc(Property<T, ?> property) {
        return this.asc(this.toProperty(property));
    }

    @Override
    public C asc(String property) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.order(StandardOrder.asc(this, column.getColumn()));
        }
        return this.self();
    }

    @Override
    public C asc(List<String> properties) {
        return this.order(StandardOrder.asc(this, this.toColumnList(properties)));
    }

    @Override
    public C funcAsc(String funcAlias) {
        return this.order(FuncOrder.asc(this.getFunction(funcAlias)));
    }

    @Override
    public C funcAsc(List<String> funcAliases) {
        return this.order(FuncOrder.asc(this.genFunctions(funcAliases)));
    }

    @Override
    public C desc(Property<T, ?> property) {
        return this.desc(this.toProperty(property));
    }

    @Override
    public C desc(String property) {
        final Column column = this.toColumn(property);
        if (Objects.nonNull(column)) {
            this.order(StandardOrder.desc(this, column.getColumn()));
        }
        return this.self();
    }

    @Override
    public C desc(List<String> properties) {
        return this.order(StandardOrder.desc(this, this.toColumnList(properties)));
    }

    @Override
    public C funcDesc(String funcAlias) {
        return this.order(FuncOrder.desc(this.getFunction(funcAlias)));
    }

    @Override
    public C funcDesc(List<String> funcAliases) {
        return this.order(FuncOrder.desc(this.genFunctions(funcAliases)));
    }

    @Override
    public C nativeOrder(String orderBy) {
        if (Objects.isNotBlank(orderBy)) {
            this.order(new NativeOrder(orderBy));
        }
        return this.self();
    }

    @Override
    public C order(Order order) {
        this.fragmentManager.orderBy(order);
        return this.self();
    }

    @Override
    public C order(List<Order> orders) {
        this.fragmentManager.orderBy(orders);
        return this.self();
    }

    // endregion

    // region Having methods

    @Override
    public C having(Slot slot, String funcAlias, SingleComparator comparator, Object value) {
        return this.having(slot, this.getFunction(funcAlias), comparator, value);
    }

    @Override
    public C having(Slot slot, Function function, SingleComparator comparator, Object value) {
        if (Objects.nonNull(function)) {
            this.having(new SingleHaving(this, function, slot, comparator,
                this.parameterConverter.convert(DEF_PARAMETER_PLACEHOLDER_SAFE, value)));
        }
        return this.self();
    }

    @Override
    public C having(Slot slot, String funcAlias, MultiComparator comparator, Object first, Object last) {
        return this.having(slot, this.getFunction(funcAlias), comparator, first, last);
    }

    @Override
    public C having(Slot slot, Function function, MultiComparator comparator, Object first, Object last) {
        if (Objects.nonNull(function)) {
            this.having(new MultiHaving(this, function, slot, comparator,
                this.parameterConverter.convert(DEF_PARAMETER_PLACEHOLDER_SAFE, first),
                this.parameterConverter.convert(DEF_PARAMETER_PLACEHOLDER_SAFE, last)));
        }
        return this.self();
    }

    @Override
    public C nativeHaving(String havingBody) {
        if (Objects.isNotBlank(havingBody)) {
            this.having(new NativeHaving(havingBody));
        }
        return this.self();
    }

    @Override
    public C nativeHaving(String havingBody, Object value) {
        if (Objects.isNotBlank(havingBody)) {
            this.having(new NativeHaving(havingBody, this.parameterConverter.convert(DEF_PARAMETER_PLACEHOLDER_SAFE,
                value)));
        }
        return this.self();
    }

    @Override
    public C nativeHaving(String havingBody, List<Object> args) {
        if (Objects.isNotBlank(havingBody)) {
            this.having(new NativeHaving(havingBody, this.parameterConverter.converts(DEF_PARAMETER_PLACEHOLDER_SAFE,
                args)));
        }
        return this.self();
    }

    @Override
    public C nativeHaving(String havingBody, Map<String, Object> args) {
        if (Objects.isNotBlank(havingBody)) {
            this.having(new NativeHaving(havingBody, this.parameterConverter.converts(DEF_PARAMETER_PLACEHOLDER_SAFE,
                args)));
        }
        return this.self();
    }

    @Override
    public C having(Having having) {
        this.fragmentManager.having(having);
        return this.self();
    }

    @Override
    public C having(Collection<Having> havingList) {
        this.fragmentManager.having(havingList);
        return this.self();
    }

    // endregion

}
