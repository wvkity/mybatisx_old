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

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.core.plugin.exception.MyBatisPluginException;
import com.wvkity.mybatis.core.plugin.utils.Objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Optional;
import java.util.Properties;

/**
 * 本地缓存工厂
 * @author wvkity
 * @created 2021-02-12
 * @since 1.0.0
 */
public final class LocalCacheFactory {

    static final String DEF_CACHE_TYPE = "caffeine";

    private LocalCacheFactory() {
    }

    /**
     * 创建缓存对象
     * @param cacheImplClass 缓存接口实现类名
     * @param properties     缓存配置项
     * @param prefix         缓存配置key前缀
     * @param <K>            键类型
     * @param <V>            值类型
     * @return {@link LocalCache}
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LocalCache<K, V> create(final String cacheImplClass, final Properties properties,
                                                 final String prefix) {
        if (Objects.isBlank(cacheImplClass) || Constants.DEF_STR_FALSE.equalsIgnoreCase(cacheImplClass)
            || DEF_CACHE_TYPE.equalsIgnoreCase(cacheImplClass)) {
            try {
                Class.forName("com.github.benmanes.caffeine.cache.Cache");
                return new CaffeineLocalCache<>(properties, prefix);
            } catch (Exception ignore) {
                final String cacheId =
                    Optional.ofNullable(properties.getProperty(prefix + LocalCache.PROP_KEY_CACHE_ID))
                        .filter(Objects::isNotBlank).orElse("Ms_Record_Sql_Cache");
                return new MyBatisLocalCache<>(properties, prefix, cacheId);
            }
        } else {
            try {
                final Class<? extends LocalCache<K, V>> cacheClass =
                    (Class<? extends LocalCache<K, V>>) Class.forName(cacheImplClass);
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                try {
                    try {
                        final MethodHandle mh = lookup.findConstructor(cacheClass,
                            MethodType.methodType(void.class, Properties.class, String.class));
                        return (LocalCache<K, V>) mh.invokeWithArguments(properties, prefix);
                    } catch (Exception ignore) {
                        final String cacheId =
                            Optional.ofNullable(properties.getProperty(prefix + LocalCache.PROP_KEY_CACHE_ID))
                                .filter(Objects::isNotBlank).orElse("Ms_Record_Sql_Cache");
                        final MethodHandle mh = lookup.findConstructor(cacheClass,
                            MethodType.methodType(void.class, Properties.class, String.class, String.class));
                        return (LocalCache<K, V>) mh.invokeWithArguments(properties, prefix, cacheId);
                    }
                } catch (Throwable ignore) {
                    final MethodHandle mh = lookup.findConstructor(cacheClass, MethodType.methodType(void.class));
                    return (LocalCache<K, V>) mh.invokeWithArguments();
                }
            } catch (Throwable e) {
                throw new MyBatisPluginException("SQL cache instance creation failed: `" + cacheImplClass + "`: " + e, e);
            }
        }
    }

}
