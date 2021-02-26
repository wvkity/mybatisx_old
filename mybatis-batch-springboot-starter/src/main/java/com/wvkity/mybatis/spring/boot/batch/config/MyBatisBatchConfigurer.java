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

import com.wvkity.mybatis.core.plugin.batch.AbstractBatchHandler;
import com.wvkity.mybatis.spring.boot.batch.MyBatisBatchAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 批量操作配置
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore(MyBatisBatchAutoConfiguration.class)
@EnableConfigurationProperties(MyBatisBatchProperties.class)
public class MyBatisBatchConfigurer {

    private final MyBatisBatchProperties batchProperties;
    private final Properties properties;

    public MyBatisBatchConfigurer(MyBatisBatchProperties batchProperties) {
        this.batchProperties = batchProperties;
        final Properties target = this.batchProperties.getProperties();
        if (target == null) {
            this.properties = new Properties();
            this.batchProperties.setProperties(this.properties);
        } else {
            this.properties = target;
        }
        if (this.batchProperties.getBatchSize() > 0
            && !StringUtils.hasText(this.properties.getProperty(AbstractBatchHandler.PROP_KEY_BATCH_SIZE))) {
            this.properties.setProperty(AbstractBatchHandler.PROP_KEY_BATCH_SIZE,
                String.valueOf(this.batchProperties.getBatchSize()));
        }
        final List<String> methods = this.batchProperties.getFilterMethods();
        if (!StringUtils.hasText(this.properties.getProperty(AbstractBatchHandler.PROP_KEY_BATCH_FILTER_METHODS)) &&
            methods != null && !methods.isEmpty()) {
            final String methodStr = methods.stream().filter(StringUtils::hasText).collect(Collectors.joining(","));
            if (StringUtils.hasText(methodStr)) {
                this.properties.setProperty(AbstractBatchHandler.PROP_KEY_BATCH_FILTER_METHODS, methodStr);
            }
        }
    }

    public MyBatisBatchProperties getBatchProperties() {
        return batchProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
