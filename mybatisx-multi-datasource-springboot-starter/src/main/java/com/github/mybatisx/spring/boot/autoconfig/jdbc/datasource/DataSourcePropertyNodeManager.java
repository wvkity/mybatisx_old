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

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.immutable.ImmutableList;
import com.github.mybatisx.jdbc.datasource.DataSourceNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据源配置节点管理器
 * @author wvkity
 * @created 2021-08-08
 * @since 1.0.0
 */
public class DataSourcePropertyNodeManager {

    private static final Logger log = LoggerFactory.getLogger(DataSourcePropertyNodeManager.class);
    private static final String DOT = Constants.DOT;
    private static final Map<String, DataSourcePropertyNodeManager> INSTANCE_CACHE = new ConcurrentReferenceHashMap<>();
    private final List<DataSourcePropertyNode> dataSourcePropertyNodes = new ArrayList<>();
    private final MultiDataSourceProperties properties;
    private final AtomicBoolean parsed = new AtomicBoolean(false);
    private final MultiDataSourceEnvKey sourceEnvKey;

    private DataSourcePropertyNodeManager(ApplicationContext context, MultiDataSourceProperties properties) {
        this.properties = properties;
        final Environment env = context.getEnvironment();
        this.sourceEnvKey = new MultiDataSourceEnvKey(env);
        this.sourceEnvKey.parse();
    }

    public void parse() {
        if (!this.parsed.get()) {
            try {
                this.parsed.set(true);
                String nodeKeyPrefix = MultiDataSourceProperties.CFG_PREFIX;
                final ConfigNode rootNode = new ConfigNode(this.sourceEnvKey);
                rootNode.setNodeKeyPrefix(nodeKeyPrefix);
                rootNode.parse(this.properties);
                final List<DataSourcePropertyNode> nodes = new ArrayList<>();
                if (Objects.nonNull(this.properties.getMaster())) {
                    final List<DataSourcePropertyNode> list = this.writeParse(rootNode, this.properties.getMaster());
                    if (Objects.isNotEmpty(list)) {
                        nodes.addAll(list);
                    }
                }
                if (Objects.nonNull(this.properties.getSlave())) {
                    final List<DataSourcePropertyNode> list = this.readParse(rootNode, this.properties.getSlave());
                    if (Objects.isNotEmpty(list)) {
                        nodes.addAll(list);
                    }
                }
                final GroupedDataSourceProperties dataSourceGroups = this.properties.getGroup();
                if (Objects.nonNull(dataSourceGroups)) {
                    nodeKeyPrefix = nodeKeyPrefix + DOT + "group";
                    final List<GroupingDataSourceProperties> groupsList = dataSourceGroups.getList();
                    if (Objects.isNotEmpty(groupsList)) {
                        final int size = groupsList.size();
                        for (int i = 0; i < size; i++) {
                            final String nodeKey = nodeKeyPrefix + DOT + "list[" + i + "]";
                            rootNode.setNodeKeyPrefix(nodeKey);
                            final List<DataSourcePropertyNode> list = this.parse(rootNode, groupsList.get(i));
                            if (Objects.isNotEmpty(list)) {
                                nodes.addAll(list);
                            }
                        }
                    }
                    final Map<String, GroupingDataSourceProperties> groupsMap = dataSourceGroups.getMap();
                    if (Objects.isNotEmpty(groupsMap)) {
                        for (Map.Entry<String, GroupingDataSourceProperties> entry : groupsMap.entrySet()) {
                            final String nodeKey = nodeKeyPrefix + DOT + "map" + DOT + entry.getKey();
                            final String group = entry.getKey();
                            rootNode.setGroup(group);
                            rootNode.setNodeKeyPrefix(nodeKey);
                            final List<DataSourcePropertyNode> list = this.parse(rootNode, entry.getValue());
                            if (Objects.isNotEmpty(list)) {
                                nodes.addAll(list);
                            }
                        }
                    }
                }
                if (Objects.isNotEmpty(nodes)) {
                    this.dataSourcePropertyNodes.addAll(nodes);
                }
            } catch (Exception e) {
                log.error("The data source configuration fails to be resolved: ", e);
            }
        }
    }

    List<DataSourcePropertyNode> parse(final ConfigNode parent, final GroupingDataSourceProperties properties) {
        if (Objects.nonNull(properties)) {
            if (Objects.nonNull(properties.getMaster()) || Objects.nonNull(properties.getSlave())) {
                final ConfigNode it = new ConfigNode(parent.getSourceEnvKey());
                it.setNodeKeyPrefix(parent.getNodeKeyPrefix());
                it.compare(parent, properties);
                final List<DataSourcePropertyNode> nodes = new ArrayList<>();
                List<DataSourcePropertyNode> list = this.writeParse(it, properties.getMaster());
                if (Objects.isNotEmpty(list)) {
                    nodes.addAll(list);
                }
                list = this.readParse(it, properties.getSlave());
                if (Objects.isNotEmpty(list)) {
                    nodes.addAll(list);
                }
                return nodes;
            }
        }
        return null;
    }

