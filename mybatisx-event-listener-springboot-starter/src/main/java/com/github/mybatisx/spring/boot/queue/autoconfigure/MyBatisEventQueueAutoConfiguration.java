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
package com.github.mybatisx.spring.boot.queue.autoconfigure;

import com.github.mybatisx.Objects;
import com.github.mybatisx.event.handle.Handler;
import com.github.mybatisx.processor.DefaultEventQueueProcessor;
import com.github.mybatisx.processor.EventQueueProcessor;
import com.github.mybatisx.queue.DefaultBlockingEventQueue;
import com.github.mybatisx.queue.EventQueue;
import com.github.mybatisx.thread.QueueThreadExecutor;
import com.github.mybatisx.thread.ThreadPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wvkity
 * @created 2021-07-26
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({MyBatisEventQueueProperties.class})
@ConditionalOnProperty(
    prefix = MyBatisEventQueueAutoConfiguration.CFG_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true
)
public class MyBatisEventQueueAutoConfiguration {

    public static final String CFG_PREFIX = MyBatisEventQueueProperties.CFG_PREFIX;
    private final MyBatisEventQueueProperties properties;
    private final ThreadPoolConfig poolConfig;
    private final List<Handler> eventHandlers;

    public MyBatisEventQueueAutoConfiguration(MyBatisEventQueueProperties properties,
                                              ObjectProvider<List<Handler>> eventHandlerProvider) {
        this.properties = properties;
        this.eventHandlers = eventHandlerProvider.getIfAvailable();
        if (Objects.isNull(this.properties.getPool())) {
            this.poolConfig = new ThreadPoolConfig();
        } else {
            this.poolConfig = this.properties.getPool();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "policy", havingValue = "QUEUE", matchIfMissing = true)
    public ThreadPoolExecutor threadPoolExecutor() {
        final ThreadPoolConfig cfg = this.poolConfig;
        final String threadNamePrefix =
            Objects.isBlank(cfg.getThreadNamePrefix()) ? "mybatisx-event-thread-pool" : cfg.getThreadNamePrefix();
        final ThreadFactory factory = new CustomizableThreadFactory(threadNamePrefix);
        return new ThreadPoolExecutor(cfg.getCorePoolSize(), cfg.getMaximumPoolSize(), cfg.getKeepAliveTime(),
            cfg.getTimeUnit(), new LinkedBlockingQueue<>(cfg.getCapacity()), factory,
            new ThreadPoolExecutor.AbortPolicy());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "policy", havingValue = "QUEUE", matchIfMissing = true)
    public QueueThreadExecutor queueThreadExecutor(ThreadPoolExecutor threadPoolExecutor) {
        return new QueueThreadExecutor(threadPoolExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "policy", havingValue = "QUEUE", matchIfMissing = true)
    public EventQueue eventQueue() {
        int capacity = this.properties.getCapacity();
        if (capacity <= 0) {
            capacity = this.poolConfig.getCapacity();
        }
        return new DefaultBlockingEventQueue(capacity);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "policy", havingValue = "QUEUE", matchIfMissing = true)
    public EventQueueProcessor queueProcessor(ObjectProvider<QueueThreadExecutor> queueExecutorProvider,
                                              ObjectProvider<EventQueue> eventQueueProvider) {
        return new DefaultEventQueueProcessor(queueExecutorProvider.getIfAvailable(),
            eventQueueProvider.getIfAvailable(), this.eventHandlers);
    }

    public MyBatisEventQueueProperties getProperties() {
        return properties;
    }
}
