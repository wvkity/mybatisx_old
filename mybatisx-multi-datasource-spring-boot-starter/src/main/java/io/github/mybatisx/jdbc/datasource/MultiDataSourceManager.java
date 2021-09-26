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

import io.github.mybatisx.Objects;
import io.github.mybatisx.immutable.ImmutableList;
import io.github.mybatisx.jdbc.datasource.exception.MultiDataSourceException;
import io.github.mybatisx.jdbc.datasource.policy.DataSourcePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多数据源管理器
 * @author wvkity
 * @created 2021-08-11
 * @since 1.0.0
 */
public class MultiDataSourceManager implements DataSourceManager, InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceManager.class);
    /**
     * 默认组名
     */
    private static final String DEF_GROUP = "default";
    /**
     * 数据源选择策略
     */
    private final DataSourcePolicy dataSourcePolicy;
    /**
     * 数据源节点构建器
     */
    private final DataSourceNodeBuilder<DataSource> dataSourceNodeBuilder;
    /**
     * 数据源节点
     */
    private List<DataSourceNode<DataSource>> dataSourceNodes;
    /**
     * 锁
     */
    private final Lock lock = new ReentrantLock();
    /**
     * 分组+节点类型+节点名称数据源
     */
    private final Map<String, Map<DataSourceNodeType, Map<String, DataSource>>> groupNodeDataSourceCache =
        new ConcurrentHashMap<>();
    /**
     * 分组+节点类型数据源
     */
    private final Map<String, Map<DataSourceNodeType, List<DataSource>>> groupDataSourceCache =
        new ConcurrentHashMap<>();
    /**
     * 节点类型+节点名称数据源
     */
    private final Map<DataSourceNodeType, Map<String, DataSource>> nodeDataSourceCache = new ConcurrentHashMap<>();
    /**
     * 所有主库数据源
     */
    private final List<DataSource> allWriteDataSourceCache = new CopyOnWriteArrayList<>();
    /**
     * 所有读库数据源
     */
    private final List<DataSource> allReadDataSourceCache = new CopyOnWriteArrayList<>();

    public MultiDataSourceManager(DataSourcePolicy policy, DataSourceNodeBuilder<DataSource> builder) {
        this.dataSourceNodeBuilder = builder;
        this.dataSourcePolicy = policy;
    }

    private void creating() {
        if (Objects.isNotEmpty(this.dataSourceNodes)) {
            for (DataSourceNode<DataSource> node : this.dataSourceNodes) {
                final String group = node.getGroup();
                final String name = node.getName();
                final DataSourceNodeType nodeType = node.getNodeType();
                final DataSource dataSource = node.getTarget();
                this.add(group, name, nodeType, dataSource);
            }
        }
    }

    @Override
    public DataSource determine(String group, String name, DataSourceNodeType nodeType) {
        if (Objects.isBlank(group)) {
            group = DEF_GROUP;
        }
        final boolean hasName = Objects.isNotBlank(name);
        log.debug("determine dataSource: nodeType({}), group({}), node({})", nodeType, group, hasName ? name : "\"\"");
        if (hasName) {
            return this.smart(group, name, nodeType);
        } else {
            return this.groupSmart(group, nodeType);
        }
    }

    private DataSource smart(final String group, final String name, final DataSourceNodeType nodeType) {
        final Map<DataSourceNodeType, Map<String, DataSource>> dsc = this.groupNodeDataSourceCache.get(group);
        if (Objects.nonNull(dsc)) {
            final Map<String, DataSource> nsc = dsc.get(nodeType);
            if (Objects.nonNull(nsc) && nsc.containsKey(name)) {
                return nsc.get(name);
            }
        }
        throw new MultiDataSourceException("Failed to find the corresponding data source based on 'nodeType: " +
            nodeType.getType() + ", group: " + group + ", node: " + name + "'");
    }

    private DataSource groupSmart(final String group, final DataSourceNodeType nodeType) {
        final Map<DataSourceNodeType, List<DataSource>> dsc = this.groupDataSourceCache.get(group);
        if (Objects.isNotEmpty(dsc)) {
            final List<DataSource> dataSources = dsc.get(nodeType);
            if (Objects.isNotEmpty(dataSources)) {
                return this.dataSourcePolicy.determine(dataSources);
            }
        }
        throw new MultiDataSourceException("Failed to find the corresponding data source based on 'nodeType: " +
            nodeType.getType() + ", group: " + group + "'");
    }

    private DataSource nameSmart(final String name, final DataSourceNodeType nodeType) {
        final Map<String, DataSource> nds = this.nodeDataSourceCache.get(nodeType);
        if (Objects.isNotEmpty(nds) && nds.containsKey(name)) {
            return nds.get(name);
        }
        throw new MultiDataSourceException("Failed to find the corresponding data source based on '" +
            "nodeType: " + nodeType.getType() + ", node: " + name + "'");
    }

    @Override
    public void addDataSource(String group, String name, DataSourceNodeType nodeType, DataSource dataSource) {
        final Lock curLock = this.lock;
        if (curLock.tryLock()) {
            try {
                this.add(group, name, nodeType, dataSource);
            } finally {
                curLock.unlock();
            }
        }
    }

    private void add(String group, String name, DataSourceNodeType nodeType, DataSource dataSource) {
        if (Objects.nonNull(nodeType) && Objects.nonNull(dataSource)) {
            if (Objects.isBlank(group)) {
                group = DEF_GROUP;
            }
            // 分组 + 节点类型
            final boolean hasName = Objects.isNotBlank(name);
            final Map<DataSourceNodeType, List<DataSource>> dsc =
                this.groupDataSourceCache.computeIfAbsent(group, k -> new ConcurrentHashMap<>(16));
            dsc.computeIfAbsent(nodeType, k -> new CopyOnWriteArrayList<>()).add(dataSource);
            if (hasName) {
                // 节点类型 + 节点名称
                final Map<String, DataSource> nds =
                    this.nodeDataSourceCache.computeIfAbsent(nodeType, k -> new ConcurrentHashMap<>(16));
                nds.putIfAbsent(name, dataSource);
                // 分组 + 节点类型 + 节点名称
                final Map<DataSourceNodeType, Map<String, DataSource>> gns =
                    this.groupNodeDataSourceCache.computeIfAbsent(group, k -> new ConcurrentHashMap<>(16));
                final Map<String, DataSource> nsc =
                    gns.computeIfAbsent(nodeType, k -> new ConcurrentHashMap<>(16));
                nsc.putIfAbsent(name, dataSource);
            }
            if (nodeType == DataSourceNodeType.MASTER) {
                this.allWriteDataSourceCache.add(dataSource);
            } else {
                this.allReadDataSourceCache.add(dataSource);
            }
            log.debug("Adding data source: {}, nodeType: {}, group: {}, node: {}", dataSource,
                nodeType.getType(), group, hasName ? name : "\"\"");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dataSourceNodes = Objects.isNull(this.dataSourceNodeBuilder) ? ImmutableList.of() :
            ImmutableList.of(this.dataSourceNodeBuilder.build());
        this.creating();
    }

    @Override
    public void destroy() throws Exception {
        for (DataSource it : this.allWriteDataSourceCache) {
            this.close(it);
        }
        for (DataSource it : this.allReadDataSourceCache) {
            this.close(it);
        }
    }

    private void close(final DataSource dataSource) throws IllegalAccessException,
        InvocationTargetException {
        try {
            final Method method = dataSource.getClass().getMethod("close");
            method.setAccessible(true);
            method.invoke(dataSource);
        } catch (NoSuchMethodException e) {
            log.warn("Data source shutdown failure: ", e);
        }
    }

    public List<DataSourceNode<DataSource>> getDataSourceNodes() {
        return dataSourceNodes;
    }
}
