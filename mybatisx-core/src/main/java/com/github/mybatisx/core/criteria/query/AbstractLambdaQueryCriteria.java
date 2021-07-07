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
package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.criteria.support.AbstractLambdaCriteria;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.core.support.select.Selection;
import com.github.mybatisx.core.support.select.StandardSelection;
import com.github.mybatisx.support.basic.Matched;

import java.util.Map;

/**
 * 抽象基础条件/查询容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractLambdaQueryCriteria<T, C extends LambdaQueryCriteria<T, C>> extends
    AbstractLambdaCriteria<T, C> implements LambdaQueryCriteria<T, C> {

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
    public C useTabAlias(boolean using) {
        this.useAlias.set(using);
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
    public C mapKey(final Property<T, ?> property) {
        final String prop = this.toProperty(property);
        if (this.propAsAlias) {
            return this.mapKey(prop);
        }
        return this.mapKey(this.toColumn(prop));
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
    public C rangeWithRow(final long rowStart, final long rowEnd) {
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        return this.self();
    }

    @Override
    public C rangeWithPage(final long pageStart, final long pageEnd, final long pageSize) {
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        this.pageSize = pageSize;
        return this.self();
    }

    @Override
    public C foreign(ExtCriteria<?> query) {
        this.addForeign(query);
        return this.self();
    }

    // endregion

    // region Select column methods

    @Override
    public C select(Selection selection) {
        this.fragmentManager.select(selection);
        return this.self();
    }

    @Override
    public C select(Property<T, ?> property, String alias) {
        return this.select(this.toProperty(property), alias);
    }

    @Override
    public C select(String property, String alias) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.select(new StandardSelection(this, null, column.getColumn(), alias,
                column.getProperty(), Matched.STANDARD));
        }
        return this.self();
    }

    @Override
    public C ignore(Property<T, ?> property) {
        return this.ignore(this.toProperty(property));
    }

    @Override
    public C ignore(String property) {
        this.fragmentManager.exclude(property);
        return this.self();
    }

    // endregion

}
