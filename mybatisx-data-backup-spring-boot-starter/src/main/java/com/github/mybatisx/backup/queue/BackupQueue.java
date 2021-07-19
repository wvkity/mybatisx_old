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

/**
 * 数据备份队列
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public interface BackupQueue {

    /**
     * 入队
     * @param data 数据
     * @throws InterruptedException 线程终止时将抛出异常
     */
    void put(final QueueData data) throws InterruptedException;

    /**
     * 入队
     * @param data 数据
     * @return boolean
     */
    boolean offer(final QueueData data);

    /**
     * 出队
     * @return {@link QueueData}
     * @throws InterruptedException 线程终止时将抛出异常
     */
    QueueData take() throws InterruptedException;

    /**
     * 出队
     * @return {@link QueueData}
     */
    QueueData poll();

    /**
     * 获取头部元素
     * @return {@link QueueData}
     */
    QueueData peek();

    /**
     * 移除元素
     * @param data {@link QueueData}
     * @return boolean
     */
    boolean remove(final QueueData data);

    /**
     * 检查队列中是否包含指定元素
     * @param data {@link QueueData}
     * @return boolean
     */
    boolean contains(final QueueData data);

    /**
     * 队列是否为空
     * @return boolean
     */
    boolean isEmpty();

}
