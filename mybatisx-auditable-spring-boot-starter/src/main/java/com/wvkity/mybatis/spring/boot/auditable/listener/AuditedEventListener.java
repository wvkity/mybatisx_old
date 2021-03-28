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

import com.wvkity.mybatis.core.auditable.event.AuditedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 审计事务事件监听器
 * @param <T> 数据类型
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public interface AuditedEventListener<T> extends EventListener<AuditedEvent<T>> {

    /**
     * 事务回滚后监听
     * @param event {@link AuditedEvent}
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT,
        condition = "T(com.wvkity.mybatis.core.auditable.event.EventPhase).BEFORE_COMMIT eq #event.eventPhase")
    default void beforeCommit(final AuditedEvent<T> event) {
        this.listen(event);
    }

    /**
     * 事务回滚后监听
     * @param event {@link AuditedEvent}
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,
        condition = "T(com.wvkity.mybatis.core.auditable.event.EventPhase).AFTER_COMMIT eq #event.eventPhase")
    default void afterCommit(final AuditedEvent<T> event) {
        this.listen(event);
    }

    /**
     * 事务回滚后监听
     * @param event {@link AuditedEvent}
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK,
        condition = "T(com.wvkity.mybatis.core.auditable.event.EventPhase).AFTER_ROLLBACK eq #event.eventPhase")
    default void afterRollback(final AuditedEvent<T> event) {
        this.listen(event);
    }

    /**
     * 事务完成后监听
     * @param event {@link AuditedEvent}
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION,
        condition = "T(com.wvkity.mybatis.core.auditable.event.EventPhase).AFTER_COMPLETION eq #event.eventPhase")
    default void afterCompletion(final AuditedEvent<T> event) {
        this.listen(event);
    }


    @Override
    default void listen(final AuditedEvent<T> event) {
        this.handle(event);
    }

    /**
     * 事件处理
     * @param event 事件对象
     */
    void handle(final AuditedEvent<T> event);
}
