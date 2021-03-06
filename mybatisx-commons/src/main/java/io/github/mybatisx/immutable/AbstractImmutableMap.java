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
package io.github.mybatisx.immutable;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 抽象不可变Map集合
 * @param <K> 键
 * @param <V> 值
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractImmutableMap<K, V> extends AbstractMap<K, V> implements Serializable {

    protected static final UnsupportedOperationException UOE = new UnsupportedOperationException();

    @Override
    public void clear() {
        throw UOE;
    }

    @Override
    public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> rf) {
        throw UOE;
    }

    @Override
    public final V computeIfAbsent(K key, Function<? super K, ? extends V> mf) {
        throw UOE;
    }

    @Override
    public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> rf) {
        throw UOE;
    }

    @Override
    public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> rf) {
        throw UOE;
    }

    @Override
    public final V put(K key, V value) {
        throw UOE;
    }

    @Override
    public final V remove(Object key) {
        throw UOE;
    }

    @Override
    public final void putAll(Map<? extends K, ? extends V> m) {
        throw UOE;
    }

    @Override
    public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw UOE;
    }

    @Override
    public final V putIfAbsent(K key, V value) {
        throw UOE;
    }

    @Override
    public final boolean remove(Object key, Object value) {
        throw UOE;
    }

    @Override
    public final boolean replace(K key, V oldValue, V newValue) {
        throw UOE;
    }

    @Override
    public final V replace(K key, V value) {
        throw UOE;
    }
}
