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
package io.github.sequence.snowflake.distributor;

/**
 * 默认毫秒级分配器
 * @author wvkity
 * @created 2021-02-21
 * @since 1.0.0
 */
public class DefaultMilliDistributor implements Distributor {

    private static final long serialVersionUID = 5034345163340931035L;
    private final int timestampBits = 41;
    private final int dataCenterBits = 5;
    private final int workerBits = 5;
    private final int sequenceBits = 12;
    private final long workerId;
    private final long dataCenterId;

    public DefaultMilliDistributor(long workerId, long dataCenterId) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
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
        return "DefaultMilliDistributor{" +
            "timestampBits=" + timestampBits +
            ", dataCenterBits=" + dataCenterBits +
            ", workerBits=" + workerBits +
            ", sequenceBits=" + sequenceBits +
            ", workerId=" + workerId +
            ", dataCenterId=" + dataCenterId +
            '}';
    }
}
