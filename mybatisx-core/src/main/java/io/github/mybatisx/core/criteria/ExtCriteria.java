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
package io.github.mybatisx.core.criteria;

import io.github.mybatisx.core.convert.PropertyConverter;
import io.github.mybatisx.core.support.select.Selection;
import io.github.mybatisx.support.constant.Join;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.expr.Expression;

import java.util.Collection;
import java.util.List;

/**
 * 条件包装接口
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface ExtCriteria<T> extends Criteria<T> {

    /**
     * 是否抓取联表查询字段
     * @return boolean
     */
    default boolean isFetch() {
        return false;
    }

    /**
     * 是否存在查询字段
     * @return boolean
     */
    default boolean hasSelect() {
        return false;
    }

    /**
     * 是否只查询聚合函数
     * @return boolean
     */
    default boolean isOnlyFunc() {
        return false;
    }

    /**
     * 查询是否包含聚合函数
     * @return boolean
     */
    default boolean isContainsFunc() {
        return true;
    }

    /**
     * 是否拼接keep orderby注释
     * @return boolean
     */
    boolean isKeepOrderBy();

    /**
     * 是否去重
     * @return boolean
     */
    boolean isDistinct();

    /**
     * 是否全部字段分组
     * @return boolean
     */
    boolean isGroupAll();

    /**
     * 获取查询列
     * @return {@link Selection}列表
     */
    default List<Selection> fetchSelects() {
        return null;
    }

    /**
     * 获取联表方式
     * @return {@link Join}
     */
    default Join getJoin() {
        return null;
    }

    /**
     * 转换成抽象扩展条件
     * @return {@link AbstractExtCriteria}
     */
    AbstractExtCriteria<T> transfer();

    /**
     * 获取属性转换器
     * @return {@link PropertyConverter}
     */
    PropertyConverter getConverter();

    /**
     * 添加{@link Expression}
     * @param expression {@link Expression}
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Expression<?> expression);

    /**
     * 添加多个{@link Expression}
     * @param expressions {@link Expression}列表
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Expression<?>... expressions);

    /**
     * 添加多个{@link Expression}
     * @param expressions {@link Expression}集合
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Collection<Expression<?>> expressions);

    /**
     * 获取查询字段语句
     * @param self 是否为自身
     * @return 查询字段语句
     */
    String getSelectSegment(final boolean self);
}
