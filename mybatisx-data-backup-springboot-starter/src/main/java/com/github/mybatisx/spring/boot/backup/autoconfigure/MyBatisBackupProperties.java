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
package com.github.mybatisx.spring.boot.backup.autoconfigure;

import com.github.mybatisx.backup.BackupPolicy;
import com.github.mybatisx.event.EventPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;
import java.util.Set;

/**
 * 数据备份配置项
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = MyBatisBackupProperties.CFG_PREFIX)
public class MyBatisBackupProperties {

    public static final String CFG_PREFIX = "github.mybatisx.plugin.backup";
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 监听处理策略
     */
    private EventPolicy policy = EventPolicy.QUEUE;
    /**
     * 是否开启注解方式拦截
     */
    private boolean annotationEnable = true;
    /**
     * 无条件是否拦截备份
     */
    private boolean nonConditionFilter;
    /**
     * 审计属性缓存类([空值,false,caffeine,实现LocalCache接口类])
     */
    private String cacheClass;
    /**
     * 审计属性缓存配置项前缀
     */
    private String cacheCfgPrefix;
    /**
     * 拦截策略(默认拦截所有)
     */
    private Set<BackupPolicy> filterPolicies;
    /**
     * 其他配置项
     */
    private Properties properties;

    public MyBatisBackupProperties() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public EventPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(EventPolicy policy) {
        this.policy = policy;
    }

    public boolean isAnnotationEnable() {
        return annotationEnable;
    }

    public void setAnnotationEnable(boolean annotationEnable) {
        this.annotationEnable = annotationEnable;
    }

    public boolean isNonConditionFilter() {
        return nonConditionFilter;
    }

    public void setNonConditionFilter(boolean nonConditionFilter) {
        this.nonConditionFilter = nonConditionFilter;
    }

    public String getCacheClass() {
        return cacheClass;
    }

    public void setCacheClass(String cacheClass) {
        this.cacheClass = cacheClass;
    }

    public String getCacheCfgPrefix() {
        return cacheCfgPrefix;
    }

    public void setCacheCfgPrefix(String cacheCfgPrefix) {
        this.cacheCfgPrefix = cacheCfgPrefix;
    }

    public Set<BackupPolicy> getFilterPolicies() {
        return filterPolicies;
    }

    public void setFilterPolicies(Set<BackupPolicy> filterPolicies) {
        this.filterPolicies = filterPolicies;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
