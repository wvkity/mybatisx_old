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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.core.condition.expression.Expression;
import com.wvkity.mybatis.core.convert.converter.PlaceholderConverter;
import com.wvkity.mybatis.core.convert.converter.PropertyConverter;
import com.wvkity.mybatis.core.segment.Fragment;

import java.util.Collection;

/**
 * 条件
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
public interface Criteria<T> extends Search<T>, PropertyConverter<T>, PlaceholderConverter, Fragment {

    /**
     * 获取实体类
     * @return 实体类
     */
    Class<T> getEntityClass();

    /**
     * 获取别名
     * @return 别名
     */
    String as();

    /**
     * 设置表别名
     * @param alias 表别名
     * @return {@link Criteria}
     */
    Criteria<T> as(final String alias);

    /**
     * 使用表别名
     * @return {@link Criteria}
     */
    Criteria<T> useAlias();

    /**
     * 设置是否使用表别名
     * @param used 是否使用
     * @return {@link Criteria}
     */
    Criteria<T> useAlias(final boolean used);

    /**
     * 属性不匹配是否抛出异常
     * @param throwing 是否抛异常
     * @return {@link Criteria}
     */
    Criteria<T> notMatchingWithThrows(final boolean throwing);

    /**
     * 添加多个{@link Expression}
     * @param expressions {@link Expression}列表
     * @return {@link Criteria}
     */
    Criteria<T> where(final Expression... expressions);

    /**
     * 添加多个{@link Expression}
     * @param expressions {@link Expression}集合
     * @return {@link Criteria}
     */
    Criteria<T> where(final Collection<Expression> expressions);
}
