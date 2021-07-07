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
package com.github.mybatisx.plugin.paging;

import com.github.paging.Pageable;
import com.github.paging.StandardPageable;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 分页工具
 * @author wvkity
 * @created 2021-02-10
 * @since 1.0.0
 */
public final class PageableUtil {

    private PageableUtil() {
    }

    private static boolean hasRequest;
    private static Class<?> requestClass;
    private static Method getParameterMap;
    private static final String PARAM_REQUEST = "request";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final Map<String, String> PARAMETERS = new HashMap<>(6, 1);

    static {
        PARAMETERS.put(PARAM_PAGE, PARAM_PAGE);
        PARAMETERS.put(PARAM_SIZE, PARAM_SIZE);
        try {
            requestClass = Class.forName("javax.servlet.ServletRequest");
            getParameterMap = requestClass.getDeclaredMethod("getParameterMap");
            hasRequest = true;
        } catch (Exception ignore) {
            // ignore
            hasRequest = false;
        }
    }

    /**
     * 获取分页对象
     * @param parameter 方法参数
     * @return {@link Pageable}
     */
    public static Pageable getPageable(final Object parameter) {
        if (hasRequest) {
            final Object request = getRequestArg(parameter);
            if (request != null) {
                try {
                    final MetaObject meta = SystemMetaObject.forObject(getParameterMap.invoke(request));
                    final Object page = getValue(meta, PARAM_PAGE);
                    final Object size = getValue(meta, PARAM_SIZE);
                    if (page != null) {
                        return StandardPageable.of(String.valueOf(page), String.valueOf(size));
                    }
                } catch (Exception ignore) {
                    // ignore
                }
            }
        }
        return null;
    }

    /**
     * 获取请求参数值
     * @param meta      源数据
     * @param paramName 参数名
     * @return 参数值
     */
    public static Object getValue(final MetaObject meta, final String paramName) {
        if (PARAMETERS.containsKey(paramName) && meta.hasGetter(paramName)) {
            final Object value = meta.getValue(paramName);
            if (value != null) {
                if (value.getClass().isArray()) {
                    final Object[] values = (Object[]) value;
                    if (values.length > 0) {
                        return values[0];
                    }
                    return null;
                }
                return value;
            }
        }
        return null;
    }

    /**
     * 获取ServletRequest对象
     * @param parameter 方法参数
     * @return ServletRequest对象
     */
    @SuppressWarnings("unchecked")
    public static Object getRequestArg(final Object parameter) {
        if (requestClass.isAssignableFrom(parameter.getClass())) {
            return parameter;
        } else if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            final boolean isMatch = paramMap.containsKey(PARAM_REQUEST)
                && Optional.ofNullable(paramMap.get(PARAM_REQUEST)).map(Object::getClass)
                .map(requestClass::isAssignableFrom).orElse(false);
            if (isMatch) {
                return paramMap.get(PARAM_REQUEST);
            } else {
                for (Object arg : paramMap.values()) {
                    if (arg != null && requestClass.isAssignableFrom(arg.getClass())) {
                        return arg;
                    }
                }
            }
        }
        return null;
    }
}