    List<DataSourcePropertyNode> writeParse(final ConfigNode parent, final WriteDataSourceProperties master) {
        if (Objects.nonNull(master)) {
            return this.parseList(parent, master, DataSourceNodeType.MASTER, master.getList(), master.getMap());
        }
        return null;
    }

    List<DataSourcePropertyNode> readParse(final ConfigNode parent, final ReadDataSourceProperties slave) {
        if (Objects.nonNull(slave)) {
            return this.parseList(parent, slave, DataSourceNodeType.SLAVE, slave.getList(), slave.getMap());
        }
        return null;
    }

    List<DataSourcePropertyNode> parseList(final ConfigNode parent, final GroupableDataSourceProperty property,
                                           final DataSourceNodeType nodeType,
                                           final List<DataSourceProperty> propertyList,
                                           final Map<String, DataSourceProperty> propertyMap) {
        if (Objects.isNotEmpty(propertyList) || Objects.isNotEmpty(propertyMap)) {
            final ConfigNode it = new ConfigNode(parent.getSourceEnvKey());
            final String nodeKey = parent.getNodeKeyPrefix() + DOT + nodeType.getType().toLowerCase(Locale.ENGLISH);
            it.setNodeKeyPrefix(nodeKey);
            it.setNodeType(nodeType);
            it.compare(parent, property);
            final List<DataSourcePropertyNode> nodes = new ArrayList<>();
            List<DataSourcePropertyNode> list = this.parseList(it, propertyList);
            if (Objects.isNotEmpty(list)) {
                nodes.addAll(list);
            }
            list = this.parseList(it, propertyMap);
            if (Objects.isNotEmpty(list)) {
                nodes.addAll(list);
            }
            return nodes;
        }
        return null;
    }

    List<DataSourcePropertyNode> parseList(final ConfigNode parent,
                                           final List<DataSourceProperty> dataSourceProperties) {
        if (Objects.isNotEmpty(dataSourceProperties)) {
            final int size = dataSourceProperties.size();
            final List<DataSourcePropertyNode> nodes = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final String nodeKey = parent.getNodeKeyPrefix() + DOT + "list[" + i + "]";
                final DataSourcePropertyNode node = this.parse(parent, dataSourceProperties.get(i), nodeKey, "");
                if (Objects.nonNull(node)) {
                    nodes.add(node);
                }
            }
            return nodes;
        }
        return null;
    }

    List<DataSourcePropertyNode> parseList(final ConfigNode parent,
                                           final Map<String, DataSourceProperty> dataSourceProperties) {
        if (Objects.isNotEmpty(dataSourceProperties)) {
            final int size = dataSourceProperties.size();
            final List<DataSourcePropertyNode> nodes = new ArrayList<>(size);
            for (Map.Entry<String, DataSourceProperty> entry : dataSourceProperties.entrySet()) {
                final String nodeKey = parent.getNodeKeyPrefix() + DOT + "map" + DOT + entry.getKey();
                final DataSourcePropertyNode node = this.parse(parent, entry.getValue(), nodeKey, entry.getKey());
                if (Objects.nonNull(node)) {
                    nodes.add(node);
                }
            }
            return nodes;
        }
        return null;
    }

    DataSourcePropertyNode parse(final ConfigNode parent, DataSourceProperty property, final String nodeKeyPrefix,
                                 final String name) {
        if (Objects.nonNull(property)) {
            final ConfigNode it = new ConfigNode(parent.getSourceEnvKey());
            it.setNodeKeyPrefix(nodeKeyPrefix);
            it.setName(name);
            it.compareAndSet(parent, property);
            final DataSourcePropertyNode node = new DataSourcePropertyNode();
            node.setProperty(property);
            node.setName(property.getName());
            node.setGroup(property.getGroup());
            node.setKey(it.getNodeKeyPrefix());
            node.setType(it.getNodeType());
            node.setConfigNode(it);
            return node;
        }
        return null;
    }

    public List<DataSourcePropertyNode> getNodes() {
        return ImmutableList.of(this.dataSourcePropertyNodes);
    }

    //// static methods ////

    public static DataSourcePropertyNodeManager of(final ApplicationContext context,
                                                   final MultiDataSourceProperties properties) {
        final String key = properties.toString();
        if (INSTANCE_CACHE.containsKey(key)) {
            return INSTANCE_CACHE.get(key);
        }
        final DataSourcePropertyNodeManager it = new DataSourcePropertyNodeManager(context, properties);
        return Optional.ofNullable(INSTANCE_CACHE.putIfAbsent(key, it)).orElse(it);
    }
}
