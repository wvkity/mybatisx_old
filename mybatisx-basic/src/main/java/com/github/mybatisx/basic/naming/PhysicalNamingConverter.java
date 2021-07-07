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
package com.github.mybatisx.basic.naming;

import com.github.mybatisx.annotation.NamingStrategy;

/**
 * 命名转换器
 * @author wvkity
 * @created 2020-10-05
 * @since 1.0.0
 */
@FunctionalInterface
public interface PhysicalNamingConverter {

    /**
     * 根据命名策略转换对应字符串
     * @param name     名称
     * @param original 原策略
     * @param format   格式化策略
     * @return 新名称
     */
    String convert(final String name, final NamingStrategy original, final NamingStrategy format);

    /**
     * 类名转表名
     * @param entity 实体类名
     * @param format 转换策略
     * @return 表名
     */
    default String entityToTable(final String entity, final NamingStrategy format) {
        return convert(entity, NamingStrategy.UPPER_CAMEL, format);
    }

    /**
     * 属性名转字段名
     * @param property 属性名
     * @param format   转换策略
     * @return 字段名
     */
    default String propToColumn(final String property, final NamingStrategy format) {
        return convert(property, NamingStrategy.LOWER_CAMEL, format);
    }

}
