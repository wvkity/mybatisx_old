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
package io.github.mybatisx.core.convert;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 默认参数转换器
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public class DefaultParameterConverter implements ParameterConverter {

    private static final long serialVersionUID = -7817996589266807074L;
    public static final String DEF_PARAMETER_KEY_PREFIX = "_v_idx_";
    public static final String DEF_PARAMETER_ALIAS = Constants.PARAM_CRITERIA;
    public static final String DEF_PARAMETER_VALUE_MAPPING = DEF_PARAMETER_ALIAS + ".parameterValueMapping.%s";
    public static final String DEF_PARAMETER_PLACEHOLDER = "{%s}";
    public static final String DEF_PARAMETER_PLACEHOLDER_SAFE = "#{{0}}";
    protected final AtomicInteger parameterSequence;
    protected final Map<String, Object> parameterValueMapping;

    public DefaultParameterConverter(AtomicInteger parameterSequence, Map<String, Object> parameterValueMapping) {
        this.parameterSequence = parameterSequence;
        this.parameterValueMapping = parameterValueMapping;
    }

    @Override
    public String convert(Object source) {
        return this.convert(DEF_PARAMETER_PLACEHOLDER_ZERO, source);
    }

    @Override
    public String convert(String template, Object... args) {
        if (Objects.isNotBlank(template) && !Objects.isEmpty(args)) {
            final int len = args.length;
            String source = template;
            for (int i = 0; i < len; i++) {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                source = source.replace(Constants.BRACE_OPEN + i + Constants.BRACE_CLOSE,
                    String.format(DEF_PARAMETER_VALUE_MAPPING, paramName));
                this.parameterValueMapping.put(paramName, args[i]);
            }
            return source;
        }
        return template;
    }

    @Override
    public List<String> converts(String template, Iterable<?> args) {
        if (Objects.isNotBlank(template) && Objects.nonNull(args)) {
            return StreamSupport.stream(args.spliterator(), false).map(it -> {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                final Object value = it == null ? Constants.DEF_STR_NULL : it;
                this.parameterValueMapping.put(paramName, value);
                return template.replace(DEF_PARAMETER_PLACEHOLDER_ZERO,
                    String.format(DEF_PARAMETER_VALUE_MAPPING, paramName));
            }).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public Map<String, String> converts(String template, Map<String, ?> args) {
        if (Objects.isNotBlank(template) && Objects.isNotEmpty(args)) {
            final Map<String, String> newArgs = new HashMap<>(args.size());
            for (Map.Entry<String, ?> it : args.entrySet()) {
                newArgs.put(it.getKey(), this.convert(template, it.getValue()));
            }
            return newArgs;
        }
        return new HashMap<>(0);
    }

}
