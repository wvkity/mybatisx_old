/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
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
package com.wvkity.mybatis.spring.jdbc.datasource;

/**
 * 数据源类型
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
public enum DSType {

    /**
     * 从库
     */
    SLAVE("slave", "从库"),
    /**
     * 主库
     */
    MASTER("master", "主库");

    /**
     * 类型
     */
    private final String type;
    /**
     * 名称
     */
    private final String name;

    DSType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
