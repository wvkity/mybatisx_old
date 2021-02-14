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

import com.wvkity.mybatis.core.plugin.paging.AbstractPageableHandler;
import com.wvkity.mybatis.spring.boot.pageable.MyBatisPageableAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Properties;

/**
 * 分页相关配置
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore(MyBatisPageableAutoConfiguration.class)
@EnableConfigurationProperties(MyBatisPageableProperties.class)
public class MyBatisPageableConfigurer {

    private final MyBatisPageableProperties pageableProperties;
    private final Properties properties;

    public MyBatisPageableConfigurer(MyBatisPageableProperties pageableProperties) {
        this.pageableProperties = pageableProperties;
        this.properties = Optional.ofNullable(pageableProperties.getProperties()).orElse(new Properties());
        if (!StringUtils.hasText(this.properties.getProperty(AbstractPageableHandler.PROXY_KEY_RANGE))
            && StringUtils.hasText(pageableProperties.getRangePageableProxyClass())) {
            this.properties.setProperty(AbstractPageableHandler.PROXY_KEY_RANGE,
                pageableProperties.getRangePageableProxyClass());
        }
        if (!StringUtils.hasText(this.properties.getProperty(AbstractPageableHandler.PROXY_KEY_PAGEABLE))
            && StringUtils.hasText(pageableProperties.getStandardPageableProxyClass())) {
            this.properties.setProperty(AbstractPageableHandler.PROXY_KEY_PAGEABLE,
                pageableProperties.getStandardPageableProxyClass());
        }
        if (!StringUtils.hasText(this.properties.getProperty("databaseDialect"))
            && this.pageableProperties.getDialect() != null) {
            this.properties.setProperty("databaseDialect", this.pageableProperties.getDialect().name());
        }
        if (!StringUtils.hasText(this.properties.getProperty("autoRuntimeParsingJdbc"))) {
            this.properties.setProperty("autoRuntimeParsingJdbc",
                String.valueOf(this.pageableProperties.isAutoRuntimeParsingJdbc()));
        }
        if (!StringUtils.hasText(this.properties.getProperty("autoReleaseConnect"))) {
            this.properties.setProperty("autoReleaseConnect",
                String.valueOf(this.pageableProperties.isAutoReleaseConnect()));
        }
        if (this.pageableProperties.getProperties() == null) {
            this.pageableProperties.setProperties(this.properties);
        }
    }

    public MyBatisPageableProperties getPageableProperties() {
        return pageableProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
