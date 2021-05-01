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
package com.wvkity.mybatis.core.basic.manager;

import com.wvkity.mybatis.core.basic.group.Group;
import com.wvkity.mybatis.core.basic.having.Having;
import com.wvkity.mybatis.core.basic.order.Order;
import com.wvkity.mybatis.core.basic.select.FuncSelection;
import com.wvkity.mybatis.core.basic.select.Selection;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Collection;
import java.util.List;

/**
 * 标准片段管理器
 * @param <C> 查询条件
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public interface StandardManager<C extends Criteria<?>> extends Manager<C> {

    /**
     * 添加{@link Selection}
     * @param selection {@link Selection}
     * @return {@link StandardManager}
     */
    StandardManager<C> select(final Selection selection);

    /**
     * 添加多个{@link Selection}
     * @param selections {@link Selection}列表
     * @return {@link StandardManager}
     */
    StandardManager<C> select(final Collection<Selection> selections);

    /**
     * 过滤查询列
     * @param property 属性
     * @return {@link StandardManager}
     */
    StandardManager<C> exclude(final String property);

    /**
     * 过滤查询列
     * @param column 字段
     * @return {@link StandardManager}
     */
    StandardManager<C> excludeCol(final String column);

    /**
     * 获取所有查询字段
     * @return {@link Selection}列表
     */
    List<Selection> getSelects();

    /**
     * 添加{@link Group}
     * @param group {@link Group}
     * @return {@link StandardManager}
     */
    StandardManager<C> groupBy(final Group group);

    /**
     * 添加多个{@link Group}对象
     * @param groups {@link Group}列表
     * @return {@link StandardManager}
     */
    StandardManager<C> groupBy(final Collection<Group> groups);

    /**
     * 添加{@link Having}
     * @param having {@link Having}
     * @return {@link StandardManager}
     */
    StandardManager<C> having(final Having having);

    /**
     * 添加多个{@link Having}对象
     * @param havingList {@link Having}列表
     * @return {@link StandardManager}
     */
    StandardManager<C> having(final Collection<Having> havingList);

    /**
     * 添加{@link Order}对象
     * @param order {@link Order}对象
     * @return {@link StandardManager}
     */
    StandardManager<C> orderBy(final Order order);

    /**
     * 添加多选{@link Order}对象
     * @param orders {@link Order}对象列表
     * @return {@link StandardManager}
     */
    StandardManager<C> orderBy(final List<Order> orders);

    /**
     * 检查是否存在查询字段
     * @return boolean
     */
    boolean hasSelect();

    /**
     * 获取{@link FuncSelection}
     * @param alias 聚合函数别名
     * @return {@link FuncSelection}
     */
    FuncSelection getFunc(final String alias);

    /**
     * 获取查询字段SQL片段
     * @return 字段SQL片段
     */
    default String getSelectString() {
        return this.getSelectString(true);
    }

    /**
     * 获取查询字段SQL片段
     * @param isQuery 是否为查询
     * @return 字段SQL片段
     */
    String getSelectString(final boolean isQuery);

    /**
     * 获取分组SQL片段
     * @return 分组SQL片段
     */
    String getGroupString();

    /**
     * 获取分组筛选SQL片段
     * @return 分组筛选SQL片段
     */
    String getHavingString();

    /**
     * 获取排序SQL片段
     * @return 排序SQL片段
     */
    String getOrderString();

    /**
     * 获取完整条件SQL片段
     * <p>where + group by + having + order by</p>
     * @param groupReplacement 替换的分组片段
     * @return SQL片段
     */
    String getSegment(final String groupReplacement);

}