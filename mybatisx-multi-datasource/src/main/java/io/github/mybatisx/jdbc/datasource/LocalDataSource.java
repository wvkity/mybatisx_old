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
package io.github.mybatisx.jdbc.datasource;

import java.io.Serializable;

/**
 * 线程数据源
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public interface LocalDataSource extends Serializable {

    /**
     * 获取当前数据源类型
     * @return {@link DataSourceNodeType}
     */
    DataSourceNodeType getType();

    /**
     * 所属分组
     * @return 分组
     */
    String getGroup();

    /**
     * 获取数据源名称
     * @return 数据源名称
     */
    String getName();

    /**
     * 检查当前数据源是否为读库
     * @return boolean
     */
    default boolean isRead() {
        return this.getType() == DataSourceNodeType.SLAVE;
    }

    /**
     * 检查当前数据源是否为写库
     * @return boolean
     */
    default boolean isWrite() {
        return this.getType() == DataSourceNodeType.MASTER;
    }
}
