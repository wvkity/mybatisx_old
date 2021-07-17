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
package com.github.mybatisx.plugin.handler;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.plugin.filter.Filter;
import com.github.mybatisx.reflect.Reflections;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 抽象拦截处理器
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public abstract class AbstractHandler implements Handler, Filter {

    /**
     * Mapper方法上的注解缓存
     */
    protected static final Map<String, Set<Class<? extends Annotation>>> MAPPER_METHOD_ANNOTATION_CACHE =
        Collections.synchronizedMap(new WeakHashMap<>());
    /**
     * 是否存在注解缓存
     */
    protected static final Map<String, Boolean> MAPPER_METHOD_HAS_ANNOTATION_CACHE = new ConcurrentHashMap<>();
    /**
     * 相关配置
     */
    protected Properties properties;

    /**
     * 获取当前执行的方法名
     * @param ms {@link MappedStatement}对象
     * @return 方法名
     */
    protected String execMethod(final MappedStatement ms) {
        final String msId = ms.getId();
        final int index = msId.lastIndexOf(".");
        return index < 0 ? msId : msId.substring(index + 1);
    }

    /**
     * 是否反射Mapper方法
     * @return boolean
     */
    protected boolean isReflect() {
        return false;
    }

    /**
     * 检查Mapper方法是否存在指定注解
     * @param ms     {@link MappedStatement}
     * @param target 指定注解
     * @return boolean
     */
    protected boolean isAnnotationPresent(final MappedStatement ms, final Class<? extends Annotation> target) {
        if (Objects.isNull(target)) {
            return false;
        }
        final String namespace = ms.getId();
        final String cacheKey = namespace + "@" + target.getName();
        if (MAPPER_METHOD_HAS_ANNOTATION_CACHE.containsKey(cacheKey)) {
            return MAPPER_METHOD_HAS_ANNOTATION_CACHE.get(cacheKey);
        }
        final Set<Class<? extends Annotation>> annotations = MAPPER_METHOD_ANNOTATION_CACHE.get(namespace);
        if (Objects.nonNull(annotations)) {
            final boolean hasAnt =
                Objects.isNotEmpty(annotations) && annotations.stream().anyMatch(it -> it.equals(target));
            MAPPER_METHOD_HAS_ANNOTATION_CACHE.putIfAbsent(cacheKey, hasAnt);
            return hasAnt;
        } else if (this.isReflect()) {
            final int index = namespace.lastIndexOf(Constants.DOT);
            final String className = namespace.substring(0, index);
            final String methodName = namespace.substring(index + 1);
            try {
                final Class<?> mapperClass = Class.forName(className);
                final Method method = new ArrayList<>(Reflections.getAllMethods(mapperClass,
                    (Predicate<Method>) it -> it.getName().equals(methodName))).get(0);
                final Set<Annotation> ants = Reflections.getAllAnnotations(method,
                    Reflections.METADATA_ANNOTATION_FILTER);
                if (Objects.isNotEmpty(ants)) {
                    MAPPER_METHOD_ANNOTATION_CACHE.put(namespace,
                        ants.stream().map(Annotation::annotationType).collect(Collectors.toSet()));
                    final boolean hasAnt = method.isAnnotationPresent(target);
                    MAPPER_METHOD_HAS_ANNOTATION_CACHE.putIfAbsent(cacheKey, hasAnt);
                    return hasAnt;
                } else {
                    MAPPER_METHOD_HAS_ANNOTATION_CACHE.put(cacheKey, Boolean.FALSE);
                }
            } catch (Exception ignore) {
                // ignore
            }
        }
        return false;
    }

    /**
     * 检查是否为保存操作
     * @param ms {@link MappedStatement}
     * @return boolean
     */
    protected boolean isInsert(final MappedStatement ms) {
        return ms.getSqlCommandType() == SqlCommandType.INSERT;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
