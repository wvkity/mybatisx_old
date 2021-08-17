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

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据源基础配置
 * @author wvkity
 * @created 2021-08-06
 * @since 1.0.0
 */
public class BasicDataSourceProperty {

    /**
     * 类加载器
     */
    protected ClassLoader classLoader;

    /**
     * 数据源类型
     */
    protected Class<? extends DataSource> type;

    /**
     * JDBC驱动
     */
    protected String driverClassName;

    /**
     * 数据库登录名
     */
    protected String username;

    /**
     * 数据库登录密码
     */
    protected String password;

    /**
     * XA数据源配置
     */
    protected Xa xa;

    /**
     * 连接池配置
     */
    protected Map<String, String> poolConfig;

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
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

    public Xa getXa() {
        return xa;
    }

    public void setXa(Xa xa) {
        this.xa = xa;
    }

    public Map<String, String> getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(Map<String, String> poolConfig) {
        this.poolConfig = poolConfig;
    }
}
