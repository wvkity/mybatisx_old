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
package com.wvkity.mybatis.basic.filter;

import com.wvkity.mybatis.annotation.Column;
import com.wvkity.mybatis.annotation.ColumnExt;
import com.wvkity.mybatis.annotation.Transient;
import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.reflect.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 属性过滤器
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
public class FieldFilter implements Filter<Field> {

    /**
     * 使用简单类型
     */
    private final boolean useSimpleType;
    /**
     * 枚举类转简单类型
     */
    private final boolean enumAsSimpleType;

    public FieldFilter(boolean useSimpleType, boolean enumAsSimpleType) {
        this.useSimpleType = useSimpleType;
        this.enumAsSimpleType = enumAsSimpleType;
    }

    @Override
    public boolean matches(Field it) {
        return it != null && !Modifier.isStatic(it.getModifiers()) && !Modifier.isFinal(it.getModifiers())
            && !Reflections.isAnnotationPresent(it, Transient.class, Constants.JPA_TRANSIENT, Reflections.METADATA_ANNOTATION_FILTER)
            && ((this.useSimpleType && Reflections.isSimpleJavaType(it.getType()))
            || (Reflections.isAnnotationPresent(it, Column.class, Constants.JPA_COLUMN, Reflections.METADATA_ANNOTATION_FILTER)
            || it.isAnnotationPresent(ColumnExt.class))
            || (this.enumAsSimpleType && Enum.class.isAssignableFrom(it.getType()))
        );
    }

    public static FieldFilter of(final boolean useSimpleJavaType, final boolean enumAsSimpleJavaType) {
        return new FieldFilter(useSimpleJavaType, enumAsSimpleJavaType);
    }

    public boolean isUseSimpleType() {
        return useSimpleType;
    }

    public boolean isEnumAsSimpleType() {
        return enumAsSimpleType;
    }
}
