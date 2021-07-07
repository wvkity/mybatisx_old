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
package com.github.mybatisx.executor.resultset;

import java.util.Map;

/**
 * 嵌入结果接口
 * @author wvkity
 * @created 2021-01-10
 * @since 1.0.0
 */
public interface EmbedResult {

    /**
     * 获取XML自定义配置的ResultMap结果集
     * @return ResultMap结果集
     */
    String getResultMap();

    /**
     * 获取自定义返回值类型
     * @return 返回值类型
     */
    Class<?> getResultType();

    /**
     * 获取返回Map对象指定的key值
     * @return key
     */
    String getMapKey();

    /**
     * 获取{@link Map}实现类
     * @return {@link Map}实现类
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Map> getMapType();

    /**
     * 设置自定义结果集
     * @param resultMap 结果集
     * @return {@code this}
     */
    EmbedResult resultMap(final String resultMap);

    /**
     * 设置返回值类型
     * @param resultType 返回值类型
     * @return {@code this}
     */
    EmbedResult resultType(final Class<?> resultType);

    /**
     * 设置map结果中的key值
     * <p>{@code @MapKey("key")}</p>
     * @param mapKey key
     * @return {@code this}
     */
    EmbedResult mapKey(final String mapKey);

    /**
     * 设置{@link Map}实现类
     * @param mapImplClass {@link Map}实现类
     * @return {@code this}
     */
    @SuppressWarnings("rawtypes")
    EmbedResult mapType(final Class<? extends Map> mapImplClass);

}
