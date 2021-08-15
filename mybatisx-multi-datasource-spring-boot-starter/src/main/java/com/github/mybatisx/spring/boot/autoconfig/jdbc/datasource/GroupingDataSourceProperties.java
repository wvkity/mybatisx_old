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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 分组读写数据源配置
 * @author wvkity
 * @created 2021-08-06
 * @since 1.0.0
 */
public class GroupingDataSourceProperties extends GroupableDataSourceProperty {

    /**
     * 主库数据源配置
     */
    @NestedConfigurationProperty
    private WriteDataSourceProperties master;

    /**
     * 读库数据源配置
     */
    @NestedConfigurationProperty
    private ReadDataSourceProperties slave;

    public WriteDataSourceProperties getMaster() {
        return master;
    }

    public void setMaster(WriteDataSourceProperties master) {
        this.master = master;
    }

    public ReadDataSourceProperties getSlave() {
        return slave;
    }

    public void setSlave(ReadDataSourceProperties slave) {
        this.slave = slave;
    }
}
