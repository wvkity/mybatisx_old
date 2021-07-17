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
package com.github.mybatisx.plugin.paging.proxy;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.plugin.paging.dialect.AbstractDialect;
import com.github.mybatisx.plugin.paging.dialect.Dialect;
import com.github.mybatisx.plugin.paging.dialect.support.Db2Dialect;
import com.github.mybatisx.plugin.paging.dialect.support.HsqldbDialect;
import com.github.mybatisx.plugin.paging.dialect.support.InformixDialect;
import com.github.mybatisx.plugin.paging.dialect.support.MySqlDialect;
import com.github.mybatisx.plugin.paging.dialect.support.Oracle9iDialect;
import com.github.mybatisx.plugin.paging.dialect.support.OracleDialect;
import com.github.mybatisx.plugin.paging.dialect.support.SqlServer2012LaterDialect;
import com.github.mybatisx.plugin.paging.dialect.support.SqlServerDialect;
import com.github.mybatisx.plugin.exception.MyBatisPluginException;
import org.apache.ibatis.mapping.MappedStatement;

import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 抽象方言
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
public abstract class AbstractDialectProxy {

    /**
     * 数据库方言缓存
     */
    protected static final Map<String, Class<? extends Dialect>> DATABASE_DIALECT_REGISTRY = new ConcurrentHashMap<>();

    /**
     * 分页方言缓存(JDBC解析)
     */
    protected Map<String, AbstractDialect> dialectFromJdbcCache = new ConcurrentHashMap<>();
    /**
     * 分页方言缓存(直接指定)
     */
    protected Map<String, AbstractDialect> dialectFromSpecifiedCache = new ConcurrentHashMap<>();
    /**
     * 分页方言线程缓存
     */
    protected ThreadLocal<AbstractDialect> threadLocalDelegateDialect = new ThreadLocal<>();
    /**
     * 分页方言委托对象
     */
    protected AbstractDialect delegate;
    /**
     * 属性
     */
    protected Properties properties;
    /**
     * 数据库类型
     */
    protected String databaseDialect;
    /**
     * 自动解析JDBC获取数据库方言
     */
    protected boolean autoRuntimeParsingJdbc;
    /**
     * 多数据源时根据JDBC URL获取后是否自动释放
     */
    protected boolean autoReleaseConnect = true;
    /**
     * 锁
     */
    private final Lock lock = new ReentrantLock();

    static {
        DATABASE_DIALECT_REGISTRY.put("MYSQL", MySqlDialect.class);
        DATABASE_DIALECT_REGISTRY.put("MARIADB", MySqlDialect.class);
        DATABASE_DIALECT_REGISTRY.put("SQLITE", MySqlDialect.class);
        DATABASE_DIALECT_REGISTRY.put("OSCAR", MySqlDialect.class);
        DATABASE_DIALECT_REGISTRY.put("CLICKHOUSE", MySqlDialect.class);
        DATABASE_DIALECT_REGISTRY.put("ORACLE9I", Oracle9iDialect.class);
        DATABASE_DIALECT_REGISTRY.put("ORACLE", OracleDialect.class);
        DATABASE_DIALECT_REGISTRY.put("DM", OracleDialect.class);
        DATABASE_DIALECT_REGISTRY.put("EDB", OracleDialect.class);
        DATABASE_DIALECT_REGISTRY.put("DB2", Db2Dialect.class);
        DATABASE_DIALECT_REGISTRY.put("POSTGRESQL", HsqldbDialect.class);
        DATABASE_DIALECT_REGISTRY.put("H2", HsqldbDialect.class);
        DATABASE_DIALECT_REGISTRY.put("HSQLDB", HsqldbDialect.class);
        DATABASE_DIALECT_REGISTRY.put("PHONEIX", HsqldbDialect.class);
        DATABASE_DIALECT_REGISTRY.put("INFORMIX", InformixDialect.class);
        DATABASE_DIALECT_REGISTRY.put("INFORMIXSQLI", InformixDialect.class);
        DATABASE_DIALECT_REGISTRY.put("SQLSERVER2012LATER", SqlServer2012LaterDialect.class);
        DATABASE_DIALECT_REGISTRY.put("DERBY", SqlServer2012LaterDialect.class);
        DATABASE_DIALECT_REGISTRY.put("SQLSERVER", SqlServerDialect.class);
    }

    /**
     * 初始化方言
     * @param ms {@link MappedStatement}
     */
    public void initDialect(final MappedStatement ms) {
        if (this.delegate == null) {
            if (this.autoRuntimeParsingJdbc) {
                this.delegate = this.getDialectFromJdbcUrl(ms);
            } else {
                this.threadLocalDelegateDialect.set(this.getDialectFromJdbcUrl(ms));
            }
        }
    }

