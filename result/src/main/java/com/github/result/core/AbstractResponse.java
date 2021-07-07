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
package com.github.result.core;

import com.github.result.error.AbstractError;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 抽象响应
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractResponse extends AbstractError implements Response {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 响应时间
     */
    protected LocalDateTime time = LocalDateTime.now();
    /**
     * 时间戳
     */
    protected long timestamp = time.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    /**
     * 时间字符串
     */
    protected String gmtTime = FORMATTER.format(time);

    public String getGmtTime() {
        return this.gmtTime;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

}
