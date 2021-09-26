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
package io.github.mybatisx.reflection;

import io.github.mybatisx.plugin.exception.MyBatisPluginException;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 对象描述工具
 * @author wvkity
 * @created 2021-02-10
 * @since 1.0.0
 */
public final class MetaObjects {

    private MetaObjects() {
    }

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static MethodHandle methodHandle;

    static {
        try {
            Class.forName("org.apache.ibatis.reflection.ReflectorFactory");
            final Class<?> metaClass = Class.forName("com.wvkity.mybatisx.reflection.MyBatisMetaObject");
            methodHandle = LOOKUP.findStatic(metaClass, "forObject",
                MethodType.methodType(MetaObject.class, Object.class));
        } catch (Exception e) {
            try {
                final Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.SystemMetaObject");
                methodHandle = LOOKUP.findStatic(metaClass, "forObject",
                    MethodType.methodType(MetaObject.class, Object.class));
            } catch (Exception e1) {
                try {
                    final Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.MetaObject");
                    methodHandle = LOOKUP.findStatic(metaClass, "forObject",
                        MethodType.methodType(MetaObject.class, Object.class, ObjectFactory.class,
                            ObjectWrapperFactory.class, ReflectorFactory.class));
                } catch (Exception e2) {
                    throw new MyBatisPluginException(e2);
                }
            }
        }
    }

    public static MetaObject forObject(final Object... args) {
        try {
            return (MetaObject) methodHandle.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new MyBatisPluginException(e);
        }
    }
}
