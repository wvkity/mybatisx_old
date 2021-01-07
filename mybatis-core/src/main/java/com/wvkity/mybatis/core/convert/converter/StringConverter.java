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
package com.wvkity.mybatis.core.convert.converter;

import java.util.Collection;
import java.util.List;

/**
 * 字符串转换器
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
public interface StringConverter<T> {

    /**
     * 格式化
     * @param source 源对象
     * @param args   参数列表
     * @return 转换后的结果
     */
    default String convert(final T source, final Object... args) {
        return convert(source, true, args);
    }

    /**
     * 格式化
     * @param source 源对象
     * @param format 是否格式化
     * @param args   参数列表
     * @return 转换后的结果
     */
    String convert(final T source, final boolean format, final Object... args);

    /**
     * 格式化
     * @param source 源对象
     * @param args   参数集合
     * @return 转换后的结果
     */
    List<String> converts(final T source, final Collection<Object> args);
}
