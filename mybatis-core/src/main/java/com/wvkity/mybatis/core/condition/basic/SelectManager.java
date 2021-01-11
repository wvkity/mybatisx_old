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
package com.wvkity.mybatis.core.condition.basic;

import com.wvkity.mybatis.core.condition.basic.select.BasicSelect;
import com.wvkity.mybatis.core.condition.basic.select.ImmediateSelect;
import com.wvkity.mybatis.core.condition.basic.select.Select;
import com.wvkity.mybatis.core.condition.criteria.AbstractQueryCriteria;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.handler.TableHandler;
import com.wvkity.mybatis.core.immutable.ImmutableList;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.segment.Fragment;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 查询列管理器
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public class SelectManager implements Fragment {

    private static final long serialVersionUID = -4508461674924936576L;
    /**
     * 查询条件包装对象
     */
    private final AbstractQueryCriteria<?> criteria;
    /**
     * 查询列
     */
    private final List<Select> selects = new ArrayList<>();
    /**
     * 查询列缓存
     */
    private List<Select> selectCache;
    /**
     * 过滤查询属性
     */
    private final Set<String> excludeProperties = new HashSet<>(8);
    /**
     * 过滤查询字段
     */
    private final Set<String> excludeColumns = new HashSet<>(8);
    /**
     * 标识SQL片段是否已生成
     */
    private final AtomicBoolean cached = new AtomicBoolean(false);
    /**
     * SQL片段
     */
    private String segment = "";

    public SelectManager(AbstractQueryCriteria<?> criteria) {
        this.criteria = criteria;
    }

    /**
     * 添加多个查询列
     * @param selects {@link Select}列表
     * @return {@link SelectManager}
     */
    public SelectManager select(final Select... selects) {
        return this.select(Objects.asList(selects));
    }

    /**
     * 添加多个查询列
     * @param selects {@link Select}集合
     * @return {@link SelectManager}
     */
    public SelectManager select(final Collection<Select> selects) {
        if (Objects.isNotEmpty(selects)) {
            final List<Select> its = selects.stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (Objects.isNotEmpty(its)) {
                this.cached.compareAndSet(true, false);
                for (Select it : selects) {
                    this.selects.add(it);
                }
            }
        }
        return this;
    }

    /**
     * 过滤查询列
     * @param property 属性
     * @return {@link SelectManager}
     */
    public SelectManager exclude(final String property) {
        if (Objects.isNotBlank(property)) {
            this.excludeProperties.add(property);
            this.cached.compareAndSet(true, false);
        }
        return this;
    }

    /**
     * 过滤查询列
     * @param column 字段
     * @return {@link SelectManager}
     */
    public SelectManager excludeCol(final String column) {
        if (Objects.isNotBlank(column)) {
            this.excludeColumns.add(column);
            this.cached.compareAndSet(true, false);
        }
        return this;
    }

    /**
     * 检查是否存在查询列
     * @return boolean
     */
    public boolean hasSelects() {
        return Objects.isNotEmpty(this.selects);
    }

    /**
     * 获取所有{@link Select}
     * @return {@link Select}集合
     */
    public List<Select> getSelects() {
        if (this.cached.get()) {
            return ImmutableList.of(this.selectCache);
        }
        // 检查是否存在指定查询列
        final List<Select> tmp;
        if (this.hasSelects()) {
            tmp = this.selects.stream().filter(it -> {
                if (it instanceof BasicSelect) {
                    return this.accept(this.excludeProperties, ((BasicSelect) it).getProperty(), false);
                } else if (it instanceof ImmediateSelect) {
                    return this.accept(this.excludeColumns, it.getColumn(), true);
                }
                return Objects.nonNull(it);
            }).collect(Collectors.toList());
        } else {
            // 查询所有字段
            final List<Column> list = TableHandler.getColumns(this.criteria.getEntityClass(), __ -> true);
            if (Objects.isNotEmpty(list)) {
                tmp = list.stream().filter(it -> this.accept(this.excludeProperties, it.getProperty(), false)
                    && this.accept(this.excludeColumns, it.getColumn(), true)).map(it ->
                    BasicSelect.create().column(it).criteria(this.criteria).build()).collect(Collectors.toList());
            } else {
                return ImmutableList.of();
            }
        }
        if (Objects.isNotEmpty(tmp)) {
            this.selectCache = ImmutableList.of(tmp);
            this.cached.compareAndSet(false, true);
            return ImmutableList.of(tmp);
        }
        return ImmutableList.of();
    }

    /**
     * 检查字符串是否在指定的集合中
     * @param sources    字符串集合
     * @param target     目标字符串
     * @param ignoreCase 是否忽略大小写比较
     * @return boolean
     */
    private boolean accept(final Collection<String> sources, final String target, final boolean ignoreCase) {
        if (Objects.isEmpty(sources)) {
            return true;
        }
        if (ignoreCase) {
            return sources.stream().noneMatch(target::equalsIgnoreCase);
        } else {
            return sources.stream().noneMatch(target::equals);
        }
    }

    @Override
    public String getSegment() {
        if (this.cached.get() && Objects.isNotBlank(this.segment)) {
            return segment;
        }
        this.segment = getSegment(true);
        return this.segment;
    }

    /**
     * 获取查询SQL片段
     * @param isQuery 是否应用为查询
     * @return SQL片段
     */
    public String getSegment(final boolean isQuery) {
        final List<Select> selects = this.getSelects();
        if (Objects.isNotEmpty(selects)) {
            return selects.stream().map(it -> it.getSegment(isQuery)).filter(Objects::isNotBlank)
                .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return Constants.EMPTY;
    }
}
