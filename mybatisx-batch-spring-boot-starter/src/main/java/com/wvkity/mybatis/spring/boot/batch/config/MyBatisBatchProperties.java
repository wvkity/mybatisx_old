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
package com.wvkity.mybatis.spring.boot.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Properties;

/**
 * MyBatis批量操作配置项
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "wvkity.mybatis.plugin.batch")
public class MyBatisBatchProperties {

    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 批量大小
     */
    private int batchSize;
    /**
     * 拦截方法列表
     */
    private List<String> interceptMethods;
    /**
     * 其他配置
     */
    private Properties properties;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public List<String> getInterceptMethods() {
        return interceptMethods;
    }

    public void setInterceptMethods(List<String> interceptMethods) {
        this.interceptMethods = interceptMethods;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
