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
package com.github.mybatisx.plugin.utils;

/**
 * 字符串工具
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public final class Objects {

    /**
     * 检查字符串是否为空
     * @param value 待检查字符串
     * @return boolean
     */
    public static boolean isBlank(final String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 检查字符串是否不为空
     * @param value 待检查字符串
     * @return boolean
     */
    public static boolean isNotBlank(final String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 字符串转boolean
     * @param value 待转换字符串
     * @return boolean
     */
    public static boolean toBool(final String value) {
        return isNotBlank(value) && ("true".equalsIgnoreCase(value) || "1".equals(value));
    }

    /**
     * 字符串类转实例
     * @param className 类名
     * @return 实例
     */
    public static Object newInstance(final String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return newInstance(clazz);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 创建实例
     * @param clazz 类
     * @param <T>   类型
     * @return 实例
     * @throws Exception 异常
     */
    public static <T> T newInstance(Class<T> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

}
