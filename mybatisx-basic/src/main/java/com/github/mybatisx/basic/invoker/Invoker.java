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
package com.github.mybatisx.basic.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * 调用器
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
public interface Invoker {

    /**
     * 获取类型
     * @return 类型
     */
    Class<?> getType();

    /**
     * 调用方法
     * @param target 目标对象
     * @param args   参数
     * @return Object
     * @throws IllegalAccessException    无访问权限异常
     * @throws InvocationTargetException ``
     */
    Object invoke(final Object target, Object... args) throws IllegalAccessException, InvocationTargetException;
}
