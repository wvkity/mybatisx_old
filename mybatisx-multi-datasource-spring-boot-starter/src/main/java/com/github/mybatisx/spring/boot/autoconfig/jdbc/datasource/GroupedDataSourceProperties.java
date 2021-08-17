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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wvkity
 * @created 2021-08-07
 * @since 1.0.0
 */
public class GroupedDataSourceProperties {

    /**
     * 分组数据源列表
     */
    private List<GroupingDataSourceProperties> list = new ArrayList<>();
    /**
     * 分组数据源列表
     */
    private Map<String, GroupingDataSourceProperties> map = new HashMap<>();

    public List<GroupingDataSourceProperties> getList() {
        return list;
    }

    public void setList(List<GroupingDataSourceProperties> list) {
        this.list = list;
    }

    public Map<String, GroupingDataSourceProperties> getMap() {
        return map;
    }

    public void setMap(Map<String, GroupingDataSourceProperties> map) {
        this.map = map;
    }
}