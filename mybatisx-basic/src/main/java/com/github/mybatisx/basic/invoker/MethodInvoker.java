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


import com.github.mybatisx.basic.reflect.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法调用器
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
public class MethodInvoker implements Invoker {

    /**
     * 类型
     */
    private final Class<?> type;

    /**
     * 方法对象
     */
    private final Method method;

    public MethodInvoker(Method method) {
        this.method = method;
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            this.type = parameterTypes[0];
        } else {
            this.type = method.getReturnType();
        }
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public Object invoke(Object target, Object... args) throws IllegalAccessException, InvocationTargetException {
        try {
            return this.method.invoke(target, args);
        } catch (IllegalAccessException e) {
            if (Reflections.canControlMemberAccessible()) {
                this.method.setAccessible(true);
                return this.method.invoke(target, args);
            } else {
                throw e;
            }
        }
    }

    public static MethodInvoker of(final Method method) {
        return new MethodInvoker(method);
    }
}
