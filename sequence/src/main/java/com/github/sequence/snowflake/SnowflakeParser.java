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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ID解析器
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public class SnowflakeParser {

    private final SnowflakeConfig config;

    public SnowflakeParser(SnowflakeConfig config) {
        this.config = config;
    }

    public SnowflakeSequenceInfo parse(final String id) {
        return id == null || id.trim().isEmpty() ? null : parse(Long.parseLong(id));
    }

    public SnowflakeSequenceInfo parse(final long id) {
        long totalBits = SnowflakeConfig.TOTAL_BITS;
        long signBits = this.config.getSignBits();
        long timestampBits = this.config.getTimestampBits();
        long workerIdBits = this.config.getWorkerBits();
        long dataCenterIdBits = this.config.getDataCenterBits();
        long sequenceBits = this.config.getSequenceBits();

        long sequence = (id << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (id << (timestampBits + signBits + dataCenterIdBits)) >>> (totalBits - workerIdBits);
        long dataCenterId = (id << (timestampBits + signBits)) >>> (totalBits - dataCenterIdBits);
        long deltaTime = id >>> (workerIdBits + dataCenterIdBits + sequenceBits);
        return new SnowflakeSequenceInfo(String.valueOf(id), this.config.getEpochTimestamp(), dataCenterId, workerId,
            sequence, deltaTime, this.config.getTimeUnit());
    }

    public List<SnowflakeSequenceInfo> parse(final Long... ids) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<>(0);
        }
        return Stream.of(ids).filter(Objects::nonNull).map(this::parse).collect(Collectors.toList());
    }

    public List<SnowflakeSequenceInfo> parse(final List<Long> ids) {
        return ids == null || ids.isEmpty() ? new ArrayList<>(0) : this.parse(ids.toArray(new Long[0]));
    }

}
