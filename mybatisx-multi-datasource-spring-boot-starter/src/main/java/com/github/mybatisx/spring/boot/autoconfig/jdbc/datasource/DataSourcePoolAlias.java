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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import com.github.mybatisx.Objects;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * 数据源连接池类型别名
 * @author wvkity
 * @created 2021-08-10
 * @since 1.0.0
 */
public enum DataSourcePoolAlias {

    /**
     * tomcat
     */
    TOMCAT("org.apache.tomcat.jdbc.pool.DataSource", "tomcat"),
    /**
     * dbcp2
     */
    DBCP2("org.apache.commons.dbcp2.BasicDataSource", "dbcp2"),
    /**
     * hikari
     */
    HIKARI("com.zaxxer.hikari.HikariDataSource", "hikari"),
    /**
     * druid
     */
    DRUID("com.alibaba.druid.pool.DruidDataSource", "druid"),
    /**
     * c3p0
     */
    C3P0("com.mchange.v2.c3p0.ComboPooledDataSource", "c3p0");

    final String type;
    final String alias;

    DataSourcePoolAlias(String type, String alias) {
        this.type = type;
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }

    private static final Map<String, DataSourcePoolAlias> CACHE = new HashMap<>(8);

    static {
        for (DataSourcePoolAlias it : DataSourcePoolAlias.values()) {
            CACHE.put(it.getType().toLowerCase(Locale.ENGLISH), it);
        }
    }

    public static DataSourcePoolAlias get(final Class<? extends DataSource> type) {
        if (Objects.nonNull(type)) {
            return get(type.getName());
        }
        return null;
    }

    public static DataSourcePoolAlias get(final String type) {
        if (Objects.isNotBlank(type)) {
            return CACHE.get(type.toLowerCase(Locale.ENGLISH));
        }
        return null;
    }

    public static String getAlias(final Class<? extends DataSource> type) {
        return Optional.ofNullable(get(type)).map(DataSourcePoolAlias::getAlias).orElse(null);
    }

    public static String getAlias(final String type) {
        return Optional.ofNullable(get(type)).map(DataSourcePoolAlias::getAlias).orElse(null);
    }
}
