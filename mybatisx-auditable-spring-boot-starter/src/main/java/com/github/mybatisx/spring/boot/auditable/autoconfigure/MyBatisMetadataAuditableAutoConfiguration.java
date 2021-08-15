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
import com.github.mybatisx.auditable.event.handle.AuditedEventHandler;
import com.github.mybatisx.auditable.event.handle.DefaultAuditedEventHandler;
import com.github.mybatisx.auditable.event.listener.AuditedEventListener;
import com.github.mybatisx.auditable.event.listener.DefaultAuditedEventListener;
import com.github.mybatisx.auditable.event.listener.DefaultBlockingQueueAuditedEventListener;
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
import com.github.mybatisx.queue.EventQueue;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

import java.util.Properties;
import java.util.function.Function;

/**
 * 元数据审计配置
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({MyBatisAuditedProperties.class, MyBatisMetadataAuditedProperties.class})
@ConditionalOnProperty(prefix = MyBatisAuditedProperties.CFG_PREFIX, name = "enable",
    havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(name = {"com.github.mybatisx.spring.boot.queue.autoconfigure.MyBatisEventQueueAutoConfiguration"})
public class MyBatisMetadataAuditableAutoConfiguration {

    public static final String CFG_PREFIX = MyBatisMetadataAuditedProperties.CFG_PREFIX;
    private final MyBatisAuditedProperties configProperties;
    private final MyBatisMetadataAuditedProperties metadataAuditedProperties;
    private final ApplicationContext context;
    private final AuditorAware auditorAware;
    private final Properties properties;

    public MyBatisMetadataAuditableAutoConfiguration(MyBatisAuditedProperties configProperties,
                                                     MyBatisMetadataAuditedProperties metadataAuditedProperties,
                                                     ApplicationContext context,
                                                     ObjectProvider<AuditorAware> auditorAwareProvider) {
        this.configProperties = configProperties;
        this.metadataAuditedProperties = metadataAuditedProperties;
        this.context = context;
        this.auditorAware = auditorAwareProvider.getIfAvailable();
        if (Objects.isNull(configProperties.getProperties())) {
            this.properties = new Properties();
        } else {
            this.properties = configProperties.getProperties();
        }
        this.ifPresentOfString(DefaultMetadataAuditedHandler.PROP_KEY_CACHE_CLASS,
            MyBatisAuditedProperties::getCacheClass);
        this.ifPresentOfString(DefaultMetadataAuditedHandler.PROP_KEY_CACHE_CFG_PREFIX,
            MyBatisAuditedProperties::getCacheCfgPrefix);
        this.ifPresentOfString(AbstractMetadataAuditedHandler.PROP_KEY_AUDITED_LOGIC_DELETE_METHODS,
            MyBatisAuditedProperties::getLogicDeleteMethods);
        this.ifPresentOfString(AbstractMetadataAuditedHandler.PROP_KEY_AUDITED_INTERCEPT_METHODS,
            this.metadataAuditedProperties.getInterceptMethods());
        this.ifPresentOfString(AbstractMetadataAuditedHandler.PROP_KEY_AUDITED_IGNORE_METHODS,
            this.metadataAuditedProperties.getIgnoreMethods());
        // other properties
        final Properties ops;
        if (Objects.nonNull((ops = this.metadataAuditedProperties.getProperties()))) {
            if (Objects.isNotEmpty(ops)) {
                for (Object key : ops.keySet()) {
                    if (Objects.nonNull(key)) {
                        final String prop = key.toString();
                        this.properties.setProperty(prop, ops.getProperty(prop));
                    }
                }
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = MyBatisAuditedProperties.CFG_PREFIX, name = "rollback-restore", havingValue = "true")
    public AuditedEventPublisher auditedEventPublisher() {
        return new DefaultAuditedEventPublisher(this.context);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = MyBatisAuditedProperties.CFG_PREFIX, name = "rollback-restore", havingValue = "true")
    public AuditedEventHandler auditedEventHandler() {
        return new DefaultAuditedEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({AuditedEventHandler.class})
    @ConditionalOnProperty(prefix = MyBatisAuditedProperties.CFG_PREFIX, name = "policy", havingValue = "STANDARD")
    public AuditedEventListener auditedEventListener(final ObjectProvider<AuditedEventHandler> auditedEventDataHandlerProvider) {
        return new DefaultAuditedEventListener(auditedEventDataHandlerProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({AuditedEventHandler.class})
    @ConditionalOnProperty(prefix = MyBatisAuditedProperties.CFG_PREFIX, name = "policy", havingValue = "QUEUE",
        matchIfMissing = true)
    public AuditedEventListener queueAuditedEventListener(final ObjectProvider<EventQueue> eventQueueProvider) {
        return new DefaultBlockingQueueAuditedEventListener(eventQueueProvider.getIfAvailable());
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
        return new DefaultMetadataAuditedHandler(this.configProperties.isRollbackRestore(),
            propertyLoader, metadataAuditable, auditedEventPublisherProvider.getIfAvailable());

    }

    @Order(198)
    @Bean
    @ConditionalOnMissingBean
    public DefaultMetadataAuditedInterceptor metadataAuditedInterceptor(@Lazy MetadataAuditedHandler metadataAuditedHandler) {
        final DefaultMetadataAuditedInterceptor interceptor =
            new DefaultMetadataAuditedInterceptor(metadataAuditedHandler);
        interceptor.setProperties(this.properties);
        return interceptor;
    }

    private void ifPresentOfString(final String property,
                                   final Function<MyBatisAuditedProperties, String> action) {
        final String value = this.properties.getProperty(property);
        if (Objects.isBlank(value)) {
            this.ifPresentOfString(property, action.apply(this.configProperties));
        }
    }

    private void ifPresentOfString(final String property, final String newValue) {
        if (Objects.isNotBlank(newValue)) {
            this.properties.setProperty(property, newValue);
        }
    }

    public MyBatisAuditedProperties getConfigProperties() {
        return configProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
