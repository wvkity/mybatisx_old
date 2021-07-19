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

import com.github.mybatisx.backup.event.handle.BackupMetadataHandler;
import com.github.mybatisx.backup.thread.QueueThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 备份数据队列处理器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public class DefaultBackupQueueProcessor implements BackupQueueProcessor {

    private static final Logger log = LoggerFactory.getLogger(DefaultBackupQueueProcessor.class);
    final QueueThreadExecutor executor;
    private final BackupQueue queue;
    private final BackupMetadataHandler delegate;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public DefaultBackupQueueProcessor(QueueThreadExecutor executor, BackupQueue queue,
                                       BackupMetadataHandler delegate) {
        this.executor = executor;
        this.queue = queue;
        this.delegate = delegate;
    }

    @PostConstruct
    public void init() {
        this.start();
        this.executor.submit(this);
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                QueueData data = this.queue.take();
                this.process(data);
            }
        } catch (Exception e) {
            log.error("队列数据处理失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void process(QueueData data) {
        this.delegate.handle(data.getEvent(), data.getPhase());
    }

    @Override
    public void start() {
        this.running.set(true);
    }

    @Override
    public void stop() {
        this.running.set(false);
    }
}
