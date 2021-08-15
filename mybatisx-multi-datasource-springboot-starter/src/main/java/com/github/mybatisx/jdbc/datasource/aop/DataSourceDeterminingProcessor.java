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
package com.github.mybatisx.jdbc.datasource.aop;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 数据源选择处理器
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public interface DataSourceDeterminingProcessor {

    /**
     * 选择数据源
     * @param invocation {@link MethodInvocation}
     * @return 执行结果
     * @throws Throwable if the interceptors or the target object throws an exception
     */
    Object determineDataSource(final MethodInvocation invocation) throws Throwable;
}
