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
package io.github.mybatisx.spring.boot.auditable.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * 内置审计配置项
 * @author wvkity
 * @created 2021-07-21
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = MyBatisSysBuiltAuditedProperties.CFG_PREFIX)
public class MyBatisSysBuiltAuditedProperties {

    public static final String CFG_PREFIX = "github.mybatisx.plugin.auditable.sys";

    /**
     * 其他配置项
     */
    private Properties properties;

    public MyBatisSysBuiltAuditedProperties() {
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
