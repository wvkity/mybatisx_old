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
package com.wvkity.mybatis.core.plugin.handler;

import org.apache.ibatis.plugin.Invocation;

import java.util.Properties;

/**
 * 拦截处理器
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
public interface Handler {

    /**
     * 拦截
     * @param invocation 代理对象
     * @return 结果
     * @throws Throwable 异常信息
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 设置相关属性值
     * @param properties 属性配置
     */
    void setProperties(final Properties properties);
}
