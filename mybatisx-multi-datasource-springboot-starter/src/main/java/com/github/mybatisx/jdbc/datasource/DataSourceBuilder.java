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
package com.github.mybatisx.jdbc.datasource;

import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.DataSourcePoolAlias;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源构建器
 * @author wvkity
 * @created 2021-08-06
 * @since 1.0.0
 */
public final class DataSourceBuilder<T extends DataSource> {

    private static final String[] DATA_SOURCE_TYPE_NAMES = new String[]{DataSourcePoolAlias.HIKARI.getType(),
        DataSourcePoolAlias.TOMCAT.getType(), DataSourcePoolAlias.DBCP2.getType(), DataSourcePoolAlias.C3P0.getType(),
        DataSourcePoolAlias.DRUID.getType()};
    private static final String PROP_USERNAME = "username";
    private static final String PROP_PASSWORD = "password";
    private static final String PROP_URL = "url";
    private static final String PROP_DRIVER_CLASS_NAME = "driverClassName";

    private Class<? extends DataSource> type;

    private final ClassLoader classLoader;

    private final Map<String, String> properties = new HashMap<>();

    public static DataSourceBuilder<?> create() {
        return new DataSourceBuilder<>(null);
    }

    public static DataSourceBuilder<?> create(ClassLoader classLoader) {
        return new DataSourceBuilder<>(classLoader);
    }

    private DataSourceBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        Class<? extends DataSource> type = getType();
        DataSource result = BeanUtils.instantiateClass(type);
        maybeGetDriverClassName();
        bind(result);
        return (T) result;
    }

    private void maybeGetDriverClassName() {
        if (!this.properties.containsKey(PROP_DRIVER_CLASS_NAME) && this.properties.containsKey(PROP_URL)) {
            String url = this.properties.get(PROP_URL);
            String driverClass = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
            this.properties.put(PROP_DRIVER_CLASS_NAME, driverClass);
        }
    }

    private void bind(DataSource result) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(this.properties);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("driver-class-name", "driver-class");
        aliases.addAliases(PROP_URL, "jdbc-url");
        aliases.addAliases(PROP_USERNAME, "user");
        Binder binder = new Binder(source.withAliases(aliases));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    @SuppressWarnings("unchecked")
    public <D extends DataSource> DataSourceBuilder<D> type(Class<D> type) {
        this.type = type;
        return (DataSourceBuilder<D>) this;
    }

    public DataSourceBuilder<T> url(String url) {
        this.properties.put(PROP_URL, url);
        return this;
    }

    public DataSourceBuilder<T> driverClassName(String driverClassName) {
        this.properties.put(PROP_DRIVER_CLASS_NAME, driverClassName);
        return this;
    }

    public DataSourceBuilder<T> username(String username) {
        this.properties.put(PROP_USERNAME, username);
        return this;
    }

    public DataSourceBuilder<T> password(String password) {
        this.properties.put(PROP_PASSWORD, password);
        return this;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends DataSource> findType(ClassLoader classLoader) {
        for (String name : DATA_SOURCE_TYPE_NAMES) {
            try {
                return (Class<? extends DataSource>) ClassUtils.forName(name, classLoader);
            } catch (Exception ignore) {
                // Swallow and continue
            }
        }
        return null;
    }

    private Class<? extends DataSource> getType() {
        Class<? extends DataSource> type = (this.type != null) ? this.type : findType(this.classLoader);
        if (type != null) {
            return type;
        }
        throw new IllegalStateException("No supported DataSource type found");
    }

}
