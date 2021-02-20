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
package com.wvkity.sequence.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 毫秒时钟
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
public class MillisecondsClock {

    private final long period;
    private final AtomicLong now;

    public MillisecondsClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.start();
    }

    private void start() {
        Executors.newSingleThreadScheduledExecutor(it -> {
            final Thread thread = new Thread(it, "MILLISECOND CLOCK");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> this.now.set(System.currentTimeMillis()),
            this.period, this.period, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return this.now.get();
    }

    ///// Inner class /////

    private static final class SingletonHolder {
        protected static final MillisecondsClock INSTANCE = new MillisecondsClock(1L);
    }

    public static MillisecondsClock getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static long currentTimeMillis() {
        return getInstance().now();
    }
}
