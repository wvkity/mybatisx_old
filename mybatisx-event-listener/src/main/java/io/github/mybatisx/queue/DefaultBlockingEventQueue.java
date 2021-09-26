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
package io.github.mybatisx.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 默认事件队列
 * @author wvkity
 * @created 2021-07-25
 * @since 1.0.0
 */
public class DefaultBlockingEventQueue implements EventQueue {

    private final BlockingQueue<QueueMetadata> queue;

    public DefaultBlockingEventQueue(int capacity) {
        this(new LinkedBlockingQueue<>(capacity));
    }

    public DefaultBlockingEventQueue(BlockingQueue<QueueMetadata> queue) {
        this.queue = queue;
    }

    @Override
    public void put(QueueMetadata data) throws InterruptedException {
        this.queue.put(data);
    }

    @Override
    public boolean offer(QueueMetadata data) {
        return this.queue.offer(data);
    }

    @Override
    public QueueMetadata take() throws InterruptedException {
        return this.queue.take();
    }

    @Override
    public QueueMetadata poll() {
        return null;
    }

    @Override
    public QueueMetadata peek() {
        return this.queue.peek();
    }

    @Override
    public boolean remove(QueueMetadata data) {
        return this.queue.remove(data);
    }

    @Override
    public boolean contains(QueueMetadata data) {
        return this.queue.contains(data);
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
