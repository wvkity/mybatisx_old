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
package io.github.mybatisx.basic.metadata;

import io.github.mybatisx.Objects;
import io.github.mybatisx.annotation.Id;
import io.github.mybatisx.annotation.Identity;
import io.github.mybatisx.annotation.Snowflake;
import io.github.mybatisx.basic.reflect.AnnotationMetadata;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.immutable.ImmutableMap;
import io.github.mybatisx.immutable.ImmutableSet;
import io.github.mybatisx.reflect.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实体属性对象
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public class Field implements AnnotationMetadata {

    /**
     * 原属性
     */
    private final java.lang.reflect.Field originalField;
    /**
     * 属性名称
     */
    private final String name;
    /**
     * JAVA类型
     */
    private final Class<?> javaType;
    /**
     * get方法
     */
    private final Method getter;
    /**
     * set方法
     */
    private final Method setter;
    /**
     * 是否为主键
     */
    private boolean primaryKey;
    /**
     * 属性所有注解
     */
    private Set<? extends Annotation> annotations;
    /**
     * 注解缓存
     */
    private Map<String, ? extends Annotation> annotationCaches;

    /**
     * 构造方法
     * @param originalField 原JAVA属性
     * @param name          属性名
     * @param javaType      属性类型
     * @param getter        get方法
     * @param setter        set方法
     */
    public Field(java.lang.reflect.Field originalField, final String name,
                 final Class<?> javaType, final Method getter, final Method setter) {
        this.originalField = originalField;
        if (Objects.isBlank(name)) {
            this.name = originalField.getName();
        } else {
            this.name = name;
        }
        if (Objects.nonNull(javaType)) {
            this.javaType = javaType;
        } else {
            this.javaType = originalField.getType();
        }
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * 处理属性
     * @return {@link Field}
     */
    @SuppressWarnings("unchecked")
    public Field handle() {
        this.annotations =
            ImmutableSet.of(Reflections.getAllAnnotations(this.originalField, Reflections.METADATA_ANNOTATION_FILTER));
        if (Objects.isNotEmpty(this.annotations)) {
            this.annotationCaches = ImmutableMap.of(this.annotations.stream().collect(Collectors.toMap(it ->
                it.annotationType().getCanonicalName(), it -> it)));
            // 检查是否为主键
            this.primaryKey = isAnnotationPresent(Id.class, Identity.class, Snowflake.class)
                || this.isAnnotationPresent(Constants.JPA_ID);
        } else {
            this.annotationCaches = ImmutableMap.of();
        }
        return this;
    }

    public java.lang.reflect.Field getOriginalField() {
        return originalField;
    }

    public String getName() {
        return name;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    @Override
    public Set<? extends Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public Map<String, ? extends Annotation> getAnnotationCaches() {
        return annotationCaches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Field)) {
            return false;
        }
        Field field = (Field) o;
        return Objects.equals(originalField, field.originalField) &&
            Objects.equals(name, field.name) &&
            Objects.equals(javaType, field.javaType) &&
            Objects.equals(getter, field.getter) &&
            Objects.equals(setter, field.setter);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(originalField, name, javaType, getter, setter);
    }

    @Override
    public String toString() {
        return "Field{" +
            "originalField=" + originalField +
            ", name='" + name + '\'' +
            ", javaType=" + javaType +
            ", getter=" + getter +
            ", setter=" + setter +
            ", annotations=" + annotations +
            ", annotationCaches=" + annotationCaches +
            '}';
    }
}
