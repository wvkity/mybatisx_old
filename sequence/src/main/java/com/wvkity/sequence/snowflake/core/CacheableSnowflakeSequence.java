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
package com.wvkity.sequence.snowflake.core;

import com.wvkity.sequence.snowflake.SnowflakeConfig;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.StampedLock;

/**
 * 可缓存雪花算法ID生成器
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
public class CacheableSnowflakeSequence extends AbstractSnowflakeSequence implements SnowflakeSequence {

    private static final long serialVersionUID = -8074391072308842191L;
    private final StampedLock lock = new StampedLock();
    private final Queue<Long> queue;

    public CacheableSnowflakeSequence(SnowflakeConfig config) {
        super(config);
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public long nextId() {
        Long id;
        while ((id = this.queue.poll()) == null) {
            final long stamp = this.lock.writeLock();
            try {
                if (this.queue.peek() == null) {
                    this.batchGenerated(this.config.getCacheSize());
                }
            } finally {
                this.lock.unlockWrite(stamp);
            }
        }
        return id;
    }

    private void batchGenerated(final int size) {
        for (int i = 0; i < size; i++) {
            this.queue.offer(this.nextId());
        }
    }

}
