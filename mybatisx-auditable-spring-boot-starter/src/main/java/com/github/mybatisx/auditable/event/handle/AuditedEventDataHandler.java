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
package com.github.mybatisx.auditable.event.handle;

import com.github.mybatisx.auditable.event.AuditedEvent;
import com.github.mybatisx.event.EventPhase;
import com.github.mybatisx.event.handle.EventDataHandler;

/**
 * 审计元数据处理器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface AuditedEventDataHandler extends EventDataHandler<AuditedEvent> {

    /**
     * 处理审计元数据
     * @param event 审计事件数据
     * @param phase {@link EventPhase}
     */
    @Override
    void handle(final AuditedEvent event, final EventPhase phase);
}
