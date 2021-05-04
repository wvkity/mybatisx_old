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

import java.util.concurrent.atomic.AtomicReference;

/**
 * 子查询条件
 * @param <S> 实体类型
 * @author wvkity
 * @created 2021-04-16
 * @since 1.0.0
 */
public class SubQuery<S> extends AbstractSubCriteria<S> {

    private static final long serialVersionUID = 2456566849505666044L;

    private SubQuery() {
    }

    public <R> SubQuery(AbstractQueryCriteria<R> root, Class<S> entity) {
        this(root, entity, null);
    }

    public <R> SubQuery(AbstractQueryCriteria<R> root, Class<S> entity, String alias) {
        this.master = root;
        this.entityClass = entity;
        this.parameterSequence = root.parameterSequence;
        this.parameterValueMapping = root.parameterValueMapping;
        this.parameterConverter = master.parameterConverter;
        this.notMatchingWithThrows = root.notMatchingWithThrows;
        this.tableAliasSequence = root.tableAliasSequence;
        this.useAlias = root.useAlias;
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.conditionConverter = new ConditionConverter(this);
        this.segmentManager = new StandardFragmentManager(this);
        this.search = new ColumnSearch(this);
    }

    @Override
    protected SubQuery<S> newInstance() {
        final SubQuery<S> instance = new SubQuery<>();
        instance.master = this.master;
        instance.entityClass = this.entityClass;
        instance.clone(this);
        return instance;
    }
}
