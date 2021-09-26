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
package io.github.mybatisx.spring.boot.auditable.autoconfigure;

import io.github.mybatisx.Objects;
import io.github.mybatisx.auditable.event.handle.AuditedEventHandler;
import io.github.mybatisx.auditable.event.handle.DefaultAuditedEventHandler;
import io.github.mybatisx.auditable.event.listener.AuditedEventListener;
import io.github.mybatisx.auditable.event.listener.DefaultAuditedEventListener;
import io.github.mybatisx.auditable.event.listener.DefaultBlockingQueueAuditedEventListener;
import io.github.mybatisx.auditable.event.publisher.AuditedEventPublisher;
import io.github.mybatisx.auditable.event.publisher.DefaultAuditedEventPublisher;
import io.github.mybatisx.plugin.auditable.DefaultSysBuiltAuditedHandler;
import io.github.mybatisx.plugin.auditable.DefaultSysBuiltAuditedInterceptor;
import io.github.mybatisx.plugin.auditable.SysBuiltAuditedHandler;
import io.github.mybatisx.queue.EventQueue;
import io.github.mybatisx.spring.boot.autoconfigure.MyBatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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

/**
 * 内置审计自动注入配置
 * @author wvkity
 * @created 2021-07-21
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({MyBatisAuditedProperties.class, MyBatisSysBuiltAuditedProperties.class})
@AutoConfigureAfter({MyBatisAutoConfiguration.class})
@ConditionalOnProperty(prefix = MyBatisAuditedProperties.CFG_PREFIX, name = "enable",
    havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(name = {
        "io.github.mybatisx.spring.boot.auditable.autoconfigure.MyBatisMetadataAuditableAutoConfiguration",
    "io.github.mybatisx.spring.boot.queue.autoconfigure.MyBatisEventQueueAutoConfiguration"
})
public class MyBatisSysBuiltAuditableAutoConfiguration {

    public static final String CFG_PREFIX = MyBatisSysBuiltAuditedProperties.CFG_PREFIX;
    private final MyBatisAuditedProperties configProperties;
    private final MyBatisSysBuiltAuditedProperties sysAuditedProperties;
    private final ApplicationContext context;
    private final Properties properties;

    public MyBatisSysBuiltAuditableAutoConfiguration(MyBatisAuditedProperties configProperties,
                                                     MyBatisSysBuiltAuditedProperties sysAuditedProperties,
                                                     ApplicationContext context) {
        this.configProperties = configProperties;
        this.sysAuditedProperties = sysAuditedProperties;
        this.context = context;
        if (Objects.nonNull(this.configProperties.getProperties())) {
            this.properties = this.configProperties.getProperties();
        } else {
            this.properties = new Properties();
        }
        final Properties ops;
        if (Objects.nonNull((ops = this.sysAuditedProperties.getProperties()))) {
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
    public SysBuiltAuditedHandler sysBuiltAuditedHandler(ObjectProvider<AuditedEventPublisher> auditedEventPublisherProvider) {
        return new DefaultSysBuiltAuditedHandler(this.configProperties.isRollbackRestore(),
            auditedEventPublisherProvider.getIfAvailable());
    }

    @Order(218)
    @Bean
    @ConditionalOnMissingBean
    public DefaultSysBuiltAuditedInterceptor sysBuiltAuditedInterceptor(@Lazy SysBuiltAuditedHandler sysBuiltAuditedHandler) {
        final DefaultSysBuiltAuditedInterceptor interceptor =
            new DefaultSysBuiltAuditedInterceptor(sysBuiltAuditedHandler);
        interceptor.setProperties(this.properties);
        return interceptor;
    }

    public MyBatisAuditedProperties getConfigProperties() {
        return configProperties;
    }

    public MyBatisSysBuiltAuditedProperties getSysAuditedProperties() {
        return sysAuditedProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
