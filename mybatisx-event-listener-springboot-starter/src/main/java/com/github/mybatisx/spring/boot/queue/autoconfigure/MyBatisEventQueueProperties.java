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

import com.github.mybatisx.event.EventPolicy;
import com.github.mybatisx.thread.ThreadPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author wvkity
 * @created 2021-07-26
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = MyBatisEventQueueProperties.CFG_PREFIX)
public class MyBatisEventQueueProperties {

    public static final String CFG_PREFIX = "github.mybatisx.event";
    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 策略
     */
    private EventPolicy policy = EventPolicy.QUEUE;
    /**
     * 队列容量
     */
    private int capacity;
    /**
     * 线程池配置
     */
    @NestedConfigurationProperty
    private ThreadPoolConfig pool;

    public MyBatisEventQueueProperties() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public EventPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(EventPolicy policy) {
        this.policy = policy;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ThreadPoolConfig getPool() {
        return pool;
    }

    public void setPool(ThreadPoolConfig pool) {
        this.pool = pool;
    }
}
