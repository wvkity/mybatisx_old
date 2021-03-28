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
package com.wvkity.mybatis.basic.reflect;

import com.wvkity.mybatis.basic.utils.Objects;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 注解数据
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
public interface AnnotationMetadata {

    /**
     * 获取所有注解
     * @return Set注解列表
     */
    Set<? extends Annotation> getAnnotations();

    /**
     * 获取所有注解
     * @return Map注解列表
     */
    Map<String, ? extends Annotation> getAnnotationCaches();

    /**
     * 获取指定注解实例
     * @param annotationClass 注解类
     * @param <A>             注解类泛型
     * @return 注解实例
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
        return (A) Optional.ofNullable(annotationClass).map(it ->
            getAnnotation(annotationClass.getCanonicalName())).orElse(null);
    }

    /**
     * 获取指定注解实例
     * @param annotationName 注解类名
     * @param <A>            注解类泛型
     * @return 注解实例
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getAnnotation(final String annotationName) {
        return (A) Optional.ofNullable(annotationName).filter(Objects::isNotBlank).map(it ->
            getAnnotationCaches().get(it)).orElse(null);
    }

    /**
     * 获取{@link ReflectMetadata}对象
     * @param annotationName 注解类名
     * @return {@link ReflectMetadata}
     */
    default ReflectMetadata getReflectMetadata(final String annotationName) {
        return ReflectMetadata.of(Reflections.annotationToMap(getAnnotation(annotationName)));
    }

    /**
     * 检查是否存在指定注解
     * @param annotationClass 注解类名
     * @return boolean
     */
    default boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return annotationClass != null && Optional.ofNullable(getAnnotations()).map(it ->
            !it.isEmpty() && it.stream().anyMatch(at -> annotationClass.equals(at.annotationType()))
        ).orElse(false);
    }

    /**
     * 检查是否存在指定注解(满足一个即可)
     * @param annotationClasses 注解类集合
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    default boolean isAnnotationPresent(final Class<? extends Annotation>... annotationClasses) {
        return isAnnotationPresent(Arrays.asList(annotationClasses));
    }

    /**
     * 检查是否存在指定注解(满足一个即可)
     * @param annotationClasses 注解类集合
     * @return boolean
     */
    default boolean isAnnotationPresent(final Collection<Class<? extends Annotation>> annotationClasses) {
        if (Objects.isEmpty(annotationClasses)) {
            return false;
        }
        final Set<Class<? extends Annotation>> classes =
            annotationClasses.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (Objects.isEmpty(classes)) {
            return false;
        }
        return Optional.ofNullable(getAnnotations()).map(it ->
            !it.isEmpty() && it.stream().anyMatch(at -> classes.contains(at.annotationType()))
        ).orElse(false);
    }

    /**
     * 检查是否存在指定注解
     * @param annotationName 注解类名
     * @return boolean
     */
    default boolean isAnnotationPresent(final String annotationName) {
        return getAnnotationCaches().containsKey(annotationName);
    }
}
