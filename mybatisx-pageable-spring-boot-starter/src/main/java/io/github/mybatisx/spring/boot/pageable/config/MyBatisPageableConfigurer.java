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
package io.github.mybatisx.spring.boot.pageable.config;

import io.github.mybatisx.plugin.paging.dialect.Dialect;
import io.github.mybatisx.spring.boot.pageable.MyBatisPageableAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

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
        if (!StringUtils.hasText(this.properties.getProperty(Dialect.PROP_KEY_DIALECT))
            && this.pageableProperties.getDialect() != null) {
            this.properties.setProperty(Dialect.PROP_KEY_DIALECT, this.pageableProperties.getDialect().name());
        }
        this.ifPresentOfString(Dialect.PROP_KEY_RANGE_DIALECT_PROXY,
            MyBatisPageableProperties::getRangePageableDialectClass);
        this.ifPresentOfString(Dialect.PROP_KEY_STANDARD_DIALECT_PROXY,
            MyBatisPageableProperties::getStandardPageableDialectClass);
        this.ifPresentOfBoolean(Dialect.PROP_KEY_AUTO_RUNTIME_PARSING_JDBC,
            MyBatisPageableProperties::isAutoRuntimeParsingJdbc);
        this.ifPresentOfBoolean(Dialect.PROP_KEY_AUTO_RELEASE_CONNECT,
            MyBatisPageableProperties::isAutoReleaseConnect);
        this.ifPresentOfString(Dialect.PROP_KEY_RECORD_MS_CACHE_CLASS,
            MyBatisPageableProperties::getRecordMsCacheClass);
        this.ifPresentOfString(Dialect.PROP_KEY_RECORD_MS_CFG_PREFIX,
            MyBatisPageableProperties::getRecordMsCacheCfgPrefix);
        this.ifPresentOfString(Dialect.PROP_KEY_WITH_NO_LOCK_CACHE_CLASS,
            MyBatisPageableProperties::getWithNoLockCacheClass);
        this.ifPresentOfString(Dialect.PROP_KEY_WITH_NO_LOCK_REPLACER_CLASS,
            MyBatisPageableProperties::getWithNoLockReplacerClass);
        this.ifPresentOfString(Dialect.PROP_KEY_WITH_NO_LOCK_RECORD_CFG_PREFIX,
            MyBatisPageableProperties::getWithNoLockRecordCacheCfgPrefix);
        this.ifPresentOfString(Dialect.PROP_KEY_WITH_NO_LOCK_PAGEABLE_CFG_PREFIX,
            MyBatisPageableProperties::getWithNoLockPageableCacheCfgPrefix);
        if (this.pageableProperties.getProperties() == null) {
            this.pageableProperties.setProperties(this.properties);
        }
    }

    private void ifPresentOfString(final String property, final Function<MyBatisPageableProperties, String> action) {
        final String value = this.properties.getProperty(property);
        if (!StringUtils.hasText(value)) {
            final String newValue = action.apply(this.pageableProperties);
            if (StringUtils.hasText(newValue)) {
                this.properties.setProperty(property, newValue);
            }
        }
    }

    private void ifPresentOfBoolean(final String property, final Function<MyBatisPageableProperties, Boolean> action) {
        final String value = this.properties.getProperty(property);
        if (!StringUtils.hasText(value)) {
            this.properties.setProperty(property, action.apply(pageableProperties).toString());
        }
    }

    public MyBatisPageableProperties getPageableProperties() {
        return pageableProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
