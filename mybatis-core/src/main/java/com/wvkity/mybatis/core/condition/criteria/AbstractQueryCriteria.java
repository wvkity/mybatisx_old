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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.core.condition.basic.select.StandardSelect;
import com.wvkity.mybatis.core.condition.basic.select.ImmediateSelect;
import com.wvkity.mybatis.core.condition.basic.SelectManager;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.handler.TableHandler;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;
import com.wvkity.mybatis.executor.resultset.EmbeddedResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 抽象查询条件
 * @param <T> 泛型类
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractQueryCriteria<T> extends AbstractCriteria<T> implements
    QueryWrapper<T, AbstractQueryCriteria<T>>, EmbeddedResult {

    // region Basic fields

    /**
     * 是否使用属性名作为别名
     */
    protected boolean usePropertyAsAlias = false;
    /**
     * 查询列管理器
     */
    protected SelectManager selectManager;
    /**
     * 结果集
     */
    protected String resultMap;
    /**
     * 返回值类型
     */
    protected Class<?> resultType;

    // endregion

    // region Select columns

    @Override
    public AbstractQueryCriteria<T> filtrate(Predicate<Column> accept) {
        final List<Column> columns = TableHandler.getColumns(this.entityClass, accept);
        if (Objects.isNotEmpty(columns)) {
            for (Column column : columns) {
                if (Objects.nonNull(column)) {
                    this.selectManager.select(StandardSelect.create().column(column).criteria(this).build());
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> select(Property<T, ?> property, String alias) {
        this.selectManager.select(StandardSelect.create().property(property).criteria(this).alias(alias).build());
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selects(Collection<Property<T, ?>> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Property<T, ?> property : properties) {
                if (Objects.nonNull(property)) {
                    this.select(property);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> select(String property, String alias) {
        this.selectManager.select(StandardSelect.create().property(property).criteria(this).alias(alias).build());
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selects(String... properties) {
        if (!Objects.isEmpty(properties)) {
            for (String property : properties) {
                if (Objects.isNotBlank(property)) {
                    this.select(property);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selects(Map<String, String> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                final String property = entry.getValue();
                if (Objects.isNotBlank(property)) {
                    this.select(property, entry.getKey());
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selectCol(String column, String alias) {
        this.selectManager.select(ImmediateSelect.create().criteria(this).column(column).alias(alias).build());
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selectCols(Collection<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (String column : columns) {
                if (Objects.isNotBlank(column)) {
                    this.selectCol(column);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> selectCols(Map<String, String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                final String column = entry.getValue();
                if (Objects.isNotBlank(column)) {
                    this.selectCol(column, entry.getKey());
                }
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> exclude(Property<T, ?> property) {
        return this.exclude(this.convert(property));
    }

    @Override
    public AbstractQueryCriteria<T> exclude(String property) {
        if (Objects.isNotBlank(property)) {
            this.selectManager.exclude(property);
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> excludeCol(String column) {
        if (Objects.isNotBlank(column)) {
            this.selectManager.excludeCol(column);
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractQueryCriteria<T> excludes(Property<T, ?>... properties) {
        if (!Objects.isEmpty(properties)) {
            for (Property<T, ?> property : properties) {
                this.exclude(property);
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> excludes(Collection<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (String property : properties) {
                this.exclude(property);
            }
        }
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> excludeCols(Collection<String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (String column : columns) {
                this.excludeCol(column);
            }
        }
        return this;
    }

    // endregion

    // region Other methods

    @Override
    protected void inits(String alias) {
        super.inits(alias);
        this.selectManager = new SelectManager(this);
    }

    @Override
    public AbstractQueryCriteria<T> as(String alias) {
        this.tableAlias.set(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        return this;
    }

    @Override
    public AbstractQueryCriteria<T> useAlias() {
        return this.useAlias(true);
    }

    @Override
    public AbstractQueryCriteria<T> useAlias(boolean used) {
        this.useAlias.compareAndSet(!used, used);
        return this;
    }

    @Override
    public boolean isUsePropertyAsAlias() {
        return this.usePropertyAsAlias;
    }

    @Override
    public AbstractQueryCriteria<T> usePropertyAsAlias(boolean used) {
        this.usePropertyAsAlias = used;
        return this;
    }

    @Override
    public String getResultMap() {
        return this.resultMap;
    }

    @Override
    public AbstractQueryCriteria<T> resultMap(String resultMap) {
        this.resultMap = resultMap;
        return this;
    }

    @Override
    public Class<?> getResultType() {
        return this.resultType;
    }

    @Override
    public AbstractQueryCriteria<T> resultType(Class<?> resultType) {
        this.resultType = resultType;
        return this;
    }

    @Override
    public String getSelectSegment() {
        return this.selectManager.getSegment();
    }

    // endregion
}
