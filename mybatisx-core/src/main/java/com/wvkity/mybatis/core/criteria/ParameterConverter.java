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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 参数转换器
 * @author wvkity
 * @created 2021-05-03
 * @since 1.0.0
 */
public class ParameterConverter {

    /**
     * 参数前缀
     */
    public static final String DEF_PARAMETER_KEY_PREFIX = "_v_idx_";
    /**
     * 参数值映射
     */
    public static final String DEF_PARAMETER_VALUE_MAPPING = "%s.parameterValueMapping.%s";
    /**
     * 参数占位符
     */
    public static final String DEF_PARAMETER_PLACEHOLDER = "{%s}";
    /**
     * 默认模板
     */
    public static final String DEF_PARAMETER_PLACEHOLDER_ZERO = "{0}";
    /**
     * #{}参数模板
     */
    public static final String DEF_PARAMETER_PLACEHOLDER_SAFE = "#{{0}}";
    /**
     * {@link Criteria}默认参数名
     */
    public static final String DEF_PARAMETER_ALIAS = Constants.PARAM_CRITERIA;
     /**
     * 参数序列
     */
     private final AtomicInteger parameterSequence;
    /**
     * 参数值映射
     */
    protected Map<String, Object> parameterValueMapping;

    public ParameterConverter(AtomicInteger parameterSequence, Map<String, Object> parameterValueMapping) {
        this.parameterSequence = parameterSequence;
        this.parameterValueMapping = parameterValueMapping;
    }

    /**
     * 默认参数值转占位符
     * @param args 参数列表
     * @return SQL字符串
     */
    public String defPlaceholder(final Object... args) {
        return this.placeholder(DEF_PARAMETER_PLACEHOLDER_ZERO, true, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数列表
     * @return SQL字符串
     */
    public List<String> defPlaceholders(final Object... args) {
        return this.defPlaceholders(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数集合
     * @return SQL字符串
     */
    public List<String> defPlaceholders(final Collection<Object> args) {
        return this.placeholder(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数集合
     * @return SQL字符串
     */
    public Map<String, String> defPlaceHolders(final Map<String, Object> args) {
        return this.placeholder(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符
     * @param template 模板
     * @param args     参数列表
     * @return SQL字符串
     */
    public List<String> defPlaceholders(final String template, final Object... args) {
        return this.placeholder(template, Arrays.asList(args));
    }

    public String placeholder(String source, boolean format, Object... args) {
        if (Objects.isBlank(source)) {
            return null;
        }
        if (format && !Objects.isEmpty(args)) {
            final int size = args.length;
            String template = source;
            for (int i = 0; i < size; i++) {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                template = template.replace("{" + i + "}",
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
                this.parameterValueMapping.put(paramName, args[i]);
            }
            return template;
        }
        return source;
    }

    public Map<String, String> placeholder(String source, Map<String, Object> args) {
        if (Objects.isNotBlank(source) && Objects.isNotEmpty(args)) {
            final Map<String, String> newArgs = new HashMap<>(args.size());
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                newArgs.put(entry.getKey(), this.defPlaceholder(source, true, entry.getValue()));
            }
            return newArgs;
        }
        return new HashMap<>(0);
    }

    public List<String> placeholder(String source, Collection<Object> args) {
        if (Objects.isNotBlank(source) && Objects.isNotEmpty(args)) {
            return args.stream().map(it -> {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                final Object value = it == null ? "null" : it;
                this.parameterValueMapping.put(paramName, value);
                return source.replace(DEF_PARAMETER_PLACEHOLDER_ZERO,
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
            }).collect(Collectors.toList());
        }
        return null;
    }

}
