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

import com.github.mybatisx.jdbc.datasource.DataSourceNodeType;

/**
 * 数据源配置节点
 * @author wvkity
 * @created 2021-08-08
 * @since 1.0.0
 */
public class DataSourcePropertyNode {

    private String key;
    private String group;
    private String name;
    private DataSourceNodeType type;
    private DataSourceProperty property;
    private ConfigNode configNode;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public DataSourceNodeType getType() {
        return type;
    }

    public void setType(DataSourceNodeType type) {
        this.type = type;
    }

    public DataSourceProperty getProperty() {
        return property;
    }

    public void setProperty(DataSourceProperty property) {
        this.property = property;
    }

    public ConfigNode getConfigNode() {
        return configNode;
    }

    public void setConfigNode(ConfigNode configNode) {
        this.configNode = configNode;
    }
}
