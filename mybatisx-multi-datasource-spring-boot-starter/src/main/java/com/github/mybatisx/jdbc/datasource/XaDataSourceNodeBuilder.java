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
import com.github.mybatisx.Strings;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.ConfigNode;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.DataSourceProperty;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.DataSourcePropertyNode;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.Xa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * XA数据源节点构建器
 * @author wvkity
 * @created 2021-08-15
 * @since 1.0.0
 */
public class XaDataSourceNodeBuilder implements DataSourceNodeBuilder<DataSource> {

    private static final Logger log = LoggerFactory.getLogger(XaDataSourceNodeBuilder.class);
    private final AtomicInteger defSequence = new AtomicInteger(0);
    private final XADataSourceWrapper xaDataSourceWrapper;
    private final List<DataSourcePropertyNode> propertyNodes;

    public XaDataSourceNodeBuilder(XADataSourceWrapper xaDataSourceWrapper,
                                   List<DataSourcePropertyNode> propertyNodes) {
        this.xaDataSourceWrapper = xaDataSourceWrapper;
        this.propertyNodes = propertyNodes;
    }

    @Override
    public List<DataSourceNode<DataSource>> build() {
        if (Objects.nonNull(this.xaDataSourceWrapper) && Objects.isNotNullElement(this.propertyNodes)) {
            final List<DataSourceNode<DataSource>> dataSourceNodes = new ArrayList<>(this.propertyNodes.size());
            try {
                for (DataSourcePropertyNode it : this.propertyNodes) {
                    final ConfigNode cn = it.getConfigNode();
                    final DataSourceProperty property = it.getProperty();
                    final DataSource dataSource = this.createDataSource(cn, it.getProperty());
                    if (Objects.nonNull(dataSource)) {
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

    protected DataSource createDataSource(final ConfigNode cn, final DataSourceProperty property) throws Exception {
        final Xa xa = property.getXa();
        String className = null;
        Map<String, String> properties = null;
        if (Objects.nonNull(xa)) {
            className = xa.getDataSourceClassName();
            properties = xa.getProperties();
        }
        if (Objects.isBlank(className)) {
            className = DatabaseDriver.fromJdbcUrl(property.getUrl()).getXaDataSourceClassName();
        }
        Assert.state(Objects.isNotBlank(className), "No XA DataSource class name specified");
        final XADataSource xaDataSource = this.createXaDataSourceInstance(property, className);
        this.bindXaProperties(xaDataSource, property, properties);
        return bindProperties(this.setUniqueName(this.xaDataSourceWrapper.wrapDataSource(xaDataSource), property,
            className), property);
    }

    private DataSource bindProperties(final DataSource dataSource, final DataSourceProperty property) {
        final Map<String, String> poolConfig = property.getPoolConfig();
        if (Objects.isNotEmpty(poolConfig)) {
            final Map<String, String> properties = new LinkedHashMap<>(poolConfig);
            MapConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
            new Binder(source).bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
        }
        return dataSource;
    }

    private DataSource setUniqueName(final DataSource dataSource, final DataSourceProperty property,
                                     final String className) {
        try {
            final Method method = dataSource.getClass().getMethod("setUniqueResourceName", String.class);
            method.setAccessible(true);
            final String name = property.getName();
            final String group = property.getGroup();
            final StringBuilder builder = new StringBuilder(30);
            builder.append(className.substring(className.lastIndexOf(Constants.DOT) + 1)).append("_");
            if (Objects.isNotBlank(group)) {
                builder.append(group).append("_");
            }
            if (Objects.isNotBlank(name)) {
                builder.append(name);
            } else {
                builder.append("xa").append("_").append(defSequence.incrementAndGet());
            }
            final String uniqueName = Strings.underscoreToLowerCamelCase(builder.toString());
            method.invoke(dataSource, uniqueName);
        } catch (Exception ignore) {
            // ignore
        }
        return dataSource;
    }

    private void bindXaProperties(final XADataSource target, final DataSourceProperty property,
                                  Map<String, String> properties) {
        final Map<String, String> config = new LinkedHashMap<>();
        if (Objects.isNotEmpty(properties)) {
            config.putAll(properties);
        }
        final Map<String, String> poolConfig = property.getPoolConfig();
        if (Objects.isNotEmpty(poolConfig)) {
            for (Map.Entry<String, String> entry : poolConfig.entrySet()) {
                config.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
        config.computeIfAbsent("user", k -> property.getUsername());
        config.computeIfAbsent("password", k -> property.getPassword());
        config.computeIfAbsent("url", k -> property.getUrl());
        MapConfigurationPropertySource source = new MapConfigurationPropertySource(config);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("user", "username");
        new Binder(source.withAliases(aliases)).bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(target));
    }

    private XADataSource createXaDataSourceInstance(final DataSourceProperty property, final String className) {
        try {
            Class<?> dataSourceClass = ClassUtils.forName(className, property.getClassLoader());
            Object instance = BeanUtils.instantiateClass(dataSourceClass);
            Assert.isInstanceOf(XADataSource.class, instance);
            return (XADataSource) instance;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create XADataSource instance from '" + className + "'");
        }
    }

}
