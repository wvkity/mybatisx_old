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

import com.wvkity.mybatis.core.criteria.Criterion;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Collection;
import java.util.List;

/**
 * 抽象管理器
 * @param <C> 条件类型
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractManager<C extends Criteria<?>> implements Manager<C> {

    /**
     * 条件包装器
     */
    protected C criteria;
    /**
     * 是否为查询
     */
    protected boolean forQuery;
    /**
     * 条件片段存储
     */
    protected final WhereFragmentStorage whereStorage;

    public AbstractManager(C criteria) {
        this.criteria = criteria;
        this.whereStorage = new WhereFragmentStorage();
    }

    @Override
    public AbstractManager<C> where(Criterion condition) {
        this.whereStorage.add(condition);
        return this;
    }

    @Override
    public AbstractManager<C> where(Collection<Criterion> conditions) {
        this.whereStorage.addAll(conditions);
        return this;
    }

    @Override
    public List<Criterion> getConditions() {
        return this.whereStorage.getConditions();
    }

    @Override
    public boolean hasCondition() {
        return !this.whereStorage.isEmpty();
    }

    @Override
    public String getWhereString() {
        return this.whereStorage.getSegment();
    }

    @Override
    public String getSegment() {
        return this.getWhereString();
    }
}
