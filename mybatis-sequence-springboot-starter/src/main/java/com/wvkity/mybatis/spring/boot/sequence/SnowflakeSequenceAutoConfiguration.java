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
package com.wvkity.mybatis.spring.boot.sequence;

import com.wvkity.sequence.snowflake.Category;
import com.wvkity.sequence.snowflake.SnowflakeConfig;
import com.wvkity.sequence.snowflake.Strategy;
import com.wvkity.sequence.snowflake.Worker;
import com.wvkity.sequence.snowflake.core.AtomicStampedSnowflakeSequence;
import com.wvkity.sequence.snowflake.core.CacheableSnowflakeSequence;
import com.wvkity.sequence.snowflake.core.DefaultSnowflakeSequence;
import com.wvkity.sequence.snowflake.core.SnowflakeSequence;
import com.wvkity.sequence.snowflake.distributor.DefaultMilliMacDistributor;
import com.wvkity.sequence.snowflake.distributor.DefaultSecondMacDistributor;
import com.wvkity.sequence.snowflake.distributor.DefaultMilliDistributor;
import com.wvkity.sequence.snowflake.distributor.DefaultSecondDistributor;
import com.wvkity.sequence.snowflake.distributor.Distributor;
import com.wvkity.sequence.snowflake.distributor.MacDistributor;
import com.wvkity.sequence.snowflake.distributor.SpecifiedDistributor;
import com.wvkity.mybatis.spring.boot.sequence.config.SnowflakeSequenceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法ID生成器自动注册配置
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(SnowflakeSequence.class)
@EnableConfigurationProperties(SnowflakeSequenceProperties.class)
@ConditionalOnProperty(prefix = "wvkity.mybatis.sequence", name = "enable", havingValue = "true", matchIfMissing = true)
public class SnowflakeSequenceAutoConfiguration {

    private final SnowflakeSequenceProperties properties;

    public SnowflakeSequenceAutoConfiguration(SnowflakeSequenceProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Distributor distributor() {
        final Worker worker = this.properties.getWorker();
        if (this.properties.isUseDefaultConfig()) {
            // 使用系统默认配置
            final Category category = this.properties.getCategory();
            if (worker == Worker.MAC) {
                return category == Category.SECONDS ?
                    new DefaultSecondMacDistributor() : new DefaultMilliMacDistributor();
            } else {
                return category == Category.SECONDS ?
                    new DefaultSecondDistributor(this.properties.getWorkerId(), this.properties.getDataCenterId()) :
                    new DefaultMilliDistributor(this.properties.getWorkerId(), this.properties.getDataCenterId());
            }
        } else {
            if (worker == Worker.MAC) {
                return new MacDistributor(this.properties.getTimestampBits(), this.properties.getWorkerBits(),
                    this.properties.getDataCenterBits(), this.properties.getSequenceBits());
            } else {
                return new SpecifiedDistributor(this.properties.getTimestampBits(), this.properties.getWorkerBits(),
                    this.properties.getDataCenterBits(), this.properties.getSequenceBits(),
                    this.properties.getWorkerId(), this.properties.getDataCenterId());
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeConfig snowflakeConfig(final Distributor distributor) {
        // 自定义配置
        return new SnowflakeConfig(this.properties.getEpochTimestamp(), this.properties.getCacheSize(),
            this.properties.getCategory(), this.properties.getStrategy(), distributor);
    }

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeSequence snowflakeSequence(SnowflakeConfig snowflakeConfig) {
        final Strategy strategy = snowflakeConfig.getStrategy();
        if (strategy == Strategy.ATOMIC) {
            return new AtomicStampedSnowflakeSequence(snowflakeConfig);
        } else if (strategy == Strategy.CACHEABLE) {
            return new CacheableSnowflakeSequence(snowflakeConfig);
        }
        return new DefaultSnowflakeSequence(snowflakeConfig);
    }

    public SnowflakeSequenceProperties getProperties() {
        return properties;
    }
}
