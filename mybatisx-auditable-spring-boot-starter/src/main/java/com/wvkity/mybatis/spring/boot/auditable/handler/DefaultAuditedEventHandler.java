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

import com.wvkity.mybatis.core.auditable.alter.AuditedAlterData;
import com.wvkity.mybatis.core.auditable.event.AuditedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 审计事件处理器
 * @author wvkity
 * @created 2021-03-17
 * @since 1.0.0
 */
public class DefaultAuditedEventHandler extends AbstractAuditedEventHandler<List<AuditedAlterData>> {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuditedEventHandler.class);

    @Override
    public void handle(AuditedEvent<List<AuditedAlterData>> event) {
        try {
            final List<AuditedAlterData> source = event.getSource();
            if (source != null && !source.isEmpty()) {
                for (AuditedAlterData it: source) {
                    it.invoke();
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

}
