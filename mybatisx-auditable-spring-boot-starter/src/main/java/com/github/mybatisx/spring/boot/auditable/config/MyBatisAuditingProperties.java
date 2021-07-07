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
package com.github.mybatisx.spring.boot.auditable.config;

import com.github.mybatisx.cache.LocalCache;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;
import java.util.Set;

/**
 * 属性审计相关配置项
 * @author wvkity
 * @created 2021-03-05
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "wvkity.mybatis.plugin.auditable")
public class MyBatisAuditingProperties {

    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 事务提交失败，审计数据是否回滚
     */
    private boolean rollback;
    /**
     * 审计属性缓存类([空值,false,caffeine,实现LocalCache接口类])
     * @see LocalCache
     */
    private String metadataAuditCacheClass;
    /**
     * 审计属性缓存配置项前缀
     */
    private String metadataAuditCacheCfgPrefix;
    /**
     * 拦截方法
     */
    private Set<String> interceptMethods;
    /**
     * 过滤方法
     */
    private Set<String> filterMethods;
    /**
     * 逻辑删除方法
     */
    private Set<String> logicDeleteMethods;
    /**
     * 配置项
     */
    private Properties properties;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isRollback() {
        return rollback;
    }

    public void setRollback(boolean rollback) {
        this.rollback = rollback;
    }

    public String getMetadataAuditCacheClass() {
        return metadataAuditCacheClass;
    }

    public void setMetadataAuditCacheClass(String metadataAuditCacheClass) {
        this.metadataAuditCacheClass = metadataAuditCacheClass;
    }

    public String getMetadataAuditCacheCfgPrefix() {
        return metadataAuditCacheCfgPrefix;
    }

    public void setMetadataAuditCacheCfgPrefix(String metadataAuditCacheCfgPrefix) {
        this.metadataAuditCacheCfgPrefix = metadataAuditCacheCfgPrefix;
    }

    public Set<String> getInterceptMethods() {
        return interceptMethods;
    }

    public void setInterceptMethods(Set<String> interceptMethods) {
        this.interceptMethods = interceptMethods;
    }

    public Set<String> getFilterMethods() {
        return filterMethods;
    }

    public void setFilterMethods(Set<String> filterMethods) {
        this.filterMethods = filterMethods;
    }

    public Set<String> getLogicDeleteMethods() {
        return logicDeleteMethods;
    }

    public void setLogicDeleteMethods(Set<String> logicDeleteMethods) {
        this.logicDeleteMethods = logicDeleteMethods;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
