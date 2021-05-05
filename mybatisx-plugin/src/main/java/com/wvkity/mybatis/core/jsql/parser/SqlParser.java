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
package com.wvkity.mybatis.core.jsql.parser;

import com.wvkity.mybatis.basic.utils.Objects;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL解析器
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
public class SqlParser {

    public static final String KEEP_ORDER_BY = "/*keep orderby*/";
    public static final String DEF_REGEX_SELECT_ONE_STR = "^(?i)(\\s*select\\s+1\\s+)(.*)$";
    public static final Pattern DEF_PATTERN_SELECT_ONE =
        Pattern.compile(DEF_REGEX_SELECT_ONE_STR, Pattern.CASE_INSENSITIVE);
    public static final String DEF_REGEX_SELECT_STR = "^(?i)((\\s*select\\s*)(((?!select).)*)(\\s*from)(\\s*.*))$";
    public static final String DEF_PATTERN_PM_STR = "(#\\{[^(#{)]+})";
    public static final Pattern DEF_PATTERN_PM = Pattern.compile(DEF_PATTERN_PM_STR);
    public static final Pattern DEF_PATTERN_MATCHER = Pattern.compile(".*#\\{((?!#\\{).)*}.*");
    public static final Pattern DEF_PATTERN_PM_RESTORE = Pattern.compile("((?<!\\\\)(\\?))");
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
    public String smartCountParse(final String originalSql) {
        return this.smartCountParse(originalSql, "0");
    }

    /**
     * 智能转换成查询总记录数SQL语句
     * @param originalSql 原SQL语句
     * @param colName     列名
     * @return 总记录数SQL
     */
    public String smartCountParse(final String originalSql, final String colName) {
        if (originalSql.contains(KEEP_ORDER_BY)) {
            return this.toSimpleQueryRecordSql(originalSql, colName);
        }
        final Select select;
        try {
            select = parse(originalSql);
            this.handleTryRemoveOrderBy(select);
        } catch (Exception ignore) {
            return this.toSimpleQueryRecordSql(originalSql, colName);
        }
        this.toQueryRecordSql(select, colName);
        return select.toString();
    }

    /**
     * 智能移除order by排序语句
     * @param originalSql 原SQL语句
     * @return 处理后的SQL语句
     */
    public String smartRemoveOrderBy(final String originalSql) {
        if (originalSql.contains(KEEP_ORDER_BY)) {
            return originalSql;
        }
        final ParameterHandler handler = new ParameterHandler(originalSql);
        final Select select;
        try {
            select = parse(handler.replace().isReplaced() ? handler.getReplaceSql() : originalSql);
            this.handleTryRemoveOrderBy(select);
        } catch (Exception ignore) {
            return originalSql;
        }
        return handler.restore(select.toString());
    }

    protected Select parse(final String originalSql) throws JSQLParserException {
        return (Select) CCJSqlParserUtil.parse(originalSql);
    }

    /**
     * 智能转换成exists查询语句
     * @param originalSql 原SQL
     * @return exists查询语句
     */
    public String smartExistsParse(final String originalSql) {
        if (this.isSelectOne(originalSql)) {
            return originalSql;
        }
        final ParameterHandler handler = new ParameterHandler(originalSql);
        final Select select;
        try {
            select = this.parse(handler.replace().isReplaced() ? handler.getReplaceSql() : originalSql);
        } catch (Exception ignore) {
            return this.regexExistsParse(originalSql);
        }
        if (originalSql.contains(KEEP_ORDER_BY)) {
            return this.toSelectOneSql(select, handler);
        }
        try {
            this.handleTryRemoveOrderBy(select);
        } catch (Exception ignore) {
            return this.toSelectOneSql(select, handler);
        }
        return this.toSelectOneSql(select, handler);
    }

