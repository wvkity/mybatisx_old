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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 将值进一步做相关处理工具(不抛出NPE)
 * @author wvkity
 * @created 2020-10-08
 * @see Optional
 * @since 1.0.0
 */
public final class When<T> {

    private static final When<?> EMPTY = When.of(null);

    /**
     * 值
     */
    private final T value;

    private When(T value) {
        this.value = value;
    }

    /**
     * 转成{@link Optional}对象
     * @return {@link Optional}
     */
    @SuppressWarnings("unchecked")
    public Optional<T> optional() {
        return this.value instanceof Optional ? (Optional<T>) this.value : Optional.ofNullable(this.value);
    }

    /**
     * 设置值
     * @param mapper {@link Consumer}
     */
    public void apply(final Consumer<T> mapper) {
        this.ifPresent(it -> Optional.ofNullable(mapper).ifPresent(c -> c.accept(it)));
    }

    /**
     * 比较指定值，并设置值
     * @param value  待比较值
     * @param mapper {@link Consumer}
     */
    public void apply(final T value, final Consumer<Boolean> mapper) {
        this.ifPresent(it -> mapper.accept(Objects.equals(it, value)));
    }

    /**
     * 比较指定值
     * @param value      值
     * @param comparator 比较器
     * @return {@link When}
     */
    public When<T> compare(final T value, final ObjectComparator<T> comparator) {
        return When.of(comparator.compare(this.value, value));
    }

    /**
     * 比较指定值，如果相等，则以现在的值生成新的对象，否则以指定值生成对象
     * @param value 指定值
     * @return {@link When}
     */
    public When<T> then(final T value) {
        return Objects.equals(this.value, value) ? of(this.value) : of(value);
    }

    /**
     * 比较指定值，如果相等，则返回新的对象，否则返回空值对象
     * @param value 待比较值
     * @param u     返回值
     * @param <U>   返回值类型
     * @return {@link When}
     */
    public <U> When<U> then(final T value, final U u) {
        return then(value, (t1, t2) -> Objects.equals(t1, t2) ? u : null);
    }

    /**
     * 比较并返回全新的对象
     * @param value  待比较值
     * @param mapper {@link BiFunction}
     * @param <U>    返回值类型
     * @return {@link When}
     */
    @SuppressWarnings("unchecked")
    public <U> When<U> then(final T value, final BiFunction<? super T, ? super T, ? extends U> mapper) {
        return (When<U>) this.map(it ->
            When.of(mapper).map(at -> When.of(mapper.apply(it, value))).orElse(empty())
        ).orElse(empty());
    }

    /**
     * 获取值
     * @return 值
     */
    public T get() {
        return this.value;
    }

    /**
     * 检查值是否不为空
     * @return boolean
     */
    public boolean isPresent() {
        return this.value != null;
    }

    /**
     * 如果存在值则消费，否则不做任何事
     * @param mapper {@link Consumer}
     */
    public void ifPresent(final Consumer<? super T> mapper) {
        if (this.value != null) {
            mapper.accept(this.value);
        }
    }

    /**
     * 过滤筛选
     * @param predicate {@link Predicate}
     * @return {@link When}
     */
    public When<T> filter(final Predicate<? super T> predicate) {
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(this.value) ? this : empty();
        }
    }

    /**
     * 返回新的流
     * @param mapper {@link Consumer}
     * @param <U>    新的值类型
     * @return {@link When}
     */
    public <U> When<U> map(final Function<? super T, ? extends U> mapper) {
        if (!isPresent()) {
            return empty();
        }
        return When.of(mapper.apply(this.value));
    }

    /**
     * 返回新的流
     * @param mapper {@link Consumer}
     * @param <U>    新的值类型
     * @return {@link When}
     */
    public <U> When<U> flatMap(Function<? super T, When<U>> mapper) {
        if (!isPresent()) {
            return empty();
        }
        return mapper.apply(this.value);
    }

    public T orElse(final T other) {
        return this.value != null ? this.value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return this.value != null ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 创建{@link When}对象
     * @param value 值
     * @param <T>   泛型对象
     * @return {@link When}
     */
    public static <T> When<T> of(final T value) {
        return new When<>(value);
    }

    /**
     * 创建空值{@link When}对象
     * @param <T> 泛型类型
     * @return {@link When}
     */
    public static <T> When<T> empty() {
        @SuppressWarnings("unchecked") final When<T> when = (When<T>) EMPTY;
        return when;
    }

    /**
     * if (t1 == null) { return t2;} else {return null;}
     * @param t1  待检查值
     * @param t2  返回值
     * @param <T> 值类型
     * @return {@link When}
     */
    public static <T> When<T> ifNullable(final T t1, final T t2) {
        return ObjectComparator.nullable(t1, t2);
    }

    /**
     * if (mapper.apply(target) == null) { return value;} else {return null;}
     * @param target 目标对象
     * @param value  值
     * @param mapper {@link Function}对象
     * @param <T>    泛型
     * @param <U>    值类型
     * @return {@link When}
     */
    public static <T, U> When<U> ifNullable(final T target, final U value,
                                            final Function<? super T, ? extends U> mapper) {
        return ObjectComparator.nullable(target, mapper, value);
    }

    /**
     * 根据{@link ObjectComparator}返回值生成{@link When}对象
     * @param t1         对象1
     * @param t2         对象2
     * @param comparator {@link ObjectComparator}
     * @param <T>        泛型类型
     * @return {@link When}
     */
    public static <T> When<T> ifNullable(final T t1, final T t2, final ObjectComparator<T> comparator) {
        return When.of(comparator.compare(t1, t2));
    }

    /**
     * if (Objects.equals(t1, t2)) { return null;} else {return t1}
     * @param t1  值1
     * @param t2  值2
     * @param <T> 值类型
     * @return {@link When}
     */
    public static <T> When<T> ifNotEqual(final T t1, final T t2) {
        return ObjectComparator.notEqual(t1, t2);
    }

    /**
     * if (Objects.equals(getter.apply(target), value)) { return null;} else {return value;}
     * @param target 目标对象
     * @param value  值
     * @param mapper {@link Function}对象
     * @param <T>    泛型
     * @param <U>    值类型
     * @return {@link When}
     */
    public static <T, U> When<U> ifNotEqual(final T target, final U value,
                                            final Function<? super T, ? extends U> mapper) {
        return ObjectComparator.notEqual(target, mapper, value);
    }

    /**
     * 根据{@link ObjectComparator}返回的值生成{@link When}对象
     * @param t1         对象1
     * @param t2         对象2
     * @param comparator {@link ObjectComparator}
     * @param <T>        泛型类型
     * @return {@link When}
     */
    public static <T> When<T> ifNotEqual(final T t1, final T t2, final ObjectComparator<T> comparator) {
        return When.of(comparator.compare(t1, t2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof When)) return false;
        When<?> when = (When<?>) o;
        return java.util.Objects.equals(value, when.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Await{" +
            "value=" + value +
            '}';
    }
}
