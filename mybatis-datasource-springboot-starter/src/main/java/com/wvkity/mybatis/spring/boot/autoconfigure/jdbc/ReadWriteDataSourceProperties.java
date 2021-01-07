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
package com.wvkity.mybatis.spring.boot.autoconfigure.jdbc;

import com.wvkity.mybatis.spring.jdbc.datasource.strategy.BalanceDataSourceStrategy;
import com.wvkity.mybatis.spring.jdbc.datasource.strategy.DataSourceStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 读写数据源配置
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class ReadWriteDataSourceProperties {

    /**
     * 全局数据源策略
     */
    private Class<? extends DataSourceStrategy> strategy = BalanceDataSourceStrategy.class;
    /**
     * 主数据源
     */
    @NestedConfigurationProperty
    private MasterDataSourceProperties master;
    /**
     * 从数据源
     */
    @NestedConfigurationProperty
    private SlaveDataSourceProperties slave;

    /**
     * 主库数据源配置
     * @author wvkity
     * @created 2020-12-02
     * @since 1.0.0
     */
    public static class MasterDataSourceProperties {
        /**
         * 主数据源List集合
         */
        private List<DataSourceProperties> list;
        /**
         * 主数据源Map集合
         */
        private Map<String, DataSourceProperties> map;
        /**
         * 数据源类型
         */
        private Class<? extends DataSource> type;

        public MasterDataSourceProperties() {
        }

        public List<DataSourceProperties> getList() {
            return list;
        }

        public void setList(List<DataSourceProperties> list) {
            this.list = list;
        }

        public Map<String, DataSourceProperties> getMap() {
            return map;
        }

        public void setMap(Map<String, DataSourceProperties> map) {
            this.map = map;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }
    }

    /**
     * 从库数据源配置
     * @author wvkity
     * @created 2020-12-02
     * @since 1.0.0
     */
    public static class SlaveDataSourceProperties {

        /**
         * 从数据源List集合
         */
        private List<DataSourceProperties> list;
        /**
         * 从数据源Map集合
         */
        private Map<String, DataSourceProperties> map;
        /**
         * 数据源类型
         */
        private Class<? extends DataSource> type;

        public SlaveDataSourceProperties() {
        }

        public List<DataSourceProperties> getList() {
            return list;
        }

        public void setList(List<DataSourceProperties> list) {
            this.list = list;
        }

        public Map<String, DataSourceProperties> getMap() {
            return map;
        }

        public void setMap(Map<String, DataSourceProperties> map) {
            this.map = map;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }
    }

    /**
     * 数据源配置
     * @author wvkity
     * @created 2020-12-02
     * @since 1.0.0
     */
    public static class DataSourceProperties {

        /**
         * 数据库驱动类名
         */
        private String driverClassName;

        /**
         * 数据库JDBC-URL地址
         */
        private String url;

        /**
         * 数据库登录用户名
         */
        private String username;

        /**
         * 数据库登录密码
         */
        private String password;

        /**
         * JNDI名称
         */
        private String jndiName;

        /**
         * 数据源类型
         */
        private Class<? extends DataSource> type;

        public DataSourceProperties() {
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getJndiName() {
            return jndiName;
        }

        public void setJndiName(String jndiName) {
            this.jndiName = jndiName;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }
    }

    public Class<? extends DataSourceStrategy> getStrategy() {
        return strategy;
    }

    public void setStrategy(Class<? extends DataSourceStrategy> strategy) {
        this.strategy = strategy;
    }

    public MasterDataSourceProperties getMaster() {
        return master;
    }

    public void setMaster(MasterDataSourceProperties master) {
        this.master = master;
    }

    public SlaveDataSourceProperties getSlave() {
        return slave;
    }

    public void setSlave(SlaveDataSourceProperties slave) {
        this.slave = slave;
    }
}
