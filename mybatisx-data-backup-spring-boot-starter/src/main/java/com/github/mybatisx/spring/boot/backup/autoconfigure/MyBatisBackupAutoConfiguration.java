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

import com.github.mybatisx.Objects;
import com.github.mybatisx.backup.additional.AdditionalProcessor;
import com.github.mybatisx.backup.convert.BeanConverter;
import com.github.mybatisx.backup.convert.DefaultBeanConverter;
import com.github.mybatisx.backup.event.handle.BackupEventHandler;
import com.github.mybatisx.backup.event.handle.DefaultBackupEventHandler;
import com.github.mybatisx.backup.event.listenr.BackupEventListener;
import com.github.mybatisx.backup.event.listenr.DefaultBackupEventListener;
import com.github.mybatisx.backup.event.listenr.DefaultBlockingQueueBackupEventListener;
import com.github.mybatisx.backup.event.publisher.BackupEventPublisher;
import com.github.mybatisx.backup.event.publisher.DefaultBackupEventPublisher;
import com.github.mybatisx.backup.message.Broadcast;
import com.github.mybatisx.plugin.annotation.Order;
import com.github.mybatisx.plugin.backup.BackupHandler;
import com.github.mybatisx.plugin.backup.DefaultBackupHandler;
import com.github.mybatisx.plugin.backup.DefaultBackupInterceptor;
import com.github.mybatisx.plugin.backup.process.QueryProcessor;
import com.github.mybatisx.queue.EventQueue;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.function.Function;

/**
 * 数据备份插件注入自动配置
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(MyBatisBackupProperties.class)
@ConditionalOnProperty(prefix = MyBatisBackupAutoConfiguration.CFG_PREFIX, name = "enable",
    havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(name = {"com.github.mybatisx.spring.boot.queue.autoconfigure.MyBatisEventQueueAutoConfiguration"})
public class MyBatisBackupAutoConfiguration {

    public static final String CFG_PREFIX = MyBatisBackupProperties.CFG_PREFIX;
    private final MyBatisBackupProperties configProperties;
    private final ApplicationContext context;
    private final Properties properties;
    private final QueryProcessor queryProcessor;
    private final AdditionalProcessor additionalProcessor;
    private final Broadcast broadcast;

    public MyBatisBackupAutoConfiguration(MyBatisBackupProperties configProperties, ApplicationContext context,
                                          ObjectProvider<QueryProcessor> queryProcessorProvider,
                                          ObjectProvider<AdditionalProcessor> additionalProcessorProvider,
                                          ObjectProvider<Broadcast> broadcastProvider) {
        this.configProperties = configProperties;
        this.context = context;
        this.queryProcessor = queryProcessorProvider.getIfAvailable();
        this.additionalProcessor = additionalProcessorProvider.getIfAvailable();
        this.broadcast = broadcastProvider.getIfAvailable();
        if (Objects.isNull(configProperties.getProperties())) {
            this.properties = new Properties();
        } else {
            this.properties = configProperties.getProperties();
        }
        this.ifPresentOfString(DefaultBackupHandler.PROP_KEY_CACHE_CLASS,
            MyBatisBackupProperties::getCacheClass);
        this.ifPresentOfString(DefaultBackupHandler.PROP_KEY_CACHE_CFG_PREFIX,
            MyBatisBackupProperties::getCacheCfgPrefix);
    }

    @Bean
    @ConditionalOnMissingBean
    public BackupEventPublisher backupEventPublisher() {
        return new DefaultBackupEventPublisher(this.context);
    }

    @Bean
    @ConditionalOnMissingBean
    public BackupHandler backupHandler(final BackupEventPublisher backupEventPublisher) {
        return new DefaultBackupHandler(this.configProperties.isNonConditionFilter(), queryProcessor,
            this.configProperties.getFilterPolicies(), backupEventPublisher);
    }

    @Order(168)
    @Bean
    @ConditionalOnMissingBean
    public DefaultBackupInterceptor backupInterceptor(final BackupHandler backupHandler) {
        final DefaultBackupInterceptor interceptor = new DefaultBackupInterceptor(backupHandler);
        interceptor.setProperties(this.properties);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanConverter beanConverter() {
        return new DefaultBeanConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public BackupEventHandler backupMetadataHandler(final BeanConverter beanConverter) {
        return new DefaultBackupEventHandler(this.context, beanConverter, this.additionalProcessor,
            this.broadcast);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "policy", havingValue = "STANDARD")
    public BackupEventListener backupEventListener(final BackupEventHandler metadataHandler) {
        return new DefaultBackupEventListener(metadataHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "policy", havingValue = "QUEUE", matchIfMissing = true)
    public BackupEventListener queueBackupEventListener(final ObjectProvider<EventQueue> eventQueueProvider) {
        return new DefaultBlockingQueueBackupEventListener(eventQueueProvider.getIfAvailable());
    }

    private void ifPresentOfString(final String property,
                                   final Function<MyBatisBackupProperties, String> action) {
        final String value = this.properties.getProperty(property);
        if (Objects.isBlank(value)) {
            final String newValue = action.apply(this.configProperties);
            if (Objects.isNotBlank(newValue)) {
                this.properties.setProperty(property, newValue);
            }
        }
    }

    public MyBatisBackupProperties getConfigProperties() {
        return configProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
