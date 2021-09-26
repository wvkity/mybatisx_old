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
package io.github.mybatisx.jdbc.datasource.annotation;

import io.github.mybatisx.jdbc.datasource.DataSourceNodeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DataSource {

    /**
     * 数据源节点类型
     * @return 数据源类型
     */
    DataSourceNodeType type() default DataSourceNodeType.UNKNOWN;

    /**
     * 指定数据源组
     * @return 数据源组
     */
    String group() default "";

    /**
     * 数据源节点名称
     * @return 节点名称
     */
    String value() default "";
}
