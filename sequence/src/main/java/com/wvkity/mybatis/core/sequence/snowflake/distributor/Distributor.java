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

/**
 * 分配器
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
public interface Distributor {

    /**
     * 获取时间戳位数
     * @return 时间戳位数
     */
    int getTimestampBits();

    /**
     * 获取数据中心位数
     * @return 数据中心位数
     */
    int getDataCenterBits();

    /**
     * 获取机器标识位数
     * @return 机器标识位数
     */
    int getWorkerBits();

    /**
     * 获取序号位数
     * @return 序号位数
     */
    int getSequenceBits();

    /**
     * 获取机器ID
     * @return 机器ID
     */
    long getWorkerId();

    /**
     * 获取数据中心ID
     * @return 数据中心ID
     */
    long getDataCenterId();

}
