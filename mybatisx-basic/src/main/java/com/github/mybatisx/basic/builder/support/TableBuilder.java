/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
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
package com.github.mybatisx.basic.builder.support;

import com.github.mybatisx.basic.builder.Builder;
import com.github.mybatisx.basic.keyword.ReservedWordRegistry;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.basic.utils.Strings;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;

import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 表映射构建器
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public class TableBuilder extends AbstractBuilder implements Builder<Table> {

    /**
     * 实体类
     */
    private Class<?> entity;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 表名
     */
    private String name;
    /**
     * 表名前缀
     */
    private String prefix;
    /**
     * 别名
     */
    private String alias;
    /**
     * 数据库目录
     */
    private String catalog;
    /**
     * 数据库模式
     */
    private String schema;
    /**
     * 排序
     */
    private String order;
    /**
     * 只有一个主键
     */
    private boolean onlyOneId = true;
    /**
     * 是否存在逻辑删除
     */
    private boolean logicDelete;
    /**
     * 是否存在多租户字段
     */
    private boolean multiTenant;
    /**
     * 主键属性
     */
    private String idProperty;
    /**
     * 主键字段
     */
    private ColumnBuilder idColumn;
    /**
     * 乐观锁字段
     */
    private ColumnBuilder optimisticLockColumn;
    /**
     * 逻辑删除字段
     */
    private ColumnBuilder logicDeleteColumn;
    /**
     * 多租户字段
     */
    private ColumnBuilder multiTenantColumn;
    /**
     * 多个主键字段
     */
    private Set<ColumnBuilder> idColumns = new LinkedHashSet<>();
    /**
     * 所有字段
     */
    private Set<ColumnBuilder> columns = new LinkedHashSet<>();

    /**
     * 创建数据库表构建器对象
     * @return {@link TableBuilder}
     */
    public static TableBuilder create() {
        return new TableBuilder();
    }

    /**
     * 添加列
     * @param column {@link ColumnBuilder}
     * @return {@link TableBuilder}
     */
    public TableBuilder addColumn(final ColumnBuilder column) {
        Optional.ofNullable(column).ifPresent(it -> {
            this.columns.add(column);
            if (it.primaryKey()) {
                this.idColumns.add(column);
            }
        });
        return this;
    }

    /**
     * 检查是否存在主键字段
     * @return boolean
     */
    public boolean hasPrimaryKey() {
        return this.idColumn != null || Objects.isNotEmpty(this.idColumns);
    }

    @Override
    public Table build() {
        final String realPrefix = Strings.nvl(this.prefix, "");
        final String tableName;
        if (Objects.isNotBlank(this.name)) {
            tableName = realPrefix + this.name;
        } else {
            tableName = realPrefix +
                this.namingConverter.entityToTable(this.entity.getSimpleName(), this.strategy);
        }
        final String realTableName;
        if (Objects.isNotBlank(this.keyWordFormat) && ReservedWordRegistry.contains(tableName)) {
            realTableName = MessageFormat.format(this.keyWordFormat, tableName);
        } else {
            realTableName = tableName;
        }
        final String realCatalog = Strings.nvl(this.catalog, "");
        final String realSchema = Strings.nvl(this.schema, "");
        // 处理属性
        Column idColumn = null;
        Column deletionColumn = null;
        Column versionColumn = null;
        Column tenementColumn = null;
        final Set<Column> columnSet = new LinkedHashSet<>(this.columns.size());
        final Set<Column> idColumnSet = new LinkedHashSet<>(this.idColumns.size());
        if (Objects.isNotEmpty(this.columns)) {
            for (ColumnBuilder cb : this.columns) {
                final Column column = cb.build();
                if (column.isUnique()) {
                    if (this.onlyOneId && idColumn == null) {
                        idColumn = column;
                    } else {
                        this.onlyOneId = false;
                    }
                    idColumnSet.add(column);
                } else if (column.isLogicDelete() && deletionColumn == null) {
                    deletionColumn = column;
                } else if (column.isVersion() && versionColumn == null) {
                    versionColumn = column;
                } else if (column.isMultiTenant() && tenementColumn == null) {
                    tenementColumn = column;
                }
                columnSet.add(column);
            }
        }
        return new Table(this.entity, realTableName, this.namespace, realCatalog, realSchema,
            realPrefix, null, this.onlyOneId, this.logicDelete, this.multiTenant,
            Strings.nvl(this.onlyOneId, this.idProperty, null), idColumn, versionColumn, deletionColumn,
            tenementColumn, idColumnSet, columnSet);
    }

    public Class<?> entity() {
        return entity;
    }

    public TableBuilder entity(Class<?> entity) {
        this.entity = entity;
        return this;
    }

    public String namespace() {
        return namespace;
    }

    public TableBuilder namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String name() {
        return name;
    }

    public TableBuilder name(String name) {
        this.name = name;
        return this;
    }

    public String prefix() {
        return prefix;
    }

    public TableBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String alias() {
        return alias;
    }

    public TableBuilder alias(String alias) {
        this.alias = alias;
        return this;
    }

    public String catalog() {
        return catalog;
    }

    public TableBuilder catalog(String catalog) {
        this.catalog = catalog;
        return this;
    }

    public String schema() {
        return schema;
    }

    public TableBuilder schema(String schema) {
        this.schema = schema;
        return this;
    }

    public String order() {
        return order;
    }

    public TableBuilder order(String order) {
        this.order = order;
        return this;
    }

    public boolean onlyOneId() {
        return onlyOneId;
    }

    public TableBuilder onlyOneId(boolean onlyOneId) {
        this.onlyOneId = onlyOneId;
        return this;
    }

    public boolean logicDelete() {
        return logicDelete;
    }

    public TableBuilder logicDelete(boolean logicDelete) {
        this.logicDelete = logicDelete;
        return this;
    }

    public boolean multiTenant() {
        return multiTenant;
    }

    public TableBuilder multiTenant(boolean multiTenant) {
        this.multiTenant = multiTenant;
        return this;
    }

    public String idProperty() {
        return idProperty;
    }

    public TableBuilder idProperty(String idProperty) {
        this.idProperty = idProperty;
        return this;
    }

    public ColumnBuilder idColumn() {
        return idColumn;
    }

    public TableBuilder idColumn(ColumnBuilder idColumn) {
        this.idColumn = idColumn;
        return this;
    }

    public ColumnBuilder optimisticLockColumn() {
        return optimisticLockColumn;
    }

    public TableBuilder optimisticLockColumn(ColumnBuilder optimisticLockColumn) {
        this.optimisticLockColumn = optimisticLockColumn;
        return this;
    }

    public ColumnBuilder logicDeleteColumn() {
        return logicDeleteColumn;
    }

    public TableBuilder logicDeleteColumn(ColumnBuilder logicDeleteColumn) {
        this.logicDeleteColumn = logicDeleteColumn;
        return this;
    }

    public ColumnBuilder multiTenantColumn() {
        return multiTenantColumn;
    }

    public TableBuilder multiTenantColumn(ColumnBuilder multiTenantColumn) {
        this.multiTenantColumn = multiTenantColumn;
        return this;
    }

    public Set<ColumnBuilder> idColumns() {
        return idColumns;
    }

    public TableBuilder idColumns(Set<ColumnBuilder> idColumns) {
        this.idColumns = idColumns;
        return this;
    }

    public Set<ColumnBuilder> columns() {
        return columns;
    }

    public TableBuilder columns(Set<ColumnBuilder> columns) {
        this.columns = columns;
        return this;
    }
}
