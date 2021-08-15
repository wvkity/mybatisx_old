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
package com.github.mybatisx.backup.event.handle;

import com.github.mybatisx.Objects;
import com.github.mybatisx.backup.event.BackupEvent;
import com.github.mybatisx.event.EventPhase;
import com.github.mybatisx.event.handle.EventHandler;

/**
 * 备份数据处理器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public interface BackupEventHandler extends EventHandler<BackupEvent> {

    /**
     * 备份数据处理
     * @param event {@link BackupEvent}
     * @param phase {@link EventPhase}
     */
    @Override
    default void doHandle(final BackupEvent event, final EventPhase phase) {
        if (Objects.nonNull(event)) {
            switch (phase) {
                case AFTER_COMMIT:
                    this.onCommit(event, phase);
                    break;
                case AFTER_ROLLBACK:
                    this.onRollback(event, phase);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default String getEventUnique() {
        return BackupEvent.EVENT_UNIQUE;
    }

    /**
     * 事务提交成功处理
     * @param event {@link BackupEvent}
     * @param phase {@link EventPhase}
     */
    void onCommit(final BackupEvent event, final EventPhase phase);

    /**
     * 事务回滚处理
     * @param event {@link BackupEvent}
     * @param phase {@link EventPhase}
     */
    void onRollback(final BackupEvent event, final EventPhase phase);

}
