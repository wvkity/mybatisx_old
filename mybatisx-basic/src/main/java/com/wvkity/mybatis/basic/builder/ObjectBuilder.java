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
package com.wvkity.mybatis.basic.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 对象构建器
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public final class ObjectBuilder<T> implements Builder<T> {

    /**
     * {@link Supplier}对象
     */
    private final Supplier<T> supplier;
    /**
     * {@link Consumer}对象集合
     */
    private final List<Consumer<T>> consumers;

    private ObjectBuilder(Supplier<T> supplier) {
        this.supplier = supplier;
        this.consumers = new ArrayList<>();
    }

    private ObjectBuilder(Supplier<T> supplier, int size) {
        this.supplier = supplier;
        this.consumers = new ArrayList<>(size);
    }

    /**
     * 设置值
     * @param consumer {@link SingleConsumer}
     * @param v        值
     * @param <V>      值类型
     * @return {@link ObjectBuilder}
     */
    public <V> ObjectBuilder<T> with(final SingleConsumer<T, V> consumer, V v) {
        Optional.ofNullable(consumer).ifPresent(cs -> this.consumers.add(it -> consumer.accept(it, v)));
        return this;
    }

    /**
     * 构建指定对象
     * @return 泛型对象
     */
    @Override
    public T build() {
        final T instance = this.supplier.get();
        if (!this.consumers.isEmpty()) {
            this.consumers.forEach(it -> it.accept(instance));
        }
        return instance;
    }

    /**
     * 构建指定对象，并清除{@link Consumer}产生
     * @return 泛型对象
     */
    public T release() {
        final T it = build();
        this.clear();
        return it;
    }

    /**
     * 清除{@link Consumer}参数
     * @return {@link ObjectBuilder}
     */
    public ObjectBuilder<T> clear() {
        if (!this.consumers.isEmpty()) {
            this.consumers.clear();
        }
        return this;
    }

    /**
     * Consumer接口
     * @param <T> 泛型类型
     * @param <V> 值
     */
    @FunctionalInterface
    public interface SingleConsumer<T, V> {
        /**
         * 消费
         * @param t 目标类型
         * @param v 值
         */
        void accept(final T t, final V v);
    }

    /**
     * 创建构建器
     * @param supplier {@link Supplier}对象
     * @param <T>      目标对象类型
     * @return {@link ObjectBuilder}
     */
    public static <T> ObjectBuilder<T> of(final Supplier<T> supplier) {
        return new ObjectBuilder<>(supplier);
    }

    /**
     * 创建构建器
     * @param supplier {@link Supplier}对象
     * @param size     {@link Consumer}数目
     * @param <T>      目标对象类型
     * @return {@link ObjectBuilder}
     */
    public static <T> ObjectBuilder<T> of(final Supplier<T> supplier, final int size) {
        return new ObjectBuilder<>(supplier, size);
    }
}
