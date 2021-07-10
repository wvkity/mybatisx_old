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
package com.github.mybatisx.core.convert;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.List;
import java.util.Map;

/**
 * 占位符参数转换器
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public interface PlaceholderConverter extends Converter<Object, Object> {

    /**
     * 解析参数值列表成占位符参数
     * @param args        参数值列表
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否拼接JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return 占位符参数
     */
    List<?> convert(final Iterable<?> args, final Class<? extends TypeHandler<?>> typeHandler,
                         final boolean useJavaType, final Class<?> javaType, final JdbcType jdbcType);

    /**
     * 解析参数值列表成占位符参数
     * @param args 参数值列表
     * @return 占位符参数
     */
    List<?> convert(final Iterable<?> args);

    /**
     * 解析参数值列表成占位符参数
     * @param args 参数值集合
     * @return 占位符参数
     */
    Map<String, ?> convert(final Map<String, ?> args);

}
