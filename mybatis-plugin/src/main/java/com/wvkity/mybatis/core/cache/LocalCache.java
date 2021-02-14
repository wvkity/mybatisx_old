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

/**
 * 缓存
 * @param <K> 键类型
 * @param <V> 值类型
 * @author wvkity
 * @created 2021-02-11
 * @since 1.0.0
 */
public interface LocalCache<K, V> {

    String DEF_KEY_MYBATIS_TYPE_CLASS = ".typeClass";
    String DEF_KEY_MYBATIS_EVICTION_CLASS = ".evictionClass";
    String DEF_KEY_MYBATIS_FLUSH_INTERVAL = ".flushInterval";
    String DEF_KEY_MYBATIS_SIZE = ".size";
    String DEF_KEY_CAFFEINE_MAXIMUM_SIZE= ".maximumSize";
    String DEF_KEY_CAFFEINE_EXPIRE_AFTER_ACCESS= ".expireAfterAccess";
    String DEF_KEY_CAFFEINE_EXPIRE_AFTER_WRITE= ".expireAfterWrite";
    String DEF_KEY_CAFFEINE_INITIAL_CAPACITY= ".initialCapacity";

    /**
     * 获取值
     * @param key 键
     * @return 值
     */
    V get(final K key);

    /**
     * 缓存值
     * @param key   键
     * @param value 值
     */
    void put(final K key, final V value);
}
