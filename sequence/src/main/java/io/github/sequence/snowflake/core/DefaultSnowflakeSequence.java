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
package io.github.sequence.snowflake.core;

import io.github.sequence.snowflake.SnowflakeConfig;

import java.util.concurrent.locks.StampedLock;

/**
 * 默认雪花算法ID生成器
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
public class DefaultSnowflakeSequence extends AbstractSnowflakeSequence implements SnowflakeSequence {

    private static final long serialVersionUID = 8131898063389489772L;
    private final StampedLock lock = new StampedLock();

    public DefaultSnowflakeSequence(SnowflakeConfig config) {
        super(config);
    }

    @Override
    public long nextId() {
        final long stamp = this.lock.writeLock(), id;
        try {
            id = this.nextValue();
        } finally {
            this.lock.unlockWrite(stamp);
        }
        return id;
    }

}
