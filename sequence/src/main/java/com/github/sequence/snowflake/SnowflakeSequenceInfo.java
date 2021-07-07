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
package com.github.sequence.snowflake;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 雪花算法ID详细信息
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public class SnowflakeSequenceInfo {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final String id;
    private final long epochTimestamp;
    private final long dataCenterId;
    private final long workerId;
    private final long sequence;
    private final long deltaTime;
    private final long timestamp;
    private final TimeUnit timeUnit;
    private final LocalDateTime dateTime;
    private final String dateTimeString;

    public SnowflakeSequenceInfo(String id, long epochTimestamp, long dataCenterId, long workerId,
                                 long sequence, long deltaTime, TimeUnit timeUnit) {
        this.id = id;
        this.epochTimestamp = epochTimestamp;
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
        this.sequence = sequence;
        this.deltaTime = deltaTime;
        this.timeUnit = timeUnit;
        this.timestamp = this.timeUnit.toMillis(this.epochTimestamp + this.deltaTime);
        this.dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timestamp), ZoneId.systemDefault());
        this.dateTimeString = TIME_FORMATTER.format(this.dateTime);
    }

    public String toJsonString() {
        return "{" +
            "\"id\": \"" + this.id + "\"" +
            ", \"epochTimestamp\": " + this.epochTimestamp +
            ", \"dataCenterId\": " + this.dataCenterId +
            ", \"workerId\": " + this.workerId +
            ", \"sequence\": " + this.sequence +
            ", \"deltaTime\": " + this.deltaTime +
            ", \"timestamp\": " + this.timestamp +
            ", \"timeUnit\": \"" + this.timeUnit + "\"" +
            ", \"dateTime\": \"" + this.dateTime + "\"" +
            ", \"dateTimeString\": \"" + this.dateTimeString +
            "\"}";
    }

    @Override
    public String toString() {
        return "SnowflakeSequence{" +
            "id=" + id +
            ", epochTimestamp=" + epochTimestamp +
            ", dataCenterId=" + dataCenterId +
            ", workerId=" + workerId +
            ", sequence=" + sequence +
            ", deltaTime=" + deltaTime +
            ", timestamp=" + timestamp +
            ", timeUnit=" + timeUnit +
            ", dateTime=" + dateTime +
            ", dateTimeString='" + dateTimeString + '\'' +
            '}';
    }

    public String getId() {
        return id;
    }

    public long getEpochTimestamp() {
        return epochTimestamp;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getSequence() {
        return sequence;
    }

    public long getDeltaTime() {
        return deltaTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }
}
