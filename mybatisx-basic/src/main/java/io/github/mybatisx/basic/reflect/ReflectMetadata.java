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


import io.github.mybatisx.Objects;
import io.github.mybatisx.immutable.ImmutableMap;
import io.github.mybatisx.reflect.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 反射获取元数据
 * @author wvkity
 * @created 2020-10-07
 * @since 1.0.0
 */
public class ReflectMetadata implements Metadata {

    /**
     * 反射对象获取的属性-值数据
     */
    private final Map<String, Object> data;

    public ReflectMetadata(Map<String, Object> data) {
        this.data = data == null ? new HashMap<>(0) : data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String prop, T defValue, Class<T> clazz) {
        if (Objects.nonNull(clazz) && this.data.containsKey(prop)) {
            final Object value = this.data.get(prop);
            if (Objects.nonNull(value) && clazz.equals(value.getClass())) {
                return (T) value;
            }
        }
        return defValue;
    }

    /**
     * 获取数据
     * @return {@link ImmutableMap}
     */
    public Map<String, Object> getData() {
        return ImmutableMap.of(this.data);
    }

    @SuppressWarnings("unchecked")
    public static ReflectMetadata of(final Object target) {
        return Optional.ofNullable(target).map(it -> {
            if (target instanceof Map) {
                return of((Map<String, Object>) target);
            } else if (target instanceof Annotation) {
                return of(Reflections.annotationToMap((Annotation) target));
            }
            return of(new HashMap<>(0));
        }).orElse(of(new HashMap<>(0)));
    }

    public static ReflectMetadata of(final Map<String, Object> data) {
        return Optional.ofNullable(data).map(ReflectMetadata::new).orElse(new ReflectMetadata(new HashMap<>(0)));
    }
}
