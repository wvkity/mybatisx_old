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
package io.github.mybatisx.jdbc.datasource.resolver;

import io.github.mybatisx.Objects;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 数据源真实类解析器
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public class DataSourceRealClassResolver implements ProxyClassResolver {

    private static boolean findMapperInterface;
    private static Field mapperInterfaceField;

    static {
        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName("com.github.mybatisx.binding.MyBatisMapperProxy");
        } catch (ClassNotFoundException e1) {
            try {
                proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
            } catch (ClassNotFoundException ignore) {
                // ignore
            }
        }
        if (Objects.nonNull(proxyClass)) {
            try {
                mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
                mapperInterfaceField.setAccessible(true);
                findMapperInterface = true;
            } catch (Exception ignore) {
                // ignore
            }
        }
    }

    @Override
    public Class<?> getTargetClass(Object target) throws IllegalAccessException {
        if (findMapperInterface) {
            final Class<?> targetClass = target.getClass();
            return Proxy.isProxyClass(targetClass) ?
                (Class<?>) mapperInterfaceField.get(Proxy.getInvocationHandler(target)) : targetClass;
        }
        return target.getClass();
    }
}
