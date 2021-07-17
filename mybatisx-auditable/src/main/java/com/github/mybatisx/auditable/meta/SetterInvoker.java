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
package com.github.mybatisx.auditable.meta;

import com.github.mybatisx.auditable.exception.AuditedException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * ser方法调用
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class SetterInvoker implements MethodInvoker {

    private final MethodHandle handle;

    public SetterInvoker(Class<?> ref, Method method) {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            this.handle = lookup.findVirtual(ref, method.getName(), MethodType.methodType(method.getReturnType(),
                method.getParameterTypes()));
        } catch (Exception e) {
            throw new AuditedException("Failed to get handle to setter method: " + e.getMessage(), e);
        }
    }


    @Override
    public Object invoke(Object... args) {
        try {
            return this.handle.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new AuditedException("Failed to change data: " + e.getMessage(), e);
        }
    }
}
