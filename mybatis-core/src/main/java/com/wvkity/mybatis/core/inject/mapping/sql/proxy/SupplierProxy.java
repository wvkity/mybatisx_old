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
package com.wvkity.mybatis.core.inject.mapping.sql.proxy;

import com.wvkity.mybatis.core.inject.mapping.sql.Supplier;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * {@link Supplier}动态代理
 * @author wvkity
 * @created 2020-12-25
 * @since 1.0.0
 */
public class SupplierProxy<T extends Supplier> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -5389984887770178373L;
    private final Class<T> supplierInterface;
    private final MyBatisConfiguration configuration;
    private final Object[] args;

    public SupplierProxy(Class<T> supplierInterface, MyBatisConfiguration configuration, final Object[] args) {
        this.supplierInterface = supplierInterface;
        this.configuration = configuration;
        this.args = args;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(proxy, args);
            } else {
                return invokeMethod(proxy, method, args);
            }
        } catch (Exception e) {
            throw ExceptionUtil.unwrapThrowable(e);
        }
    }

    private Object invokeMethod(Object proxy, Method method, Object[] args)
        throws Throwable {
        return method.invoke(MethodHandles.lookup().findConstructor(this.supplierInterface,
            MethodType.methodType(void.class, Reflections.getArgumentType(this.args)))
            .invokeWithArguments(this.args), args);
    }

    /**
     * Backport of {@link Method#isDefault()}
     * @param method 方法对象
     * @return boolean
     */
    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
            && method.getDeclaringClass().isInterface();
    }

    public MyBatisConfiguration getConfiguration() {
        return configuration;
    }

    public Class<T> getSupplierInterface() {
        return supplierInterface;
    }

    public Object[] getArgs() {
        return args;
    }
}
