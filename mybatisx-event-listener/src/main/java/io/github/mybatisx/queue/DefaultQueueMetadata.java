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

import io.github.mybatisx.event.Event;
import io.github.mybatisx.event.EventPhase;

/**
 * 默认队列数据
 * @author wvkity
 * @created 2021-07-25
 * @since 1.0.0
 */
public class DefaultQueueMetadata implements QueueMetadata {

    private final Event<?> event;
    private final EventPhase eventPhase;

    public DefaultQueueMetadata(Event<?> event, EventPhase eventPhase) {
        this.event = event;
        this.eventPhase = eventPhase;
    }

    @Override
    public Event<?> getEvent() {
        return this.event;
    }

    @Override
    public EventPhase getEventPhase() {
        return this.eventPhase;
    }
}
