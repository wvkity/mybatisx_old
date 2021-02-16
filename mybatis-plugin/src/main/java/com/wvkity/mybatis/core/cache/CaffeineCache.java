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
package com.wvkity.mybatis.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Caffeine缓存
 * @param <K> 键类型
 * @param <V> 值类型
 * @author wvkity
 * @created 2021-02-11
 * @since 1.0.0
 */
public class CaffeineCache<K, V> implements LocalCache<K, V> {

    private final Cache<K, V> cache;
    private final String prefix;
    private final Properties properties;

    public CaffeineCache(final Properties properties, final String prefix) {
        this.prefix = prefix;
        this.properties = properties;
        final Caffeine<Object, Object> it = Caffeine.newBuilder();
        it.maximumSize(Long.parseLong(Optional.ofNullable(properties.getProperty(prefix + DEF_KEY_CAFFEINE_MAXIMUM_SIZE))
            .orElse("1000")));
        ifPresent(DEF_KEY_CAFFEINE_EXPIRE_AFTER_ACCESS, v -> it.expireAfterAccess(v, TimeUnit.MILLISECONDS));
        ifPresent(DEF_KEY_CAFFEINE_EXPIRE_AFTER_WRITE, v -> it.expireAfterWrite(v, TimeUnit.MILLISECONDS));
        ifPresent(DEF_KEY_CAFFEINE_INITIAL_CAPACITY, v -> it.initialCapacity(v.intValue()));
        this.cache = it.build();
    }

    private void ifPresent(final String key, final Consumer<Long> consumer) {
        Optional.ofNullable(this.properties.getProperty(this.prefix + key)).ifPresent(it ->
            consumer.accept(Long.parseLong(it)));
    }

    @Override
    public V get(K key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void put(K key, V value) {
        this.cache.put(key, value);
    }
}