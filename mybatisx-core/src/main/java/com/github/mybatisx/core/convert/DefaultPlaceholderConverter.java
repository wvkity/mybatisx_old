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
package com.github.mybatisx.core.convert;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.core.inject.mapping.utils.Scripts;
import com.github.mybatisx.reflect.Reflections;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 默认占位符参数转换器
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public class DefaultPlaceholderConverter implements PlaceholderConverter {

    private static final long serialVersionUID = -4114554043645301235L;
    private final ParameterConverter converter;

    public DefaultPlaceholderConverter(ParameterConverter parameterConverter) {
        this.converter = parameterConverter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Object source) {
        if (Objects.isNull(source)) {
            return Constants.DEF_STR_NULL;
        }
        final Class<?> clazz = source.getClass();
        if (Iterable.class.isAssignableFrom(clazz)) {
            return this.convert((Iterable<?>) source);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return this.convert((Map<String, ?>) source);
        } else if (clazz.isArray()) {
            return this.convert(Objects.asList((Object[]) source)).toArray(new Object[0]);
        }
        return Scripts.safeJoining(this.converter.convert(source));
    }

    @Override
    public List<?> convert(Iterable<?> args, Class<? extends TypeHandler<?>> typeHandler, boolean useJavaType,
                                Class<?> javaType, JdbcType jdbcType) {
        final Stream<?> stream = StreamSupport.stream(args.spliterator(), false);
        if (Reflections.isPureType(args)) {
            return stream.map(it -> Scripts.safeJoining(this.convert(it).toString(),
                Scripts.concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)))
                .collect(Collectors.toList());
        }
        return this.convert(args);
    }

    @Override
    public List<?> convert(Iterable<?> args) {
        final Stream<?> stream = StreamSupport.stream(args.spliterator(), false);
        if (Reflections.isPureType(args)) {
            return stream.map(it -> Scripts.safeJoining(this.converter.convert(it))).collect(Collectors.toList());
        }
        return stream.map(this::convert).collect(Collectors.toList());
    }

    @Override
    public Map<String, ?> convert(Map<String, ?> args) {
        if (Objects.isEmpty(args)) {
            return new HashMap<>(0);
        }
        return args.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
            it -> this.convert(it.getValue()), (o, n) -> n, LinkedHashMap::new));
    }

    public ParameterConverter getConverter() {
        return converter;
    }

}
