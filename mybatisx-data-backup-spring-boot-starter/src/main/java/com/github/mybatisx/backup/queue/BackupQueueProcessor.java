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
 * 备份数据队列处理器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public interface BackupQueueProcessor extends Runnable {

    /**
     * 处理数据队列
     * @param data 队列数据
     */
    void process(final QueueData data);

    /**
     * 继续开始任务
     */
    void start();

    /**
     * 暂停任务
     */
    void stop();
}
