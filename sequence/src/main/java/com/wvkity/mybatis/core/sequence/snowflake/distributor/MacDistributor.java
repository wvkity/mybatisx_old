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
 * MAC机器ID-数据中心ID分配器
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public class MacDistributor implements Distributor {

    private final int workerBits;
    private final int dataCenterBits;
    private final long workerId;
    private final long dataCenterId;

    public MacDistributor(int workerBits, int dataCenterBits) {
        this.workerBits = workerBits;
        this.dataCenterBits = dataCenterBits;
        this.workerId = Sequences.getDefWorkerId(this.workerBits, this.dataCenterBits);
        this.dataCenterId = Sequences.getDefDataCenterId(this.dataCenterBits);
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public int getDataCenterBits() {
        return dataCenterBits;
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
            "workerBits=" + workerBits +
            ", dataCenterBits=" + dataCenterBits +
            ", workerId=" + workerId +
            ", dataCenterId=" + dataCenterId +
            '}';
    }
}
