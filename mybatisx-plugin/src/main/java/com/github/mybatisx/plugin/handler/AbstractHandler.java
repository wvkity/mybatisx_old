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
package com.github.mybatisx.plugin.handler;

import com.github.mybatisx.plugin.filter.Filter;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Properties;

/**
 * 抽象拦截处理器
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public abstract class AbstractHandler implements Handler, Filter {

    /**
     * 相关配置
     */
    protected Properties properties;

    /**
     * 获取当前执行的方法名
     * @param ms {@link MappedStatement}对象
     * @return 方法名
     */
    protected String execMethod(final MappedStatement ms) {
        final String msId = ms.getId();
        final int index = msId.lastIndexOf(".");
        return index < 0 ? msId : msId.substring(index + 1);
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
