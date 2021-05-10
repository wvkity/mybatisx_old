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

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.basic.group.Group;
import com.wvkity.mybatis.core.basic.having.Having;
import com.wvkity.mybatis.core.basic.order.Order;
import com.wvkity.mybatis.core.basic.select.FuncSelection;
import com.wvkity.mybatis.core.basic.select.Selection;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Collection;
import java.util.List;

/**
 * 抽象标准片段管理器
 * @param <C> 条件类型
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractStandardManager<C extends Criteria<?>> extends AbstractManager<C>
    implements StandardManager<C> {

    /**
     * 查询字段存储
     */
    protected final SelectFragmentStorage selectStorage;
    /**
     * 分组片段存储
     */
    protected final GroupFragmentStorage groupStorage;
    /**
     * 分组筛选片段存储
     */
    protected final HavingFragmentStorage havingStorage;
    /**
     * 排序片段存储
     */
    protected final OrderFragmentStorage orderStorage;

    public AbstractStandardManager(C criteria) {
        this(criteria, criteria instanceof CommonQueryWrapper);
    }

    public AbstractStandardManager(C criteria, boolean forQuery) {
        super(criteria);
        this.selectStorage = new SelectFragmentStorage(criteria);
        this.groupStorage = new GroupFragmentStorage();
        this.havingStorage = new HavingFragmentStorage();
        this.orderStorage = new OrderFragmentStorage();
        this.forQuery = forQuery;
    }

    @Override
    public AbstractStandardManager<C> select(Selection selection) {
        this.selectStorage.add(selection);
        return this;
    }

    @Override
    public AbstractStandardManager<C> select(Collection<Selection> selections) {
        this.selectStorage.addAll(selections);
        return this;
    }

    @Override
    public AbstractStandardManager<C> exclude(String property) {
        this.selectStorage.exclude(property);
        return this;
    }

    @Override
    public AbstractStandardManager<C> excludeCol(String column) {
        this.selectStorage.excludeCol(column);
        return this;
    }

    @Override
    public List<Selection> getSelects() {
        return this.selectStorage.getSelects();
    }

    @Override
    public AbstractStandardManager<C> groupBy(Group group) {
        this.groupStorage.add(group);
        return this;
    }

    @Override
    public AbstractStandardManager<C> groupBy(Collection<Group> groups) {
        this.groupStorage.addAll(groups);
        return this;
    }

    @Override
    public AbstractStandardManager<C> having(Having having) {
        this.havingStorage.add(having);
        return this;
    }

    @Override
    public AbstractStandardManager<C> having(Collection<Having> havingList) {
        this.havingStorage.addAll(havingList);
        return this;
    }

    @Override
    public AbstractStandardManager<C> orderBy(Order order) {
        this.orderStorage.add(order);
        return this;
    }

    @Override
    public AbstractStandardManager<C> orderBy(List<Order> orders) {
        this.orderStorage.addAll(orders);
        return this;
    }

    @Override
    public boolean hasSelect() {
        return this.selectStorage.hasSelect();
    }

    @Override
    public boolean hasOrderBy() {
        return !this.orderStorage.isEmpty();
    }

    @Override
    public FuncSelection getFunc(String alias) {
        return this.selectStorage.getFunc(alias);
    }

    @Override
    public boolean hasSegment() {
        return super.hasSegment() || !this.groupStorage.isEmpty() || !this.orderStorage.isEmpty();
    }

    @Override
    public String getSelectString(boolean isQuery) {
        return this.selectStorage.getSegment(isQuery);
    }

    @Override
    public String getGroupString() {
        return this.groupStorage.getSegment();
    }

    @Override
    public String getHavingString() {
        return this.havingStorage.getSegment();
    }

    @Override
    public String getOrderString() {
        return this.orderStorage.getSegment();
    }

    @Override
    public String getSegment() {
        return this.getSegment(null);
    }

    @Override
    public String getSegment(String groupReplacement) {
        if (this.hasSegment()) {
            final StringBuilder builder = new StringBuilder();
            final String where = this.getWhereString();
            if (Objects.isNotBlank(where)) {
                builder.append(where.trim());
            }
            if (Objects.isNotBlank(groupReplacement)) {
                builder.append(" GROUP BY ").append(groupReplacement.trim());
            } else {
                final String group = this.getGroupString();
                if (Objects.isNotBlank(group)) {
                    builder.append(Constants.SPACE).append(group.trim());
                }
            }
            final String having = this.getHavingString();
            if (Objects.isNotBlank(having)) {
                builder.append(Constants.SPACE).append(having.trim());
            }
            final String order = this.getOrderString();
            if (Objects.isNotBlank(order)) {
                builder.append(Constants.SPACE).append(order.trim());
            }
            return builder.toString().trim();
        }
        return Constants.EMPTY;
    }
}
