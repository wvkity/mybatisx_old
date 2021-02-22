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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.core.condition.basic.Matched;
import com.wvkity.mybatis.core.condition.basic.SelectManager;
import com.wvkity.mybatis.core.condition.basic.group.Group;
import com.wvkity.mybatis.core.condition.basic.group.StandardGroup;
import com.wvkity.mybatis.core.condition.basic.order.Order;
import com.wvkity.mybatis.core.condition.basic.order.StandardOrder;
import com.wvkity.mybatis.core.condition.basic.select.Selection;
import com.wvkity.mybatis.core.condition.basic.select.StandardSelection;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.helper.TableHelper;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.plugin.paging.RangeFetch;
import com.wvkity.mybatis.core.plugin.paging.RangeMode;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.core.utils.Objects;
import com.wvkity.mybatis.executor.resultset.EmbeddedResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 抽象查询条件
 * @param <T> 泛型类
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
    protected boolean usePropertyAsAlias = false;
    /**
     * 查询列管理器
     */
    protected SelectManager selectManager;
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
     * 查询SQL片段
     */
    protected String selectSegment = "";

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

    // endregion

    // region Select columns


    @Override
    public AbstractQueryCriteria<T> select(Selection selection) {
        if (Objects.nonNull(selection) && Objects.isNotBlank(selection.getColumn())) {
            this.selectManager.select(selection);
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
    public AbstractQueryCriteria<T> nativeSelect(String sql, String alias) {
        if (Objects.isNotBlank(sql)) {
            this.selectManager.select(new StandardSelection(null, sql, alias, Matched.IMMEDIATE));
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> exclude(Property<T, ?> property) {
        return this.exclude(this.convert(property));
    }

    @Override
    public AbstractQueryCriteria<T> exclude(String property) {
        if (Objects.isNotBlank(property)) {
            this.selectManager.exclude(property);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colExclude(String column) {
        if (Objects.isNotBlank(column)) {
            this.selectManager.excludeCol(column);
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractQueryCriteria<T> excludes(Property<T, ?>... properties) {
        if (!Objects.isEmpty(properties)) {
            for (Property<T, ?> property : properties) {
                this.exclude(property);
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> excludes(Collection<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (String property : properties) {
                this.exclude(property);
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> colExcludes(Collection<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (String column : columns) {
                this.colExclude(column);
            }
        }
        return this;
    }

    // endregion

    // region Group methods


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
    public AbstractQueryCriteria<T> descWithAlias(String alias, List<String> columns) {
        this.orderBy(StandardOrder.descWithAlias(alias, columns));
        return this;
    }

    // endregion

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
    protected void inits(String alias) {
        super.inits(alias);
        this.selectManager = new SelectManager(this);
    }

    @Override
    public AbstractQueryCriteria<T> as(String alias) {
        this.tableAliasRef.set(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        return this;
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
    public boolean isUsePropertyAsAlias() {
        return this.usePropertyAsAlias;
    }

    @Override
    public AbstractQueryCriteria<T> usePropertyAsAlias(boolean used) {
        this.usePropertyAsAlias = used;
        return this;
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
    public AbstractQueryCriteria<T> mapKey(Property<T, ?> property) {
        return this.mapKey(this.convert(property));
    }

    @Override
    public AbstractQueryCriteria<T> mapKey(String mapKey) {
        this.mapKey = mapKey;
        return this;
    }

    @Override
    public String getSelectSegment() {
        return this.selectManager.getSegment();
    }

    // endregion
}
