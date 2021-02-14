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
package com.wvkity.mybatis.core.plugin.paging.parser;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 总记录数SQL解析器
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
public class RecordsSqlParser {

    public static final String KEEP_ORDER_BY = "/*keep orderby*/";
    private static final Alias TAB_ALIAS;
    /**
     * 忽略聚合函数
     */
    private final Set<String> IGNORE_FUNCTION_CACHE = new HashSet<>();
    /**
     * 不通过聚合函数
     */
    private final Set<String> NOT_THROUGH_FUNCTION_CACHE = new HashSet<>();
    /**
     * 聚合函数缓存
     */
    private static final Set<String> AGGREGATE_FUNCTION_CACHE = new HashSet<>();

    static {
        TAB_ALIAS = new Alias("TMP_TAB_RECORD");
        TAB_ALIAS.setUseAs(false);
        // 聚合函数列表
        AGGREGATE_FUNCTION_CACHE.add("APPROX_COUNT_DISTINCT");
        AGGREGATE_FUNCTION_CACHE.add("ARRAY_AGG");
        AGGREGATE_FUNCTION_CACHE.add("AVG");
        AGGREGATE_FUNCTION_CACHE.add("BIT_");
        AGGREGATE_FUNCTION_CACHE.add("BOOL_");
        AGGREGATE_FUNCTION_CACHE.add("CHECKSUM_AGG");
        AGGREGATE_FUNCTION_CACHE.add("COLLECT");
        AGGREGATE_FUNCTION_CACHE.add("CORR");
        AGGREGATE_FUNCTION_CACHE.add("COUNT");
        AGGREGATE_FUNCTION_CACHE.add("COVAR");
        AGGREGATE_FUNCTION_CACHE.add("CUME_DIST");
        AGGREGATE_FUNCTION_CACHE.add("DENSE_RANK");
        AGGREGATE_FUNCTION_CACHE.add("EVERY");
        AGGREGATE_FUNCTION_CACHE.add("FIRST");
        AGGREGATE_FUNCTION_CACHE.add("GROUP");
        AGGREGATE_FUNCTION_CACHE.add("JSON_");
        AGGREGATE_FUNCTION_CACHE.add("LAST");
        AGGREGATE_FUNCTION_CACHE.add("LISTAGG");
        AGGREGATE_FUNCTION_CACHE.add("MAX");
        AGGREGATE_FUNCTION_CACHE.add("MEDIAN");
        AGGREGATE_FUNCTION_CACHE.add("MIN");
        AGGREGATE_FUNCTION_CACHE.add("PERCENT_");
        AGGREGATE_FUNCTION_CACHE.add("RANK");
        AGGREGATE_FUNCTION_CACHE.add("REGR_");
        AGGREGATE_FUNCTION_CACHE.add("SELECTIVITY");
        AGGREGATE_FUNCTION_CACHE.add("STATS_");
        AGGREGATE_FUNCTION_CACHE.add("STD");
        AGGREGATE_FUNCTION_CACHE.add("STRING_AGG");
        AGGREGATE_FUNCTION_CACHE.add("SUM");
        AGGREGATE_FUNCTION_CACHE.add("SYS_OP_ZONE_ID");
        AGGREGATE_FUNCTION_CACHE.add("SYS_XMLAGG");
        AGGREGATE_FUNCTION_CACHE.add("VAR");
        AGGREGATE_FUNCTION_CACHE.add("XMLAGG");
    }

    /**
     * 智能转换成查询总记录数SQL语句
     * @param originalSql 原SQL语句
     * @return 总记录数SQL
     */
    public String smartParse(final String originalSql) {
        return this.smartParse(originalSql, "0");
    }

    /**
     * 智能转换成查询总记录数SQL语句
     * @param originalSql 原SQL语句
     * @param colName     列名
     * @return 总记录数SQL
     */
    public String smartParse(final String originalSql, final String colName) {
        if (originalSql.contains(KEEP_ORDER_BY)) {
            return toSimpleQueryRecordSql(originalSql, colName);
        }
        final Statement stmt;
        try {
            stmt = CCJSqlParserUtil.parse(originalSql);
        } catch (Exception ignore) {
            return toSimpleQueryRecordSql(originalSql, colName);
        }
        final Select select = (Select) stmt;
        final SelectBody selectBody = select.getSelectBody();
        try {
            this.handleSelectBodyTryRemoveOrderBy(selectBody);
        } catch (Exception ignore) {
            return this.toSimpleQueryRecordSql(originalSql, colName);
        }
        final List<WithItem> items;
        if (isNotEmpty((items = select.getWithItemsList()))) {
            items.forEach(this::handleSelectBodyTryRemoveOrderBy);
        }
        this.toQueryRecordSql(select, colName);
        return select.toString();
    }

