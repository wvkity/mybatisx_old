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
package com.wvkity.mybatis.basic.builder.support;

import com.wvkity.mybatis.annotation.Executing;
import com.wvkity.mybatis.basic.builder.Builder;
import com.wvkity.mybatis.basic.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.basic.invoker.Invoker;
import com.wvkity.mybatis.basic.keyword.ReservedWordRegistry;
import com.wvkity.mybatis.basic.metadata.Auditor;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Descriptor;
import com.wvkity.mybatis.basic.metadata.Field;
import com.wvkity.mybatis.basic.metadata.PrimaryKey;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.basic.utils.Strings;
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
    private boolean tenant;
    /**
     * 是否为保存操作时间自动填充标识
     */
    private boolean createdDate;
    /**
     * 是否为保存操作用户ID自动填充标识
     */
    private boolean createdUserId;
    /**
     * 是否为保存操作用户名自动填充标识
     */
    private boolean createdUserName;
    /**
     * 是否为更新操作时间自动填充标识
     */
    private boolean lastModifiedDate;
    /**
     * 是否为更新操作用户ID自动填充标识
     */
    private boolean lastModifiedUserId;
    /**
     * 是否为更新操作用户名自动填充标识
     */
    private boolean lastModifiedUserName;
    /**
     * 是否为删除操作时间自动填充标识
     */
    private boolean logicDeletedDate;
    /**
     * 是否为删除操作用户ID自动填充标识
     */
    private boolean logicDeletedUserId;
    /**
     * 是否为删除操作用户名自动填充标识
     */
    private boolean logicDeletedUserName;
    /**
     * boolean类型属性对应表字段自动添加IS前缀
     */
    private boolean autoAddedIsPrefixed;
    /**
     * 是否为逻辑删除标识
     */
    private boolean logicalDelete;
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
        return !this.version && !this.logicalDelete && !this.tenant;
    }

    @Override
    public Column build() {
        final Descriptor descriptor = new Descriptor(this.field.getOriginalField(), this.javaType,
            this.property, this.field.getGetter(), this.field.getSetter());
        final PrimaryKey primaryKey = new PrimaryKey(this.priority, this.uuid, this.identity, this.snowflake,
            this.generator, this.executing);
        final Auditor auditor = new Auditor(this.version, this.tenant, this.createdDate,
            this.createdUserId, this.createdUserName, this.lastModifiedDate,
            this.lastModifiedUserId, this.lastModifiedUserName, this.logicDeletedDate,
            this.logicDeletedUserId, this.logicDeletedUserName, this.logicalDelete,
            this.deletedValue, this.undeletedValue);
        return new Column(this.entity, this.property, handleColumnName(), this.jdbcType, this.typeHandler,
            this.javaType, this.sequence, this.primaryKey, this.blob, this.insertable, this.updatable,
            this.useJavaType, this.checkNotNull, this.checkNotEmpty, this.version, this.tenant,
            this.logicalDelete, descriptor, primaryKey, auditor);
    }

    /**
     * 处理字段名
     * @return 字段名
     */
    private String handleColumnName() {
        final String realColumnName;
        if (Objects.isBlank(this.column)) {
            final String columnName;
            if (this.isAutoAddedIsPrefixed() && Boolean.class.isAssignableFrom(this.javaType)) {
                if (Strings.isLower(this.property.charAt(0))) {
                    columnName = "is" + Strings.firstCharToUpper(this.property);
                } else {
                    columnName = "Is" + this.property;
                }
            } else {
                columnName = this.property;
            }
            realColumnName = this.physicalNamingConverter.propToColumn(columnName, this.strategy);
        } else {
            realColumnName = this.column;
        }
        final String format = getValue(MyBatisGlobalConfiguration::getKeyWordFormat, null);
        if (Objects.isNotBlank(format) && ReservedWordRegistry.contains(realColumnName)) {
            return MessageFormat.format(format, realColumnName);
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

    public Class<?> getEntity() {
        return entity;
    }

    public ColumnBuilder setEntity(Class<?> entity) {
        this.entity = entity;
        return this;
    }

    public Field getField() {
        return field;
    }

    public ColumnBuilder setField(Field field) {
        this.field = field;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public ColumnBuilder setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public ColumnBuilder setColumn(String column) {
        this.column = column;
        return this;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public ColumnBuilder setJavaType(Class<?> javaType) {
        this.javaType = javaType;
        return this;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public ColumnBuilder setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
        return this;
    }

    public Class<? extends TypeHandler<?>> getTypeHandler() {
        return typeHandler;
    }

    public ColumnBuilder setTypeHandler(Class<? extends TypeHandler<?>> typeHandler) {
        this.typeHandler = typeHandler;
        return this;
    }

    public String getSequence() {
        return sequence;
    }

    public ColumnBuilder setSequence(String sequence) {
        this.sequence = sequence;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public ColumnBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getGenerator() {
        return generator;
    }

    public ColumnBuilder setGenerator(String generator) {
        this.generator = generator;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public ColumnBuilder setValue(Object value) {
        this.value = value;
        return this;
    }

    public Executing getExecuting() {
        return executing;
    }

    public ColumnBuilder setExecuting(Executing executing) {
        this.executing = executing;
        return this;
    }

    public Invoker getGetterInvoker() {
        return getterInvoker;
    }

    public ColumnBuilder setGetterInvoker(Invoker getterInvoker) {
        this.getterInvoker = getterInvoker;
        return this;
    }

    public Invoker getSetterInvoker() {
        return setterInvoker;
    }

    public ColumnBuilder setSetterInvoker(Invoker setterInvoker) {
        this.setterInvoker = setterInvoker;
        return this;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public ColumnBuilder setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public boolean isUuid() {
        return uuid;
    }

    public ColumnBuilder setUuid(boolean uuid) {
        this.uuid = uuid;
        return this;
    }

    public boolean isIdentity() {
        return identity;
    }

    public ColumnBuilder setIdentity(boolean identity) {
        this.identity = identity;
        return this;
    }

    public boolean isSnowflake() {
        return snowflake;
    }

    public ColumnBuilder setSnowflake(boolean snowflake) {
        this.snowflake = snowflake;
        return this;
    }

    public boolean isBlob() {
        return blob;
    }

    public ColumnBuilder setBlob(boolean blob) {
        this.blob = blob;
        return this;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public ColumnBuilder setInsertable(boolean insertable) {
        this.insertable = insertable;
        return this;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public ColumnBuilder setUpdatable(boolean updatable) {
        this.updatable = updatable;
        return this;
    }

    public boolean isUseJavaType() {
        return useJavaType;
    }

    public ColumnBuilder setUseJavaType(boolean useJavaType) {
        this.useJavaType = useJavaType;
        return this;
    }

    public boolean isCheckNotNull() {
        return checkNotNull;
    }

    public ColumnBuilder setCheckNotNull(boolean checkNotNull) {
        this.checkNotNull = checkNotNull;
        return this;
    }

    public boolean isCheckNotEmpty() {
        return checkNotEmpty;
    }

    public ColumnBuilder setCheckNotEmpty(boolean checkNotEmpty) {
        this.checkNotEmpty = checkNotEmpty;
        return this;
    }

    public boolean isVersion() {
        return version;
    }

    public ColumnBuilder setVersion(boolean version) {
        this.version = version;
        return this;
    }

    public boolean isTenant() {
        return tenant;
    }

    public ColumnBuilder setTenant(boolean tenant) {
        this.tenant = tenant;
        return this;
    }

    public boolean isCreatedDate() {
        return createdDate;
    }

    public ColumnBuilder setCreatedDate(boolean createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public boolean isCreatedUserId() {
        return createdUserId;
    }

    public ColumnBuilder setCreatedUserId(boolean createdUserId) {
        this.createdUserId = createdUserId;
        return this;
    }

    public boolean isCreatedUserName() {
        return createdUserName;
    }

    public ColumnBuilder setCreatedUserName(boolean createdUserName) {
        this.createdUserName = createdUserName;
        return this;
    }

    public boolean isLastModifiedDate() {
        return lastModifiedDate;
    }

    public ColumnBuilder setLastModifiedDate(boolean lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public boolean isLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public ColumnBuilder setLastModifiedUserId(boolean lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
        return this;
    }

    public boolean isLastModifiedUserName() {
        return lastModifiedUserName;
    }

    public ColumnBuilder setLastModifiedUserName(boolean lastModifiedUserName) {
        this.lastModifiedUserName = lastModifiedUserName;
        return this;
    }

    public boolean isLogicDeletedDate() {
        return logicDeletedDate;
    }

    public ColumnBuilder setLogicDeletedDate(boolean logicDeletedDate) {
        this.logicDeletedDate = logicDeletedDate;
        return this;
    }

    public boolean isLogicDeletedUserId() {
        return logicDeletedUserId;
    }

    public ColumnBuilder setLogicDeletedUserId(boolean logicDeletedUserId) {
        this.logicDeletedUserId = logicDeletedUserId;
        return this;
    }

    public boolean isLogicDeletedUserName() {
        return logicDeletedUserName;
    }

    public ColumnBuilder setLogicDeletedUserName(boolean logicDeletedUserName) {
        this.logicDeletedUserName = logicDeletedUserName;
        return this;
    }

    public boolean isAutoAddedIsPrefixed() {
        return autoAddedIsPrefixed;
    }

    public ColumnBuilder setAutoAddedIsPrefixed(boolean autoAddedIsPrefixed) {
        this.autoAddedIsPrefixed = autoAddedIsPrefixed;
        return this;
    }

    public boolean isLogicalDelete() {
        return logicalDelete;
    }

    public ColumnBuilder setLogicalDelete(boolean logicalDelete) {
        this.logicalDelete = logicalDelete;
        return this;
    }

    public Object getDeletedValue() {
        return deletedValue;
    }

    public ColumnBuilder setDeletedValue(Object deletedValue) {
        this.deletedValue = deletedValue;
        return this;
    }

    public Object getUndeletedValue() {
        return undeletedValue;
    }

    public ColumnBuilder setUndeletedValue(Object undeletedValue) {
        this.undeletedValue = undeletedValue;
        return this;
    }
}
