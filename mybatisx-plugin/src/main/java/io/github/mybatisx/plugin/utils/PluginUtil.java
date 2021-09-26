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
package io.github.mybatisx.plugin.utils;

import io.github.mybatisx.plugin.exception.MyBatisPluginException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 插件工具
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public final class PluginUtil {

    private PluginUtil() {
    }

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final MethodHandle METHOD_HANDLE_ADDITIONAL;

    static {
        try {
            final Field field = BoundSql.class.getDeclaredField("additionalParameters");
            field.setAccessible(true);
            METHOD_HANDLE_ADDITIONAL = LOOKUP.unreflectGetter(field);
        } catch (Exception e) {
            throw new MyBatisPluginException("Failure to obtain BoundSql attribute additionalParameters："
                + e.getMessage(), e);
        }
    }

    /**
     * 获取{@link BoundSql}对象additionalParameters属性值
     * @param bs {@link BoundSql}对象
     * @return Map集合
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAdditionalParameter(final BoundSql bs) {
        try {
            return (Map<String, Object>) METHOD_HANDLE_ADDITIONAL.bindTo(bs).invokeWithArguments();
        } catch (Throwable e) {
            throw new MyBatisPluginException("Failure to obtain BoundSql attribute value additionalParameters: " + e, e);
        }
    }

    /**
     * 获取真实对象
     * @param target 目标对象
     * @return 真实处理对象
     */
    public static Object getRealTarget(final Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return getRealTarget(metaObject.getValue("h.target"));
        }
        return target;
    }

    /**
     * 获取参数值
     * @param parameter 参数对象
     * @param key       key
     * @return 参数
     */
    @SuppressWarnings("unchecked")
    public static Object getParameter(Object parameter, String key) {
        if (key != null && key.trim().length() > 0 && parameter instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (paramMap.containsKey(key)) {
                return paramMap.get(key);
            }
        }
        return null;
    }

    /**
     * 获取参数值
     * @param parameter 参数对象
     * @param key       key
     * @param <T>       参数类型
     * @return 参数
     */
    @SuppressWarnings("unchecked")
    public static <T> T getParameter(Object parameter, String key, final Class<T> clazz) {
        if (parameter != null) {
            if (parameter instanceof Map) {
                final Object value = getParameter(parameter, key);
                if (value != null && clazz.isAssignableFrom(value.getClass())) {
                    return (T) value;
                }
            } else if (clazz.isAssignableFrom(parameter.getClass())) {
                return (T) parameter;
            }
        }
        return null;
    }
}
