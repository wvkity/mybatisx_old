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
package com.github.mybatisx.basic.metadata;

import com.github.mybatisx.basic.invoker.FieldGetterInvoker;
import com.github.mybatisx.basic.invoker.FieldSetterInvoker;
import com.github.mybatisx.basic.invoker.Invoker;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.Objects;

/**
 * 数据库表字段映射
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public class Column {

    /**
     * 实体类
     */
    private final Class<?> entity;
    /**
     * 属性
     */
    private final String property;
    /**
     * 数据库字段
     */
    private final String column;
    /**
     * {@link JdbcType}类型
     */
    private final JdbcType jdbcType;
    /**
     * 类型处理器
     */
    private final Class<? extends TypeHandler<?>> typeHandler;
    /**
     * JAVA类型
     */
    private final Class<?> javaType;
    /**
     * 属性get方法调用器
     */
    private final Invoker getterInvoker;
    /**
     * 属性sett方法调用器
     */
    private final Invoker setterInvoker;
    /**
     * 主键序列生成器(oracle)
     */
    private final String sequence;
    /**
     * 是否为主键
     */
    private final boolean unique;
    /**
     * 优先标识
     */
    private final boolean priority;
    /**
     * 是否为blob类型
     */
    private final boolean blob;
    /**
     * 是否可保存
     */
    private final boolean insertable;
    /**
     * 是否可更新
     */
    private final boolean updatable;
    /**
     * 参数是否使用JAVA类型
     */
    private final boolean useJavaType;
    /**
     * 是否非空校验
     */
    private final boolean checkNotNull;
    /**
     * 是否空值校验(字符串)
     */
    private final boolean checkNotEmpty;
    /**
     * 是否为乐观锁标识
     */
    private final boolean version;
    /**
     * 是否为多租户标识
     */
    private final boolean multiTenant;
    /**
     * 是否为逻辑删除标识
     */
    private final boolean logicDelete;
    /**
     * JAVA属性信息
     */
    private final Descriptor descriptor;
    /**
     * 主键信息
     */
    private final PrimaryKey primaryKey;
    /**
     * 审计信息
     */
    private final Auditor auditor;

    public Column(Class<?> entity, String property, String column,
                  JdbcType jdbcType, Class<? extends TypeHandler<?>> typeHandler, Class<?> javaType,
                  String sequence, boolean unique, boolean blob, boolean insertable,
                  boolean updatable, boolean useJavaType, boolean checkNotNull,
                  boolean checkNotEmpty, boolean version, boolean multiTenant, boolean logicDelete,
                  Descriptor descriptor, PrimaryKey primaryKey, Auditor auditor) {
        this.entity = entity;
        this.property = property;
        this.column = column;
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        final java.lang.reflect.Field field = descriptor.getField();
        this.getterInvoker = FieldGetterInvoker.of(field);
        this.setterInvoker = FieldSetterInvoker.of(field);
        this.typeHandler = typeHandler;
        this.sequence = sequence;
        this.unique = unique;
        this.blob = blob;
        this.insertable = insertable;
        this.updatable = updatable;
        this.useJavaType = useJavaType;
        this.checkNotNull = checkNotNull;
        this.checkNotEmpty = checkNotEmpty;
        this.version = version;
        this.multiTenant = multiTenant;
        this.logicDelete = logicDelete;
        this.descriptor = descriptor;
        this.primaryKey = primaryKey;
        this.priority = primaryKey.isPriority();
        this.auditor = auditor;
    }

    public Class<?> getEntity() {
        return entity;
    }

    public String getProperty() {
        return property;
    }

    public String getColumn() {
        return column;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public Class<? extends TypeHandler<?>> getTypeHandler() {
        return typeHandler;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public Invoker getGetterInvoker() {
        return getterInvoker;
    }

    public Invoker getSetterInvoker() {
        return setterInvoker;
    }

    public String getSequence() {
        return sequence;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isPriority() {
        return priority;
    }

    public boolean isBlob() {
        return blob;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public boolean isUseJavaType() {
        return useJavaType;
    }

    public boolean isCheckNotNull() {
        return checkNotNull;
    }

    public boolean isCheckNotEmpty() {
        return checkNotEmpty;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isMultiTenant() {
        return multiTenant;
    }

    public boolean isLogicDelete() {
        return logicDelete;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public Auditor getAuditor() {
        return auditor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Column)) {
            return false;
        }
        Column column1 = (Column) o;
        return unique == column1.unique &&
            priority == column1.priority &&
            blob == column1.blob &&
            insertable == column1.insertable &&
            updatable == column1.updatable &&
            useJavaType == column1.useJavaType &&
            checkNotNull == column1.checkNotNull &&
            checkNotEmpty == column1.checkNotEmpty &&
            version == column1.version &&
            multiTenant == column1.multiTenant &&
            logicDelete == column1.logicDelete &&
            Objects.equals(entity, column1.entity) &&
            Objects.equals(property, column1.property) &&
            Objects.equals(column, column1.column) &&
            jdbcType == column1.jdbcType &&
            Objects.equals(javaType, column1.javaType) &&
            Objects.equals(typeHandler, column1.typeHandler) &&
            Objects.equals(sequence, column1.sequence) &&
            Objects.equals(descriptor, column1.descriptor) &&
            Objects.equals(primaryKey, column1.primaryKey) &&
            Objects.equals(auditor, column1.auditor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, property, column, jdbcType, javaType, typeHandler, sequence, unique,
            priority, blob, insertable, updatable, useJavaType, checkNotNull, checkNotEmpty,
            version, multiTenant, logicDelete, descriptor, primaryKey, auditor);
    }

    @Override
    public String toString() {
        return "Column{" +
            "entity=" + entity +
            ", property='" + property + '\'' +
            ", column='" + column + '\'' +
            ", jdbcType=" + jdbcType +
            ", typeHandler=" + typeHandler +
            ", javaType=" + javaType +
            ", getterInvoker=" + getterInvoker +
            ", setterInvoker=" + setterInvoker +
            ", sequence='" + sequence + '\'' +
            ", unique=" + unique +
            ", priority=" + priority +
            ", blob=" + blob +
            ", insertable=" + insertable +
            ", updatable=" + updatable +
            ", useJavaType=" + useJavaType +
            ", checkNotNull=" + checkNotNull +
            ", checkNotEmpty=" + checkNotEmpty +
            ", version=" + version +
            ", multiTenant=" + multiTenant +
            ", logicDelete=" + logicDelete +
            ", descriptor=" + descriptor +
            ", primaryKey=" + primaryKey +
            ", auditor=" + auditor +
            '}';
    }
}
