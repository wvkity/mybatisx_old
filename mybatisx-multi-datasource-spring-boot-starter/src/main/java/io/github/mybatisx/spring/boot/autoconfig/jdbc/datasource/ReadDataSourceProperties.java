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
package io.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读库数据源配置
 * @author wvkity
 * @created 2021-08-06
 * @since 1.0.0
 */
public class ReadDataSourceProperties extends GroupableDataSourceProperty {

    /**
     * 数据源列表
     */
    private List<DataSourceProperty> list = new ArrayList<>();
    /**
     * 数据源列表
     */
    private Map<String, DataSourceProperty> map = new HashMap<>();

    public List<DataSourceProperty> getList() {
        return list;
    }

    public void setList(List<DataSourceProperty> list) {
        this.list = list;
    }

    public Map<String, DataSourceProperty> getMap() {
        return map;
    }

    public void setMap(Map<String, DataSourceProperty> map) {
        this.map = map;
    }
}
