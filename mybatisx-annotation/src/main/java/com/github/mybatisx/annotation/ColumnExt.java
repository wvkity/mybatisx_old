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
package com.github.mybatisx.annotation;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段扩展注解
 * @author wvkity
 * @created 2020-09-30
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnExt {

    /**
     * 字段名
     * @return 字段名
     */
    String name() default "";

    /**
     * 是否为BLOB类型
     * @return boolean
     */
    boolean blob() default false;

    /**
     * JDBC类型
     * @return JDBC类型
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * 类型处理器
     * @return {@link TypeHandler}
     */
    Class<? extends TypeHandler<?>> typeHandler() default UnknownTypeHandler.class;

    /**
     * 使用JAVA类型
     * @return {@link Option}
     */
    Option javaType() default Option.CONFIG;

    /**
     * 非空校验
     * @return {@link Option}
     */
    Option notNull() default Option.CONFIG;

    /**
     * 非空值校验(字符串)
     * @return {@link Option}
     */
    Option notEmpty() default Option.CONFIG;
}
