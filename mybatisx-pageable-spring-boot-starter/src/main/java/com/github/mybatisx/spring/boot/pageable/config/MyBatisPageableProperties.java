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
package com.github.mybatisx.spring.boot.pageable.config;

import com.github.mybatisx.cache.LocalCache;
import com.github.mybatisx.plugin.paging.DatabaseDialect;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * MyBatis分页配置
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "github.mybatisx.plugin.pageable")
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
     * 查询总记录数缓存类([空值,false,caffeine,实现LocalCache接口类])
     * @see LocalCache
     */
    private String recordMsCacheClass;
    /**
     * 查询总记录数缓存配置项前缀
     */
    private String recordMsCacheCfgPrefix;
    /**
     * 动态从JDBC获取分页方言
     */
    private boolean autoRuntimeParsingJdbc = true;
    /**
     * 多数据源动态解析JDBC后是否释放连接
     */
    private boolean autoReleaseConnect = true;
    /**
     * 范围分页方言
     */
    private String rangePageableDialectClass;
    /**
     * 分页方言
     */
    private String standardPageableDialectClass;
    /**
     * SQLSERVER-WITCH(NOLOCK)替换/还原类
     */
    private String withNoLockReplacerClass;
    /**
     * SQLSERVER防止锁住查询缓存类([空值,false,caffeine,实现LocalCache接口类])
     * @see LocalCache
     */
    private String withNoLockCacheClass;
    /**
     * SQLSERVER查询总记录数SQL缓存配置项前缀
     */
    private String withNoLockRecordCacheCfgPrefix;
    /**
     * SQLSERVER分页查询缓存配置项前缀
     */
    private String withNoLockPageableCacheCfgPrefix;
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

    public String getRecordMsCacheClass() {
        return recordMsCacheClass;
    }

    public void setRecordMsCacheClass(String recordMsCacheClass) {
        this.recordMsCacheClass = recordMsCacheClass;
    }

    public String getRecordMsCacheCfgPrefix() {
        return recordMsCacheCfgPrefix;
    }

    public void setRecordMsCacheCfgPrefix(String recordMsCacheCfgPrefix) {
        this.recordMsCacheCfgPrefix = recordMsCacheCfgPrefix;
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

    public String getRangePageableDialectClass() {
        return rangePageableDialectClass;
    }

    public void setRangePageableDialectClass(String rangePageableDialectClass) {
        this.rangePageableDialectClass = rangePageableDialectClass;
    }

    public String getStandardPageableDialectClass() {
        return standardPageableDialectClass;
    }

    public void setStandardPageableDialectClass(String standardPageableDialectClass) {
        this.standardPageableDialectClass = standardPageableDialectClass;
    }

    public String getWithNoLockReplacerClass() {
        return withNoLockReplacerClass;
    }

    public void setWithNoLockReplacerClass(String withNoLockReplacerClass) {
        this.withNoLockReplacerClass = withNoLockReplacerClass;
    }

    public String getWithNoLockCacheClass() {
        return withNoLockCacheClass;
    }

    public void setWithNoLockCacheClass(String withNoLockCacheClass) {
        this.withNoLockCacheClass = withNoLockCacheClass;
    }

    public String getWithNoLockRecordCacheCfgPrefix() {
        return withNoLockRecordCacheCfgPrefix;
    }

    public void setWithNoLockRecordCacheCfgPrefix(String withNoLockRecordCacheCfgPrefix) {
        this.withNoLockRecordCacheCfgPrefix = withNoLockRecordCacheCfgPrefix;
    }

    public String getWithNoLockPageableCacheCfgPrefix() {
        return withNoLockPageableCacheCfgPrefix;
    }

    public void setWithNoLockPageableCacheCfgPrefix(String withNoLockPageableCacheCfgPrefix) {
        this.withNoLockPageableCacheCfgPrefix = withNoLockPageableCacheCfgPrefix;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
