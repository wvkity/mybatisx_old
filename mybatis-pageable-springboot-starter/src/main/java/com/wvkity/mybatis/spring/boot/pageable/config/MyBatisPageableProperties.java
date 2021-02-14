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
package com.wvkity.mybatis.spring.boot.pageable.config;

import com.wvkity.mybatis.core.plugin.paging.DatabaseDialect;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * MyBatis分页配置
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "wvkity.mybatis.plugin.pageable", ignoreInvalidFields = true)
public class MyBatisPageableProperties {

    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 数据库方言
     */
    private DatabaseDialect dialect = DatabaseDialect.UNDEFINED;
    /**
     * 动态从JDBC获取分页方言
     */
    private boolean autoRuntimeParsingJdbc;
    /**
     * 多数据源动态解析JDBC后是否释放连接
     */
    private boolean autoReleaseConnect = true;
    /**
     * 范围分页方言
     */
    private String rangePageableProxyClass;
    /**
     * 分页方言
     */
    private String standardPageableProxyClass;
    /**
     * 其他配置属性
     */
    private Properties properties;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public DatabaseDialect getDialect() {
        return dialect;
    }

    public void setDialect(DatabaseDialect dialect) {
        this.dialect = dialect;
    }

    public boolean isAutoRuntimeParsingJdbc() {
        return autoRuntimeParsingJdbc;
    }

    public void setAutoRuntimeParsingJdbc(boolean autoRuntimeParsingJdbc) {
        this.autoRuntimeParsingJdbc = autoRuntimeParsingJdbc;
    }

    public boolean isAutoReleaseConnect() {
        return autoReleaseConnect;
    }

    public void setAutoReleaseConnect(boolean autoReleaseConnect) {
        this.autoReleaseConnect = autoReleaseConnect;
    }

    public String getRangePageableProxyClass() {
        return rangePageableProxyClass;
    }

    public void setRangePageableProxyClass(String rangePageableProxyClass) {
        this.rangePageableProxyClass = rangePageableProxyClass;
    }

    public String getStandardPageableProxyClass() {
        return standardPageableProxyClass;
    }

    public void setStandardPageableProxyClass(String standardPageableProxyClass) {
        this.standardPageableProxyClass = standardPageableProxyClass;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
