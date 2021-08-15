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
import com.github.mybatisx.jdbc.datasource.DataSourceNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源配置节点
 * @author wvkity
 * @created 2021-08-10
 * @since 1.0.0
 */
public class ConfigNode {

    private static final Logger log = LoggerFactory.getLogger(ConfigNode.class);
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private ClassLoader classLoader;
    private String username;
    private String password;
    private Class<? extends DataSource> type;
    private String driverClassName;
    private String group = "";
    private String name = "";
    private String nodeKeyPrefix;
    private String poolAlias;
    private String poolSourceKey;
    private boolean hasConfigPoolAlias;
    private Properties fastPoolConfig;
    private Properties poolConfig;
    private boolean selfHasPoolConfig;
    private DataSourceNodeType nodeType;
    private final MultiDataSourceEnvKey sourceEnvKey;

    public ConfigNode(MultiDataSourceEnvKey sourceEnvKey) {
        this.sourceEnvKey = sourceEnvKey;
    }

    public void parse(final BasicDataSourceProperty property) {
        if (Objects.nonNull(property)) {
            this.classLoader = property.getClassLoader();
            this.username = property.getUsername();
            this.password = property.getPassword();
            this.type = property.getType();
            this.driverClassName = property.getDriverClassName();
            this.parsePoolAlias().parsePoolConfig(property);
        }
    }

    public void compare(final ConfigNode parent, final BasicDataSourceProperty property) {
        if (Objects.nonNull(parent) && Objects.nonNull(property)) {
            if (Objects.isNull(this.nodeType)) {
                this.nodeType = parent.getNodeType();
            }
            this.classLoader = this.ifMatch(property.getClassLoader(), parent.getClassLoader());
            this.username = this.ifMatch(property.getUsername(), parent.getUsername());
            this.password = this.ifMatch(property.getPassword(), parent.getPassword());
            this.type = this.ifMatch(property.getType(), parent.getType());
            this.driverClassName = this.ifMatch(property.getDriverClassName(), parent.getDriverClassName());
            this.parsePoolAlias().parsePoolConfig(property);
            if (Objects.isNull(this.poolConfig)) {
                this.poolConfig = parent.getPoolConfig();
            } else {
                this.merge(parent.getPoolConfig(), this.poolConfig);
            }
        }
    }

    public void compare(final ConfigNode parent, final GroupableDataSourceProperty property) {
        this.compare(parent, (BasicDataSourceProperty) property);
        if (Objects.nonNull(parent) && Objects.nonNull(property)) {
            this.group = this.ifMatch(property.getGroup(), parent.getGroup());
        }
    }

    public void compareAndSet(final ConfigNode parent, final DataSourceProperty property) {
        this.compare(parent, property);
        if (Objects.nonNull(parent) && Objects.nonNull(property)) {
            final PropertySet ps = new PropertySet(property);
            ps.set(DataSourceProperty::setClassLoader, property.getClassLoader(), this.classLoader)
                .set(DataSourceProperty::setUsername, property.getUsername(), this.username)
                .set(DataSourceProperty::setPassword, property.getPassword(), this.password)
                .set(DataSourceProperty::setType, property.getType(), this.type)
                .set(DataSourceProperty::setDriverClassName, property.getDriverClassName(), this.driverClassName)
                .set(DataSourceProperty::setGroup, property.getGroup(), this.group)
                .set(DataSourceProperty::setName, property.getName(), this.name);
            if (Objects.isNull(property.getPoolConfig())) {
                property.setPoolConfig(new Properties());
            }
            final Properties poolConfig = property.getPoolConfig();
            if (!this.selfHasPoolConfig) {
                this.merge(this.poolConfig, poolConfig);
            }
            if (Objects.isBlank(property.getPoolName())) {
                final String type = property.getType().getName();
                final String prefix = type.substring(type.lastIndexOf(Constants.DOT) + 1);
                final StringBuilder builder = new StringBuilder(30);
                final boolean hasName = Objects.isNotBlank(property.getName());
                builder.append(prefix);
                if (Objects.isNotBlank(property.getGroup())) {
                    builder.append("-").append(property.getGroup());
                }
                if (hasName) {
                    builder.append("-").append(property.getName());
                } else {
                    builder.append("-").append(COUNTER.incrementAndGet());
                }
                poolConfig.setProperty("pool-name", builder.toString());
            } else {
                poolConfig.setProperty("pool-name", property.getPoolName());
            }
        }
    }

