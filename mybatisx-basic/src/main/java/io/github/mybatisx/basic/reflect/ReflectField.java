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
package io.github.mybatisx.basic.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 类反射属性包装器
 * @author wvkity
 * @created 2020-10-05
 * @since 1.0.0
 */
public class ReflectField {

    /**
     * 属性信息
     */
    private final Field field;
    /**
     * JAVA类型
     */
    private final Class<?> javaType;
    /**
     * 属性名称
     */
    private final String name;
    /**
     * get方法
     */
    private final Method getter;
    /**
     * set方法
     */
    private final Method setter;

    public ReflectField(Field field, Class<?> javaType, String name, Method getter, Method setter) {
        this.field = field;
        this.javaType = javaType;
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public String getName() {
        return name;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReflectField)) {
            return false;
        }
        ReflectField that = (ReflectField) o;
        return Objects.equals(field, that.field) &&
            Objects.equals(javaType, that.javaType) &&
            Objects.equals(name, that.name) &&
            Objects.equals(getter, that.getter) &&
            Objects.equals(setter, that.setter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, javaType, name, getter, setter);
    }

    @Override
    public String toString() {
        return "FieldWrapper{" +
            "field=" + field +
            ", javaType=" + javaType +
            ", name='" + name + '\'' +
            ", getter=" + getter +
            ", setter=" + setter +
            '}';
    }
}
