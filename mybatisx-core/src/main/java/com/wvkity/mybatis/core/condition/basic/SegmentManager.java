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
package com.wvkity.mybatis.core.condition.basic;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.condition.basic.group.Group;
import com.wvkity.mybatis.core.condition.basic.order.Order;
import com.wvkity.mybatis.core.condition.criteria.Criterion;
import com.wvkity.mybatis.support.segment.Fragment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * SQL片段管理器
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public class SegmentManager implements Fragment {

    private static final long serialVersionUID = -884244554745201766L;

    /**
     * 条件管理器
     */
    private final WhereManager whereManager = new WhereManager();
    /**
     * 分组管理器
     */
    private final GroupManager groupManager = new GroupManager();
    /**
     * 排序管理器
     */
    private final OrderManager orderManager = new OrderManager();

    /**
     * 添加{@link Criterion}对象
     * @param condition {@link Criterion}对象
     * @return {@link SegmentManager}
     */
    public SegmentManager where(final Criterion condition) {
        Optional.ofNullable(condition).ifPresent(this.whereManager::add);
        return this;
    }

    /**
     * 添加多个{@link Criterion}对象
     * @param conditions {@link Criterion}集合
     * @return {@link SegmentManager}
     */
    public SegmentManager where(final Collection<Criterion> conditions) {
        this.whereManager.addAll(conditions);
        return this;
    }

    /**
     * 添加{@link Group}对象
     * @param group {@link Group}对象
     * @return {@link SegmentManager}
     */
    public SegmentManager groupBy(final Group group) {
        Optional.ofNullable(group).ifPresent(this.groupManager::add);
        return this;
    }

    /**
     * 添加多个{@link Group}对象
     * @param groups {@link Group}列表
     * @return {@link SegmentManager}
     */
    public SegmentManager groupBy(final Collection<Group> groups) {
        this.groupManager.addAll(groups);
        return this;
    }

    /**
     * 添加{@link Order}对象
     * @param order {@link Order}对象
     * @return {@link SegmentManager}
     */
    public SegmentManager orderBy(final Order order) {
        Optional.ofNullable(order).ifPresent(this.orderManager::add);
        return this;
    }

    /**
     * 添加多选{@link Order}对象
     * @param orders {@link Order}对象列表
     * @return {@link SegmentManager}
     */
    public SegmentManager orderBy(final List<Order> orders) {
        this.orderManager.addAll(orders);
        return this;
    }

    /**
     * 获取where条件
     * @return 条件字符串
     */
    public String getWhereString() {
        return this.whereManager.getSegment();
    }

    /**
     * 获取所有{@link Criterion}对象
     * @return {@link Criterion}集合
     */
    public List<Criterion> getConditions() {
        return this.whereManager.getConditions();
    }

    /**
     * 检查是否存在条件
     * @return boolean
     */
    public boolean hasCondition() {
        return !this.whereManager.isEmpty() || !this.groupManager.isEmpty() || !this.orderManager.isEmpty();
    }

    /**
     * 获取SQL片段
     * @param groupBySegment 分组替换SQL片段
     * @return SQL片段
     */
    public String getSegment(final String groupBySegment) {
        return Objects.isNotBlank(groupBySegment) ? (this.getWhereString() + " GROUP BY " + groupBySegment
            + this.orderManager.getSegment()) : this.getSegment();
    }

    @Override
    public String getSegment() {
        return this.getWhereString() + this.groupManager.getSegment() + this.orderManager.getSegment();
    }
}
