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
package com.wvkity.mybatis.core.support.group;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.fragment.AbstractFragmentList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象分组
 * @author wvkity
 * @created 2021-01-29
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractGroup<E> extends AbstractFragmentList<E> implements Group {

    /**
     * {@link Criteria}
     */
    protected Criteria<?> criteria;
    /**
     * 表别名
     */
    protected String tableAlias;

    /**
     * 获取表别名
     * @return 表别名
     */
    protected String alias() {
        return Objects.isNotBlank(this.tableAlias) ? this.tableAlias : this.criteria == null ? "" : this.criteria.as();
    }

    @Override
    public String getSegment() {
        if (!isEmpty()) {
            final String tmp = this.alias();
            final String alias = Objects.isNotBlank(tmp) ? (tmp + Constants.DOT) : Constants.EMPTY;
            final List<String> columns = this.distinct(this.fragments);
            return columns.stream().map(it -> alias + it).collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return "";
    }
}
