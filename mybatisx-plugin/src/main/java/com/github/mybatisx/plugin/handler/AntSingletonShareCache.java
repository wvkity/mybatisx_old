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
package com.github.mybatisx.plugin.handler;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.reflect.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 单例共享缓存
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public final class AntSingletonShareCache {

    private AntSingletonShareCache() {
    }

    /**
     * Mapper接口方法上的注解缓存
     */
    private final Map<String, Set<Annotation>> mapperMethodAntCache =
        Collections.synchronizedMap(new WeakHashMap<>(128));
    /**
     * Mapper接口方法是否存在指定注解缓存
     */
    private final Map<String, Boolean> mapperMethodSpecifiedAntCache = new ConcurrentHashMap<>(128);

    /**
     * 获取并缓存指定方法上的注解
     * @param namespace Mapper接口方法(MappedStatement唯一ID)
     * @return 注解列表
     */
    public Set<Annotation> getAndCacheAnnotations(final String namespace) {
        final int index = namespace.lastIndexOf(Constants.DOT);
        final String className = namespace.substring(0, index);
        final String methodName = namespace.substring(index + 1);
        try {
            final Class<?> mapperInterface = Class.forName(className);
            final Method method = new ArrayList<>(Reflections.getAllMethods(mapperInterface,
                (Predicate<? super Method>) it -> it.getName().equals(methodName))).get(0);
            final Set<Annotation> annotations =
                Reflections.getAllAnnotations(method, Reflections.METADATA_ANNOTATION_FILTER);
            if (Objects.isNotEmpty(annotations)) {
                this.mapperMethodAntCache.put(namespace, annotations);
                return annotations;
            }
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }

    /**
     * 检查Mapper方法是否存在指定注解
     * @param namespace Mapper接口方法(MappedStatement唯一ID)
     * @param target    指定注解类
     * @param reflect   是否反射
     * @return boolean
     */
    public boolean isAnnotationPresent(final String namespace, final Class<? extends Annotation> target,
                                       final boolean reflect) {
        if (Objects.isNull(target)) {
            return false;
        }
        final String cacheKey = namespace + "@" + target.getName();
        if (this.mapperMethodSpecifiedAntCache.containsKey(cacheKey)) {
            return this.mapperMethodSpecifiedAntCache.get(cacheKey);
        }
        Set<Annotation> annotations = this.mapperMethodAntCache.get(namespace);
        if (Objects.nonNull(annotations)) {
            final boolean hasAnt = Objects.isNotEmpty(annotations) && annotations.stream().anyMatch(it ->
                it.annotationType().equals(target));
            this.mapperMethodSpecifiedAntCache.putIfAbsent(cacheKey, hasAnt);
            return hasAnt;
        } else if (reflect) {
            annotations = this.getAndCacheAnnotations(namespace);
            if (Objects.isNotEmpty(annotations)) {
                final boolean hasAnt = Objects.isNotEmpty(annotations) && annotations.stream().anyMatch(it ->
                    it.annotationType().equals(target));
                this.mapperMethodSpecifiedAntCache.putIfAbsent(cacheKey, hasAnt);
                return hasAnt;
            } else {
                this.mapperMethodSpecifiedAntCache.putIfAbsent(cacheKey, Boolean.FALSE);
            }
        }
        return false;
    }

    /**
     * 获取Mapper方法上指定的注解实例
     * @param namespace Mapper接口方法(MappedStatement唯一ID)
     * @param target    指定注解类
     * @param reflect   是否反射
     * @return 注解实例
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(final String namespace, final Class<T> target,
                                                  final boolean reflect) {
        if (this.isAnnotationPresent(namespace, target, reflect)) {
            Set<Annotation> annotations = this.mapperMethodAntCache.get(namespace);
            if (Objects.isNotEmpty(annotations)) {
                return (T) annotations.stream().filter(it ->
                    it.annotationType().equals(target)).findFirst().orElse(null);
            } else {
                annotations = this.getAndCacheAnnotations(namespace);
                if (Objects.isNotEmpty(annotations)) {
                    return (T) annotations.stream().filter(it ->
                        it.annotationType().equals(target)).findFirst().orElse(null);
                }
            }
        }
        return null;
    }

    private static class SingletonHolder {
        static final AntSingletonShareCache INSTANCE = new AntSingletonShareCache();
    }

    public static AntSingletonShareCache getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
