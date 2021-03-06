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
import io.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.ConfigNode;
import io.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.DataSourceProperty;
import io.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.DataSourcePropertyNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本地数据源节点构建器
 * @author wvkity
 * @created 2021-08-11
 * @since 1.0.0
 */
public class LocalDataSourceNodeBuilder implements DataSourceNodeBuilder<DataSource> {

    private static final Logger log = LoggerFactory.getLogger(LocalDataSourceNodeBuilder.class);
    private final List<DataSourcePropertyNode> propertyNodes;

    public LocalDataSourceNodeBuilder(List<DataSourcePropertyNode> propertyNodes) {
        this.propertyNodes = propertyNodes;
    }

    @SuppressWarnings("unchecked")
    protected <T> T createDataSource(final DataSourceProperty property, Class<? extends DataSource> type) {
        return (T) property.initializeDataSourceBuilder().type(type).build();
    }

    @Override
    public List<DataSourceNode<DataSource>> build() {
        if (Objects.isNotNullElement(this.propertyNodes)) {
            final List<DataSourceNode<DataSource>> dataSourceNodes = new ArrayList<>(this.propertyNodes.size());
            try {
                for (DataSourcePropertyNode it : this.propertyNodes) {
                    final ConfigNode cn = it.getConfigNode();
                    final DataSourceProperty property = it.getProperty();
                    final DataSource dataSource = this.createDataSource(it.getProperty(), property.getType());
                    if (Objects.nonNull(dataSource)) {
                        final Map<String, String> poolConfig = property.getPoolConfig();
                        if (Objects.isNotEmpty(poolConfig)) {
                            final ConfigurationPropertySource source = new MapConfigurationPropertySource(poolConfig);
                            ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
                            aliases.addAliases("pool-name", "name");
                            final Binder binder = new Binder(source.withAliases(aliases));
                            binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
                        }
                        final DataSourceNode<DataSource> node = new GenericDataSourceNode(property.getGroup(),
                            property.getName(), cn.getNodeType(), property.getSchema(), property.getData(),
                            property.getSeparator(), dataSource);
                        dataSourceNodes.add(node);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to create the data source node: " + e.getMessage(), e);
            }
            return dataSourceNodes;
        }
        return null;
    }

}
