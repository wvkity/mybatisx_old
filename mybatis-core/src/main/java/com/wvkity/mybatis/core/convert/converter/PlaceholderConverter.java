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
package com.wvkity.mybatis.core.convert.converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 占位符转换器
 * @author wvkity
 * @created 2020-10-19
 * @since 1.0.0
 */
public interface PlaceholderConverter extends StringConverter<String> {

    /**
     * 默认模板
     */
    String PLACEHOLDER_TEMPLATE = "{0}";

    /**
     * 默认参数值转占位符
     * @param args 参数列表
     * @return SQL字符串
     */
    default String defPlaceholder(final Object... args) {
        return convert(PLACEHOLDER_TEMPLATE, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数列表
     * @return SQL字符串
     */
    default List<String> defPlaceholders(final Object... args) {
        return placeholders(PLACEHOLDER_TEMPLATE, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数集合
     * @return SQL字符串
     */
    default List<String> defPlaceholders(final Collection<Object> args) {
        return converts(PLACEHOLDER_TEMPLATE, args);
    }

    /**
     * 参数值转占位符
     * @param template 模板
     * @param args     参数列表
     * @return SQL字符串
     */
    default List<String> placeholders(final String template, final Object... args) {
        return converts(template, Arrays.asList(args));
    }
}
