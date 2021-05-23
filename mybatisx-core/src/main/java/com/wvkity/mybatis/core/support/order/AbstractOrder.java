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
package com.wvkity.mybatis.core.support.order;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.fragment.AbstractFragmentList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象排序
 * @param <E> 碎片类型
 * @author wvkity
 * @created 2021-01-12
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractOrder<E> extends AbstractFragmentList<E> implements Order {

    protected static final String ASC = " ASC";
    protected static final String DESC = " DESC";
    /**
     * {@link Criteria}
     */
    protected Criteria<?> criteria;
    /**
     * 表别名
     */
    protected String tableAlias;
    /**
     * 排序方式
     */
    protected boolean ascending;
    /**
     * 空值排序
     */
    protected NullOrdering nullOrdering;

    @Override
    public String getSegment() {
        if (!this.isEmpty()) {
            final List<String> columns = this.distinct(this.fragments);
            if (Objects.isNotEmpty(columns)) {
                final String tmp = Objects.isNotBlank(this.tableAlias) ? tableAlias :
                    Objects.nonNull(this.criteria) ? this.criteria.as() : "";
                final String as = Objects.isBlank(tmp) ? "" : (tmp + Constants.DOT);
                final String sortMode = this.ascending ? ASC : DESC;
                final String nullStr = this.getNullOrderingStr();
                return columns.stream().map(it -> as + it + sortMode + nullStr)
                    .collect(Collectors.joining(Constants.COMMA_SPACE));
            }
        }
        return "";
    }

    protected String getNullOrderingStr() {
        return this instanceof StandardOrder && Objects.nonNull(this.nullOrdering) ?
            (nullOrdering == NullOrdering.FIRST ? " NULLS FIRST" : " NULLS LAST") : Constants.EMPTY;
    }

    public NullOrdering getNullOrdering() {
        return nullOrdering;
    }

    public AbstractOrder<E> nullOrdering(NullOrdering nullOrdering) {
        this.nullOrdering = nullOrdering;
        return this;
    }
}
