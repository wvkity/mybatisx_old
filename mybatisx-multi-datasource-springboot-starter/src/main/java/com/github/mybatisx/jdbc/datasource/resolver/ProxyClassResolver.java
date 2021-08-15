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
package com.github.mybatisx.jdbc.datasource.resolver;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 代理类解析器
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public interface ProxyClassResolver {

    /**
     * 获取真实目标类
     * @param invocation {@link MethodInvocation}
     * @return 真实类
     * @throws IllegalAccessException if this Field object is enforcing Java language access control and the
     * underlying field is inaccessible.
     */
    Class<?> getTargetClass(final MethodInvocation invocation) throws IllegalAccessException;
}
