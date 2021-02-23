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
package com.wvkity.mybatis.core.function;

import com.wvkity.mybatis.core.utils.Objects;

import java.util.function.Function;

/**
 * 值比较器
 * @param <T> 泛型类型
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
@FunctionalInterface
public interface ObjectComparator<T> {

    /**
     * 比较两个值
     * @param t1 对象1
     * @param t2 对象2
     * @return 值
     */
    T compare(final T t1, final T t2);

    /**
     * 判断t1是否为空值，如果为空值则返回t2，否则返回null
     * @param t1  值1
     * @param t2  值2
     * @param <T> 值类型
     * @return {@link When}
     */
    static <T> When<T> nullable(final T t1, final T t2) {
        if (t1 == null || (t1 instanceof String && Objects.isBlank((String) t1))) {
            return When.of(t2);
        } else {
            return When.of(null);
        }
    }

    /**
     * 判断从指定对象中获取的值是否为空，为null则返回value，否则返回获取到的值
     * @param target 指定对象
     * @param mapper {@link Function}
     * @param value  值
     * @param <T>    泛型
     * @param <U>    值类型
     * @return {@link When}
     */
    static <T, U> When<U> nullable(final T target, final Function<? super T, ? extends U> mapper, final U value) {
        return nullable(mapper.apply(target), value);
    }

    /**
     * 比较两个值是否相等，不相等则返回t1，否则返回null
     * @param t1  对象1
     * @param t2  对象2
     * @param <T> 值类型
     * @return {@link When}
     */
    static <T> When<T> notEqual(final T t1, final T t2) {
        return Objects.equals(t1, t2) ? When.of(null) : When.of(t1);
    }

    /**
     * 从指定对象获取到的值和指定的值比较是否相等，不相等则返回对象值，否则返回null
     * @param target 指定对象
     * @param mapper {@link Function}
     * @param value  指定值
     * @param <T>    泛型类型
     * @param <U>    值类型
     * @return {@link When}
     */
    static <T, U> When<U> notEqual(final T target, final Function<? super T, ? extends U> mapper, final U value) {
        return notEqual(mapper.apply(target), value);
    }
}