    /**
     * 根据指定分页方言获取对象
     * @param dialect 指定分页方言
     * @return {@link AbstractDialect}
     */
    private AbstractDialect getDialectFromSpecified(final String dialect) {
        if (Objects.isNotBlank(dialect) && !Constants.DEF_UNDEFINED.equalsIgnoreCase(dialect)) {
            final String key = dialect.toUpperCase(Locale.ENGLISH);
            if (this.dialectFromSpecifiedCache.containsKey(key)) {
                return this.dialectFromSpecifiedCache.get(key);
            }
            try {
                this.lock.lock();
                if (this.dialectFromSpecifiedCache.containsKey(key)) {
                    return this.dialectFromSpecifiedCache.get(key);
                }
                final AbstractDialect instance = this.newInstance(dialect);
                instance.setProperties(properties);
                final AbstractDialect old = this.dialectFromSpecifiedCache.putIfAbsent(key, instance);
                if (old != null) {
                    return old;
                }
                return instance;
            } catch (Exception e) {
                throw new MyBatisPluginException(e);
            } finally {
                this.lock.unlock();
            }
        } else {
            throw new MyBatisPluginException("The paging plugin initialization must specify the database dialect " +
                "value");
        }
    }

    /**
     * 解析JDBC路径获取分页方言对象
     * @param ms {@link MappedStatement}
     * @return {@link AbstractDialect}
     */
    private AbstractDialect getDialectFromJdbcUrl(final MappedStatement ms) {
        final String jdbcUrl = this.getJdbcUrlFromDataSource(ms.getConfiguration().getEnvironment().getDataSource());
        final String key = jdbcUrl.toUpperCase(Locale.ENGLISH);
        if (this.dialectFromJdbcCache.containsKey(key)) {
            return this.dialectFromJdbcCache.get(key);
        }
        try {
            this.lock.lock();
            if (this.dialectFromJdbcCache.containsKey(key)) {
                return this.dialectFromJdbcCache.get(key);
            }
            final String dbAlias = this.getDialectAliasFromJdbcUrl(jdbcUrl);
            if (Objects.isBlank(dbAlias)) {
                throw new MyBatisPluginException("The plugin does not currently support the current database or " +
                    "cannot recognize the current database type.");
            }
            final AbstractDialect instance = this.newInstance(dbAlias);
            instance.setProperties(this.properties);
            final AbstractDialect old = this.dialectFromJdbcCache.putIfAbsent(key, instance);
            if (old != null) {
                return old;
            }
            return instance;
        } catch (Exception e) {
            throw new MyBatisPluginException(e);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 从数据源获取JDBC连接地址
     * @param dataSource {@link DataSource}
     * @return JDBC地址
     */
    private String getJdbcUrlFromDataSource(final DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return connection.getMetaData().getURL();
        } catch (SQLException e) {
            throw new MyBatisPluginException(e);
        } finally {
            if (connection != null && this.autoReleaseConnect) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                    // ignore
                }
            }
        }
    }

    /**
     * 根据JDBC路径获取数据库方言
     * @param jdbcUrl JDBC路径
     * @return 数据库方言
     */
    private String getDialectAliasFromJdbcUrl(final String jdbcUrl) {
        return DATABASE_DIALECT_REGISTRY.keySet().stream().map(it -> it.toUpperCase(Locale.ENGLISH))
            .filter(it -> jdbcUrl.contains("JDBC:" + it + ":")).findAny().orElse(null);
    }

    /**
     * 创建方言实例
     * @param dialect 方言全类名
     * @return {@link AbstractDialect}
     */
    private AbstractDialect newInstance(final String dialect) {
        if (Objects.isBlank(dialect)) {
            throw new MyBatisPluginException("The corresponding database dialect identifier must be specified.");
        }
        try {
            final Class<?> clazz;
            if (DATABASE_DIALECT_REGISTRY.containsKey(dialect.toUpperCase(Locale.ENGLISH))) {
                clazz = DATABASE_DIALECT_REGISTRY.get(dialect.toUpperCase(Locale.ENGLISH));
            } else {
                clazz = Class.forName(dialect);
            }
            if (AbstractDialect.class.isAssignableFrom(clazz)) {
                final MethodType methodType = MethodType.methodType(void.class);
                return (AbstractDialect) MethodHandles.lookup().findConstructor(clazz, methodType).invokeWithArguments();
            } else {
                throw new MyBatisPluginException("The current plugin must inherit the `" +
                    AbstractDialect.class.getName() + "` abstract class and implement the corresponding methods");
            }
        } catch (Throwable e) {
            throw new MyBatisPluginException("Failed to initialize database dialect object: " + e.getMessage(), e);
        }
    }

    /**
     * 移除线程缓存
     */
    public void removeDelegate() {
        this.threadLocalDelegateDialect.remove();
    }

    /**
     * 设置配置
     * @param properties 配置
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
        this.databaseDialect = Optional.ofNullable(properties.getProperty(Dialect.PROP_KEY_DIALECT))
            .filter(Objects::isNotBlank).orElse(null);
        this.autoRuntimeParsingJdbc =
            Optional.ofNullable(properties.getProperty(Dialect.PROP_KEY_AUTO_RUNTIME_PARSING_JDBC))
                .map(Objects::toBool).orElse(false);
        Optional.ofNullable(properties.getProperty(Dialect.PROP_KEY_AUTO_RELEASE_CONNECT))
            .ifPresent(release -> this.autoReleaseConnect = Objects.toBool(release));
        if (Objects.isNotBlank(this.databaseDialect)
            && !Constants.DEF_UNDEFINED.equalsIgnoreCase(this.databaseDialect)) {
            this.autoRuntimeParsingJdbc = false;
            this.delegate = this.getDialectFromSpecified(this.databaseDialect);
        }
    }

    /**
     * 获取分页方言委托对象
     * @return 分页方言对象
     */
    public abstract AbstractDialect getDelegate();
}
