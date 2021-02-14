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
package com.wvkity.mybatis.core.property;

import com.wvkity.mybatis.core.handler.TableHandler;
import com.wvkity.mybatis.core.invoke.SerializedLambda;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.core.utils.Objects;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * 属性映射缓存
 * @author wvkity
 * @created 2020-10-18
 * @since 1.0.0
 */
public final class PropertiesMappingCache {

    private PropertiesMappingCache() {
    }

    /**
     * lambda缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> LAMBDA_CACHE = new WeakHashMap<>();

    /**
     * 解析{@link Property}对象
     * @param property {@link Property}
     * @param <T>      泛型类型
     * @return {@link SerializedLambda}
     */
    public static <T> SerializedLambda parse(final Property<T, ?> property) {
        final Class<?> clazz = property.getClass();
        return Optional.ofNullable(LAMBDA_CACHE.get(clazz)).map(WeakReference::get).orElseGet(() -> {
            final SerializedLambda lambda = SerializedLambda.resolve(property);
            final WeakReference<SerializedLambda> reference =
                LAMBDA_CACHE.put(clazz, new WeakReference<>(lambda));
            return LAMBDA_CACHE.get(clazz).get();
        });
    }

    /**
     * {@link Property}对象转成属性名
     * @param lambda {@link Property}对象
     * @param <T>    泛型类型
     * @return 属性
     */
    public static <T> String lambdaToProperty(final Property<T, ?> lambda) {
        return methodToProperty(Optional.ofNullable(parse(lambda))
            .map(SerializedLambda::getImplMethodName).orElse(null));
    }

    /**
     * 方法名转成属性名
     * @param methodName 方法名
     * @return 属性
     */
    public static String methodToProperty(final String methodName) {
        return Optional.ofNullable(methodName).filter(Objects::isNotBlank)
            .map(Reflections::methodToProperty).orElse(null);
    }

    /**
     * 根据实体类、属性名获取字段信息
     * @param clazz    实体类
     * @param property 属性
     * @return {@link Column}
     */
    public static Column getColumn(final Class<?> clazz, final String property) {
        return TableHandler.getColumn(clazz, property);
    }
}
