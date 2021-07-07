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

import com.github.mybatisx.annotation.Executing;
import com.github.mybatisx.basic.builder.Builder;
import com.github.mybatisx.basic.invoker.Invoker;
import com.github.mybatisx.basic.keyword.ReservedWordRegistry;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.basic.utils.Strings;
import com.github.mybatisx.basic.metadata.Auditor;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Descriptor;
import com.github.mybatisx.basic.metadata.Field;
import com.github.mybatisx.basic.metadata.PrimaryKey;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.text.MessageFormat;

/**
 * 表字段构建器
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public class ColumnBuilder extends AbstractBuilder implements Builder<Column> {

    /**
     * 所属实体类
     */
    private Class<?> entity;
    /**
     * 属性信息
     */
    private Field field;
    /**
     * 属性名
     */
    private String property;
    /**
     * 数据库字段
     */
    private String column;
    /**
     * JAVA类型
     */
    private Class<?> javaType;
    /**
     * JDBC类型
     */
    private JdbcType jdbcType;
    /**
     * 类型处理器
     */
    private Class<? extends TypeHandler<?>> typeHandler = UnknownTypeHandler.class;
    /**
     * 主键序列名
     */
    private String sequence;
    /**
     * 排序
     */
    private String orderBy;
    /**
     * 主键生成方式(根据SQL语句)
     */
    private String generator;
    /**
     * 值
     */
    private Object value;
    /**
     * 获取主键SQL执行时机
     */
    private Executing executing;
    /**
     * 属性get方法调用器
     */
    private Invoker getterInvoker;
    /**
     * 属性set方法调用器
     */
    private Invoker setterInvoker;
    /**
     * 是否为主键
     */
    private boolean primaryKey;
    /**
     * 优先
     */
    private boolean priority;
    /**
     * 主键是否为UUID
     */
    private boolean uuid;
    /**
     * 主键是否为自增
     */
    private boolean identity;
    /**
     * 主键是否为雪花算法ID
     */
    private boolean snowflake;
    /**
     * 字段是否为BLOB类型
     */
    private boolean blob;
    /**
     * 字段是否可保存
     */
    private boolean insertable = true;
    /**
     * 字段是否可更新
     */
    private boolean updatable = true;
    /**
     * SQL语句是否设置JAVA类型
     */
    private boolean useJavaType;
    /**
     * 非null校验
     */
    private boolean checkNotNull;
    /**
     * 字符串空值校验
     */
    private boolean checkNotEmpty;
    /**
     * 是否为乐观锁
     */
    private boolean version;
    /**
     * 是否为多租户
     */
    private boolean multiTenant;
    /**
     * 是否为保存操作时间自动填充标识
     */
    private boolean createdDate;
    /**
     * 是否为保存操作用户ID自动填充标识
     */
    private boolean createdById;
    /**
     * 是否为保存操作用户名自动填充标识
     */
    private boolean createdByName;
    /**
     * 是否为更新操作时间自动填充标识
     */
    private boolean lastModifiedDate;
    /**
     * 是否为更新操作用户ID自动填充标识
     */
    private boolean lastModifiedById;
    /**
     * 是否为更新操作用户名自动填充标识
     */
    private boolean lastModifiedByName;
    /**
     * 是否为删除操作时间自动填充标识
     */
    private boolean deletedDate;
    /**
     * 是否为删除操作用户ID自动填充标识
     */
    private boolean deletedById;
    /**
     * 是否为删除操作用户名自动填充标识
     */
    private boolean deletedByName;
    /**
     * boolean类型属性对应表字段自动添加IS前缀
     */
    private boolean autoAddedIsPrefixed;
    /**
     * 是否为逻辑删除标识
     */
    private boolean logicDelete;
    /**
     * 已删除标识值
     */
    private Object deletedValue;
    /**
     * 未删除标识值
     */
    private Object undeletedValue;

    /**
     * 检查属性是否可审计
     * @return boolean
     */
    public boolean canAuditing() {
        return !this.version && !this.logicDelete && !this.multiTenant;
    }

    @Override
    public Column build() {
        final Descriptor descriptor = new Descriptor(this.field.getOriginalField(), this.javaType,
            this.property, this.field.getGetter(), this.field.getSetter());
        final PrimaryKey primaryKey = new PrimaryKey(this.priority, this.uuid, this.identity, this.snowflake,
            this.generator, this.executing);
        final Auditor auditor = new Auditor(this.version, this.multiTenant, this.createdDate,
            this.createdById, this.createdByName, this.lastModifiedDate,
            this.lastModifiedById, this.lastModifiedByName, this.deletedDate,
            this.deletedById, this.deletedByName, this.logicDelete,
            this.deletedValue, this.undeletedValue);
        return new Column(this.entity, this.property, handleColumnName(), this.jdbcType, this.typeHandler,
            this.javaType, this.sequence, this.primaryKey, this.blob, this.insertable, this.updatable,
            this.useJavaType, this.checkNotNull, this.checkNotEmpty, this.version, this.multiTenant,
            this.logicDelete, descriptor, primaryKey, auditor);
    }

    /**
     * 处理字段名
     * @return 字段名
     */
    private String handleColumnName() {
        final String realColumnName;
        if (Objects.isBlank(this.column)) {
            final String columnName;
            if (this.autoAddedIsPrefixed() && Boolean.class.isAssignableFrom(this.javaType)) {
                if (Strings.isLower(this.property.charAt(0))) {
                    columnName = "is" + Strings.firstCharToUpper(this.property);
                } else {
                    columnName = "Is" + this.property;
                }
            } else {
                columnName = this.property;
            }
            realColumnName = this.namingConverter.propToColumn(columnName, this.strategy);
        } else {
            realColumnName = this.column;
        }
        if (Objects.isNotBlank(this.keyWordFormat) && ReservedWordRegistry.contains(realColumnName)) {
            return MessageFormat.format(this.keyWordFormat, realColumnName);
        }
        return realColumnName;
    }

    /**
     * 检查是否存在主键生成策略
     * @return boolean
     */
    public boolean hasPrimaryKeyStrategy() {
        return this.identity || this.uuid || this.snowflake || Objects.isNotBlank(this.sequence);
    }

    /**
     * 创建字段构建器对象
     * @return {@link ColumnBuilder}
     */
    public static ColumnBuilder create() {
        return new ColumnBuilder();
    }

    public Class<?> entity() {
        return entity;
    }

    public ColumnBuilder entity(Class<?> entity) {
        this.entity = entity;
        return this;
    }

    public Field field() {
        return field;
    }

    public ColumnBuilder field(Field field) {
        this.field = field;
        return this;
    }

    public String property() {
        return property;
    }

    public ColumnBuilder property(String property) {
        this.property = property;
        return this;
    }

    public String column() {
        return column;
    }

    public ColumnBuilder column(String column) {
        this.column = column;
        return this;
    }

    public Class<?> javaType() {
        return javaType;
    }

    public ColumnBuilder javaType(Class<?> javaType) {
        this.javaType = javaType;
        return this;
    }

    public JdbcType jdbcType() {
        return jdbcType;
    }

    public ColumnBuilder jdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
        return this;
    }

    public Class<? extends TypeHandler<?>> typeHandler() {
        return typeHandler;
    }

    public ColumnBuilder typeHandler(Class<? extends TypeHandler<?>> typeHandler) {
        this.typeHandler = typeHandler;
        return this;
    }

    public String sequence() {
        return sequence;
    }

    public ColumnBuilder sequence(String sequence) {
        this.sequence = sequence;
        return this;
    }

    public String orderBy() {
        return orderBy;
    }

    public ColumnBuilder orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String generator() {
        return generator;
    }

    public ColumnBuilder generator(String generator) {
        this.generator = generator;
        return this;
    }

    public Object value() {
        return value;
    }

    public ColumnBuilder value(Object value) {
        this.value = value;
        return this;
    }

    public Executing executing() {
        return executing;
    }

    public ColumnBuilder executing(Executing executing) {
        this.executing = executing;
        return this;
    }

    public Invoker getterInvoker() {
        return getterInvoker;
    }

    public ColumnBuilder getterInvoker(Invoker getterInvoker) {
        this.getterInvoker = getterInvoker;
        return this;
    }

    public Invoker setterInvoker() {
        return setterInvoker;
    }

    public ColumnBuilder setterInvoker(Invoker setterInvoker) {
        this.setterInvoker = setterInvoker;
        return this;
    }

    public boolean primaryKey() {
        return primaryKey;
    }

    public ColumnBuilder primaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public boolean priority() {
        return priority;
    }

    public void priority(boolean priority) {
        this.priority = priority;
    }

    public boolean uuid() {
        return uuid;
    }

    public ColumnBuilder uuid(boolean uuid) {
        this.uuid = uuid;
        return this;
    }

    public boolean identity() {
        return identity;
    }

    public ColumnBuilder identity(boolean identity) {
        this.identity = identity;
        return this;
    }

    public boolean snowflake() {
        return snowflake;
    }

    public ColumnBuilder snowflake(boolean snowflake) {
        this.snowflake = snowflake;
        return this;
    }

    public boolean blob() {
        return blob;
    }

    public ColumnBuilder blob(boolean blob) {
        this.blob = blob;
        return this;
    }

    public boolean insertable() {
        return insertable;
    }

    public ColumnBuilder insertable(boolean insertable) {
        this.insertable = insertable;
        return this;
    }

    public boolean updatable() {
        return updatable;
    }

    public ColumnBuilder updatable(boolean updatable) {
        this.updatable = updatable;
        return this;
    }

    public boolean useJavaType() {
        return useJavaType;
    }

    public ColumnBuilder useJavaType(boolean useJavaType) {
        this.useJavaType = useJavaType;
        return this;
    }

    public boolean checkNotNull() {
        return checkNotNull;
    }

    public ColumnBuilder checkNotNull(boolean checkNotNull) {
        this.checkNotNull = checkNotNull;
        return this;
    }

    public boolean checkNotEmpty() {
        return checkNotEmpty;
    }

    public ColumnBuilder checkNotEmpty(boolean checkNotEmpty) {
        this.checkNotEmpty = checkNotEmpty && String.class.equals(this.javaType);
        return this;
    }

    public boolean version() {
        return version;
    }

    public ColumnBuilder version(boolean version) {
        this.version = version;
        return this;
    }

    public boolean multiTenant() {
        return multiTenant;
    }

    public ColumnBuilder multiTenant(boolean multiTenant) {
        this.multiTenant = multiTenant;
        return this;
    }

    public boolean createdDate() {
        return createdDate;
    }

    public ColumnBuilder createdDate(boolean createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public boolean createdById() {
        return createdById;
    }

    public ColumnBuilder createdById(boolean createdById) {
        this.createdById = createdById;
        return this;
    }

    public boolean createdByName() {
        return createdByName;
    }

    public ColumnBuilder createdByName(boolean createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public boolean lastModifiedDate() {
        return lastModifiedDate;
    }

    public ColumnBuilder lastModifiedDate(boolean lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public boolean lastModifiedById() {
        return lastModifiedById;
    }

    public ColumnBuilder lastModifiedById(boolean lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
        return this;
    }

    public boolean lastModifiedByName() {
        return lastModifiedByName;
    }

    public ColumnBuilder lastModifiedByName(boolean lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
        return this;
    }

    public boolean deletedDate() {
        return deletedDate;
    }

    public ColumnBuilder deletedDate(boolean deletedDate) {
        this.deletedDate = deletedDate;
        return this;
    }

    public boolean deletedById() {
        return deletedById;
    }

    public ColumnBuilder deletedById(boolean deletedById) {
        this.deletedById = deletedById;
        return this;
    }

    public boolean deletedByName() {
        return deletedByName;
    }

    public ColumnBuilder deletedByName(boolean logicDeletedUserName) {
        this.deletedByName = logicDeletedUserName;
        return this;
    }

    public boolean autoAddedIsPrefixed() {
        return autoAddedIsPrefixed;
    }

    public ColumnBuilder autoAddedIsPrefixed(boolean autoAddedIsPrefixed) {
        this.autoAddedIsPrefixed = autoAddedIsPrefixed;
        return this;
    }

    public boolean logicDelete() {
        return logicDelete;
    }

    public ColumnBuilder logicDelete(boolean logicDelete) {
        this.logicDelete = logicDelete;
        return this;
    }

    public Object deletedValue() {
        return deletedValue;
    }

    public ColumnBuilder deletedValue(Object deletedValue) {
        this.deletedValue = deletedValue;
        return this;
    }

    public Object undeletedValue() {
        return undeletedValue;
    }

    public ColumnBuilder undeletedValue(Object undeletedValue) {
        this.undeletedValue = undeletedValue;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnBuilder)) {
            return false;
        }
        ColumnBuilder that = (ColumnBuilder) o;
        return java.util.Objects.equals(entity, that.entity) && java.util.Objects.equals(column, that.column);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(entity, column);
    }
}
