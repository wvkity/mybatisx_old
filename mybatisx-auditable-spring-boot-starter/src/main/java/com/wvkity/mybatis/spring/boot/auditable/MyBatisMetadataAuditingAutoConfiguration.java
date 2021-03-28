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
package com.wvkity.mybatis.spring.boot.auditable;

import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;
import com.wvkity.mybatis.core.auditable.event.AuditedEventPublisher;
import com.wvkity.mybatis.core.plugin.auditable.AuditingHandler;
import com.wvkity.mybatis.core.plugin.auditable.MetadataAuditingHandler;
import com.wvkity.mybatis.core.plugin.auditable.MetadataAuditingInterceptor;
import com.wvkity.mybatis.core.plugin.auditable.support.AuditMatcher;
import com.wvkity.mybatis.core.plugin.auditable.support.AuditorAware;
import com.wvkity.mybatis.core.plugin.auditable.support.DefaultMetadataAuditable;
import com.wvkity.mybatis.core.plugin.auditable.support.MetadataAuditable;
import com.wvkity.mybatis.core.plugin.handler.Handler;
import com.wvkity.mybatis.spring.boot.auditable.config.MyBatisAuditingProperties;
import com.wvkity.mybatis.spring.boot.auditable.handler.AuditedEventHandler;
import com.wvkity.mybatis.spring.boot.auditable.handler.DefaultAuditedEventHandler;
import com.wvkity.mybatis.spring.boot.auditable.listener.AuditedEventListener;
import com.wvkity.mybatis.spring.boot.auditable.listener.DefaultAuditedEventListener;
import com.wvkity.mybatis.spring.boot.auditable.listener.DefaultAuditedEventPublisher;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

/**
 * 审计拦截器自动注入配置
 * @author wvkity
 * @created 2021-03-05
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(MyBatisAuditingProperties.class)
@ConditionalOnProperty(prefix = "wvkity.mybatis.plugin.auditable", name = "enable", havingValue = "true", matchIfMissing = true)
public class MyBatisMetadataAuditingAutoConfiguration {

    private final ApplicationContext context;
    private final MyBatisAuditingProperties auditingProperties;
    private final Properties properties;
    private final AuditorAware auditorAware;

    public MyBatisMetadataAuditingAutoConfiguration(MyBatisAuditingProperties auditingProperties,
                                                    ApplicationContext applicationContext,
                                                    ObjectProvider<AuditorAware> auditorAwareProvider) {
        this.context = applicationContext;
        this.auditingProperties = auditingProperties;
        if (auditingProperties.getProperties() == null) {
            this.properties = new Properties();
            auditingProperties.setProperties(this.properties);
        } else {
            this.properties = auditingProperties.getProperties();
        }
        this.ifPresentOfString(AuditingHandler.PROP_KEY_MA_CACHE_CLASS,
            MyBatisAuditingProperties::getMetadataAuditCacheClass);
        this.ifPresentOfString(AuditingHandler.PROP_KEY_MA_CACHE_CFG_PREFIX,
            MyBatisAuditingProperties::getMetadataAuditCacheCfgPrefix);
        this.auditorAware = auditorAwareProvider.getIfAvailable();
        this.properties.setProperty(AuditingHandler.PROP_KEY_ROLLBACK,
            String.valueOf(auditingProperties.isRollback()));
        final Set<String> interceptMethods = this.auditingProperties.getInterceptMethods();
        if (!StringUtils.hasText(this.properties.getProperty(AuditingHandler.PROP_KEY_AUDITING_INTERCEPT_METHODS))
            && !CollectionUtils.isEmpty(interceptMethods)) {
            this.properties.setProperty(AuditingHandler.PROP_KEY_AUDITING_INTERCEPT_METHODS,
                String.join(",", interceptMethods));
        }
        final Set<String> filterMethods = this.auditingProperties.getFilterMethods();
        if (!StringUtils.hasText(this.properties.getProperty(AuditingHandler.PROP_KEY_AUDITING_FILTER_METHODS))
            && !CollectionUtils.isEmpty(filterMethods)) {
            this.properties.setProperty(AuditingHandler.PROP_KEY_AUDITING_FILTER_METHODS,
                String.join(",", filterMethods));
        }
        final Set<String> logicDeleteMethods = this.auditingProperties.getLogicDeleteMethods();
        if (!StringUtils.hasText(this.properties.getProperty(AuditingHandler.PROP_KEY_AUDITING_LOGIC_DELETE_METHODS))
            && !CollectionUtils.isEmpty(logicDeleteMethods)) {
            this.properties.setProperty(AuditingHandler.PROP_KEY_AUDITING_LOGIC_DELETE_METHODS,
                String.join(",", logicDeleteMethods));
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "wvkity.mybatis.plugin.auditable", name = "rollback", havingValue = "true")
    public AuditedEventPublisher<List<AuditedAlterData>> auditedEventPublisher() {
        return new DefaultAuditedEventPublisher(this.context);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "wvkity.mybatis.plugin.auditable", name = "rollback", havingValue = "true")
    public AuditedEventHandler<List<AuditedAlterData>> auditedEventHandler() {
        return new DefaultAuditedEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "wvkity.mybatis.plugin.auditable", name = "rollback", havingValue = "true")
    public AuditedEventListener<List<AuditedAlterData>> auditedEventListener(ObjectProvider<AuditedEventHandler<List<AuditedAlterData>>> auditedEventHandlerProvider) {
        return new DefaultAuditedEventListener(auditedEventHandlerProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataAuditable metadataAuditable() {
        return new DefaultMetadataAuditable(this.auditorAware);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataAuditingInterceptor metadataAuditingInterceptor(@Lazy AuditMatcher auditMatcher,
                                                                   @Lazy MetadataAuditable metadataAuditable,
                                                                   ObjectProvider<AuditedEventPublisher<List<AuditedAlterData>>> eventPublisherProvider) {
        final Handler handler = new MetadataAuditingHandler(auditMatcher, metadataAuditable,
            eventPublisherProvider.getIfAvailable());
        final MetadataAuditingInterceptor interceptor = new MetadataAuditingInterceptor(handler);
        interceptor.setProperties(this.properties);
        return interceptor;
    }

    private void ifPresentOfString(final String property, final Function<MyBatisAuditingProperties, String> action) {
        final String value = this.properties.getProperty(property);
        if (!StringUtils.hasText(value)) {
            final String newValue = action.apply(this.auditingProperties);
            if (StringUtils.hasText(newValue)) {
                this.properties.setProperty(property, newValue);
            }
        }
    }

    private void ifPresentOfBoolean(final String property, final Function<MyBatisAuditingProperties, Boolean> action) {
        final String value = this.properties.getProperty(property);
        if (!StringUtils.hasText(value)) {
            this.properties.setProperty(property, action.apply(auditingProperties).toString());
        }
    }

    public MyBatisAuditingProperties getAuditingProperties() {
        return auditingProperties;
    }

    public Properties getProperties() {
        return properties;
    }
}
