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

import javax.sql.DataSource;

/**
 * 数据源节点
 * @param <T> 数据源类型
 * @author wvkity
 * @created 2021-08-07
 * @since 1.0.0
 */
public interface DataSourceNode<T extends DataSource> {

    /**
     * 分组
     * @return 分组
     */
    String getGroup();

    /**
     * 数据源名称
     * @return 名称
     */
    String getName();

    /**
     * 节点类型
     * @return {@link DataSourceNodeType}
     */
    DataSourceNodeType getNodeType();

    /**
     * 初始化数据库脚本
     * @return 初始化数据库脚本
     */
    String getSchema();

    /**
     * 初始化数据
     * @return 初始化数据
     */
    String getData();

    /**
     * 分隔符
     * @return 分隔符
     */
    String getSeparator();

    /**
     * 数据源
     * @return 数据源
     */
    T getTarget();

}