    /**
     * 处理{@link SelectBody}中的order by
     * @param selectBody {@link SelectBody}
     */
    public void handleSelectBodyTryRemoveOrderBy(final SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            this.handlePlainSelectTryRemoveOrderBy((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            Optional.ofNullable(((WithItem) selectBody).getSelectBody())
                .ifPresent(this::handleSelectBodyTryRemoveOrderBy);
        } else {
            final SetOperationList sol = (SetOperationList) selectBody;
            final List<SelectBody> items;
            if (isNotEmpty((items = sol.getSelects()))) {
                items.forEach(this::handleSelectBodyTryRemoveOrderBy);
            }
            if (notHasOrderByArg(sol.getOrderByElements())) {
                sol.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理{@link PlainSelect}中的order by
     * @param select {@link PlainSelect}
     */
    public void handlePlainSelectTryRemoveOrderBy(final PlainSelect select) {
        if (this.notHasOrderByArg(select.getOrderByElements())) {
            select.setOrderByElements(null);
        }
        Optional.ofNullable(select.getFromItem()).ifPresent(this::handleFromItemTryRemoveOrderBy);
        final List<Join> joins;
        if (isNotEmpty((joins = select.getJoins()))) {
            joins.stream().filter(Objects::nonNull).map(Join::getRightItem)
                .forEach(this::handleFromItemTryRemoveOrderBy);
        }
    }

    /**
     * 处理{@link FromItem}中的order by
     * @param fromItem {@link FromItem}
     */
    public void handleFromItemTryRemoveOrderBy(final FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            final SubJoin sj = (SubJoin) fromItem;
            final List<Join> joins;
            if (isNotEmpty((joins = sj.getJoinList()))) {
                joins.stream().filter(Objects::nonNull).map(Join::getRightItem)
                    .forEach(this::handleFromItemTryRemoveOrderBy);
            }
            Optional.ofNullable(sj.getLeft()).ifPresent(this::handleFromItemTryRemoveOrderBy);
        } else if (fromItem instanceof SubSelect) {
            Optional.ofNullable(((SubSelect) fromItem).getSelectBody())
                .ifPresent(this::handleSelectBodyTryRemoveOrderBy);
        } else if (fromItem instanceof LateralSubSelect) {
            Optional.ofNullable(((LateralSubSelect) fromItem).getSubSelect())
                .map(SubSelect::getSelectBody).ifPresent(this::handleSelectBodyTryRemoveOrderBy);
        }
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
        }
        return true;
    }

    private <E> boolean isNotEmpty(final Collection<E> items) {
        return items != null && !items.isEmpty();
    }

    /**
     * 转成查询总记录数SQL
     * @param select  {@link Select}
     * @param colName 列名
     */
    public void toQueryRecordSql(final Select select, final String colName) {
        final SelectBody selectBody = select.getSelectBody();
        final List<SelectItem> items = new ArrayList<>();
        items.add(new SelectExpressionItem(new Column("COUNT(" + colName + ") AS RECORDS")));
        if (selectBody instanceof PlainSelect && isSimpleQuery((PlainSelect) selectBody)) {
            ((PlainSelect) selectBody).setSelectItems(items);
        } else {
            PlainSelect ps = new PlainSelect();
            final SubSelect ss = new SubSelect();
            ss.setSelectBody(selectBody);
            ss.setAlias(TAB_ALIAS);
            ps.setFromItem(ss);
            ps.setSelectItems(items);
            select.setSelectBody(ps);
        }
    }

    /**
     * 检查是否为简单查询
     * @param select {@link PlainSelect}
     * @return boolean
     */
    public boolean isSimpleQuery(final PlainSelect select) {
        if (select.getGroupBy() != null) {
            return false;
        }
        if (select.getDistinct() != null) {
            return false;
        }
        for (SelectItem it : select.getSelectItems()) {
            if (it.toString().contains("?")) {
                return false;
            }
            if (it instanceof SelectExpressionItem) {
                final Expression expr = ((SelectExpressionItem) it).getExpression();
                if (expr instanceof Function) {
                    final String funcName = ((Function) expr).getName();
                    if (com.wvkity.mybatis.core.plugin.utils.Objects.isNotBlank(funcName)) {
                        final String name = funcName.toUpperCase(Locale.ENGLISH);
                        if (IGNORE_FUNCTION_CACHE.contains(name)) {
                            continue;
                        }
                        if (NOT_THROUGH_FUNCTION_CACHE.contains(name)) {
                            return false;
                        } else {
                            for (String func : AGGREGATE_FUNCTION_CACHE) {
                                if (name.startsWith(func)) {
                                    NOT_THROUGH_FUNCTION_CACHE.add(name);
                                    return false;
                                }
                            }
                            IGNORE_FUNCTION_CACHE.add(name);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 转换成简单查询总记录数SQL语句
     * @param originalSql 原SQL语句
     * @param colName     列名
     * @return 总记录数SQL
     */
    public String toSimpleQueryRecordSql(final String originalSql, final String colName) {
        return "SELECT COUNT(" + colName + ") RECORDS FROM (" + originalSql + ") TMP_TAB_RECORDS";
    }

}
