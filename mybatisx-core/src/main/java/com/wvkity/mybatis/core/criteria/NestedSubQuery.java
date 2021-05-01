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
 * 嵌套子查询条件
 * @param <R> 实体类型
 * @author wvkity
 * @created 2021-04-20
 * @since 1.0.0
 */
public class NestedSubQuery<R> extends AbstractNestedSubCriteria<R> {

    private static final long serialVersionUID = 5220695490790534834L;

    public NestedSubQuery(final AbstractQueryCriteria<R> root) {
        this(root, null);
    }

    public NestedSubQuery(final AbstractQueryCriteria<R> root, final String alias) {
        this.master = root;
        this.entityClass = root.getEntityClass();
        this.parameterSequence = root.parameterSequence;
        this.parameterValueMapping = root.parameterValueMapping;
        this.notMatchingWithThrows = root.notMatchingWithThrows;
        this.tableAliasSequence = root.tableAliasSequence;
        this.useAlias = root.useAlias;
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.conditionConverter = new ConditionConverter(this);
        this.segmentManager = new StandardFragmentManager(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected NestedSubQuery<R> newInstance() {
        final NestedSubQuery<R> instance = new NestedSubQuery<>((AbstractQueryCriteria<R>) this.master);
        instance.clone(this);
        return instance;
    }
}
