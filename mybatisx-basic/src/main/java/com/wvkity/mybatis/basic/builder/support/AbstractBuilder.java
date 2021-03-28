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
package com.wvkity.mybatis.basic.builder.support;

import com.wvkity.mybatis.annotation.NamingStrategy;
import com.wvkity.mybatis.basic.naming.PhysicalNamingConverter;

/**
 * 抽象构建器
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public abstract class AbstractBuilder {

    /**
     * 命名策略
     */
    protected NamingStrategy strategy;
    /**
     * 命名转换器
     */
    protected PhysicalNamingConverter namingConverter;
    /**
     * 关键字格式化
     */
    protected String keyWordFormat;

    public NamingStrategy strategy() {
        return strategy;
    }

    public AbstractBuilder strategy(NamingStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public PhysicalNamingConverter namingConverter() {
        return namingConverter;
    }

    public AbstractBuilder namingConverter(PhysicalNamingConverter namingConverter) {
        this.namingConverter = namingConverter;
        return this;
    }

    public String keyWordFormat() {
        return keyWordFormat;
    }

    public AbstractBuilder keyWordFormat(String keyWordFormat) {
        this.keyWordFormat = keyWordFormat;
        return this;
    }

}
