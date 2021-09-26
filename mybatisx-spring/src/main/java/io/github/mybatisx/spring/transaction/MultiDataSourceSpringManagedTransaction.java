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
package io.github.mybatisx.spring.transaction;

import io.github.mybatisx.Objects;
import io.github.mybatisx.jdbc.datasource.LocalDataSource;
import io.github.mybatisx.jdbc.datasource.MultiDataSourceContextHolder;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多数据源事务
 * @author wvkity
 * @created 2021-08-17
 * @since 1.0.0
 */
public class MultiDataSourceSpringManagedTransaction extends SpringManagedTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataSourceSpringManagedTransaction.class);

    private final DataSource dataSource;
    private Connection connection;
    private boolean isConnectionTransactional;
    private boolean autoCommit;
    private final LocalDataSource mainLocalDataSource;
    private final Map<LocalDataSource, Connection> connectionCache;

    public MultiDataSourceSpringManagedTransaction(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
        this.connectionCache = new ConcurrentHashMap<>();
        this.mainLocalDataSource = MultiDataSourceContextHolder.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (MultiDataSourceContextHolder.nonNull()) {
            final LocalDataSource local = MultiDataSourceContextHolder.get();
            if (local.equals(mainLocalDataSource)) {
                if (this.connection == null) {
                    this.openConnection();
                }
                return connection;
            } else {
                if (!this.connectionCache.containsKey(local)) {
                    try {
                        final Connection conn = dataSource.getConnection();
                        this.connectionCache.putIfAbsent(local, conn);
                    } catch (SQLException e) {
                        throw new SQLException("Could not get JDBC Connection", e);
                    }
                }
                return this.connectionCache.get(local);
            }
        } else {
            if (this.connection == null) {
                openConnection();
            }
            return this.connection;
        }
    }

    /**
     * Gets a connection from Spring transaction manager and discovers if this {@code Transaction} should manage
     * connection or let it to Spring.
     * <p>
     * It also reads autocommit setting because when using Spring Transaction MyBatis thinks that autocommit is always
     * false and will always call commit/rollback so we need to no-op that calls.
     */
    private void openConnection() throws SQLException {
        this.connection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.connection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

        LOGGER.debug(() -> "JDBC Connection [" + this.connection + "] will"
            + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws SQLException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            LOGGER.debug(() -> "Committing JDBC Connection [" + this.connection + "]");
            this.connection.commit();
            if (Objects.isNotEmpty(this.connectionCache)) {
                for (Connection it: this.connectionCache.values()) {
                    it.commit();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws SQLException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            LOGGER.debug(() -> "Rolling back JDBC Connection [" + this.connection + "]");
            this.connection.rollback();
            if (Objects.isNotEmpty(this.connectionCache)) {
                for (Connection it: this.connectionCache.values()) {
                    it.rollback();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        DataSourceUtils.releaseConnection(this.connection, this.dataSource);
        if (Objects.isNotEmpty(this.connectionCache)) {
            for (Connection it: this.connectionCache.values()) {
                DataSourceUtils.releaseConnection(it, this.dataSource);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTimeout() {
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        if (holder != null && holder.hasTimeout()) {
            return holder.getTimeToLiveInSeconds();
        }
        return null;
    }
}