    /**
     * 转成select 1 from tab语句
     * @param select {@link Select}
     * @return 新的查询语句
     */
    public String toSelectOneSql(final Select select, final ParameterHandler handler) {
        final SelectBody body = select.getSelectBody();
        final PlainSelect psl = (PlainSelect) body;
        final List<SelectItem> selectItems = new ArrayList<>(1);
        selectItems.add(new SelectExpressionItem(new Column("1")));
        psl.setSelectItems(selectItems);
        return handler.restore(select.toString());
    }

    /**
     * 智能转换成exists查询语句
     * @param originalSql 原SQL
     * @return exists查询语句
     */
    public String regexExistsParse(final String originalSql) {
        if (originalSql != null) {
            if (this.isSelectOne(originalSql)) {
                return originalSql;
            }
            return String.format(originalSql.replaceFirst(DEF_REGEX_SELECT_STR, "$2%s$4$5$6"), "1");
        }
        return null;
    }

    /**
     * 检查是否为select 1语句
     * @param originalSql 原SQL
     * @return boolean
     */
    public boolean isSelectOne(final String originalSql) {
        return originalSql != null && DEF_PATTERN_SELECT_ONE.matcher(originalSql).matches();
    }

    /**
     * 移除order by
     * @param select {@link Select}
     */
    public void handleTryRemoveOrderBy(final Select select) {
        final SelectBody selectBody = select.getSelectBody();
        this.handleSelectBodyTryRemoveOrderBy(selectBody);
        final List<WithItem> items;
        if (isNotEmpty((items = select.getWithItemsList()))) {
            items.forEach(this::handleSelectBodyTryRemoveOrderBy);
        }
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

    /**
     * 参数处理器
     */
    public static class ParameterHandler {

        /**
         * 是否已替换
         */
        private final AtomicBoolean replaced = new AtomicBoolean(false);
        /**
         * 原SQL语句
         */
        private final String originalSql;
        /**
         * 参数集合
         */
        private final Map<Integer, String> params;
        /**
         * 替换后的SQL语句
         */
        private String replaceSql;

        public ParameterHandler(String originalSql) {
            this.originalSql = originalSql;
            this.params = new HashMap<>();
        }

        /**
         * 替换原SQL语句
         * @return {@link ParameterHandler}
         */
        public ParameterHandler replace() {
            final StringBuffer buffer = new StringBuffer(this.originalSql.length());
            if (DEF_PATTERN_MATCHER.matcher(this.originalSql).matches()) {
                int normal = -1;
                Integer index = normal;
                final Matcher matcher = DEF_PATTERN_PM.matcher(this.originalSql);
                while (matcher.find()) {
                    final String placeholder = matcher.group();
                    matcher.appendReplacement(buffer, "?");
                    index++;
                    this.params.put(index, placeholder);
                }
                matcher.appendTail(buffer);
                this.replaceSql = buffer.toString();
                this.replaced.compareAndSet(false, index > normal);
            }
            return this;
        }

        /**
         * 还原SQL语句
         * @param replacement SQL语句
         * @return SQL语句
         */
        public String restore(final String replacement) {
            if (this.hasParameter() && Objects.isNotBlank(replacement)) {
                final StringBuffer buffer = new StringBuffer();
                final Matcher matcher = DEF_PATTERN_PM_RESTORE.matcher(replacement);
                Integer index = 0;
                while (matcher.find()) {
                    final String placeholder = params.get(index);
                    matcher.appendReplacement(buffer, placeholder);
                    index++;
                }
                matcher.appendTail(buffer);
                return buffer.toString();
            }
            return replacement;
        }

        public String getOriginalSql() {
            return originalSql;
        }

        public String getReplaceSql() {
            return replaceSql;
        }

        /**
         * SQL语句是否已替换成功
         * @return boolean
         */
        public boolean isReplaced() {
            return this.replaced.get();
        }

        /**
         * 是否存在占位符参数
         * @return boolean
         */
        public boolean hasParameter() {
            return !this.params.isEmpty();
        }

    }
}
