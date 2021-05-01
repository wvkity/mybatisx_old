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
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 抽象更新条件
 * @author wvkity
 * @created 2021-03-21
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractUpdateCriteria<T> extends AbstractCriteria<T> implements
    UpdateWrapper<T, AbstractUpdateCriteria<T>> {

    private static final Logger log = LoggerFactory.getLogger(AbstractUpdateCriteria.class);
    protected final Map<Column, Object> updateColumnsOfWrap = new ConcurrentHashMap<>();
    protected final Map<String, Object> updateColumnsOfOrg = new ConcurrentHashMap<>();
    /**
     * {@code Map<Property, Column>}
     */
    protected final Map<String, String> updateProperties = new ConcurrentHashMap<>();
    protected final Set<String> updateColumns = new CopyOnWriteArraySet<>();
    protected String updateSegment;

    @Override
    public <V> AbstractUpdateCriteria<T> set(Property<T, V> property, V value) {
        return this.set(this.convert(property), value);
    }

    @Override
    public <V> AbstractUpdateCriteria<T> setIfAbsent(Property<T, V> property, V value) {
        return this.setIfAbsent(this.convert(property), value);
    }

    @Override
    public AbstractUpdateCriteria<T> setIfAbsent(String property, Object value) {
        if (Objects.isNotBlank(property) && !this.updateProperties.containsKey(property)) {
            this.set(property, value);
        }
        return this;
    }

    @Override
    public AbstractUpdateCriteria<T> set(String property, Object value) {
        final Column column = this.findColumn(property);
        if (column != null) {
            if (column.isUpdatable()) {
                this.updateProperties.put(column.getProperty(), column.getColumn());
                this.updateColumnsOfWrap.put(column, value);
                this.updateColumns.add(column.getColumn().toUpperCase(Locale.ENGLISH));
            } else {
                final Table table = TableHelper.getTable(this.entityClass);
                log.warn("The \"" + column.getColumn() + "\" column in the \"" + table.getName()
                    + "(" + this.entityClass.getName() + ")\" table does not support updates and is automatically " +
                    "ignored by the system.");
            }
        }
        return this;
    }

    @Override
    public AbstractUpdateCriteria<T> colSetIfAbsent(String column, Object value) {
        if (Objects.isNotBlank(column) && !this.updateColumns.contains(column.toUpperCase(Locale.ENGLISH))) {
            this.colSet(column, value);
        }
        return this;
    }

    @Override
    public AbstractUpdateCriteria<T> colSet(String column, Object value) {
        if (Objects.isNotBlank(column)) {
            final Column col = this.findOrgColumn(column);
            if (col != null) {
                if (col.isUpdatable()) {
                    this.updateProperties.put(col.getProperty(), col.getColumn());
                    this.updateColumnsOfWrap.put(col, value);
                    this.updateColumns.add(column.toUpperCase(Locale.ENGLISH));
                } else {
                    final Table table = TableHelper.getTable(this.entityClass);
                    log.warn("The \"" + col.getColumn() + "\" column in the \"" + table.getName()
                        + "(" + this.entityClass.getName() + ")\" table does not support updates and is " +
                        "automatically ignored by the system.");
                }
            } else {
                this.updateColumns.add(column.toUpperCase(Locale.ENGLISH));
                this.updateColumnsOfOrg.put(column, value);
            }
        }
        return this;
    }

    @Override
    public AbstractUpdateCriteria<T> colSet(Map<String, Object> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, Object> entry: columns.entrySet()) {
                this.colSet(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public String getUpdateSegment() {
        final boolean isNotEmptyOfWrap = Objects.isNotEmpty(this.updateColumnsOfWrap);
        final boolean isNotEmptyOfOrg = Objects.isNotEmpty(this.updateColumnsOfOrg);
        if (isNotEmptyOfOrg || isNotEmptyOfWrap) {
            final List<String> segments = new ArrayList<>();
            if (isNotEmptyOfWrap) {
                this.updateColumnsOfWrap.forEach((c, v) -> segments.add(Scripts.convertToConditionArg(Symbol.EQ,
                    Slot.NONE, null, c, this.defPlaceholder(v))));
            }
            if (isNotEmptyOfOrg) {
                this.updateColumnsOfOrg.forEach((c, v) -> segments.add(Scripts.convertToConditionArg(Symbol.EQ,
                    Slot.NONE, null, c, this.defPlaceholder(v))));
            }
            return String.join(Constants.COMMA_SPACE, segments);
        }
        return Constants.EMPTY;
    }
}
