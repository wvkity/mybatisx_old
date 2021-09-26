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
package io.github.mybatisx.auditable.event.listener;

import io.github.mybatisx.auditable.event.AuditedEvent;
import io.github.mybatisx.event.EventPhase;
import io.github.mybatisx.event.listener.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 审计事件监听器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface AuditedEventListener extends EventListener<AuditedEvent> {

    /**
     * 事务回滚事件监听
     * @param event {@link AuditedEvent}
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    default void afterRollback(final AuditedEvent event) {
        this.listen(event, EventPhase.AFTER_ROLLBACK);
    }

    /**
     * 审计事件监听
     * @param event 事件
     * @param phase {@link EventPhase}
     */
    @Override
    void listen(final AuditedEvent event, final EventPhase phase);
}
