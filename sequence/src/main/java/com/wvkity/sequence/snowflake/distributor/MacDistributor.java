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
package com.wvkity.sequence.snowflake.distributor;

import com.wvkity.sequence.snowflake.Sequences;

/**
 * MAC机器ID-数据中心ID分配器
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public class MacDistributor implements Distributor {

    private static final long serialVersionUID = -2081331306949711445L;
    private final int timestampBits;
    private final int dataCenterBits;
    private final int workerBits;
    private final int sequenceBits;
    private final long workerId;
    private final long dataCenterId;

    public MacDistributor(int timestampBits, int workerBits, int dataCenterBits, int sequenceBits) {
        this.timestampBits = timestampBits;
        this.workerBits = workerBits;
        this.dataCenterBits = dataCenterBits;
        this.sequenceBits = sequenceBits;
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
        return "MacDistributor{" +
            "timestampBits=" + timestampBits +
            ", dataCenterBits=" + dataCenterBits +
            ", workerBits=" + workerBits +
            ", sequenceBits=" + sequenceBits +
            ", workerId=" + workerId +
            ", dataCenterId=" + dataCenterId +
            '}';
    }
}
