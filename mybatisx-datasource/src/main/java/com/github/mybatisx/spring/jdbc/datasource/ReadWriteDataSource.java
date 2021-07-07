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
package com.github.mybatisx.spring.jdbc.datasource;

import com.github.mybatisx.spring.jdbc.datasource.strategy.DataSourceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 读写数据源
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
public class ReadWriteDataSource extends AbstractReadWriteDataSource implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ReadWriteDataSource.class);
    /**
     * 主库节点前缀
     */
    public static final String MASTER_NODE_PREFIX = "master_node_";
    /**
     * 从库节点前缀
     */
    public static final String SLAVE_NODE_PREFIX = "slave_node_";
    // master
    /**
     * 写数据源集合
     */
    private Map<String, DataSource> writeDataSourceMap;
    /**
     * 写数据源对象数组
     */
    private List<DataSource> writeDataSources;
    // slave
    /**
     * 读数据源集合
     */
    private Map<String, DataSource> readDataSourceMap;
    /**
     * 读数据源对象数组
     */
    private List<DataSource> readDataSources;
    /**
     * 启用读写分离
     */
    private boolean enableReadWriteSeparation = true;
    /**
     * 数据源策略
     */
    private DataSourceStrategy dataSourceStrategy;

    @Override
    protected DataSource determineDataSource() {
        if (ReadWriteDataSourceContextHolder.isWrite() || ReadWriteDataSourceContextHolder.isNone()
            || !enableReadWriteSeparation) {
            return determineWriteDataSource();
        }
        return determineReadDataSource();
    }

    private DataSource determineWriteDataSource() {
        return this.dataSourceStrategy.determineDataSource(this.writeDataSources);
    }

    private DataSource determineReadDataSource() {
        return this.dataSourceStrategy.determineDataSource(this.readDataSources);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(this.writeDataSourceMap)) {
            throw new IllegalArgumentException("property 'writeDataSourceMap' is required");
        }
        if (this.enableReadWriteSeparation && CollectionUtils.isEmpty(this.readDataSourceMap)) {
            throw new IllegalArgumentException("property 'readDataSourceMap' is required");
        }
        this.writeDataSources = new CopyOnWriteArrayList<>();
        for (Map.Entry<String, DataSource> entry : this.writeDataSourceMap.entrySet()) {
            this.writeDataSources.add(entry.getValue());
        }
        if (this.enableReadWriteSeparation) {
            this.readDataSources = new CopyOnWriteArrayList<>();
            for (Map.Entry<String, DataSource> entry : this.readDataSourceMap.entrySet()) {
                this.readDataSources.add(entry.getValue());
            }
        }
    }

    public void setWriteDataSourceMap(final List<DataSource> writeDataSources) {
        if (!CollectionUtils.isEmpty(writeDataSources) && CollectionUtils.isEmpty(this.writeDataSourceMap)) {
            final Map<String, DataSource> it = new LinkedHashMap<>();
            for (int i = 0, size = writeDataSources.size(); i < size; i++) {
                it.put((MASTER_NODE_PREFIX + (i + 1)), writeDataSources.get(i));
            }
            setWriteDataSourceMap(it);
        }
    }

    public void setWriteDataSourceMap(final Map<String, DataSource> writeDataSourceMap) {
        this.writeDataSourceMap = writeDataSourceMap;
    }

    public void setReadDataSourceMap(final List<DataSource> readDataSources) {
        if (!CollectionUtils.isEmpty(readDataSources) && CollectionUtils.isEmpty(this.writeDataSourceMap)) {
            final Map<String, DataSource> it = new LinkedHashMap<>();
            for (int i = 0, size = readDataSources.size(); i < size; i++) {
                it.put(SLAVE_NODE_PREFIX + (i + 1), readDataSources.get(i));
            }
            setReadDataSourceMap(it);
        }
    }

    public void setReadDataSourceMap(final Map<String, DataSource> readDataSourceMap) {
        this.readDataSourceMap = readDataSourceMap;
    }

    public void setEnableReadWriteSeparation(boolean enableReadWriteSeparation) {
        this.enableReadWriteSeparation = enableReadWriteSeparation;
    }

    public void setDataSourceStrategy(DataSourceStrategy dataSourceStrategy) {
        this.dataSourceStrategy = dataSourceStrategy;
    }
}
