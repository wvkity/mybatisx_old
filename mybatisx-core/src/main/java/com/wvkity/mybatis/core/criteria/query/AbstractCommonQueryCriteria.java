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
package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.criteria.support.AbstractCommonCriteria;
import com.wvkity.mybatis.core.support.select.NativeSelection;
import com.wvkity.mybatis.core.support.select.Selection;
import com.wvkity.mybatis.core.support.select.StandardSelection;
import com.wvkity.mybatis.support.basic.Matched;

import java.util.Map;

/**
 * 抽象基本条件/查询容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractCommonQueryCriteria<T, C extends CommonQueryCriteria<T, C>> extends
    AbstractCommonCriteria<T, C> implements CommonQueryCriteria<T, C> {

    // region Basic methods

    @Override
    public C reference(final String reference) {
        this.reference.compareAndSet(Constants.EMPTY, Objects.isBlank(reference) ? Constants.EMPTY : reference);
        return this.self();
    }

    @Override
    public C distinct(final boolean distinct) {
        this.distinct = distinct;
        return this.self();
    }

    @Override
    public C containsFunc(final boolean contains) {
        this.containsFunc = contains;
        return this.self();
    }

    @Override
    public C onlyFunc(final boolean only) {
        this.onlyFunc = only;
        return this.self();
    }

    @Override
    public C usePropAlias(final boolean used) {
        this.propAsAlias = used;
        return this.self();
    }

    @Override
    public C keepOrderBy(boolean keep) {
        this.keepOrderBy = keep;
        return this.self();
    }

    @Override
    public C resultMap(final String resultMap) {
        this.resultMap = resultMap;
        return this.self();
    }

    @Override
    public C resultType(final Class<?> resultType) {
        this.resultType = resultType;
        return this.self();
    }

    @Override
    public C mapKey() {
        return this.self();
    }

    @Override
    public C mapKey(final String mapKey) {
        this.mapKey = mapKey;
        return this.self();
    }

    /**
     * 设置map键
     * @param column {@link Column}
     * @return {@code this}
     */
    protected C mapKey(final Column column) {
        if (column != null) {
            this.mapKey(column.getColumn());
        }
        return this.self();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public C mapType(final Class<? extends Map> mapImplClass) {
        this.mapType = mapImplClass;
        return this.self();
    }

    @Override
    public C range(final long start, final long end) {
        this.rowStart = start;
        this.rowEnd = end;
        return this.self();
    }

    @Override
    public C range(final long start, final long end, final long size) {
        this.pageStart = start;
        this.pageEnd = end;
        this.pageSize = size;
        return this.self();
    }

    // endregion

    // region Select column methods

    @Override
    public C colSelect(String column, String alias) {
        if (Objects.isNotBlank(column)) {
            this.select(new StandardSelection(this, null, column, alias, Matched.IMMEDIATE));
        }
        return this.self();
    }

    @Override
    public C nativeSelect(String sql, String alias) {
        if (Objects.isNotBlank(sql)) {
            this.select(new NativeSelection(this,sql, alias));
        }
        return this.self();
    }

    @Override
    public C select(Selection selection) {
        this.fragmentManager.select(selection);
        return this.self();
    }

    @Override
    public C colIgnore(String column) {
        this.fragmentManager.colExclude(column);
        return this.self();
    }

    @Override
    public String getWhereSegment(String groupByReplacement) {
        return this.intactWhereString(groupByReplacement);
    }

    // endregion

}
