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
package com.wvkity.mybatis.core.auditable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 属性
 * @author wvkity
 * @created 2021-03-03
 * @since 1.0.0
 */
public class OriginalProperty {

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

    public OriginalProperty(Class<?> entity, Field field, String name, Class<?> javaType,
                            boolean primaryKey, Method getter, Method setter, AuditType auditType,
                            Set<? extends Annotation> annotations, Map<String, ? extends Annotation> annotationCaches) {
        this.entity = entity;
        this.field = field;
        this.name = name;
        this.javaType = javaType;
        this.primaryKey = primaryKey;
        this.getter = getter;
        this.setter = setter;
        this.auditType = auditType;
        this.annotations = annotations;
        this.annotationCaches = annotationCaches;
    }

    public boolean canInvoke(final Object target) {
        if (target == null) {
            return false;
        }
        return !this.javaType.isPrimitive() && this.getValue(target) == null;
    }

    public Object getValue(final Object target) {
        if (target == null) {
            return null;
        }
        try {
            return this.getter.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (e instanceof IllegalAccessException) {
                this.getter.setAccessible(true);
                try {
                    return this.getter.invoke(target);
                } catch (IllegalAccessException | InvocationTargetException e2) {
                    throw new AuditingException("Failed to get old value: " + e2.getMessage(), e2);
                }
            }
            throw new AuditingException("Failed to get old value: " + e.getMessage(), e);
        }
    }

    public Object invoke(final Object target, final Object... args) {
        if (target != null) {
            try {
                return this.setter.invoke(target, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e instanceof IllegalAccessException) {
                    this.setter.setAccessible(true);
                    try {
                        return this.setter.invoke(target, args);
                    } catch (IllegalAccessException | InvocationTargetException e2) {
                        throw new AuditingException("Failed to inject value into '" + this.name + "' property", e2);
                    }
                }
                throw new AuditingException("Failed to inject value into '" + this.name + "' property", e);
            }
        }
        return null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OriginalProperty)) return false;
        OriginalProperty that = (OriginalProperty) o;
        return Objects.equals(entity, that.entity) &&
            Objects.equals(field, that.field) &&
            Objects.equals(name, that.name) &&
            Objects.equals(javaType, that.javaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, field, name, javaType);
    }

    @Override
    public String toString() {
        return "OriginalProperty{" +
            "entity=" + entity +
            ", field=" + field +
            ", name='" + name + '\'' +
            ", javaType=" + javaType +
            ", primaryKey=" + primaryKey +
            ", getter=" + getter +
            ", setter=" + setter +
            ", auditType=" + auditType +
            ", annotations=" + annotations +
            ", annotationCaches=" + annotationCaches +
            '}';
    }
}
