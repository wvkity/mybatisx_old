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

import java.util.Objects;

/**
 * 线程数据源
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public class CurrentThreadDataSource implements LocalDataSource {

    private static final long serialVersionUID = 5538060480051675546L;
    private final DataSourceNodeType type;
    private final String group;
    private final String name;

    public CurrentThreadDataSource(DataSourceNodeType type, String group, String name) {
        this.type = type;
        this.group = group;
        this.name = name;
    }

    @Override
    public DataSourceNodeType getType() {
        return this.type;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrentThreadDataSource)) {
            return false;
        }
        CurrentThreadDataSource that = (CurrentThreadDataSource) o;
        return type == that.type && Objects.equals(group, that.group) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, group, name);
    }

    @Override
    public String toString() {
        return "CurrentThreadDataSource{" +
            "type=" + type +
            ", group='" + group + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

    public static CurrentThreadDataSource of(final DataSourceNodeType type, final String group, final String name) {
        Objects.requireNonNull(type, "The data source type cannot be null.");
        return new CurrentThreadDataSource(type, group, name);
    }
}
