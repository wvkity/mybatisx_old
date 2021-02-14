/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.wvkity.mybatis.core.inject.mapping.sql;

import com.wvkity.mybatis.core.exception.MyBatisException;
import com.wvkity.mybatis.core.inject.mapping.sql.proxy.SupplierProxyFactory;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link Supplier}代理工厂注册器
 * @author wvkity
 * @created 2020-12-25
 * @since 1.0.0
 */
public class SupplierRegistry {

    private final Configuration configuration;
    private final Map<Class<?>, SupplierProxyFactory<? extends Supplier>> supplierCache = new ConcurrentHashMap<>();

    public SupplierRegistry(MyBatisConfiguration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    public <T> void addSupplier(final Class<T> type) {
        if (type != null && !hasSupplier(type)) {
            try {
                final Class<?> objectClass = Reflections.getGenericClass(type, 0);
                if (Supplier.class.isAssignableFrom(objectClass)) {
                    final Class<? extends Supplier> supplier = (Class<? extends Supplier>) objectClass;
                    this.supplierCache.putIfAbsent(type, new SupplierProxyFactory<>(supplier));
                }
            } catch (Exception ignore) {
                // ignore
            }
        }
    }

    public <T> boolean hasSupplier(final Class<T> type) {
        return this.supplierCache.containsKey(type);
    }

    public <T> Supplier getSupplier(final Class<T> type, final MyBatisConfiguration configuration,
                                    final Object... args) {
        final SupplierProxyFactory<? extends Supplier> factory = this.supplierCache.get(type);
        if (factory == null) {
            throw new MyBatisException("Type" + type + " is not known to the SupplierRegistry.");
        }
        try {
            return factory.newInstance(configuration, args);
        } catch (Exception e) {
            throw new MyBatisException("Error getting supplier instance. Cause: ", e);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
