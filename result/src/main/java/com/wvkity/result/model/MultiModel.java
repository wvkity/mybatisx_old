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
package com.wvkity.result.model;

import java.util.Collection;
import java.util.Map;

/**
 * {@link java.util.Map Map}类型数据模型
 * @author wvkity
 * @created 2021-02-15
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public interface MultiModel extends Model<Map<String, Object>>, Aware {

    /**
     * 获取值
     * @param key 键
     * @param <T> 值类型
     * @return 值
     */
    <T> T get(final String key);

    /**
     * 添加值
     * @param key   键
     * @param value 值
     * @return {@link MultiModel}
     */
    MultiModel put(final String key, final Object value);

    /**
     * 添加值
     * @param key   键
     * @param value 值
     * @return {@link MultiModel}
     */
    MultiModel putIfAbsent(final String key, final Object value);

    /**
     * 添加多个值
     * @param data 值
     * @return {@link MultiModel}
     */
    MultiModel putAll(final Map<String, Object> data);

    /**
     * 是否包含指定键
     * @param key 键
     * @return boolean
     */
    boolean containsKey(final String key);

    /**
     * 是否包含指定值
     * @param value 值
     * @return boolean
     */
    boolean containsValue(final Object value);

    /**
     * 元素个数
     * @return 元素个数
     */
    int getSize();

    /**
     * 根据键移除值
     * @param key 键
     * @return {@link MultiModel}
     */
    MultiModel remove(final String key);

    /**
     * 清空
     * @return {@link MultiModel}
     */
    MultiModel clear();

    ///// Add elements methods /////

    /**
     * 添加数组元素
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel array(final String key, final T... values);

    /**
     * 追加元素到指定的数组中
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel addArray(final String key, final T... values);

    /**
     * 追加元素到指定的数组中
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel addArray(final String key, final Collection<T> values);

    /**
     * 添加{@link java.util.Set Set}元素
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel set(final String key, final T... values);

    /**
     * 追加元素到指定的{@link java.util.Set Set}中
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel addSet(final String key, final T... values);

    /**
     * 追加元素到指定的{@link java.util.Set Set}中
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel addSet(final String key, final Collection<T> values);

    /**
     * 添加{@link java.util.List List}元素
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel list(final String key, final T... values);

    /**
     * 追加元素到指定的{@link java.util.List List}中
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel addList(final String key, final T... values);

    /**
     * 追加元素到指定的{@link java.util.List List}中
     * @param key    键
     * @param values 值
     * @param <T>    元素值类型
     * @return {@link MultiModel}
     */
    <T> MultiModel addList(final String key, final Collection<T> values);

    /**
     * 添加{@link Map}元素
     * @param key 键
     * @param k   键
     * @param v   值
     * @return {@link MultiModel}
     */
    MultiModel map(final String key, final Object k, final Object v);

    /**
     * 追加元素到指定的{@link Map}中
     * @param key 键
     * @param k   键
     * @param v   值
     * @return {@link MultiModel}
     */
    MultiModel addMap(final String key, final Object k, final Object v);

    /**
     * 追加元素到指定的{@link Map}中
     * @param key    键
     * @param values 值
     * @return {@link MultiModel}
     */
    MultiModel addMap(final String key, final Map<Object, Object> values);

    /**
     * 添加{@link Map}元素
     * @param key 键
     * @param k   键
     * @param v   值
     * @return {@link MultiModel}
     */
    MultiModel mapIfAbsent(final String key, final Object k, final Object v);

    /**
     * 追加元素到指定的{@link Map}中
     * @param key 键
     * @param k   键
     * @param v   值
     * @return {@link MultiModel}
     */
    MultiModel addMapIfAbsent(final String key, final Object k, final Object v);

    /**
     * 追加元素到指定的{@link Map}中
     * @param key    键
     * @param values 值
     * @return {@link MultiModel}
     */
    MultiModel addMapIfAbsent(final String key, final Map<Object, Object> values);

}
