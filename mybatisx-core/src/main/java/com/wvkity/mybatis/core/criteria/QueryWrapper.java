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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.core.basic.func.Function;
import com.wvkity.mybatis.core.basic.select.FuncSelection;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Map;

/**
 * 查询包装器
 * @param <T>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface QueryWrapper<T, Chain extends QueryWrapper<T, Chain>> extends Criteria<T>, ForeignWrapper<T, Chain>,
    SubQueryWrapper<T, Chain>, SubForeignWrapper<T, Chain>, SelectWrapper<T, Chain>, FunctionWrapper<T, Chain>,
    GroupByWrapper<T, Chain>, HavingWrapper<T, Chain>, OrderByWrapper<T, Chain> {

    /**
     * 设置引用属性
     * @param reference 引用属性
     * @return {@link Chain}
     */
    Chain reference(final String reference);

    /**
     * 是否抓取联表查询字段
     * @return boolean
     */
    default boolean isFetch() {
        return false;
    }

    /**
     * 查询是否包含聚合函数
     * @return boolean
     */
    boolean isContainsFunc();

    /**
     * 设置查询是否包含聚合函数
     * @param contains 是否
     * @return {@link Chain}
     */
    Chain containsFunc(final boolean contains);

    /**
     * 是否仅仅查询聚合函数
     * @return boolean
     */
    boolean isOnlyFunc();

    /**
     * 设置是否仅仅查询聚合函数
     * @param only 是否
     * @return {@link Chain}
     */
    Chain onlyFunc(final boolean only);

    /**
     * 获取聚合函数对象
     * @param alias 聚合函数别名
     * @return {@link Function}
     */
    Function getFunc(final String alias);

    /**
     * 获取表别名(非内置的别名)
     * @return 表别名
     */
    String alias();

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
     * 使用属性名作为别名
     * @return {@link Chain}
     */
    default Chain usePropAlias() {
        return this.usePropAlias(true);
    }

    /**
     * 设置是否使用属性名作为别名
     * @param used 是否使用
     * @return {@link Chain}
     */
    Chain usePropAlias(final boolean used);

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
     * 设置map结果中的key值为主键值
     * @return {@link Chain}
     */
    Chain mapKey();

    /**
     * 设置map结果中的key值
     * <p>{@code @MapKey("key")}</p>
     * @param property 属性
     * @return {@link Chain}
     */
    Chain mapKey(final Property<T, ?> property);

    /**
     * 设置map结果中的key值
     * <p>{@code @MapKey("key")}</p>
     * @param mapKey key
     * @return {@link Chain}
     */
    Chain mapKey(final String mapKey);

    /**
     * 设置{@link Map}实现类
     * @param mapImplClass {@link Map}实现类
     * @return {@link Chain}
     */
    @SuppressWarnings("rawtypes")
    Chain mapType(final Class<? extends Map> mapImplClass);

    /**
     * 指定查询范围
     * @param start 起始位置
     * @param end   结束位置
     * @return {@link Chain}
     */
    Chain range(final long start, final long end);

    /**
     * 指定查询范围
     * @param start 起始页码
     * @param end   结束页码
     * @param size  每页数目
     * @return {@link Chain}
     */
    Chain range(final long start, final long end, final long size);

    /**
     * 获取查询列SQL片段
     * @return 查询列
     */
    String getSelectSegment();
}
