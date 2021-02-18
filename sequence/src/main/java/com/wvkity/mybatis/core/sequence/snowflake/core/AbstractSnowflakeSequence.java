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
package com.wvkity.mybatis.core.sequence.snowflake.core;

import com.wvkity.mybatis.core.sequence.snowflake.SnowflakeConfig;
import com.wvkity.mybatis.core.sequence.snowflake.SnowflakeException;
import com.wvkity.mybatis.core.sequence.snowflake.SnowflakeParser;
import com.wvkity.mybatis.core.sequence.snowflake.SnowflakeSequenceInfo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 抽象雪花ID生成器
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public abstract class AbstractSnowflakeSequence {

    protected long sequence = 0L;
    protected long lastTimestamp = -1L;
    protected final SnowflakeConfig config;
    protected final SnowflakeParser parser;

    public AbstractSnowflakeSequence(SnowflakeConfig config) {
        this.config = config;
        this.parser = new SnowflakeParser(config);
    }

    protected long nextId() {
        long newTimestamp = this.config.getTimestamp();
        if (newTimestamp < lastTimestamp) {
            throw new SnowflakeException(String.format("Clock moved backwards. Refusing for %s timeStamp",
                lastTimestamp - newTimestamp));
        }
        if (newTimestamp == this.lastTimestamp) {
            this.sequence = (this.sequence + 1) & this.config.getMaxSequence();
            if (this.sequence == 0) {
                newTimestamp = tilNextMills(lastTimestamp);
            }
        } else {
            this.sequence = ThreadLocalRandom.current().nextLong(1, 3);
        }
        this.lastTimestamp = newTimestamp;
        return config.allocate(newTimestamp - this.config.getEpochTimestamp(), sequence);
    }

    protected long tilNextMills(final long lastTimestamp) {
        long timestamp = this.getTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = this.getTimestamp();
        }
        return timestamp;
    }

    public SnowflakeSequenceInfo parse(final long id) {
        return this.parser.parse(id);
    }

    protected Long getTimestamp() {
        return this.config.getTimestamp();
    }
}