    public void clone(final ConfigNode other) {
        this.classLoader = other.getClassLoader();
        this.username = other.getUsername();
        this.password = other.getPassword();
        this.driverClassName = other.getDriverClassName();
        this.type = other.getType();
        this.group = other.getGroup();
        this.name = other.getName();
        this.nodeKeyPrefix = other.getNodeKeyPrefix();
        this.poolConfig = other.getPoolConfig();
        this.fastPoolConfig = other.getFastPoolConfig();
    }

    public ConfigNode parsePoolAlias() {
        final String alias = DataSourcePoolAlias.getAlias(this.type);
        if (Objects.isNotBlank(alias)) {
            this.poolAlias = alias;
            this.poolSourceKey = this.nodeKeyPrefix + Constants.DOT + alias;
            this.hasConfigPoolAlias = this.sourceEnvKey.containsKey(this.poolSourceKey);
        }
        return this;
    }

    void parsePoolConfig(final BasicDataSourceProperty property) {
        this.fastPoolConfig = property.getPoolConfig();
        this.selfHasPoolConfig = this.fastPoolConfig != null;
        if (Objects.nonNull(this.sourceEnvKey) && this.hasConfigPoolAlias) {
            try {
                final Binder binder = Binder.get(this.sourceEnvKey.getEnvironment());
                this.poolConfig = binder.bind(this.poolSourceKey, Bindable.of(Properties.class)).get();
            } catch (Exception e) {
                log.warn("The data source connection pool configuration fails to be resolved: ", e);
            }
        }
        if (Objects.isNull(this.poolConfig)) {
            this.poolConfig = this.fastPoolConfig;
        } else {
            this.merge(this.fastPoolConfig, this.poolConfig);
        }
    }

    void merge(final Properties desc, final Properties target) {
        if (Objects.nonNull(desc) && Objects.nonNull(target)) {
            for (Map.Entry<?, ?> entry : desc.entrySet()) {
                target.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    String ifMatch(final String value, final String defaultValue) {
        if (Objects.isNotBlank(value)) {
            return value;
        }
        return defaultValue;
    }

    <V> V ifMatch(final V value, final V defaultValue) {
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeKeyPrefix() {
        return nodeKeyPrefix;
    }

    public void setNodeKeyPrefix(String nodeKeyPrefix) {
        this.nodeKeyPrefix = nodeKeyPrefix;
    }

    public String getPoolAlias() {
        return poolAlias;
    }

    public String getPoolSourceKey() {
        return poolSourceKey;
    }

    public Properties getFastPoolConfig() {
        return fastPoolConfig;
    }

    public Properties getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(Properties poolConfig) {
        this.poolConfig = poolConfig;
    }

    public boolean isSelfHasPoolConfig() {
        return selfHasPoolConfig;
    }

    public boolean isHasConfigPoolAlias() {
        return hasConfigPoolAlias;
    }

    public DataSourceNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(DataSourceNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public MultiDataSourceEnvKey getSourceEnvKey() {
        return sourceEnvKey;
    }

    private interface SetConsumer<T, V> {
        /**
         * 消费
         * @param target 目标对象
         * @param value  值
         */
        void accept(final T target, final V value);
    }

    private class PropertySet {
        final DataSourceProperty property;

        public PropertySet(DataSourceProperty property) {
            this.property = property;
        }

        @SuppressWarnings("unchecked")
        <V> PropertySet set(final SetConsumer<DataSourceProperty, V> action, final V value, final V defaultValue) {
            if (value instanceof String) {
                action.accept(this.property, (V) ifMatch((String) value, (String) defaultValue));
            } else {
                action.accept(this.property, Objects.isNull(value) ? defaultValue : value);
            }
            return this;
        }
    }

}
