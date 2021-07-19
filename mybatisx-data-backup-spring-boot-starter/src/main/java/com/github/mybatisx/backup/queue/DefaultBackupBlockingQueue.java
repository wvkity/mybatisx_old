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
package com.github.mybatisx.backup.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 默认数据备份队列
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public class DefaultBackupBlockingQueue implements BackupQueue {

    private final BlockingQueue<QueueData> queue;

    public DefaultBackupBlockingQueue() {
        this(4096);
    }

    public DefaultBackupBlockingQueue(final int capacity) {
        this(new LinkedBlockingQueue<>(capacity));
    }

    public DefaultBackupBlockingQueue(BlockingQueue<QueueData> queue) {
        this.queue = queue;
    }

    @Override
    public void put(QueueData data) throws InterruptedException {
        this.queue.put(data);
    }

    @Override
    public boolean offer(QueueData data) {
        return this.queue.offer(data);
    }

    @Override
    public QueueData take() throws InterruptedException {
        return this.queue.take();
    }

    @Override
    public QueueData poll() {
        return this.queue.poll();
    }

    @Override
    public QueueData peek() {
        return this.queue.peek();
    }

    @Override
    public boolean remove(QueueData data) {
        return this.queue.remove(data);
    }

    @Override
    public boolean contains(QueueData data) {
        return this.queue.contains(data);
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
