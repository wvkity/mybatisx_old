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
package com.wvkity.mybatis.spring.boot.autoconfigure.jdbc;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 读写数据源自动配置器
 * @author wvkity
 * @created 2020-12-02
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ReadWriteDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class ReadWriteDataSourceAutoConfiguration {

    private final ReadWriteDataSourceProperties dataSourceProperties;

    public ReadWriteDataSourceAutoConfiguration(ReadWriteDataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }
}
