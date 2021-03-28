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
package com.wvkity.mybatis.basic.metadata;

import com.wvkity.mybatis.basic.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.basic.immutable.ImmutableLinkedSet;
import com.wvkity.mybatis.basic.utils.Objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 数据库表映射
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public class Table {

    /**
     * 实体类
     */
    private final Class<?> entity;
    /**
     * 数据库表名
     */
    private final String name;
    /**
     * Mapper接口命名空间
     */
    private final String namespace;
    /**
     * 数据库目录
     */
    private final String catalog;
    /**
     * 数据库模式
     */
    private final String schema;
    /**
     * 数据库表名前缀
     */
    private final String prefix;
    /**
     * 排序
     */
    private final String order;
    /**
     * 仅有一个主键
     */
    private final boolean onlyOneId;
    /**
     * 是否存在逻辑删除字段
     */
    private final boolean logicDelete;
    /**
     * 是否存在多租户字段
     */
    private final boolean multiTenant;
    /**
     * 主键属性
     */
    private final String idProperty;
    /**
     * 主键字段
     */
    private final Column idColumn;
    /**
     * 是否存在主键
     */
    private final boolean hasPrimaryKey;
    /**
     * 乐观锁字段
     */
    private final Column optimisticLockColumn;
    /**
     * 逻辑删除字段
     */
    private final Column logicalDeleteColumn;
    /**
     * 多租户字段
     */
    private final Column multiTenantColumn;
    /**
     * 属性-字段对象缓存
     */
    private final Map<String, Column> propertyColumnCache = new ConcurrentHashMap<>();
    /**
     * 属性-字段对象缓存(有序只读)
     */
    private final Map<String, Column> readOnlyPropertyColumnCache;
    /**
     * 主键集合
     */
    private final Set<Column> idColumns;
    /**
     * 所有字段缓存
     */
    private final Set<Column> columns;
    /**
     * 所有字段缓存(只读)
     */
    private final Set<Column> readOnlyColumnCache;
    /**
     * 可保存字段缓存(只读)
     */
    private final Set<Column> readOnlyInsertableColumnCache;
    /**
     * 可更新字段缓存(只读)
     */
    private final Set<Column> readOnlyUpdatableColumnCache;
    /**
     * 构造方法
     */
    private final MethodHandle constructMethod;

    public Table(Class<?> entity, String name, String namespace, String catalog,
                 String schema, String prefix, String order, boolean onlyOneId, boolean logicDelete,
                 boolean multiTenant, String idProperty, Column idColumn,
                 Column optimisticLockColumn, Column logicalDeleteColumn,
                 Column multiTenantColumn, Set<Column> idColumns, Set<Column> columns) {
        this.entity = entity;
        this.name = name;
        this.namespace = namespace;
        this.catalog = catalog;
        this.schema = schema;
        this.prefix = prefix;
        this.order = order;
        this.onlyOneId = onlyOneId;
        this.logicDelete = logicDelete;
        this.multiTenant = multiTenant;
        this.idProperty = idProperty;
        this.idColumn = idColumn;
        this.optimisticLockColumn = optimisticLockColumn;
        this.logicalDeleteColumn = logicalDeleteColumn;
        this.multiTenantColumn = multiTenantColumn;
        this.idColumns = idColumns == null ? ImmutableLinkedSet.of() : ImmutableLinkedSet.of(idColumns);
        this.hasPrimaryKey = !this.idColumns.isEmpty();
        this.columns = new LinkedHashSet<>(columns);
        this.readOnlyColumnCache = ImmutableLinkedSet.of(this.columns);
        this.readOnlyPropertyColumnCache = this.init();
        this.readOnlyInsertableColumnCache = ImmutableLinkedSet.of(this.filtrate(Column::isInsertable));
        this.readOnlyUpdatableColumnCache = ImmutableLinkedSet.of(this.filtrate(Column::isUpdatable));
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle handle = null;
        try {
            handle = lookup.findConstructor(this.entity, MethodType.methodType(void.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            // throw new MyBatisException(e.getMessage(), e);
            // ignore
        }
        this.constructMethod = handle;
    }

    /**
     * 初始化
     * @return 属性-字段{@link ImmutableLinkedMap}
     */
    private Map<String, Column> init() {
        if (Objects.isNotEmpty(this.columns)) {
            final Map<String, Column> ret = new LinkedHashMap<>();
            this.readOnlyColumnCache.forEach(it -> {
                this.propertyColumnCache.put(it.getProperty(), it);
                ret.put(it.getProperty(), it);
            });
            return ImmutableLinkedMap.of(ret);
        }
        return ImmutableLinkedMap.of();
    }

    /**
     * 创建实例对象
     * @param <T> 泛型类
     * @return 实例对象
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance() throws Exception {
        if (this.constructMethod != null) {
            try {
                return (T) this.constructMethod.invokeWithArguments();
            } catch (Throwable e) {
                return this.newInstanceOfOld();
            }
        } else {
            return this.newInstanceOfOld();
        }
    }

    /**
     * 创建实例对象
     * @param <T> 泛型类
     * @return 实例对象
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    private <T> T newInstanceOfOld() throws Exception {
        try {
            return (T) this.entity.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException
            | InvocationTargetException e1) {
            if (e1 instanceof IllegalAccessException) {
                final Constructor<?> constructor = this.entity.getDeclaredConstructor();
                constructor.setAccessible(true);
                return (T) constructor.newInstance();
            }
            throw e1;
        }
    }

    /**
     * 获取表全名
     * @return 表名
     */
    public String getFullName() {
        if (Objects.isNotBlank(this.catalog)) {
            return this.catalog + "." + this.name;
        } else if (Objects.isNotBlank(this.schema)) {
            return this.schema + "." + this.name;
        }
        return this.name;
    }

    /**
     * 筛选字段
     * @param filter {@link Predicate}
     * @return 字段集合
     */
    public Set<Column> filtrate(final Predicate<Column> filter) {
        return this.readOnlyColumnCache.stream().filter(filter).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 获取所有字段
     * @return 字段集合
     */
    public Set<Column> columns() {
        return this.readOnlyColumnCache;
    }

    /**
     * 获取所有可保存字段
     * @return 可保存字段集合
     */
    public Set<Column> insertableColumns() {
        return this.readOnlyInsertableColumnCache;
    }

    /**
     * 获取所有可更新字段
     * @return 可更新字段集合
     */
    public Set<Column> updatableColumns() {
        return this.readOnlyUpdatableColumnCache;
    }

    /**
     * 排除逻辑删除标识、逻辑删除审计、保存审计、多租户、乐观锁等特殊字段
     * @return 字段集合
     */
    public Set<Column> updateColumnsNonWithSpecial() {
        return this.readOnlyUpdatableColumnCache.stream().filter(it ->
            !it.isLogicDelete() && !it.isVersion() && !it.getAuditor().insertedAuditable()
                && !it.getAuditor().deletedAuditable()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 获取所有属性-字段映射信息
     * @return {@link ImmutableLinkedMap}字段集合
     */
    public Map<String, Column> propertyMappingColumns() {
        return this.readOnlyPropertyColumnCache;
    }

    public Class<?> getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getOrder() {
        return order;
    }

    public boolean isOnlyOneId() {
        return onlyOneId;
    }

    public boolean isLogicDelete() {
        return logicDelete;
    }

    public boolean isMultiTenant() {
        return multiTenant;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public Column getIdColumn() {
        return idColumn;
    }

    public boolean isHasPrimaryKey() {
        return !hasPrimaryKey;
    }

    public Column getOptimisticLockColumn() {
        return optimisticLockColumn;
    }

    public Column getLogicalDeleteColumn() {
        return logicalDeleteColumn;
    }

    public Column getMultiTenantColumn() {
        return multiTenantColumn;
    }

    public Set<Column> getIdColumns() {
        return idColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table table = (Table) o;
        return onlyOneId == table.onlyOneId &&
            logicDelete == table.logicDelete &&
            multiTenant == table.multiTenant &&
            java.util.Objects.equals(entity, table.entity) &&
            java.util.Objects.equals(name, table.name) &&
            java.util.Objects.equals(namespace, table.namespace) &&
            java.util.Objects.equals(catalog, table.catalog) &&
            java.util.Objects.equals(schema, table.schema) &&
            java.util.Objects.equals(prefix, table.prefix) &&
            java.util.Objects.equals(order, table.order) &&
            java.util.Objects.equals(idProperty, table.idProperty) &&
            java.util.Objects.equals(idColumn, table.idColumn) &&
            java.util.Objects.equals(optimisticLockColumn, table.optimisticLockColumn) &&
            java.util.Objects.equals(logicalDeleteColumn, table.logicalDeleteColumn) &&
            java.util.Objects.equals(multiTenantColumn, table.multiTenantColumn) &&
            java.util.Objects.equals(propertyColumnCache, table.propertyColumnCache) &&
            java.util.Objects.equals(readOnlyPropertyColumnCache, table.readOnlyPropertyColumnCache) &&
            java.util.Objects.equals(idColumns, table.idColumns) &&
            java.util.Objects.equals(columns, table.columns) &&
            java.util.Objects.equals(readOnlyColumnCache, table.readOnlyColumnCache) &&
            java.util.Objects.equals(readOnlyInsertableColumnCache, table.readOnlyInsertableColumnCache) &&
            java.util.Objects.equals(readOnlyUpdatableColumnCache, table.readOnlyUpdatableColumnCache);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(entity, name, namespace, catalog, schema, prefix, order,
            onlyOneId, logicDelete, multiTenant, idProperty, idColumn, optimisticLockColumn,
            logicalDeleteColumn, multiTenantColumn, propertyColumnCache,
            readOnlyPropertyColumnCache, idColumns, columns, readOnlyColumnCache, readOnlyInsertableColumnCache,
            readOnlyUpdatableColumnCache);
    }

    @Override
    public String toString() {
        return "Table{" +
            "entity=" + entity +
            ", name='" + name + '\'' +
            ", namespace='" + namespace + '\'' +
            ", catalog='" + catalog + '\'' +
            ", schema='" + schema + '\'' +
            ", prefix='" + prefix + '\'' +
            ", order='" + order + '\'' +
            ", onlyOneId=" + onlyOneId +
            ", logicDelete=" + logicDelete +
            ", multiTenant=" + multiTenant +
            ", idProperty='" + idProperty + '\'' +
            ", idColumn=" + idColumn +
            ", hasPrimaryKey=" + hasPrimaryKey +
            ", optimisticLockColumn=" + optimisticLockColumn +
            ", logicalDeleteColumn=" + logicalDeleteColumn +
            ", multiTenantColumn=" + multiTenantColumn +
            ", propertyColumnCache=" + propertyColumnCache +
            ", readOnlyPropertyColumnCache=" + readOnlyPropertyColumnCache +
            ", idColumns=" + idColumns +
            ", columns=" + columns +
            ", readOnlyColumnCache=" + readOnlyColumnCache +
            ", readOnlyInsertableColumnCache=" + readOnlyInsertableColumnCache +
            ", readOnlyUpdatableColumnCache=" + readOnlyUpdatableColumnCache +
            '}';
    }
}
