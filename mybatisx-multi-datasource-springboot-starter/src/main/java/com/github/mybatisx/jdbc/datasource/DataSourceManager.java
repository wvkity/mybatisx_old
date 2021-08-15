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
package com.github.mybatisx.jdbc.datasource;

import javax.sql.DataSource;

/**
 * 数据源管理器
 * @author wvkity
 * @created 2021-08-11
 * @since 1.0.0
 */
public interface DataSourceManager {

    /**
     * 获取数据源
     * @return {@link DataSource}
     */
    default DataSource determine() {
        return this.determine(MultiDataSourceContextHolder.get());
    }

    /**
     * 获取数据源
     * @param source {@link LocalDataSource}
     * @return {@link DataSource}
     */
    default DataSource determine(final LocalDataSource source) {
        return this.determine(source.getGroup(), source.getName(), source.getType());
    }

    /**
     * 获取数据源
     * @param group    所属分组
     * @param name     节点名称
     * @param nodeType 数据源类型
     * @return {@link DataSource}
     */
    DataSource determine(final String group, final String name, final DataSourceNodeType nodeType);

    /**
     * 添加数据源
     * @param name       节点名称
     * @param nodeType   {@link DataSourceNodeType}
     * @param dataSource {@link DataSource}
     */
    default void addDataSourceOfName(final String name, final DataSourceNodeType nodeType,
                                     final DataSource dataSource) {
        this.addDataSource(null, name, nodeType, dataSource);
    }

    /**
     * 添加数据源
     * @param group      所属组
     * @param nodeType   {@link DataSourceNodeType}
     * @param dataSource {@link DataSource}
     */
    default void addDataSourceOfGroup(final String group, final DataSourceNodeType nodeType,
                                      final DataSource dataSource) {
        this.addDataSource(group, null, nodeType, dataSource);
    }

    /**
     * 添加数据源
     * @param nodeType   {@link DataSourceNodeType}
     * @param dataSource {@link DataSource}
     */
    default void addDataSource(final DataSourceNodeType nodeType, final DataSource dataSource) {
        this.addDataSource(null, null, nodeType, dataSource);
    }

    /**
     * 添加数据源
     * @param group      所属组
     * @param name       节点名称
     * @param nodeType   {@link DataSourceNodeType}
     * @param dataSource {@link DataSource}
     */
    void addDataSource(final String group, final String name,
                       final DataSourceNodeType nodeType, final DataSource dataSource);

}
