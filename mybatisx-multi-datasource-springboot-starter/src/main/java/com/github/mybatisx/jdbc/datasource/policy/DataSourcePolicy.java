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
package com.github.mybatisx.jdbc.datasource.policy;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源策略
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
public interface DataSourcePolicy {

    /**
     * 从指定的数据源列表中确定数据源
     * @param dataSources 数据源列表
     * @return {@link DataSource}
     */
    DataSource determine(final List<DataSource> dataSources);
}
