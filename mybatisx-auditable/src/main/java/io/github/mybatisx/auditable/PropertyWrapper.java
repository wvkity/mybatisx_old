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
package io.github.mybatisx.auditable;

import io.github.mybatisx.Objects;
import io.github.mybatisx.auditable.exception.AuditedException;
import io.github.mybatisx.reflect.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 属性包装器
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public class PropertyWrapper {

    /**
     * 实体类
     */
    private final Class<?> entity;
    /**
     * 属性
     */
    private final Field field;
    /**
     * 属性名称
     */
    private final String name;
    /**
     * java类型
     */
    private final Class<?> javaType;
    /**
     * 是否为主键
     */
    private final boolean primaryKey;
    /**
     * 是否为UUID主键
     */
    private final boolean uuid;
    /**
     * 是否为雪花算法主键
     */
    private final boolean snowflake;
    /**
     * get方法
     */
    private final Method getter;
    /**
     * set方法
     */
    private final Method setter;
    /**
     * 审计类型
     */
    private final AuditType auditType;
    /**
     * 注解集合
     */
    private final Set<? extends Annotation> annotations;
    /**
     * 注解集合
     */
    private final Map<String, ? extends Annotation> annotationCaches;

    public PropertyWrapper(Class<?> entity, Field field, String name, Class<?> javaType, boolean primaryKey,
                           boolean uuid, boolean snowflake, Method getter, Method setter, AuditType auditType,
                           Set<? extends Annotation> annotations, Map<String, ? extends Annotation> annotationCaches) {
        this.entity = entity;
        this.field = field;
        this.name = name;
        this.javaType = javaType;
        this.primaryKey = primaryKey;
        this.uuid = uuid;
        this.snowflake = snowflake;
        this.getter = getter;
        this.setter = setter;
        this.auditType = auditType;
        this.annotations = annotations;
        this.annotationCaches = annotationCaches;
    }

    public boolean canInvoke(final Object target) {
        return Objects.nonNull(target) && !this.javaType.isPrimitive();
    }

    public Object getValue(final Object target) {
        try {
            return Reflections.methodInvoke(target, this.getter);
        } catch (Exception e) {
            throw new AuditedException("Failed to get old value: " + e.getMessage(), e);
        }
    }

    public Object invoke(final Object target, final Object arg) {
        try {
            return Reflections.methodInvoke(target, this.setter, arg);
        } catch (Exception e) {
            throw new AuditedException("Failed to inject value into '" + this.name + "' property", e);
        }
    }

    public Class<?> getEntity() {
        return entity;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean nonPrimaryKey() {
        return !isPrimaryKey();
    }

    public boolean isUuid() {
        return uuid;
    }

    public boolean isSnowflake() {
        return snowflake;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public AuditType getAuditType() {
        return auditType;
    }

    public Set<? extends Annotation> getAnnotations() {
        return annotations;
    }

    public Map<String, ? extends Annotation> getAnnotationCaches() {
        return annotationCaches;
    }
}
