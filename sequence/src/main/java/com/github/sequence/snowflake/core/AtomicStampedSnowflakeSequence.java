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
package com.github.sequence.snowflake.core;

import com.github.sequence.snowflake.SnowflakeConfig;
import com.github.sequence.snowflake.SnowflakeException;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Atomic雪花算法ID生成器
 * @author wvkity
 * @created 2021-02-17
 * @see AtomicStampedReference
 * @since 1.0.0
 */
public class AtomicStampedSnowflakeSequence extends AbstractSnowflakeSequence implements SnowflakeSequence {

    private static final long serialVersionUID = -8220000012403745019L;
    private final AtomicStampedReference<Long> reference = new AtomicStampedReference<>(-1L, 0);

    public AtomicStampedSnowflakeSequence(SnowflakeConfig config) {
        super(config);
    }

    @Override
    public long nextId() {
        Long newTimestamp, oldTimestamp;
        int newSequence, oldSequence;
        for (; ; ) {
            oldTimestamp = this.reference.getReference();
            oldSequence = this.reference.getStamp();
            newTimestamp = this.getTimestamp();
            if (newTimestamp < oldTimestamp) {
                throw new SnowflakeException(String.format("Clock moved backwards. Refusing for %s timeStamp",
                    oldTimestamp - newTimestamp));
            }
            if (newTimestamp.longValue() == oldTimestamp.longValue()) {
                newSequence = (int) ((oldSequence + 1) & this.config.getMaxSequence());
                if (newSequence == 0) {
                    newTimestamp = tilNextMills(oldTimestamp);
                }
            } else {
                newSequence = ThreadLocalRandom.current().nextInt(1, 3);
            }
            if (this.reference.compareAndSet(oldTimestamp, newTimestamp,
                oldSequence, newSequence)) {
                this.lastTimestamp = newTimestamp;
                this.sequence = newSequence;
                return this.config.allocate(newTimestamp - this.config.getEpochTimestamp(), newSequence);
            }
        }
    }

}
