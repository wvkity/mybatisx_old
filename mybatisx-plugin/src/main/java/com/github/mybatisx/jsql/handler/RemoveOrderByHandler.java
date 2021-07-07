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
package com.github.mybatisx.jsql.handler;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.immutable.ImmutableLinkedSet;
import com.github.mybatisx.basic.utils.Objects;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 移除order by处理器
 * @author wvkity
 * @created 2021-05-06
 * @since 1.0.0
 */
public class RemoveOrderByHandler {

    public static final Pattern DEF_REGEX_ORDER_BY = Pattern.compile("(?i)(((?<!/\\*)(\\s+order\\s+by))\\s*)",
        Pattern.CASE_INSENSITIVE);
    public static final Pattern DEF_REGEX_ORDER_BY_CHECK = Pattern.compile("^(?i)(.*(\\s+order\\s+by\\s+).*)$",
        Pattern.CASE_INSENSITIVE);
    public static final Pattern DEF_REGEX_KEEP_ORDER_BY_CHECK = Pattern.compile("(?i)(.*((\\s+order\\s+by\\s+.*)" +
            "\\s+/\\*keep\\s+orderby\\*/).*?)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern DEF_REGEX_KEEP_ORDER_BY =
        Pattern.compile("(?i)((?<!/\\*)(\\s+((order\\s+by\\s+)(((?<!/\\*\\s{0,15}keep orderby).)*)\\s*" +
                "(/\\*keep\\s+orderby\\*/)\\s*)))", Pattern.CASE_INSENSITIVE);
    /**
     * 原SQL
     */
    private final String originalSql;
    /**
     * {@link Select}
     */
    private final Select select;
    /**
     * 是否存在order by语句
     */
    private boolean hasOrderBy;
    /**
     * order by出现次数
     */
    private int orderBySize = 0;
    /**
     * 是否存在保持order by注释
     */
    private boolean hasKeepOrderBy;
    /**
     *
     */
    private int keepOrderBySize = 0;
    /**
     * keep order by字段列表
     */
    private final Set<String> keepOrderByElements = new HashSet<>(5);

    public RemoveOrderByHandler(String originalSql, Select select) {
        this.originalSql = originalSql;
        this.select = select;
        this.parse();
    }

    /**
     * 解析原SQL
     */
    protected void parse() {
        this.hasOrderBy = DEF_REGEX_ORDER_BY_CHECK.matcher(this.originalSql).matches();
        if (this.hasOrderBy) {
            final Matcher orderMatcher = DEF_REGEX_ORDER_BY.matcher(this.originalSql);
            int orderSize = 0;
            while (orderMatcher.find()) {
                orderSize++;
            }
            this.orderBySize = orderSize;
            this.hasKeepOrderBy = DEF_REGEX_KEEP_ORDER_BY_CHECK.matcher(this.originalSql).matches();
            if (this.hasKeepOrderBy) {
                final Matcher keepMatcher = DEF_REGEX_KEEP_ORDER_BY.matcher(this.originalSql);
                final int groupIndex = 5;
                int keepSize = 0;
                while (keepMatcher.find()) {
                    this.cacheKeepOrderByElements(keepMatcher.group(groupIndex));
                    keepSize++;
                }
                this.keepOrderBySize = keepSize;
            }
        }
    }

    /**
     * 保存keep order by字段
     * @param orderBy 排序字段
     */
    protected void cacheKeepOrderByElements(final String orderBy) {
        this.keepOrderByElements.add(Arrays.stream(orderBy.split(Constants.COMMA)).map(String::trim)
            .collect(Collectors.joining(Constants.COMMA_SPACE)));
    }

    /**
     * 尝试移除order by
     */
    public void handleTryRemove() {
        if (this.hasOrderBy && (!this.hasKeepOrderBy || this.orderBySize != this.keepOrderBySize)) {
            final SelectBody selectBody = this.select.getSelectBody();
            this.handleSelectBodyTryRemove(selectBody);
            final List<WithItem> items;
            if (isNotEmpty((items = this.select.getWithItemsList()))) {
                items.forEach(this::handleSelectBodyTryRemove);
            }
        }
    }

    /**
     * 处理{@link SelectBody}中的order by
     * @param selectBody {@link SelectBody}
     */
    public void handleSelectBodyTryRemove(final SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            this.handlePlainSelectTryRemove((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            Optional.ofNullable(((WithItem) selectBody).getSelectBody())
                .ifPresent(this::handleSelectBodyTryRemove);
        } else {
            final SetOperationList sol = (SetOperationList) selectBody;
            final List<SelectBody> items;
            if (this.isNotEmpty((items = sol.getSelects()))) {
                items.forEach(this::handleSelectBodyTryRemove);
            }
            if (this.canRemove(sol.getOrderByElements())) {
                sol.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理{@link PlainSelect}中的order by
     * @param select {@link PlainSelect}
     */
    public void handlePlainSelectTryRemove(final PlainSelect select) {
        if (this.canRemove(select.getOrderByElements())) {
            select.setOrderByElements(null);
        }
        Optional.ofNullable(select.getFromItem()).ifPresent(this::handleFromItemTryRemove);
        final List<Join> joins;
        if (isNotEmpty((joins = select.getJoins()))) {
            joins.stream().filter(Objects::nonNull).map(Join::getRightItem)
                .forEach(this::handleFromItemTryRemove);
        }
    }

    /**
     * 处理{@link FromItem}中的order by
     * @param fromItem {@link FromItem}
     */
    public void handleFromItemTryRemove(final FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            final SubJoin sj = (SubJoin) fromItem;
            final List<Join> joins;
            if (this.isNotEmpty((joins = sj.getJoinList()))) {
                joins.stream().filter(Objects::nonNull).map(Join::getRightItem)
                    .forEach(this::handleFromItemTryRemove);
            }
            Optional.ofNullable(sj.getLeft()).ifPresent(this::handleFromItemTryRemove);
        } else if (fromItem instanceof SubSelect) {
            Optional.ofNullable(((SubSelect) fromItem).getSelectBody())
                .ifPresent(this::handleSelectBodyTryRemove);
        } else if (fromItem instanceof LateralSubSelect) {
            Optional.ofNullable(((LateralSubSelect) fromItem).getSubSelect())
                .map(SubSelect::getSelectBody).ifPresent(this::handleSelectBodyTryRemove);
        }
    }

    /**
     * 检查是否可移除orderby
     * @param items {@link OrderByElement}列表
     * @return boolean
     */
    public boolean canRemove(final List<OrderByElement> items) {
        if (this.notHasOrderByArg(items)) {
            final String orderBy = items.stream().map(OrderByElement::toString)
                .collect(Collectors.joining(Constants.COMMA_SPACE));
            return !this.keepOrderByElements.contains(orderBy);
        }
        return false;
    }

    /**
     * 检查OrderBy是否存在参数，有参数则不能去掉
     * @param items {@link OrderByElement}列表
     * @return boolean
     */
    public boolean notHasOrderByArg(final List<OrderByElement> items) {
        if (this.isNotEmpty(items)) {
            for (OrderByElement it : items) {
                if (it.toString().contains("?")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private <E> boolean isNotEmpty(final Collection<E> items) {
        return items != null && !items.isEmpty();
    }

    public boolean hasOrderBy() {
        return this.hasOrderBy;
    }

    public int getOrderBySize() {
        return this.orderBySize;
    }

    public boolean hasKeepOrderBy() {
        return this.hasKeepOrderBy;
    }

    public int getKeepOrderBySize() {
        return this.keepOrderBySize;
    }

    public Set<String> getKeepOrderByElements() {
        return ImmutableLinkedSet.of(this.keepOrderByElements);
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public Select getSelect() {
        return select;
    }
}
