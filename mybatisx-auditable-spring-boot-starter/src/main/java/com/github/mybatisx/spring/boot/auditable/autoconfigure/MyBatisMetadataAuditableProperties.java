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
package com.github.mybatisx.spring.boot.auditable.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * 属性审计配置项
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "github.mybatisx.plugin.auditable")
public class MyBatisMetadataAuditableProperties {

    private boolean enable = true;
    /**
     * 是否开启事务回滚属性值还原
     */
    private boolean rollbackEnable;
    /**
     * 是否开启注解方式拦截
     */
    private boolean annotationEnable = true;
    /**
     * 审计属性缓存类([空值,false,caffeine,实现LocalCache接口类])
     */
    private String cacheClass;
    /**
     * 审计属性缓存配置项前缀
     */
    private String cacheCfgPrefix;
    /**
     * 拦截方法列表(英文逗号隔开)
     */
    protected String interceptMethods;
    /**
     * 忽略方法列表(英文逗号隔开)
     */
    protected String ignoreMethods;
    /**
     * 逻辑删除方法(英文逗号隔开)
     */
    protected String logicDeleteMethods;
    /**
     * 其他配置项
     */
    private Properties properties;

    public MyBatisMetadataAuditableProperties() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isRollbackEnable() {
        return rollbackEnable;
    }

    public void setRollbackEnable(boolean rollbackEnable) {
        this.rollbackEnable = rollbackEnable;
    }

    public boolean isAnnotationEnable() {
        return annotationEnable;
    }

    public void setAnnotationEnable(boolean annotationEnable) {
        this.annotationEnable = annotationEnable;
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

    public String getInterceptMethods() {
        return interceptMethods;
    }

    public void setInterceptMethods(String interceptMethods) {
        this.interceptMethods = interceptMethods;
    }

    public String getIgnoreMethods() {
        return ignoreMethods;
    }

    public void setIgnoreMethods(String ignoreMethods) {
        this.ignoreMethods = ignoreMethods;
    }

    public String getLogicDeleteMethods() {
        return logicDeleteMethods;
    }

    public void setLogicDeleteMethods(String logicDeleteMethods) {
        this.logicDeleteMethods = logicDeleteMethods;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
