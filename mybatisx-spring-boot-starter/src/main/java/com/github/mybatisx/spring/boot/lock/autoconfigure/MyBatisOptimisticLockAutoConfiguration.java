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
package com.github.mybatisx.spring.boot.lock.autoconfigure;

import com.github.mybatisx.Objects;
import com.github.mybatisx.plugin.lock.DefaultOptimisticLockHandler;
import com.github.mybatisx.plugin.lock.DefaultOptimisticLockInterceptor;
import com.github.mybatisx.plugin.lock.OptimisticLockHandler;
import com.github.mybatisx.spring.boot.autoconfigure.MyBatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Properties;

/**
 * 乐观锁自动注入配置
 * @author wvkity
 * @created 2021-07-29
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({MyBatisOptimisticLockProperties.class})
@AutoConfigureAfter({MyBatisAutoConfiguration.class})
@ConditionalOnProperty(prefix = MyBatisOptimisticLockAutoConfiguration.CFG_PREFIX, name = "enable",
    havingValue = "true", matchIfMissing = true)
public class MyBatisOptimisticLockAutoConfiguration {

    public static final String CFG_PREFIX = MyBatisOptimisticLockProperties.CFG_PREFIX;
    private final MyBatisOptimisticLockProperties configProperties;
    private final Properties properties;

    public MyBatisOptimisticLockAutoConfiguration(MyBatisOptimisticLockProperties configProperties) {
        this.configProperties = configProperties;
        if (Objects.nonNull(configProperties.getProperties())) {
            this.properties = configProperties.getProperties();
        } else {
            this.properties = new Properties();
            configProperties.setProperties(this.properties);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public OptimisticLockHandler optimisticLockHandler() {
        return new DefaultOptimisticLockHandler(this.configProperties.isAutoOverrideTarget(),
            this.configProperties.getMethods());
    }

    @Order(218)
    @Bean
    @ConditionalOnMissingBean
    public DefaultOptimisticLockInterceptor optimisticLockInterceptor(ObjectProvider<OptimisticLockHandler> optimisticLockHandlerProvider) {
        final DefaultOptimisticLockInterceptor interceptor =
            new DefaultOptimisticLockInterceptor(optimisticLockHandlerProvider.getIfAvailable());
        interceptor.setProperties(properties);
        return interceptor;
    }

    public MyBatisOptimisticLockProperties getConfigProperties() {
        return configProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
