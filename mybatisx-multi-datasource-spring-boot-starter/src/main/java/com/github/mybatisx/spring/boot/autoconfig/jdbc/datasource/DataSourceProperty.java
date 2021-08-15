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

import com.github.mybatisx.jdbc.datasource.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 数据源配置
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public class DataSourceProperty extends GroupableDataSourceProperty {

    /**
     * 数据源名称
     */
    private String name = "";

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * JDBC地址
     */
    private String url;

    /**
     * JNDI数据源名称
     */
    private String jndiName;

    /**
     * 建表脚本
     */
    protected String schema;

    /**
     * 数据脚本
     */
    protected String data;

    /**
     * 分隔符
     */
    protected String separator = ";";

    public DataSourceBuilder<?> initializeDataSourceBuilder() {
        return DataSourceBuilder.create(getClassLoader()).type(getType()).driverClassName(determineDriverClassName())
            .url(this.url).username(this.username).password(this.password);
    }

    public String determineDriverClassName() {
        if (StringUtils.hasText(this.driverClassName)) {
            Assert.state(driverClassIsLoadable(), () -> "Cannot load driver class: " + this.driverClassName);
            return this.driverClassName;
        }
        String driverClassName = null;
        if (StringUtils.hasText(this.url)) {
            driverClassName = DatabaseDriver.fromJdbcUrl(this.url).getDriverClassName();
        }

        if (!StringUtils.hasText(driverClassName)) {
            throw new IllegalArgumentException("Failed to determine a suitable driver class");
        }
        return driverClassName;
    }

    private boolean driverClassIsLoadable() {
        try {
            ClassUtils.forName(this.driverClassName, null);
            return true;
        } catch (UnsupportedClassVersionError ex) {
            // Driver library has been compiled with a later JDK, propagate error
            throw ex;
        } catch (Throwable ex) {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
