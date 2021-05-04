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

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.basic.manager.StandardFragmentManager;
import com.wvkity.mybatis.support.constant.Join;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 联表查询条件
 * @param <M> 主表实体类型
 * @param <S> 从表实体类型
 * @author wvkity
 * @created 2021-04-13
 * @since 1.0.0
 */
public class Foreign<M, S> extends AbstractForeignCriteria<M, S> {

    private static final long serialVersionUID = -3734792213998600471L;

    private Foreign() {
    }

    public Foreign(final AbstractQueryCriteria<M> master, final Class<S> entity,
                   final Join join) {
        this(master, entity, join, null);
    }

    public Foreign(final AbstractQueryCriteria<M> master, final Class<S> entity,
                   final Join join, final String alias) {
        this.master = master;
        this.entityClass = entity;
        this.join = join;
        this.parameterSequence = master.parameterSequence;
        this.parameterValueMapping = master.parameterValueMapping;
        this.parameterConverter = master.parameterConverter;
        this.notMatchingWithThrows = master.notMatchingWithThrows;
        this.tableAliasSequence = master.tableAliasSequence;
        this.useAlias = master.useAlias;
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.conditionConverter = new ConditionConverter(this);
        this.segmentManager = new StandardFragmentManager(this);
        this.search = new ColumnSearch(this);
    }

    @Override
    protected Foreign<M, S> newInstance() {
        final Foreign<M, S> instance = new Foreign<>();
        instance.master = this.master;
        instance.entityClass = this.entityClass;
        instance.join = this.join;
        instance.clone(this);
        return instance;
    }

}
