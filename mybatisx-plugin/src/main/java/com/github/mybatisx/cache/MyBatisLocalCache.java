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
package com.github.mybatisx.cache;

import com.github.mybatisx.Objects;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.FifoCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.CacheBuilder;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Mybatis自带缓存
 * @author wvkity
 * @created 2021-02-11
 * @since 1.0.0
 */
public class MyBatisLocalCache<K, V> implements LocalCache<K, V> {

    private final Properties properties;
    private final String prefix;
    private final Cache cache;

    @SuppressWarnings("unchecked")
    public MyBatisLocalCache(Properties properties, String prefix, String cacheId) {
        this.properties = properties;
        this.prefix = prefix;
        final CacheBuilder it = new CacheBuilder(cacheId);
        final String targetClass = this.properties.getProperty(this.prefix + PROP_KEY_MYBATIS_TYPE_CLASS);
        if (Objects.isNotBlank(targetClass)) {
            try {
                it.implementation((Class<? extends Cache>) Class.forName(targetClass));
            } catch (ClassNotFoundException ignore) {
                it.implementation(PerpetualCache.class);
            }
        } else {
            it.implementation(PerpetualCache.class);
        }
        final String evictionClass = this.properties.getProperty(this.prefix + PROP_KEY_MYBATIS_EVICTION_CLASS);
        if (Objects.isNotBlank(evictionClass)) {
            try {
                it.addDecorator((Class<? extends Cache>) Class.forName(evictionClass));
            } catch (ClassNotFoundException ignore) {
                it.addDecorator(FifoCache.class);
            }
        } else {
            it.addDecorator(FifoCache.class);
        }
        ifPresent(PROP_KEY_MYBATIS_FLUSH_INTERVAL, it::clearInterval);
        ifPresent(PROP_KEY_MYBATIS_SIZE, v -> it.size(v.intValue()));
        it.properties(this.properties);
        this.cache = it.build();
    }

    private void ifPresent(final String key, final Consumer<Long> consumer) {
        Optional.ofNullable(this.properties.getProperty(this.prefix + key)).ifPresent(it ->
            consumer.accept(Long.parseLong(it)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(K key) {
        return (V) Optional.ofNullable(this.cache.getObject(key)).orElse(null);
    }

    @Override
    public void put(K key, V value) {
        this.cache.putObject(key, value);
    }
}
