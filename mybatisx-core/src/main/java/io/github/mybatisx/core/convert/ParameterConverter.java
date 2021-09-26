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
package io.github.mybatisx.core.convert;

import io.github.mybatisx.Objects;

import java.util.List;
import java.util.Map;

/**
 * 参数转换器
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public interface ParameterConverter extends Converter<Object, String> {

    String DEF_PARAMETER_PLACEHOLDER_ZERO = "{0}";

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数
     */
    default String convert(final Object... args) {
        return this.convert(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数
     */
    String convert(final String template, final Object... args);

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数列表
     */
    default List<String> converts(final Object... args) {
        return this.converts(Objects.asList(args));
    }

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数列表
     */
    default List<String> converts(final Iterable<?> args) {
        return this.converts(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数列表
     */
    default List<String> converts(final String template, final Object... args) {
        return this.converts(template, Objects.asList(args));
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数列表
     */
    List<String> converts(final String template, final Iterable<?> args);

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数列表
     */
    default Map<String, String> converts(final Map<String, ?> args) {
        return this.converts(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数列表
     */
    Map<String, String> converts(final String template, final Map<String, ?> args);

}
