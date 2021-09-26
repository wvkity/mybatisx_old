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

/**
 * 数据源节点类型
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
public enum DataSourceNodeType {

    /**
     * 未指定
     */
    UNKNOWN("UNKNOWN", "未指定"),
    /**
     * 主
     */
    MASTER("MASTER", "主库"),
    /**
     * 从
     */
    SLAVE("SLAVE", "从库");

    /**
     * 类型
     */
    private final String type;
    /**
     * 描述
     */
    private final String desc;

    DataSourceNodeType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
