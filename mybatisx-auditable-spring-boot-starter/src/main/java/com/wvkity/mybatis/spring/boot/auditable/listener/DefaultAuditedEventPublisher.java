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
package com.wvkity.mybatis.spring.boot.auditable.listener;

import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;
import com.wvkity.mybatis.core.auditable.event.AuditedEvent;
import com.wvkity.mybatis.core.auditable.event.AuditedEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * 默认审计回滚事件发布器
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class DefaultAuditedEventPublisher implements AuditedEventPublisher<List<AuditedAlterData>> {

    private final ApplicationEventPublisher publisher;

    public DefaultAuditedEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishEvent(AuditedEvent<List<AuditedAlterData>> event) {
        this.publisher.publishEvent(event);
    }

}
