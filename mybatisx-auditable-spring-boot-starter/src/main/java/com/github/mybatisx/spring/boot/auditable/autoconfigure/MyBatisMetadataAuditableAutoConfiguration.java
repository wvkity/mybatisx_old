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

import com.github.mybatisx.Objects;
import com.github.mybatisx.auditable.event.handle.AuditedEventDataHandler;
import com.github.mybatisx.auditable.event.handle.DefaultAuditedEventDataHandler;
import com.github.mybatisx.auditable.event.listener.AuditedEventListener;
import com.github.mybatisx.auditable.event.listener.DefaultAuditedEventListener;
import com.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import com.github.mybatisx.auditable.event.publisher.DefaultAuditedEventPublisher;
import com.github.mybatisx.plugin.auditable.AbstractMetadataAuditedHandler;
import com.github.mybatisx.plugin.auditable.DefaultMetadataAuditedHandler;
import com.github.mybatisx.plugin.auditable.DefaultMetadataAuditedInterceptor;
import com.github.mybatisx.plugin.auditable.MetadataAuditedHandler;
import com.github.mybatisx.plugin.auditable.support.AuditedPropertyLoader;
import com.github.mybatisx.plugin.auditable.support.AuditorAware;
import com.github.mybatisx.plugin.auditable.support.DefaultMetadataAuditable;
import com.github.mybatisx.plugin.auditable.support.MetadataAuditable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Properties;
import java.util.function.Function;

/**
 * 元数据审计配置
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(MyBatisMetadataAuditableProperties.class)
@ConditionalOnProperty(prefix = MyBatisMetadataAuditableAutoConfiguration.CFG_PREFIX, name = "enable",
    havingValue = "true", matchIfMissing = true)
public class MyBatisMetadataAuditableAutoConfiguration {

    public static final String CFG_PREFIX = MyBatisMetadataAuditableProperties.CFG_PREFIX;
    private final MyBatisMetadataAuditableProperties configProperties;
    private final ApplicationContext context;
    private final AuditorAware auditorAware;
    private final Properties properties;

    public MyBatisMetadataAuditableAutoConfiguration(MyBatisMetadataAuditableProperties configProperties,
                                                     ApplicationContext context,
                                                     ObjectProvider<AuditorAware> auditorAwareProvider) {
        this.configProperties = configProperties;
        this.context = context;
        this.auditorAware = auditorAwareProvider.getIfAvailable();
        if (Objects.isNull(configProperties.getProperties())) {
            this.properties = new Properties();
        } else {
            this.properties = configProperties.getProperties();
        }
        this.ifPresentOfString(DefaultMetadataAuditedHandler.PROP_KEY_MA_CACHE_CLASS,
            MyBatisMetadataAuditableProperties::getCacheClass);
        this.ifPresentOfString(DefaultMetadataAuditedHandler.PROP_KEY_MA_CACHE_CFG_PREFIX,
            MyBatisMetadataAuditableProperties::getCacheCfgPrefix);
        this.ifPresentOfString(AbstractMetadataAuditedHandler.PROP_KEY_AUDITED_INTERCEPT_METHODS,
            MyBatisMetadataAuditableProperties::getInterceptMethods);
        this.ifPresentOfString(AbstractMetadataAuditedHandler.PROP_KEY_AUDITED_IGNORE_METHODS,
            MyBatisMetadataAuditableProperties::getIgnoreMethods);
        this.ifPresentOfString(AbstractMetadataAuditedHandler.PROP_KEY_AUDITED_LOGIC_DELETE_METHODS,
            MyBatisMetadataAuditableProperties::getLogicDeleteMethods);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "rollback-enable", havingValue = "true")
    public AuditedEventPublisher auditedEventPublisher() {
        return new DefaultAuditedEventPublisher(this.context);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "rollback-enable", havingValue = "true")
    public AuditedEventDataHandler auditedEventDataHandler() {
        return new DefaultAuditedEventDataHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "rollback-enable", havingValue = "true")
    public AuditedEventListener auditedEventListener(final ObjectProvider<AuditedEventDataHandler> auditedEventDataHandlerProvider) {
        return new DefaultAuditedEventListener(auditedEventDataHandlerProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataAuditable metadataAuditable() {
        return new DefaultMetadataAuditable(this.auditorAware);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataAuditedHandler metadataAuditedHandler(@Lazy MetadataAuditable metadataAuditable,
                                                         @Lazy AuditedPropertyLoader propertyLoader,
                                                         final ObjectProvider<AuditedEventPublisher> auditedEventPublisherProvider) {
        return new DefaultMetadataAuditedHandler(this.configProperties.isRollbackEnable(),
            this.configProperties.isAnnotationEnable(), propertyLoader, metadataAuditable,
            auditedEventPublisherProvider.getIfAvailable());

    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultMetadataAuditedInterceptor metadataAuditedInterceptor(@Lazy MetadataAuditedHandler metadataAuditedHandler) {
        final DefaultMetadataAuditedInterceptor interceptor =
            new DefaultMetadataAuditedInterceptor(metadataAuditedHandler);
        interceptor.setProperties(this.properties);
        return interceptor;
    }

    private void ifPresentOfString(final String property,
                                   final Function<MyBatisMetadataAuditableProperties, String> action) {
        final String value = this.properties.getProperty(property);
        if (Objects.isBlank(value)) {
            final String newValue = action.apply(this.configProperties);
            if (Objects.isNotBlank(newValue)) {
                this.properties.setProperty(property, newValue);
            }
        }
    }

    public MyBatisMetadataAuditableProperties getConfigProperties() {
        return configProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
