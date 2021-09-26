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
package io.github.mybatisx.backup.event.publisher;

import io.github.mybatisx.Objects;
import io.github.mybatisx.backup.event.BackupEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 默认数据备份事件发布器
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
public class DefaultBackupEventPublisher implements BackupEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public DefaultBackupEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishEvent(BackupEvent event) {
        if (Objects.nonNull(event)) {
            this.eventPublisher.publishEvent(event);
        }
    }
}
