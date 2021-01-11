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
package com.wvkity.mybatis.core.condition.criteria;

/**
 * 查询包装器
 * @param <T>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface QueryWrapper<T, Chain extends QueryWrapper<T, Chain>> extends Select<T, Chain> {

    /**
     * 设置表别名
     * @param alias 表别名
     * @return {@link Chain}
     */
    Chain as(final String alias);

    /**
     * 使用表别名
     * @return {@link Chain}
     */
    Chain useAlias();

    /**
     * 设置是否使用表别名
     * @param used 是否使用
     * @return {@link Chain}
     */
    Chain useAlias(final boolean used);

    /**
     * 设置是否使用属性名作为别名
     * @return {@link Chain}
     */
    default Chain usePropertyAsAlias() {
        return usePropertyAsAlias(true);
    }

    /**
     * 设置是否使用属性名作为别名
     * @param used 是否使用
     * @return {@link Chain}
     */
    Chain usePropertyAsAlias(final boolean used);

    /**
     * 设置自定义结果集
     * @param resultMap 结果集
     * @return {@link Chain}
     */
    Chain resultMap(final String resultMap);

    /**
     * 设置返回值类型
     * @param resultType 返回值类型
     * @return {@link Chain}
     */
    Chain resultType(final Class<?> resultType);

    /**
     * 获取查询列SQL片段
     * @return 查询列
     */
    String getSelectSegment();
}
