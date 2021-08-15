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
 * 通用数据源节点
 * @author wvkity
 * @created 2021-08-11
 * @since 1.0.0
 */
public class GenericDataSourceNode implements DataSourceNode<DataSource> {

    private final String group;
    private final String name;
    private final DataSourceNodeType nodeType;
    private final String schema;
    private final String data;
    private final String separator;
    private final DataSource target;

    public GenericDataSourceNode(String group, String name, DataSourceNodeType nodeType, String schema, String data,
                                 String separator, DataSource target) {
        this.group = group;
        this.name = name;
        this.nodeType = nodeType;
        this.schema = schema;
        this.data = data;
        this.separator = separator;
        this.target = target;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DataSourceNodeType getNodeType() {
        return this.nodeType;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    @Override
    public DataSource getTarget() {
        return this.target;
    }
}
