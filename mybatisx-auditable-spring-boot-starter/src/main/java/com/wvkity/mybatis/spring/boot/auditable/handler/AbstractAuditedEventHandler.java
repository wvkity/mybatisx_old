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
package com.wvkity.mybatis.spring.boot.auditable.handler;

import com.wvkity.mybatis.core.auditable.event.AuditedEvent;

/**
 * 抽象审计事件处理器
 * @param <T> 数据类型
 * @author wvkity
 * @created 2021-03-17
 * @since 1.0.0
 */
public abstract class AbstractAuditedEventHandler<T> extends AbstractEventHandler<T, AuditedEvent<T>> implements AuditedEventHandler<T> {
}
