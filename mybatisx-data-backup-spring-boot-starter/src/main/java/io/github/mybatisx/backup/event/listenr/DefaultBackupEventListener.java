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
package io.github.mybatisx.backup.event.listenr;

import io.github.mybatisx.Objects;
import io.github.mybatisx.backup.event.BackupEvent;
import io.github.mybatisx.backup.event.handle.BackupEventHandler;
import io.github.mybatisx.event.EventPhase;

/**
 * 默认数据备份事件监听器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public class DefaultBackupEventListener implements BackupEventListener {

    protected final BackupEventHandler metadataHandler;

    public DefaultBackupEventListener(BackupEventHandler metadataHandler) {
        this.metadataHandler = metadataHandler;
    }

    @Override
    public void listen(BackupEvent event, EventPhase phase) {
        if (Objects.nonNull(this.metadataHandler)) {
            this.metadataHandler.doHandle(event, phase);
        }
    }
}
