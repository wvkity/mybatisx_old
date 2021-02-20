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
package com.wvkity.mybatis.core.sequence.snowflake.distributor;

import com.wvkity.mybatis.core.sequence.snowflake.Sequences;

/**
 * 默认mac分配器(秒级)
 * @author wvkity
 * @created 2021-02-21
 * @since 1.0.0
 */
public class DefaultMacMilliDistributor implements Distributor {

    private final int timestampBits = 41;
    private final int dataCenterBits = 5;
    private final int workerBits = 5;
    private final int sequenceBits = 12;
    private final long workerId;
    private final long dataCenterId;

    public DefaultMacMilliDistributor() {
        this.workerId = Sequences.getDefWorkerId(this.workerBits, this.dataCenterBits);
        this.dataCenterId = Sequences.getDefDataCenterId(this.dataCenterBits);
    }

    @Override
    public int getTimestampBits() {
        return this.timestampBits;
    }

    @Override
    public int getWorkerBits() {
        return workerBits;
    }

    @Override
    public int getDataCenterBits() {
        return dataCenterBits;
    }

    @Override
    public int getSequenceBits() {
        return this.sequenceBits;
    }

    @Override
    public long getWorkerId() {
        return this.workerId;
    }

    @Override
    public long getDataCenterId() {
        return this.dataCenterId;
    }

    @Override
    public String toString() {
        return "DefaultMacMilliDistributor{" +
            "timestampBits=" + timestampBits +
            ", dataCenterBits=" + dataCenterBits +
            ", workerBits=" + workerBits +
            ", sequenceBits=" + sequenceBits +
            ", workerId=" + workerId +
            ", dataCenterId=" + dataCenterId +
            '}';
    }
}
