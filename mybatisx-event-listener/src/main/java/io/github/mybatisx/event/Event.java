/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
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
package io.github.mybatisx.event;

import java.io.Serializable;

/**
 * 事件
 * @param <T> 数据类型
 * @author wvkity
 * @created 2021-03-13
 * @since 1.0.0
 */
public interface Event<T> extends Serializable {

    /**
     * 数据
     * @return 数据
     */
    T getSource();

    /**
     * 获取事件类型
     * @return {@link EventPhase}
     */
    EventPhase getEventPhase();

    /**
     * 触发时间
     * @return 时间戳
     */
    long getTimestamp();

    /**
     * 事件类型唯一标识
     * @return 事件类型唯一标识
     */
    String getEventUnique();

}
