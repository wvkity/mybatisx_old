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
package com.github.mybatisx.event.handle;

import com.github.mybatisx.event.Event;
import com.github.mybatisx.event.EventPhase;

/**
 * 处理器
 * @author wvkity
 * @created 2021-07-26
 * @since 1.0.0
 */
public interface Handler {

    /**
     * 处理事件数据
     * @param event 事件数据
     * @param phase {@link EventPhase}
     * @param <E>   事件类型
     */
    <E extends Event<?>> void handle(final E event, final EventPhase phase);

    /**
     * 事件类型唯一标识
     * @return 事件类型唯一标识
     */
    String getEventUnique();

}
