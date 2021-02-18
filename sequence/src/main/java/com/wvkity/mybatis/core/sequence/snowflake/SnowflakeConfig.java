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
package com.wvkity.mybatis.core.sequence.snowflake;

import com.wvkity.mybatis.core.sequence.clock.MillisecondsClock;
import com.wvkity.mybatis.core.sequence.snowflake.distributor.Distributor;
import com.wvkity.mybatis.core.sequence.snowflake.distributor.MacDistributor;

import java.util.concurrent.TimeUnit;

/**
 * 雪花算法配置
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
public class SnowflakeConfig {

    /**
     * 默认开始时间(2021-02-18 22:00:00)
     */
    public static final long SECOND_EPOCH_TIMESTAMP = 1613656800L;
    /**
     * 默认开始时间(2021-02-18 22:00:00)
     */
    public static final long MILLIS_EPOCH_TIMESTAMP = 1613656800000L;
    public static final int TOTAL_BITS = 1 << 6;
    public static final int DEF_CACHE_SIZE = 30;
    // [sign - timestamp - dataCenterId - workerId - sequence]
    private final int signBits = 1;
    private final int timestampBits;
    private final int dataCenterIdBits;
    private final int workerIdBits;
    private final int sequenceBits;

    // max value for workerId & sequence
    private final long maxDeltaTime;
    private final long maxWorkerId;
    private final long maxDataCenterId;
    private final long maxSequence;

    // shift for timestamp & workerId
    private final int timestampShift;
    private final int workerIdShift;
    private final int dataCenterIdShift;

    // distributor
    private final long epochTimestamp;
    private final Category category;
    private final Strategy strategy;
    private final TimeUnit timeUnit;
    private final long workerId;
    private final long dataCenterId;
    private final int cacheSize;
    private final Distributor distributor;

    public SnowflakeConfig(int timestampBits, int workerBits, int dataCenterBits,
                           int sequenceBits, long epochTimestamp, Category category,
                           Distributor distributor) {
        this(timestampBits, workerBits, dataCenterBits, sequenceBits, epochTimestamp,
            DEF_CACHE_SIZE, category, Strategy.CACHEABLE, distributor);
    }

    public SnowflakeConfig(int timestampBits, int workerBits, int dataCenterBits,
                           int sequenceBits, long epochTimestamp, Category category,
                           Strategy strategy, Distributor distributor) {
        this(timestampBits, workerBits, dataCenterBits, sequenceBits, epochTimestamp,
            DEF_CACHE_SIZE, category, strategy, distributor);
    }

    public SnowflakeConfig(int timestampBits, int workerBits, int dataCenterBits,
                           int sequenceBits, long epochTimestamp, int cacheSize, Category category,
                           Strategy strategy, Distributor distributor) {
        final int total = signBits + timestampBits + workerBits + dataCenterBits + sequenceBits;
        if (total != TOTAL_BITS) {
            throw new SnowflakeException("allocate not enough 64 bits.");
        }
        this.timestampBits = timestampBits;
        this.workerIdBits = workerBits;
        this.dataCenterIdBits = dataCenterBits;
        this.sequenceBits = sequenceBits;
        this.epochTimestamp = epochTimestamp > 0 ?
            epochTimestamp : (category == Category.SECONDS ? SECOND_EPOCH_TIMESTAMP : MILLIS_EPOCH_TIMESTAMP);
        this.cacheSize = cacheSize < 1 ? DEF_CACHE_SIZE : cacheSize;
        this.category = category;
        this.strategy = strategy;
        this.timeUnit = this.category == Category.SECONDS ? TimeUnit.SECONDS : TimeUnit.MILLISECONDS;
        this.workerId = distributor.getWorkerId();
        this.dataCenterId = distributor.getDataCenterId();
        this.distributor = distributor;
        this.maxDeltaTime = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerBits);
        this.maxDataCenterId = ~(-1L << dataCenterBits);
        this.maxSequence = ~(-1L << sequenceBits);
        this.timestampShift = workerBits + dataCenterBits + sequenceBits;
        this.dataCenterIdShift = workerBits + dataCenterBits;
        this.workerIdShift = sequenceBits;
        this.validate();
    }

    private void validate() {
        if (this.workerId < 0 || this.workerId > this.maxWorkerId) {
            throw new IllegalArgumentException(String.format("Worker Id can not be greater than %d or smaller than 0",
                this.maxWorkerId));
        }
        if (this.dataCenterId < 0 || this.dataCenterId > this.maxDataCenterId) {
            throw new IllegalArgumentException(String.format("DataCenterId can not be greater than %d or smaller than 0",
                this.maxDataCenterId));
        }
    }

    public Long getTimestamp() {
        final long timestamp = this.timeUnit.convert(MillisecondsClock.currentTimeMillis(), TimeUnit.MILLISECONDS);
        if ((timestamp - this.epochTimestamp) > this.maxDeltaTime) {
            throw new SnowflakeException("Timestamp bits is exhausted. Refusing UID generate. Now: " + timestamp);
        }
        return timestamp;
    }

    /**
     * 分配ID( 时间戳部分 | 数据中心部分 | 机器码部分 | 序号部分 )
     * @param deltaTime 时间差
     * @param sequence  序列
     * @return ID
     */
    public long allocate(final long deltaTime, long sequence) {
        return (deltaTime << this.timestampShift) | (this.dataCenterId << this.dataCenterIdShift)
            | (this.workerId << this.workerIdShift) | sequence;
    }

    public int getSignBits() {
        return signBits;
    }

    public int getTimestampBits() {
        return timestampBits;
    }

    public int getWorkerIdBits() {
        return workerIdBits;
    }

    public int getDataCenterIdBits() {
        return dataCenterIdBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public long getMaxDeltaTime() {
        return maxDeltaTime;
    }

    public long getMaxWorkerId() {
        return maxWorkerId;
    }

    public long getMaxDataCenterId() {
        return maxDataCenterId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public long getEpochTimestamp() {
        return epochTimestamp;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public Category getCategory() {
        return category;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    ////// Static methods //////

    public static SnowflakeConfig secondSnowflakeConfig(Strategy strategy) {
        return secondSnowflakeConfig(0L, strategy);
    }

    public static SnowflakeConfig secondSnowflakeConfig(long epochTimestamp, Strategy strategy) {
        return secondSnowflakeConfig(epochTimestamp, strategy, new MacDistributor(8, 8));
    }

    public static SnowflakeConfig secondSnowflakeConfig(Strategy strategy, Distributor distributor) {
        return secondSnowflakeConfig(0L, strategy, distributor);
    }

    public static SnowflakeConfig secondSnowflakeConfig(long epochTimestamp, Strategy strategy,
                                                        Distributor distributor) {
        return new SnowflakeConfig(30, 10, 10, 13, epochTimestamp, Category.SECONDS, strategy, distributor);
    }

    public static SnowflakeConfig secondSnowflakeConfig(long epochTimestamp, int cacheSize, Strategy strategy,
                                                        Distributor distributor) {
        return new SnowflakeConfig(30, 10, 10, 13, epochTimestamp, cacheSize, Category.SECONDS, strategy, distributor);
    }

    public static SnowflakeConfig millisSnowflakeConfig(Strategy strategy) {
        return millisSnowflakeConfig(0L, strategy);
    }

    public static SnowflakeConfig millisSnowflakeConfig(long epochTimestamp, Strategy strategy) {
        return millisSnowflakeConfig(epochTimestamp, strategy, new MacDistributor(5, 5));
    }

    public static SnowflakeConfig millisSnowflakeConfig(Strategy strategy, Distributor distributor) {
        return millisSnowflakeConfig(0L, strategy, distributor);
    }

    public static SnowflakeConfig millisSnowflakeConfig(long epochTimestamp, Strategy strategy,
                                                        Distributor distributor) {
        return new SnowflakeConfig(41, 5, 5, 12, epochTimestamp, Category.MILLISECONDS, strategy, distributor);
    }

    public static SnowflakeConfig millisSnowflakeConfig(long epochTimestamp, int cacheSize, Strategy strategy,
                                                        Distributor distributor) {
        return new SnowflakeConfig(41, 5, 5, 12, epochTimestamp, cacheSize,
            Category.MILLISECONDS, strategy, distributor);
    }


    public static SnowflakeConfig secondSnowflakeConfig() {
        return secondSnowflakeConfig(0L, DEF_CACHE_SIZE);
    }

    public static SnowflakeConfig secondSnowflakeConfig(long epochTimestamp, int cacheSize) {
        return secondSnowflakeConfig(epochTimestamp, cacheSize, new MacDistributor(8, 8));
    }

    public static SnowflakeConfig secondSnowflakeConfig(int cacheSize, Distributor distributor) {
        return secondSnowflakeConfig(0L, cacheSize, distributor);
    }

    public static SnowflakeConfig secondSnowflakeConfig(long epochTimestamp, int cacheSize,
                                                        Distributor distributor) {
        return new SnowflakeConfig(30, 10, 10, 13, epochTimestamp, cacheSize,
            Category.SECONDS, Strategy.CACHEABLE, distributor);
    }

    public static SnowflakeConfig millisSnowflakeConfig() {
        return millisSnowflakeConfig(0L, DEF_CACHE_SIZE);
    }

    public static SnowflakeConfig millisSnowflakeConfig(long epochTimestamp, int cacheSize) {
        return millisSnowflakeConfig(epochTimestamp, cacheSize, new MacDistributor(5, 5));
    }

    public static SnowflakeConfig millisSnowflakeConfig(int cacheSize, Distributor distributor) {
        return millisSnowflakeConfig(0L, cacheSize, distributor);
    }

    public static SnowflakeConfig millisSnowflakeConfig(long epochTimestamp, int cacheSize,
                                                        Distributor distributor) {
        return new SnowflakeConfig(41, 5, 5, 12, epochTimestamp, cacheSize,
            Category.MILLISECONDS, Strategy.CACHEABLE, distributor);
    }

}
