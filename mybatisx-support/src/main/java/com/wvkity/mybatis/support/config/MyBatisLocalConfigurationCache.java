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
package com.wvkity.mybatis.support.config;

import com.wvkity.mybatis.annotation.NamingStrategy;
import com.wvkity.mybatis.basic.exception.MapperException;
import com.wvkity.mybatis.basic.exception.MyBatisException;
import com.wvkity.mybatis.basic.inject.Injector;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import com.wvkity.mybatis.support.mapper.BaseMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * MyBatis配置缓存
 * @author wvkity
 * @created 2020-10-02
 * @since 1.0.0
 */
public class MyBatisLocalConfigurationCache {

    private static final Logger log = LoggerFactory.getLogger(MyBatisLocalConfigurationCache.class);
    /**
     * 默认SQL注入器
     */
    private static final String DEF_INJECTOR = "com.wvkity.mybatis.core.inject.DefaultInjector";
    /**
     * Mapper接口
     */
    private static final Class<?> DEFAULT_BASE_MAPPER_INTERFACE = BaseMapper.class;
    /**
     * 全局配置缓存
     */
    private static final Map<String, MyBatisGlobalConfiguration> GLOBAL_CONFIGURATION_CACHE = new ConcurrentHashMap<>();
    /**
     * 已注册的接口缓存
     */
    private static final Map<String, Set<String>> MAPPER_INTERFACE_CACHE = new ConcurrentHashMap<>();
    /**
     * 创建全局配置对象
     * @return {@link MyBatisGlobalConfiguration}
     */
    public static MyBatisGlobalConfiguration newInstance() {
        final MyBatisGlobalConfiguration it = new MyBatisGlobalConfiguration();
        it.setNamingStrategy(NamingStrategy.UPPER_UNDERSCORE);
        return it;
    }

    /**
     * 获取全局配置对象
     * @param configuration {@link Configuration}
     * @return {@link MyBatisGlobalConfiguration}
     */
    public static MyBatisGlobalConfiguration getGlobalConfiguration(final Configuration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            if (configuration instanceof MyBatisConfiguration) {
                return ((MyBatisConfiguration) configuration).getGlobalConfiguration();
            }
            return getGlobalConfiguration(configuration.toString());
        }).orElseThrow(() ->
            new MapperException("The configuration object cannot be empty. You need initialize Configuration"));
    }

    /**
     * 获取全局配置对象
     * @param key 键
     * @return {@link MyBatisGlobalConfiguration}
     */
    public static MyBatisGlobalConfiguration getGlobalConfiguration(final String key) {
        return Optional.ofNullable(GLOBAL_CONFIGURATION_CACHE.get(key)).orElseGet(() -> {
            final MyBatisGlobalConfiguration newConfig = newInstance();
            final MyBatisGlobalConfiguration oldConfig = GLOBAL_CONFIGURATION_CACHE.putIfAbsent(key, newConfig);
            return Optional.ofNullable(oldConfig).orElse(newConfig);
        });
    }

    /**
     * 缓存全局配置
     * @param configuration       {@link Configuration}
     * @param globalConfiguration {@link MyBatisGlobalConfiguration}
     */
    public static void cacheGlobalConfiguration(final Configuration configuration,
                                                final MyBatisGlobalConfiguration globalConfiguration) {
        if (configuration == null) {
            throw new MyBatisException("Mybatis configuration object cannot be null, please initialize it first");
        }
        if (globalConfiguration == null) {
            throw new MyBatisException("Mybatis global configuration object cannot be null.");
        }
        if (configuration instanceof MyBatisConfiguration) {
            // 覆盖原来的
            ((MyBatisConfiguration) configuration).setGlobalConfiguration(globalConfiguration);
        } else {
            GLOBAL_CONFIGURATION_CACHE.putIfAbsent(configuration.toString(), globalConfiguration);
        }
    }

    /**
     * 获取CURD-SQL注入器
     * @param configuration {@link Configuration}
     * @return {@link Injector}
     */
    public static Injector getInjector(final Configuration configuration) {
        final MyBatisGlobalConfiguration it = getGlobalConfiguration(configuration);
        return Optional.ofNullable(it.getInjector()).orElseGet(() -> {
            final Injector injector = newInjector();
            it.setInjector(injector);
            return injector;
        });
    }

    private static Injector newInjector() {
        try {
            final Class<?> clazz = Class.forName(DEF_INJECTOR);
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            return (Injector) lookup.findConstructor(clazz, MethodType.methodType(void.class)).invokeWithArguments();
        } catch (Throwable e) {
            throw new MyBatisException(e.getMessage(), e);
        }
    }

    /**
     * 注入CURD-SQL
     * @param assistant       {@link MapperBuilderAssistant}
     * @param mapperInterface Mapper接口
     */
    public static void inject(final MapperBuilderAssistant assistant, final Class<?> mapperInterface) {
        if (DEFAULT_BASE_MAPPER_INTERFACE.isAssignableFrom(mapperInterface)) {
            getInjector(assistant.getConfiguration()).inject(assistant, mapperInterface);
        }
    }

    /**
     * 注册Mapper接口到缓存中(如果缓存中不存在)
     * @param configuration   {@link Configuration}
     * @param mapperInterface Mapper接口
     * @return true: 成功, false: 失败
     */
    public static boolean mapperInterfaceRegistryIfNotExists(final Configuration configuration,
                                                             final Class<?> mapperInterface) {
        final String key = configuration.toString();
        final String value = mapperInterface.getName();
        final Set<String> interfaces = MAPPER_INTERFACE_CACHE.computeIfAbsent(key, k -> new ConcurrentSkipListSet<>());
        final boolean exists = !interfaces.contains(key);
        if (exists) {
            interfaces.add(value);
        }
        return exists;
    }
}
