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
package com.github.mybatisx.basic.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Object工具
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public final class Objects {

    private Objects() {
    }

    /**
     * 检查对象是否为空
     * @param target 待检查对象
     * @return boolean
     */
    public static boolean isNull(final Object target) {
        return target == null;
    }

    /**
     * 检查对象是否非空
     * @param target 待检查对象
     * @return boolean
     */
    public static boolean nonNull(final Object target) {
        return target != null;
    }

    /**
     * 检查对象是否为数组对象
     * @param target 待检查对象
     * @return boolean
     */
    public static boolean isArray(final Object target) {
        return target != null && target.getClass().isArray();
    }

    /**
     * 检查数组对象是否为空
     * @param <T>  泛型类型
     * @param args 待检查
     * @return boolean
     */
    public static <T> boolean isEmpty(final T[] args) {
        return args == null || args.length == 0;
    }

    /**
     * 检查集合是否为空
     * @param collection 待检查集合
     * @return boolean
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 检查集合是否不为空
     * @param collection 待检查集合
     * @return boolean
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 检查集合元素值是否都为null值
     * @param collection 集合
     * @return boolean
     */
    public static boolean isNullElement(final Collection<?> collection) {
        return Objects.isEmpty(collection) || collection.stream().allMatch(Objects::isNull);
    }

    /**
     * 检查集合元素值是否不为null值
     * @param collection 集合
     * @return boolean
     */
    public static boolean isNotNullElement(final Collection<?> collection) {
        return Objects.isNotEmpty(collection) && collection.stream().anyMatch(Objects::nonNull);
    }

    /**
     * 检查集合是否为空
     * @param map 待检查集合
     * @return boolean
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 检查集合是否不为空
     * @param map 待检查集合
     * @return boolean
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 数组转集合
     * @param args 数组元素
     * @param <T>  泛型类型
     * @return {@link List}
     */
    @SafeVarargs
    public static <T> List<T> asList(final T... args) {
        return Optional.ofNullable(args).map(it ->
            it.length == 0 ? new ArrayList<T>(0) : new ArrayList<>(Arrays.asList(args))).orElse(new ArrayList<>(0));
    }

    /**
     * 数组转集合(排除null值)
     * @param args 数组元素
     * @param <T>  泛型类型
     * @return {@link List}
     */
    @SafeVarargs
    public static <T> List<T> asNotNullList(final T... args) {
        return Optional.ofNullable(args).map(it ->
            it.length == 0 ? new ArrayList<T>(0) :
                Arrays.stream(args).filter(Objects::nonNull).collect(Collectors.toList())).orElse(new ArrayList<>(0));
    }

    /**
     * 合并多个过滤器{@link Predicate}
     * @param predicates {@link Predicate}数组对象
     * @param <T>        泛型类型
     * @return {@link Predicate}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Predicate<T> and(final Predicate... predicates) {
        if (isEmpty(predicates)) {
            return null;
        } else {
            final int size = predicates.length;
            if (size == 1) {
                return predicates[0];
            } else {
                return Arrays.stream(predicates).reduce(it -> true, Predicate::and);
            }
        }
    }

    /**
     * 过滤不符合条件值
     * @param values     值列表
     * @param predicates {@link Predicate}数组对象
     * @param <T>        泛型类型
     * @return {@link LinkedHashSet}
     */
    @SafeVarargs
    public static <T> Set<T> filters(final Collection<T> values, Predicate<? super T>... predicates) {
        if (isEmpty(values)) {
            return new LinkedHashSet<>(0);
        }
        if (isEmpty(predicates)) {
            return new LinkedHashSet<>(values);
        }
        return Optional.ofNullable(and(predicates)).map(it ->
            values.stream().filter(it).collect(Collectors.toCollection(LinkedHashSet::new)))
            .orElse(new LinkedHashSet<>(values));
    }

    /**
     * 过滤不符合条件值
     * @param values    值列表
     * @param predicate {@link Predicate}对象
     * @param <T>       泛型类型
     * @return {@link LinkedHashSet}
     */
    public static <T> Set<T> filter(final Collection<T> values, final Predicate<T> predicate) {
        if (isEmpty(values)) {
            return new LinkedHashSet<>(0);
        }
        if (isNull(predicate)) {
            return new LinkedHashSet<>(values);
        }
        return values.stream().filter(predicate).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 过滤不符合条件值
     * @param values     值列表
     * @param predicates {@link Predicate}数组对象
     * @param <T>        泛型类型
     * @return {@link LinkedHashSet}
     */
    @SafeVarargs
    public static <T> Set<T> filters(final T[] values, Predicate<? super T>... predicates) {
        if (isEmpty(values)) {
            return new LinkedHashSet<>(0);
        }
        if (isEmpty(predicates)) {
            return new LinkedHashSet<>(Arrays.asList(values).subList(0, values.length));
        }
        return Optional.ofNullable(and(predicates)).map(it ->
            Arrays.stream(values).filter(it).collect(Collectors.toCollection(LinkedHashSet::new)))
            .orElse(new LinkedHashSet<>(Arrays.asList(values).subList(0, values.length)));
    }

    /**
     * 比较两个字符串是否一致
     * @param o1 对象1
     * @param o2 对象2
     * @return boolean
     * @see java.util.Objects#equals(Object, Object)
     */
    public static boolean equals(final Object o1, final Object o2) {
        return java.util.Objects.equals(o1, o2);
    }

    /**
     * 检查字符串是否为空
     * @param value 待检查字符串值
     * @return boolean
     */
    public static boolean isEmpty(final String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 检查字符串是否不为空
     * @param value 待检查字符串值
     * @return boolean
     */
    public static boolean isNotEmpty(final String value) {
        return !isEmpty(value);
    }

    /**
     * 检查字符串是否为空白值
     * @param value 待检查字符串值
     * @return boolean
     */
    public static boolean isBlank(final String value) {
        if (isNotEmpty(value) && value.trim().length() > 0) {
            final int size = value.length();
            for (int i = 0; i < size; i++) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查字符串是否不为空白值
     * @param value 待检查字符串值
     * @return boolean
     */
    public static boolean isNotBlank(final String value) {
        return !isBlank(value);
    }

    /**
     * 检查指定对象是否为null
     * @param object 待检查对象
     * @param <T>    泛型类型
     * @return 指定对象
     */
    public static <T> T requireNonNull(final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    /**
     * 检查指定对象是否为null
     * @param object  待检查对象
     * @param message 异常信息
     * @param <T>     泛型类型
     * @return 指定对象
     */
    public static <T> T requireNonNull(final T object, final String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 检查字符串是否为空值
     * @param value   待检查字符串
     * @param message 异常消息
     * @return 字符串
     */
    public static String requireNonEmpty(final String value, final String message) {
        if (isBlank(requireNonNull(value, message))) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * 检查表达式结果是否为真
     * @param expression 表达式结果
     * @param message    异常消息
     */
    public static void isTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
}
