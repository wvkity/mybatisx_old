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
package com.wvkity.mybatis.core.condition.basic.builder;

import com.wvkity.mybatis.core.condition.basic.select.Selection;
import com.wvkity.mybatis.core.condition.criteria.Criteria;

/**
 * 抽象查询列构建器
 * @param <T> 查询列类型
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public abstract class AbstractSelectBuilder<T extends Selection, E> implements SelectBuilder<T, E> {

    /**
     * 查询条件包装器
     */
    protected Criteria<?> criteria;
    /**
     * 表别名
     */
    protected String tableAlias;
    /**
     * 字段
     */
    protected E column;
    /**
     * 字段别名
     */
    protected String alias;

    /**
     * 获取字段
     * @return 字段
     */
    public E getColumn() {
        return this.column;
    }

    public AbstractSelectBuilder<T, E> criteria(Criteria<?> criteria) {
        this.criteria = criteria;
        return this;
    }

    public AbstractSelectBuilder<T, E> tableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
        return this;
    }

    public AbstractSelectBuilder<T, E> column(E column) {
        this.column = column;
        return this;
    }

    public AbstractSelectBuilder<T, E> alias(String alias) {
        this.alias = alias;
        return this;
    }
}
