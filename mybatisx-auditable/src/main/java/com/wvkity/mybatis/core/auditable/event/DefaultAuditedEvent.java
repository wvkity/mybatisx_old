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
package com.wvkity.mybatis.core.auditable.event;

import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;

import java.util.List;

/**
 * 默认审计事件
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class DefaultAuditedEvent implements AuditedEvent<List<AuditedAlterData>> {

    private static final long serialVersionUID = 4793736721866881055L;

    private final List<AuditedAlterData> source;
    private final EventPhase eventPhase;
    private final String code;
    private final long timestamp;

    public DefaultAuditedEvent(List<AuditedAlterData> source, EventPhase eventPhase, String code) {
        if (source == null) {
            throw new IllegalArgumentException("null source");
        }
        this.source = source;
        this.eventPhase = eventPhase;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public List<AuditedAlterData> getSource() {
        return this.source;
    }

    @Override
    public EventPhase getEventPhase() {
        return this.eventPhase;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "DefaultAuditedEvent{" +
            "source=" + source +
            ", eventPhase=" + eventPhase +
            ", timestamp=" + timestamp +
            '}';
    }

    public static DefaultAuditedEvent of(final List<AuditedAlterData> source, final EventPhase eventPhase,
                                         final String code) {
        return new DefaultAuditedEvent(source, eventPhase, code);
    }

    public static DefaultAuditedEvent rollback(final List<AuditedAlterData> source, final String code) {
        return of(source, EventPhase.AFTER_ROLLBACK, code);
    }

}
