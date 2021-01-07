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
package com.wvkity.mybatis.core.builder.support;

import com.wvkity.mybatis.annotation.NamingStrategy;
import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.naming.DefaultPhysicalNamingConverter;
import com.wvkity.mybatis.core.naming.PhysicalNamingConverter;

import java.util.Optional;
import java.util.function.Function;

/**
 * 抽象构建器
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public abstract class AbstractBuilder {

    /**
     * 默认命名转换器
     */
    protected static final PhysicalNamingConverter PHYSICAL_NAMING_CONVERTER = new DefaultPhysicalNamingConverter();
    /**
     * 全局配置
     */
    protected MyBatisGlobalConfiguration configuration;
    /**
     * 命名策略
     */
    protected NamingStrategy strategy;
    /**
     * 命名转换器
     */
    protected PhysicalNamingConverter physicalNamingConverter;

    public MyBatisGlobalConfiguration configuration() {
        return configuration;
    }

    public AbstractBuilder configuration(MyBatisGlobalConfiguration configuration) {
        this.configuration = configuration;
        if (this.configuration != null) {
            final PhysicalNamingConverter converter;
            if ((converter = this.configuration.getPhysicalNamingConverter()) == null) {
                this.physicalNamingConverter = PHYSICAL_NAMING_CONVERTER;
                this.configuration.setPhysicalNamingConverter(PHYSICAL_NAMING_CONVERTER);
            } else {
                this.physicalNamingConverter = converter;
            }
        } else {
            this.physicalNamingConverter = PHYSICAL_NAMING_CONVERTER;
        }
        return this;
    }

    public NamingStrategy strategy() {
        return strategy;
    }

    public AbstractBuilder strategy(NamingStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    <T> T getValue(final Function<MyBatisGlobalConfiguration, T> mapper) {
        return Optional.ofNullable(this.configuration).map(mapper).orElse(null);
    }

    <T> T getValue(final Function<MyBatisGlobalConfiguration, T> mapper, final T defaultValue) {
        return Optional.ofNullable(this.configuration).map(mapper).orElse(defaultValue);
    }
}
