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
package com.wvkity.mybatis.spring.boot.sequence.config;

import com.wvkity.mybatis.core.sequence.snowflake.Category;
import com.wvkity.mybatis.core.sequence.snowflake.Strategy;
import com.wvkity.mybatis.core.sequence.snowflake.Worker;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = SnowflakeSequenceProperties.PREFIX)
public class SnowflakeSequenceProperties {

    public static final String PREFIX = "wvkity.mybatis.sequence";
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 时间戳位数
     */
    private int timestampBits = 41;
    /**
     * 机器码位数
     */
    private int workerBits = 5;
    /**
     * 数据中心位数
     */
    private int dataCenterBits = 5;
    /**
     * 序号位数
     */
    private int sequenceBits = 12;
    /**
     * 初始时间
     */
    private long epochTimestamp = 0L;
    /**
     * 数据中心标识
     */
    private long dataCenterId = 0L;
    /**
     * 机器标识
     */
    private long workerId = 0L;
    /**
     * 工作模式
     */
    private Worker worker = Worker.SPECIFIED;
    /**
     * 类别
     */
    private Category category = Category.MILLISECONDS;
    /**
     * 策略
     */
    private Strategy strategy = Strategy.DEFAULT;
    /**
     * 缓存大小(针对: CacheableSnowflakeSequence)
     */
    private int cacheSize;
    /**
     * 使用默认配置
     */
    private boolean useDefaultConfig;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getTimestampBits() {
        return timestampBits;
    }

    public void setTimestampBits(int timestampBits) {
        this.timestampBits = timestampBits;
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(int workerBits) {
        this.workerBits = workerBits;
    }

    public int getDataCenterBits() {
        return dataCenterBits;
    }

    public void setDataCenterBits(int dataCenterBits) {
        this.dataCenterBits = dataCenterBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public void setSequenceBits(int sequenceBits) {
        this.sequenceBits = sequenceBits;
    }

    public long getEpochTimestamp() {
        return epochTimestamp;
    }

    public void setEpochTimestamp(long epochTimestamp) {
        this.epochTimestamp = epochTimestamp;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public boolean isUseDefaultConfig() {
        return useDefaultConfig;
    }

    public void setUseDefaultConfig(boolean useDefaultConfig) {
        this.useDefaultConfig = useDefaultConfig;
    }
}
