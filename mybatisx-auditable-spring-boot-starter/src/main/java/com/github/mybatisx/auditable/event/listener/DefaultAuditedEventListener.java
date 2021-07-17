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
package com.github.mybatisx.auditable.event.listener;

import com.github.mybatisx.Objects;
import com.github.mybatisx.auditable.event.AuditedEvent;
import com.github.mybatisx.auditable.event.handle.AuditedEventDataHandler;
import com.github.mybatisx.event.EventPhase;

/**
 * 默认审计事件监听器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultAuditedEventListener implements AuditedEventListener {

    protected final AuditedEventDataHandler metadataHandler;

    public DefaultAuditedEventListener(AuditedEventDataHandler metadataHandler) {
        this.metadataHandler = metadataHandler;
    }

    @Override
    public void listen(AuditedEvent event, EventPhase phase) {
        if (Objects.nonNull(metadataHandler)) {
            this.metadataHandler.handle(event, phase);
        }
    }
}
