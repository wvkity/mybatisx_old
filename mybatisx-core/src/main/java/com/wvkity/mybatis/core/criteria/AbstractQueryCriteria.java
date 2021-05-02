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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.annotation.NamingStrategy;
import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.basic.func.AggFunc;
import com.wvkity.mybatis.core.basic.func.Avg;
import com.wvkity.mybatis.core.basic.func.Count;
import com.wvkity.mybatis.core.basic.func.Function;
import com.wvkity.mybatis.core.basic.func.Max;
import com.wvkity.mybatis.core.basic.func.Min;
import com.wvkity.mybatis.core.basic.func.NativeFunction;
import com.wvkity.mybatis.core.basic.func.Sum;
import com.wvkity.mybatis.core.basic.group.Group;
import com.wvkity.mybatis.core.basic.group.StandardGroup;
import com.wvkity.mybatis.core.basic.having.Having;
import com.wvkity.mybatis.core.basic.having.MultiComparator;
import com.wvkity.mybatis.core.basic.having.MultiHaving;
import com.wvkity.mybatis.core.basic.having.NativeHaving;
import com.wvkity.mybatis.core.basic.having.SingleComparator;
import com.wvkity.mybatis.core.basic.having.SingleHaving;
import com.wvkity.mybatis.core.basic.order.FuncOrder;
import com.wvkity.mybatis.core.basic.order.NativeOrder;
import com.wvkity.mybatis.core.basic.order.Order;
import com.wvkity.mybatis.core.basic.order.StandardOrder;
import com.wvkity.mybatis.core.basic.select.FuncSelection;
import com.wvkity.mybatis.core.basic.select.Selection;
import com.wvkity.mybatis.core.basic.select.StandardSelection;
import com.wvkity.mybatis.core.basic.select.SubSelection;
import com.wvkity.mybatis.core.plugin.paging.RangeFetch;
import com.wvkity.mybatis.core.plugin.paging.RangeMode;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.executor.resultset.EmbeddedResult;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.constant.Join;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.helper.TableHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 抽象查询条件
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractQueryCriteria<T> extends AbstractCriteria<T> implements
    QueryWrapper<T, AbstractQueryCriteria<T>>, RangeFetch, EmbeddedResult {

    // region Basic fields

    /**
     * 是否使用属性名作为别名
     */
    protected boolean propAsAlias = false;
    /**
     * 结果集
     */
    protected String resultMap;
    /**
     * 返回值类型
     */
    protected Class<?> resultType;
    /**
     * Map结果key值
     */
    protected String mapKey;
    /**
     * Map实现类
     */
    @SuppressWarnings("rawtypes")
    protected Class<? extends Map> mapType;
    /**
     * 查询SQL片段
     */
    protected String selectSegment = Constants.EMPTY;
    /**
     * 抓取关联表数据(嵌套子查询/联表查询)
     */
    protected boolean fetch;
    /**
     * 继承子查询属性名
     */
    protected boolean propInherit;
    /**
     * 所有字段分组
     */
    protected boolean groupAll;
    /**
     * 查询是否包含聚合函数
     */
    protected boolean containsFunc = true;
    /**
     * 是否仅仅查询聚合函数
     */
    protected boolean onlyFunc;
    /**
     * 联表引用属性
     */
    protected AtomicReference<String> reference = new AtomicReference<>(Constants.EMPTY);

    // region RangeFetch fields

    /**
     * 起始位置
     */
    protected long rowStart;
    /**
     * 结束位置
     */
    protected long rowEnd;
    /**
     * 起始页码
     */
    protected long pageStart;
    /**
     * 结束页码
     */
    protected long pageEnd;
    /**
     * 每页数目
     */
    protected long pageSize;

    // endregion

    // region Foreign criteria

    protected final Set<AbstractForeignCriteria<T, ?>> foreignSet = new CopyOnWriteArraySet<>();
    protected final Set<AbstractSubCriteria<?>> subQuerySet = new CopyOnWriteArraySet<>();

    // endregion

    // region SubQuery methods

    @Override
    public <S> SubQuery<S> newQuery(Class<S> entity, String alias) {
        final SubQuery<S> instance = new SubQuery<>(this, entity, alias);
        this.subQueryCache(instance);
        return instance;
    }

    @Override
    public NestedSubQuery<T> nestQuery(String alias) {
        return this.nestQuery(this, alias);
    }

    @Override
    public NestedSubQuery<T> nestQuery(AbstractQueryCriteria<T> query, String alias) {
        final NestedSubQuery<T> instance = new NestedSubQuery<>(query, alias);
        instance.select();
        instance.useAlias();
        this.subQueryCache(instance);
        return instance;
    }

    /**
     * 缓存子查询对象
     * @param subQuery {@link SubQuery}
     * @param <S>      实体类型
     */
    private <S> void subQueryCache(final AbstractSubCriteria<S> subQuery) {
        this.subQuerySet.add(subQuery);
    }

    // endregion

    // region Foreign methods

    @Override
    public <S> Foreign<T, S> join(Class<S> entity, Join join, String alias) {
        final Foreign<T, S> instance = new Foreign<>(this, entity, join, alias);
        this.foreignCache(instance);
        return instance;
    }

    /**
     * 缓存联表查询对象
     * @param foreign {@link Foreign}
     * @param <S>     实体类型
     */
    private <S> void foreignCache(final AbstractForeignCriteria<T, S> foreign) {
        foreign.useAlias();
        foreign.getMaster().useAlias();
        this.foreignSet.add(foreign);
    }

    // endregion

    // region SubQuery foreign

    @Override
    public <S> SubForeign<T, S> join(AbstractSubCriteria<S> subQuery, Join join, String alias) {
        final SubForeign<T, S> instance = new SubForeign<>(this, subQuery, join, alias);
        this.foreignCache(instance);
        return instance;
    }

    // endregion

    // endregion

    // region Select columns


    @Override
    public AbstractQueryCriteria<T> select(Selection selection) {
        if (Objects.nonNull(selection) && Objects.isNotBlank(selection.getColumn())) {
            this.segmentManager.select(selection);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> filtrate(Predicate<Column> accept) {
        final List<Column> columns = TableHelper.getColumns(this.entityClass, accept);
        if (Objects.isNotEmpty(columns)) {
            for (Column column : columns) {
                if (Objects.nonNull(column)) {
                    this.select(new StandardSelection(this, null, column.getColumn(), null,
                        column.getProperty(), Matched.STANDARD));
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> select(Property<T, ?> property, String alias) {
        return this.select(this.convert(property), alias);
    }

    @Override
    public AbstractQueryCriteria<T> selects(Collection<Property<T, ?>> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Property<T, ?> property : properties) {
                if (Objects.nonNull(property)) {
                    this.select(property);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> select(String property, String alias) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.select(new StandardSelection(this, null, column.getColumn(), alias,
                column.getProperty(), Matched.STANDARD));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selects(String... properties) {
        if (!Objects.isEmpty(properties)) {
            for (String property : properties) {
                if (Objects.isNotBlank(property)) {
                    this.select(property);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selects(Map<String, String> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                final String property = entry.getValue();
                if (Objects.isNotBlank(property)) {
                    this.select(property, entry.getKey());
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colSelect(String column, String alias) {
        if (Objects.isNotBlank(column)) {
            this.select(new StandardSelection(this, column, alias, Matched.IMMEDIATE));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colSelects(Collection<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (String column : columns) {
                if (Objects.isNotBlank(column)) {
                    this.colSelect(column);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colSelects(Map<String, String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                final String column = entry.getValue();
                if (Objects.isNotBlank(column)) {
                    this.colSelect(column, entry.getKey());
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> select(AbstractQueryCriteria<?> query, String alias) {
        if (query != null) {
            this.segmentManager.select(new SubSelection(query, alias));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> nativeSelect(String sql, String alias) {
        if (Objects.isNotBlank(sql)) {
            this.segmentManager.select(new StandardSelection(null, sql, alias, Matched.IMMEDIATE));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> ignore(Property<T, ?> property) {
        return this.ignore(this.convert(property));
    }

    @Override
    public AbstractQueryCriteria<T> ignore(String property) {
        if (Objects.isNotBlank(property)) {
            this.segmentManager.exclude(property);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colIgnore(String column) {
        if (Objects.isNotBlank(column)) {
            this.segmentManager.excludeCol(column);
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractQueryCriteria<T> ignores(Property<T, ?>... properties) {
        if (!Objects.isEmpty(properties)) {
            for (Property<T, ?> property : properties) {
                this.ignore(property);
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> ignores(Collection<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (String property : properties) {
                this.ignore(property);
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colIgnores(Collection<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (String column : columns) {
                this.colIgnore(column);
            }
        }
        return this;
    }

    // endregion

    // region Agg function methods

    // region Count function methods

    @Override
    public AbstractQueryCriteria<T> count(String alias) {
        final Column id = this.getId();
        if (Objects.nonNull(id)) {
            this.func(new Count(this, id.getColumn(), alias, false));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> count(Property<T, ?> property, String alias, boolean distinct) {
        return this.count(this.convert(property), alias, distinct);
    }

    @Override
    public AbstractQueryCriteria<T> count(String property, String alias, boolean distinct) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.func(new Count(this, column.getColumn(), alias, distinct));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colCount(String column, String alias, boolean distinct) {
        if (Objects.isNotBlank(column)) {
            this.func(new Count(this, column, alias, distinct));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colCount(String tableAlias, String column, String alias, boolean distinct) {
        if (Objects.isNotBlank(column)) {
            this.func(new Count(tableAlias, column, alias, distinct));
        }
        return this;
    }

    // endregion

    // region Sum function methods

    @Override
    public AbstractQueryCriteria<T> sum(Property<T, ?> property, String alias, Integer scale, boolean distinct) {
        return this.sum(this.convert(property), alias, scale, distinct);
    }

    @Override
    public AbstractQueryCriteria<T> sum(String property, String alias, Integer scale, boolean distinct) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.func(new Sum(this, column.getColumn(), alias, scale, distinct));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colSum(String tableAlias, String column, String alias, Integer scale,
                                           boolean distinct) {
        if (Objects.isNotBlank(column)) {
            this.func(new Sum(tableAlias, column, alias, scale, distinct));
        }
        return this;
    }

    // endregion

    // region Avg function methods

    @Override
    public AbstractQueryCriteria<T> avg(Property<T, ?> property, String alias, Integer scale, boolean distinct) {
        return this.avg(this.convert(property), alias, scale, distinct);
    }

    @Override
    public AbstractQueryCriteria<T> avg(String property, String alias, Integer scale, boolean distinct) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.func(new Avg(this, column.getColumn(), alias, scale, distinct));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colAvg(String tableAlias, String column, String alias, Integer scale,
                                           boolean distinct) {
        if (Objects.isNotBlank(column)) {
            this.func(new Avg(tableAlias, column, alias, scale, distinct));
        }
        return this;
    }

    // endregion

    // region Min function methods

    @Override
    public AbstractQueryCriteria<T> min(Property<T, ?> property, String alias, Integer scale) {
        return this.min(this.convert(property), alias, scale);
    }

    @Override
    public AbstractQueryCriteria<T> min(String property, String alias, Integer scale) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.func(new Min(this, column.getColumn(), alias, scale));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colMin(String tableAlias, String column, String alias, Integer scale) {
        if (Objects.isNotBlank(column)) {
            this.func(new Min(tableAlias, column, alias, scale));
        }
        return this;
    }

    // endregion

    // region Max function methods

    @Override
    public AbstractQueryCriteria<T> max(Property<T, ?> property, String alias, Integer scale) {
        return this.max(this.convert(property), alias, scale);
    }

    @Override
    public AbstractQueryCriteria<T> max(String property, String alias, Integer scale) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.func(new Max(this, column.getColumn(), alias, scale));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colMax(String tableAlias, String column, String alias, Integer scale) {
        if (Objects.isNotBlank(column)) {
            this.func(new Max(tableAlias, column, alias, scale));
        }
        return this;
    }

    // endregion

    // region All function methods

    @Override
    public AbstractQueryCriteria<T> func(Property<T, ?> property, String aliasPrefix, Integer scale) {
        return this.func(this.convert(property), aliasPrefix, scale);
    }

    @Override
    public AbstractQueryCriteria<T> func(String property, String aliasPrefix, Integer scale) {
        final Column it;
        if (Objects.nonNull((it = this.findColumn(property)))) {
            this.colFunc(it.getColumn(), aliasPrefix, scale);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colFunc(String column, String aliasPrefix, Integer scale) {
        if (Objects.isNotBlank(column)) {
            this.func(new Count(this, column, this.toAlias(AggFunc.COUNT, aliasPrefix), false));
            this.func(new Sum(this, column, this.toAlias(AggFunc.SUM, aliasPrefix), scale));
            this.func(new Avg(this, column, this.toAlias(AggFunc.AVG, aliasPrefix), scale));
            this.func(new Min(this, column, this.toAlias(AggFunc.MIN, aliasPrefix), scale));
            this.func(new Max(this, column, this.toAlias(AggFunc.MAX, aliasPrefix), scale));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colFunc(String tableAlias, String column, String aliasPrefix, Integer scale) {
        if (Objects.isNotBlank(column)) {
            this.func(new Count(tableAlias, column, this.toAlias(AggFunc.COUNT, aliasPrefix), false));
            this.func(new Sum(tableAlias, column, this.toAlias(AggFunc.SUM, aliasPrefix), scale));
            this.func(new Avg(tableAlias, column, this.toAlias(AggFunc.AVG, aliasPrefix), scale));
            this.func(new Min(tableAlias, column, this.toAlias(AggFunc.MIN, aliasPrefix), scale));
            this.func(new Max(tableAlias, column, this.toAlias(AggFunc.MAX, aliasPrefix), scale));
        }
        return this;
    }

    String toAlias(final AggFunc func, final String aliasPrefix) {
        final String funcName = func.getSegment();
        if (Objects.isNotBlank(aliasPrefix)) {
            if (aliasPrefix.endsWith(Constants.UNDER_LINE)) {
                return aliasPrefix + funcName;
            } else {
                return aliasPrefix + NamingStrategy.UPPER.to(NamingStrategy.UPPER_CAMEL, funcName);
            }
        }
        return "_" + funcName;
    }

    // endregion

    @Override
    public AbstractQueryCriteria<T> nativeFunc(String funcBody, String alias) {
        if (Objects.isNotBlank(funcBody)) {
            return this.func(new NativeFunction(this, funcBody, alias));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> func(Function function) {
        if (Objects.nonNull(function)) {
            this.select(new FuncSelection(function));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> func(Collection<Function> functions) {
        if (Objects.isNotEmpty(functions)) {
            for (Function it : functions) {
                this.func(it);
            }
        }
        return this;
    }

    // endregion

    // region Group methods

    @Override
    public AbstractQueryCriteria<T> group(boolean groupAll) {
        this.groupAll = groupAll;
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colGroup(String column) {
        if (Objects.isNotBlank(column)) {
            this.groupBy(StandardGroup.group(this, column));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colGroup(Collection<String> columns) {
        this.groupBy(StandardGroup.group(this, columns));
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> groupWithAlias(String alias, Collection<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            this.groupBy(StandardGroup.groupWithAlias(alias, columns));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> group(Property<T, ?> property) {
        return this.group(this.convert(property));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractQueryCriteria<T> group(Property<T, ?>... properties) {
        return this.group(this.convert(Objects.asList(properties)));
    }

    @Override
    public AbstractQueryCriteria<T> group(String property) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.groupBy(StandardGroup.group(this, column.getColumn()));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> group(Collection<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            final List<String> columns = properties.stream().map(this::findColumn)
                .filter(Objects::nonNull).map(Column::getColumn).collect(Collectors.toList());
            this.groupBy(StandardGroup.group(this, columns));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> groupBy(Group group) {
        this.segmentManager.groupBy(group);
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> groupBy(Collection<Group> groups) {
        this.segmentManager.groupBy(groups);
        return this;
    }


    // endregion

    // region Having methods

    // region Single having methods

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, String alias, Object value) {
        final Function func;
        if (Objects.nonNull((func = this.getFunc(alias)))) {
            this.having(slot, func, value);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, Function function, Object value) {
        if (Objects.nonNull(function)) {
            this.having(new SingleHaving(this, function, slot,
                this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, value)));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, String alias, SingleComparator comparator, Object value) {
        final Function func;
        if (Objects.nonNull((func = this.getFunc(alias)))) {
            this.having(slot, func, comparator, value);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, Function function, SingleComparator comparator, Object value) {
        if (Objects.nonNull(function)) {
            this.having(new SingleHaving(this, function, slot, comparator,
                this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, value)));
        }
        return this;
    }

    // endregion

    // region Multi having methods

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, String alias, Object first, Object last) {
        final Function func;
        if (Objects.nonNull((func = this.getFunc(alias)))) {
            this.having(slot, func, first, last);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, Function function, Object first, Object last) {
        if (Objects.nonNull(function)) {
            this.having(new MultiHaving(this, function, slot,
                this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, first),
                this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, last)));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, String alias, MultiComparator comparator,
                                           Object first, Object last) {
        final Function func;
        if (Objects.nonNull((func = this.getFunc(alias)))) {
            this.having(slot, func, comparator, first, last);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Slot slot, Function function, MultiComparator comparator,
                                           Object first, Object last) {
        if (Objects.nonNull(function)) {
            this.having(new MultiHaving(this, function, slot, comparator,
                this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, first),
                this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, last)));
        }
        return this;
    }

    // endregion

    // region Native having methods

    @Override
    public AbstractQueryCriteria<T> nativeHaving(String func, Object value) {
        if (Objects.isNotBlank(func)) {
            this.having(new NativeHaving(func, this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, true, value)));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> nativeHaving(String func, List<Object> values) {
        if (Objects.isNotBlank(func)) {
            this.having(new NativeHaving(func, this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, values)));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> nativeHaving(String func, Map<String, Object> values) {
        if (Objects.isNotBlank(func)) {
            this.having(new NativeHaving(func, this.placeholder(DEF_PARAMETER_PLACEHOLDER_SAFE, values)));
        }
        return this;
    }

    // endregion

    @Override
    public AbstractQueryCriteria<T> having(Having having) {
        this.segmentManager.having(having);
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> having(Collection<Having> havingList) {
        this.segmentManager.having(havingList);
        return this;
    }

    // endregion

    // region Sort methods

    // region Asc sort methods

    @Override
    public AbstractQueryCriteria<T> asc(Property<T, ?> property) {
        return this.asc(this.convert(property));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractQueryCriteria<T> asc(Property<T, ?>... properties) {
        return this.asc(this.convert(Objects.asList(properties)));
    }

    @Override
    public AbstractQueryCriteria<T> asc(String property) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.orderBy(StandardOrder.asc(this, column.getColumn()));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> asc(List<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            final List<String> columns = properties.stream().map(this::findColumn)
                .filter(Objects::nonNull).map(Column::getColumn).collect(Collectors.toList());
            this.orderBy(StandardOrder.asc(this, columns));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colAsc(String column) {
        if (Objects.isNotBlank(column)) {
            this.orderBy(StandardOrder.asc(this, column));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colAsc(List<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            this.orderBy(StandardOrder.asc(this, columns));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> funcAsc(String alias) {
        final Function func;
        if (Objects.nonNull(func = this.getFunc(alias))) {
            this.funcAsc(func);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> funcAsc(List<String> aliases) {
        if (Objects.isNotEmpty(aliases)) {
            final List<Function> functions = aliases.stream().map(this::getFunc)
                .filter(Objects::nonNull).collect(Collectors.toList());
            this.orderBy(FuncOrder.asc(functions));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> funcAsc(Function function) {
        if (Objects.nonNull(function)) {
            this.orderBy(FuncOrder.asc(function));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> ascWithAlias(String alias, List<String> columns) {
        this.orderBy(StandardOrder.ascWithAlias(alias, columns));
        return this;
    }

    // endregion

    // region Desc sort methods

    @Override
    public AbstractQueryCriteria<T> desc(Property<T, ?> property) {
        return this.desc(this.convert(property));
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractQueryCriteria<T> desc(Property<T, ?>... properties) {
        return this.desc(this.convert(Objects.asList(properties)));
    }

    @Override
    public AbstractQueryCriteria<T> desc(String property) {
        final Column column = this.findColumn(property);
        if (Objects.nonNull(column)) {
            this.orderBy(StandardOrder.desc(this, column.getColumn()));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> desc(List<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            final List<String> columns = properties.stream().filter(Objects::isNotBlank)
                .map(this::findColumn).filter(Objects::nonNull).map(Column::getColumn).collect(Collectors.toList());
            this.orderBy(StandardOrder.desc(this, columns));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colDesc(String column) {
        this.orderBy(StandardOrder.desc(this, column));
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colDesc(List<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            this.orderBy(StandardOrder.desc(this, columns));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> funcDesc(String alias) {
        final Function func;
        if (Objects.nonNull(func = this.getFunc(alias))) {
            this.funcDesc(func);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> funcDesc(List<String> aliases) {
        if (Objects.isNotEmpty(aliases)) {
            final List<Function> functions = aliases.stream().map(this::getFunc)
                .filter(Objects::nonNull).collect(Collectors.toList());
            this.orderBy(FuncOrder.desc(functions));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> funcDesc(Function function) {
        if (Objects.nonNull(function)) {
            this.orderBy(FuncOrder.desc(function));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> descWithAlias(String alias, List<String> columns) {
        this.orderBy(StandardOrder.descWithAlias(alias, columns));
        return this;
    }

    // endregion

    @Override
    public AbstractQueryCriteria<T> nativeOrder(String orderBy) {
        if (Objects.isNotBlank(orderBy)) {
            this.orderBy(new NativeOrder(orderBy));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> orderBy(Order order) {
        this.segmentManager.orderBy(order);
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> orderBy(List<Order> orders) {
        this.segmentManager.orderBy(orders);
        return this;
    }

    // endregion

    // region RangeFetch methods

    @Override
    public AbstractQueryCriteria<T> range(long start, long end) {
        this.rowStart = Math.max(0L, Math.min(start, end));
        this.rowEnd = Math.max(0L, Math.max(start, end));
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> range(long start, long end, long size) {
        this.pageStart = Math.max(0L, Math.min(start, end));
        this.pageEnd = Math.max(0L, Math.max(start, end));
        this.pageSize = Math.max(0L, size);
        return this;
    }

    @Override
    public boolean isRange() {
        return this.getMode() != RangeMode.NONE;
    }

    @Override
    public RangeMode getMode() {
        if (this.rowStart >= 0 && this.rowEnd > 0) {
            return RangeMode.SCOPE;
        } else if (this.pageStart > 0 && this.pageEnd > 0) {
            return RangeMode.PAGEABLE;
        }
        return RangeMode.NONE;
    }

    @Override
    public long getRowStart() {
        return this.rowStart;
    }

    @Override
    public long getRowEnd() {
        return this.rowEnd;
    }

    @Override
    public long getPageStart() {
        return this.pageStart;
    }

    @Override
    public long getPageEnd() {
        return this.pageEnd;
    }

    @Override
    public long getPageSize() {
        return this.pageSize <= 0 ? 20L : this.pageSize;
    }

    // endregion

    // region Other methods

    @Override
    protected void initialize(String alias) {
        super.initialize(alias);
    }

    @Override
    public AbstractQueryCriteria<T> reference(final String reference) {
        this.reference.compareAndSet(Constants.EMPTY, Objects.isBlank(reference) ? Constants.EMPTY : reference);
        return this;
    }

    @Override
    public String getReference() {
        return this.reference.get();
    }

    @Override
    public String alias() {
        return this.tableAliasRef.get();
    }

    /**
     * 继承子查询属性
     * @return {@link AbstractQueryCriteria}
     */
    public AbstractQueryCriteria<T> propInherit() {
        return this.propInherit(true);
    }

    /**
     * 是否继承子查询属性
     * @param inherit 是否继承
     * @return {@link AbstractQueryCriteria}
     */
    public AbstractQueryCriteria<T> propInherit(final boolean inherit) {
        this.propInherit = inherit;
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> as(String alias) {
        final boolean isNotEmpty = Objects.isNotBlank(alias);
        this.tableAliasRef.set(isNotEmpty ? alias : Constants.EMPTY);
        return this.useAlias(isNotEmpty);
    }

    @Override
    public String as() {
        return this.as(this instanceof AbstractNestedSubCriteria || this instanceof AbstractSubForeignCriteria);
    }

    /**
     * 获取表别名
     * @param forceUse 是否强制使用
     * @return 表别名
     */
    public String as(final boolean forceUse) {
        if (forceUse || this.useAlias.get()) {
            final String alias = this.tableAliasRef.get();
            return Objects.isBlank(alias) ? this.defTableAlias : alias;
        }
        return Constants.EMPTY;
    }

    @Override
    public AbstractQueryCriteria<T> useAlias() {
        return this.useAlias(true);
    }

    @Override
    public AbstractQueryCriteria<T> useAlias(boolean used) {
        this.useAlias.compareAndSet(!used, used);
        return this;
    }

    @Override
    public boolean isPropAsAlias() {
        return this.propAsAlias;
    }

    @Override
    public AbstractQueryCriteria<T> usePropAlias(boolean used) {
        this.propAsAlias = used;
        return this;
    }

    @Override
    public boolean isFetch() {
        return this.fetch;
    }

    @Override
    public AbstractQueryCriteria<T> containsFunc(boolean contains) {
        this.containsFunc = contains;
        return this;
    }

    @Override
    public boolean isContainsFunc() {
        return this.containsFunc;
    }

    @Override
    public AbstractQueryCriteria<T> onlyFunc(boolean only) {
        this.onlyFunc = only;
        return this;
    }

    @Override
    public boolean isOnlyFunc() {
        return this.onlyFunc;
    }

    @Override
    public Function getFunc(String alias) {
        if (Objects.isNotBlank(alias)) {
            final FuncSelection fs = this.segmentManager.getFunc(alias);
            if (Objects.nonNull(fs)) {
                return fs.getFunction();
            }
        }
        return null;
    }

    @Override
    public String getResultMap() {
        return this.resultMap;
    }

    @Override
    public AbstractQueryCriteria<T> resultMap(String resultMap) {
        this.resultMap = resultMap;
        return this;
    }

    @Override
    public Class<?> getResultType() {
        return this.resultType;
    }

    @Override
    public AbstractQueryCriteria<T> resultType(Class<?> resultType) {
        this.resultType = resultType;
        return this;
    }

    @Override
    public String getMapKey() {
        return this.mapKey;
    }

    @Override
    public AbstractQueryCriteria<T> mapKey() {
        final Column id = this.getId();
        if (this.propAsAlias) {
            return this.mapKey(id.getProperty());
        }
        return this.mapKey(id);
    }

    @Override
    public AbstractQueryCriteria<T> mapKey(Property<T, ?> property) {
        final String prop = this.convert(property);
        if (this.propAsAlias) {
            return this.mapKey(prop);
        }
        return this.mapKey(this.findColumn(prop));
    }

    AbstractQueryCriteria<T> mapKey(final Column column) {
        if (column != null) {
            this.mapKey(column.getColumn());
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> mapKey(String mapKey) {
        this.mapKey = mapKey;
        return this;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends Map> getMapType() {
        return this.mapType;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public AbstractQueryCriteria<T> mapType(Class<? extends Map> mapImplClass) {
        this.mapType = mapImplClass;
        return this;
    }

    @Override
    public boolean isHasCondition() {
        return super.isHasCondition() || this.groupAll || Objects.isNotEmpty(this.foreignSet);
    }

    @Override
    public String completeString() {
        final String sql = this.intactString();
        if (DEF_PATTERN_PM.matcher(sql).matches()) {
            return sql.replaceAll("#\\{((?!#\\{).)*}", "?");
        }
        return sql;
    }

    @Override
    protected String intactString() {
        final StringBuilder builder = new StringBuilder(150);
        builder.append("SELECT");
        final String selectStr = this.getSelectSegment();
        builder.append(Constants.SPACE).append(selectStr.trim());
        builder.append(Constants.SPACE).append("FROM");
        final String tableStr = this.getTableName();
        builder.append(Constants.SPACE).append(tableStr.trim());
        final String whereStr = this.getWhereSegment();
        if (Objects.isNotBlank(whereStr)) {
            builder.append(Constants.SPACE).append(whereStr.trim());
        }
        return builder.toString();
    }

    /**
     * 获取查询列
     * @return {@link Selection}列表
     */
    protected List<Selection> getSelects() {
        return this.segmentManager.getSelects();
    }

    /**
     * 获取查询字段列表字符串
     * @return 查询字段列表字符串
     */
    protected String getSelectString() {
        return this.segmentManager.getSelectString();
    }

    /**
     * 加载查询字段
     * @param query {@link AbstractQueryCriteria}
     */
    protected void loadSelectionFrom(final AbstractQueryCriteria<?> query) {
        if (this.fetch && !this.segmentManager.hasSelect()) {
            if (query.isFetch() || query.segmentManager.hasSelect()) {
                final List<Selection> selects = query.getSelects();
                if (Objects.isNotEmpty(selects)) {
                    for (Selection it : selects) {
                        final String alias = it.as();
                        final String column = it.getColumn();
                        if (this.propInherit && Objects.isBlank(alias)) {
                            this.colSelect(column, it.getProperty());
                        } else {
                            final String realColumn = Objects.isNotBlank(alias) ? alias : column;
                            this.colSelect(realColumn);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getSelectSegment() {
        if (Objects.isNotEmpty(this.foreignSet)) {
            final List<String> segments = new ArrayList<>(this.foreignSet.size() + 1);
            final String segment = this.getSelectString();
            if (Objects.isNotBlank(segment)) {
                segments.add(segment);
            }
            for (AbstractForeignCriteria<?, ?> fc : this.foreignSet) {
                if (fc.isFetch() || fc.segmentManager.hasSelect()) {
                    final String selectStr = fc.getSelectString();
                    if (Objects.isNotBlank(selectStr)) {
                        segments.add(selectStr);
                    }
                }
            }
            if (Objects.isNotEmpty(segments)) {
                this.selectSegment = String.join(Constants.COMMA_SPACE, segments);
                return this.selectSegment;
            }
        }
        return this.getSelectString();
    }

    @Override
    public String getWhereSegment() {
        final String condition = super.getWhereSegment(this.groupAll ? this.getGroupByString() : null);
        if (Objects.isNotEmpty(this.foreignSet)) {
            final List<String> segments = new ArrayList<>(this.foreignSet.size() + 1);
            for (AbstractForeignCriteria<?, ?> fc : this.foreignSet) {
                final String fcc = fc.getWhereSegment();
                if (Objects.isNotBlank(fcc)) {
                    segments.add(fcc);
                }
            }
            segments.add(condition);
            return String.join(Constants.SPACE, segments);
        }
        return condition;
    }

    /**
     * 获取分组字符串
     * @return 分组字符串
     */
    protected String getGroupByString() {
        if (Objects.isNotEmpty(this.foreignSet)) {
            final List<String> segments = new ArrayList<>(this.foreignSet.size() + 1);
            segments.add(this.segmentManager.getSelectString(false));
            for (AbstractForeignCriteria<?, ?> fc : this.foreignSet) {
                final String segment = fc.getGroupByString();
                if (Objects.isNotBlank(segment)) {
                    segments.add(segment);
                }
            }
            return segments.size() == 1 ? segments.get(0) : String.join(Constants.COMMA_SPACE, segments);
        }
        return this.segmentManager.getSelectString(false);
    }

    // endregion

}
