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

import com.github.mybatisx.Objects;

/**
 * 读写数据源切换决策工具
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public class MultiDataSourceContextHolder {

    private static final ThreadLocal<LocalDataSource> DATA_SOURCE_LOOKUP = new ThreadLocal<>();

    /**
     * 是否选择读库
     * @return boolean
     */
    public static boolean isChoiceRead() {
        final LocalDataSource it = MultiDataSourceContextHolder.get();
        return Objects.nonNull(it) && it.isRead();
    }

    /**
     * 是否选择主库
     * @return boolean
     */
    public static boolean isChoiceWrite() {
        final LocalDataSource it = MultiDataSourceContextHolder.get();
        return Objects.nonNull(it) && it.isWrite();
    }

    /**
     * 获取当前线程数据源
     * @return {@link LocalDataSource}
     */
    public static LocalDataSource get() {
        return DATA_SOURCE_LOOKUP.get();
    }

    /**
     * 选择写数据源
     */
    public static void makeWrite() {
        MultiDataSourceContextHolder.makeWrite("");
    }

    /**
     * 选择写数据源
     * @param name 节点名称
     */
    public static void makeWrite(final String name) {
        MultiDataSourceContextHolder.makeWrite("", name);
    }

    /**
     * 选择写数据源
     * @param group 所属分组
     * @param name  节点名称
     */
    public static void makeWrite(final String group, final String name) {
        MultiDataSourceContextHolder.set(DataSourceNodeType.SLAVE, group, name);
    }

    /**
     * 选择只读数据源
     */
    public static void makeRead() {
        MultiDataSourceContextHolder.makeRead("");
    }

    /**
     * 选择只读数据源
     * @param name 节点名称
     */
    public static void makeRead(final String name) {
        MultiDataSourceContextHolder.makeRead("", name);
    }

    /**
     * 选择只读数据源
     * @param group 所属分组
     * @param name  节点名称
     */
    public static void makeRead(final String group, final String name) {
        MultiDataSourceContextHolder.set(DataSourceNodeType.SLAVE, group, name);
    }

    /**
     * 添加当前线程数据源
     * @param type  数据源类型
     * @param group 所属分组
     * @param name  节点名称
     */
    public static void set(final DataSourceNodeType type, final String group, final String name) {
        MultiDataSourceContextHolder.set(CurrentThreadDataSource.of(type, group, name));
    }

    /**
     * 添加当前线程数据源
     * @param dataSource {@link LocalDataSource}
     */
    public static void set(final LocalDataSource dataSource) {
        if (Objects.nonNull(dataSource)) {
            DATA_SOURCE_LOOKUP.set(dataSource);
        }
    }

    /**
     * 移除当前线程数据源
     */
    public static void remove() {
        MultiDataSourceContextHolder.clear();
    }

    /**
     * 清空本地线程
     */
    public static void clear() {
        DATA_SOURCE_LOOKUP.remove();
    }

}
