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
package com.wvkity.mybatis.support.criteria;

import com.wvkity.mybatis.support.expr.Expression;
import com.wvkity.mybatis.support.segment.Fragment;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 条件
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
public interface Criteria<T> extends Fragment {

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
     * 设置是否使用严格模式(属性不匹配是否抛出异常，默认为严格模式)
     * @param throwing 是否抛异常
     * @return {@link Criteria}
     */
    Criteria<T> strict(final boolean throwing);

    /**
     * 是否使用严格模式
     * @return boolean
     */
    boolean isStrict();

    /**
     * 添加{@link Expression}
     * @param action {@link Consumer}
     * @return {@link Criteria}
     */
    Criteria<T> where(final Consumer<Criteria<T>> action);

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

    // region Default methods

    /**
     * 是否使用属性名只作为别名
     * @return boolean
     */
    default boolean isPropAsAlias() {
        return false;
    }

    /**
     * 获取联表查询引用属性
     * @return 属性
     */
    default String getReference() {
        return null;
    }

    /**
     * 完整SQL语句
     * @return SQL语句
     */
    default String completeString() {
        return null;
    }
    // endregion

}
