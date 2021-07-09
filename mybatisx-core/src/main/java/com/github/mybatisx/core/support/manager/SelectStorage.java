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
package com.github.mybatisx.core.support.manager;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.immutable.ImmutableList;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.support.select.FuncSelection;
import com.github.mybatisx.core.support.select.Selection;
import com.github.mybatisx.core.support.select.StandardSelection;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.support.fragment.AbstractFragmentList;
import com.github.mybatisx.support.helper.TableHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 查询字段存储器
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public class SelectStorage extends AbstractFragmentList<Selection> {

    private static final long serialVersionUID = -208223190434766226L;

    /**
     * 查询条件包装对象
     */
    private final Criteria<?> criteria;
    /**
     * 查询列缓存
     */
    private List<Selection> selectionCache;
    /**
     * 聚合函数
     */
    private final List<Selection> functions = new ArrayList<>(6);
    /**
     * 聚合函数({@code Map<alias, FuncSelection>})
     */
    private final Map<String, FuncSelection> functionCache = new HashMap<>(6);
    /**
     * 过滤查询属性
     */
    private final Set<String> excludeProperties = new HashSet<>(6);
    /**
     * 过滤查询字段
     */
    private final Set<String> excludeColumns = new HashSet<>(6);
    /**
     * 标识SQL片段是否已生成
     */
    private final AtomicBoolean cached = new AtomicBoolean(false);
    /**
     * SQL片段
     */
    private String segment = "";
    /**
     * 查询是否包含聚合函数
     */
    private boolean containsFunc = true;
    /**
     * 是否仅仅查询聚合函数
     */
    private boolean onlyFunc;

    public SelectStorage(Criteria<?> criteria) {
        this.criteria = criteria;
    }

    /**
     * 添加{@link Selection}
     * @param selection {@link Selection}
     * @return {@link SelectStorage}
     */
    public SelectStorage select(final Selection selection) {
        if (selection != null) {
            this.cache(selection);
            this.cached.compareAndSet(true, false);
        }
        return this;
    }

    /**
     * 添加多个{@link Selection}
     * @param selections {@link Selection}列表
     * @return {@link SelectStorage}
     */
    public SelectStorage select(final Collection<Selection> selections) {
        if (Objects.isNotEmpty(selections)) {
            final List<Selection> its = selections.stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (Objects.isNotEmpty(its)) {
                for (Selection it : its) {
                    this.cache(it);
                }
                this.cached.compareAndSet(true, false);
            }
        }
        return this;
    }

    void cache(final Selection selection) {
        if (selection.getMatched() == Matched.FUNCTION) {
            final FuncSelection func = (FuncSelection) selection;
            final String as;
            if (Objects.isNotBlank((as = func.getAlias()))) {
                this.functionCache.put(as, func);
            }
            this.functions.add(func);
        } else {
            this.fragments.add(selection);
        }
    }

    /**
     * 过滤查询列
     * @param property 属性
     * @return {@link SelectStorage}
     */
    public SelectStorage exclude(final String property) {
        if (Objects.isNotBlank(property)) {
            this.excludeProperties.add(property);
            this.cached.set(false);
        }
        return this;
    }

    /**
     * 过滤查询列
     * @param column 字段
     * @return {@link SelectStorage}
     */
    public SelectStorage excludeCol(final String column) {
        if (Objects.isNotBlank(column)) {
            this.excludeColumns.add(column);
            this.cached.set(false);
        }
        return this;
    }

    /**
     * 检查是否存在查询列
     * @return boolean
     */
    public boolean hasSelect() {
        return !this.isEmpty() || Objects.isNotEmpty(this.functions);
    }

    boolean canReadFromCache() {
        if (this.cached.get()) {
            if (this.criteria instanceof ExtCriteria) {
                final ExtCriteria<?> query = (ExtCriteria<?>) this.criteria;
                if (this.onlyFunc == query.isOnlyFunc() && this.containsFunc == query.isContainsFunc()) {
                    return true;
                }
                this.onlyFunc = query.isOnlyFunc();
                this.containsFunc = query.isContainsFunc();
                this.cached.set(false);
            }
        }
        return false;
    }

    List<Selection> getSelectFromSpecified() {
        return this.fragments.stream().filter(it -> {
            if (it instanceof StandardSelection) {
                final Matched matched = it.getMatched();
                if (matched == Matched.STANDARD) {
                    return this.accept(this.excludeProperties, it.getProperty(), false);
                }
                return this.accept(this.excludeColumns, it.getColumn(), true);
            }
            return Objects.nonNull(it);
        }).collect(Collectors.toList());
    }

    /**
     * 获取所有{@link Selection}
     * @return {@link Selection}集合
     */
    public List<Selection> getSelects() {
        if (this.cached.get()) {
            return ImmutableList.of(this.selectionCache);
        }
        // 检查是否存在指定查询列
        final List<Selection> tmp;
        if (this.hasSelect()) {
            if (this.criteria instanceof ExtCriteria) {
                final ExtCriteria<?> query = (ExtCriteria<?>) this.criteria;
                if (query.isOnlyFunc()) {
                    tmp = this.functions;
                } else {
                    tmp = this.getSelectFromSpecified();
                    if (query.isContainsFunc()) {
                        tmp.addAll(this.functions);
                    }
                }
            } else {
                tmp = this.getSelectFromSpecified();
            }
        } else {
            // 查询所有字段
            final List<Column> list = TableHelper.getColumns(this.criteria.getEntityClass(), ignore -> true);
            if (Objects.isNotEmpty(list)) {
                tmp = list.stream().filter(it -> this.accept(this.excludeProperties, it.getProperty(), false)
                    && this.accept(this.excludeColumns, it.getColumn(), true)).map(it ->
                    new StandardSelection(this.criteria, null, it.getColumn(), null,
                        it.getProperty(), Matched.STANDARD)).collect(Collectors.toList());
            } else {
                return ImmutableList.of();
            }
        }
        if (Objects.isNotEmpty(tmp)) {
            this.selectionCache = ImmutableList.of(tmp);
            this.cached.compareAndSet(false, true);
            return this.selectionCache;
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

    /**
     * 获取查询SQL片段
     * @param isQuery 是否应用为查询
     * @return SQL片段
     */
    public String getSegment(final boolean isQuery) {
        final List<Selection> selections = this.getSelects();
        if (Objects.isNotEmpty(selections)) {
            return selections.stream().map(it -> it.getSegment(isQuery)).filter(Objects::isNotBlank)
                .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return Constants.EMPTY;
    }

    /**
     * 获取{@link FuncSelection}
     * @param alias 聚合函数别名
     * @return {@link FuncSelection}
     */
    public FuncSelection getFunc(final String alias) {
        if (Objects.isNotBlank(alias) && Objects.isNotEmpty(this.functions)) {
            if (this.functionCache.containsKey(alias)) {
                return this.functionCache.get(alias);
            } else {
                for (Selection it : this.functions) {
                    if (alias.equals(it.getAlias())) {
                        return (FuncSelection) it;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void add(Selection selection) {
        this.select(selection);
    }

    @Override
    public void addAll(Collection<Selection> selections) {
        this.select(selections);
    }

    @Override
    public String getSegment() {
        if (this.cached.get() && Objects.isNotBlank(this.segment)) {
            return segment;
        }
        this.segment = getSegment(true);
        return this.segment;
    }

    public boolean isCached() {
        return cached.get();
    }
}
