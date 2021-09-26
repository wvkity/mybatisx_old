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
package io.github.mybatisx.auditable.event;

import io.github.mybatisx.auditable.meta.AuditedMetadata;
import io.github.mybatisx.event.EventPhase;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认审计事件
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultAuditedEvent implements AuditedEvent {

    private static final long serialVersionUID = -7984339256387890359L;
    private final List<AuditedMetadata> source;
    private final EventPhase eventPhase;
    private final String uniqueCode;
    private final long timestamp;

    public DefaultAuditedEvent(EventPhase eventPhase, String uniqueCode) {
        this(new ArrayList<>(), eventPhase, uniqueCode);
    }

    public DefaultAuditedEvent(List<AuditedMetadata> source, EventPhase eventPhase, String uniqueCode) {
        this.source = source == null ? new ArrayList<>() : source;
        this.eventPhase = eventPhase;
        this.uniqueCode = uniqueCode;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 添加审计数据
     * @param metadata {@link AuditedMetadata}
     * @return {@code this}
     */
    public DefaultAuditedEvent add(final AuditedMetadata metadata) {
        if (metadata != null) {
            this.source.add(metadata);
        }
        return this;
    }

    /**
     * 添加多个审计数据
     * @param metadataList 审计数据列表
     * @return {@code this}
     */
    public DefaultAuditedEvent addAll(final List<AuditedMetadata> metadataList) {
        if (metadataList != null && !metadataList.isEmpty()) {
            this.source.addAll(metadataList);
        }
        return this;
    }

    @Override
    public String getUniqueCode() {
        return this.uniqueCode;
    }

    @Override
    public List<AuditedMetadata> getSource() {
        return this.source;
    }

    @Override
    public EventPhase getEventPhase() {
        return this.eventPhase;
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
            ", uniqueCode='" + uniqueCode + '\'' +
            ", timestamp=" + timestamp +
            '}';
    }

    public static DefaultAuditedEvent of(final EventPhase eventPhase, final String uniqueCode) {
        return new DefaultAuditedEvent(eventPhase, uniqueCode);
    }

    public static DefaultAuditedEvent of(final List<AuditedMetadata> source, final EventPhase eventPhase,
                                         final String uniqueCode) {
        return new DefaultAuditedEvent(source, eventPhase, uniqueCode);
    }
}
