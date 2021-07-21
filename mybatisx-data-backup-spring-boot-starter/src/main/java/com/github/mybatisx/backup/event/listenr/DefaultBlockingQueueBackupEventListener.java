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
package com.github.mybatisx.backup.event.listenr;

import com.github.mybatisx.Objects;
import com.github.mybatisx.backup.event.BackupEvent;
import com.github.mybatisx.backup.queue.BackupQueue;
import com.github.mybatisx.backup.queue.QueueData;
import com.github.mybatisx.event.EventPhase;

/**
 * 默认数据备份事件监听器(队列处理方式)
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public class DefaultBlockingQueueBackupEventListener implements BackupEventListener {

    private final BackupQueue backupQueue;

    public DefaultBlockingQueueBackupEventListener(BackupQueue backupQueue) {
        this.backupQueue = backupQueue;
    }

    @Override
    public void listen(BackupEvent event, EventPhase phase) {
        if (Objects.nonNull(event)) {
            this.backupQueue.offer(new QueueData(event, phase));
        }
    }
}