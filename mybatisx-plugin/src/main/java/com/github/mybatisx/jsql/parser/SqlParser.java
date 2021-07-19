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
package com.github.mybatisx.jsql.parser;

import com.github.mybatisx.Objects;
import com.github.mybatisx.jsql.handler.RemoveOrderByHandler;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * SQL解析器
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
public class SqlParser {

    public static final String DEF_REGEX_SELECT_ONE_STR = "^(?i)(\\s*select\\s+1\\s+)(.*)$";
    public static final Pattern DEF_PATTERN_SELECT_ONE = Pattern.compile(DEF_REGEX_SELECT_ONE_STR,
        Pattern.CASE_INSENSITIVE);
    public static final String DEF_REGEX_SELECT_STR = "^(?i)((\\s*select\\s*)(((?!select).)*)(\\s*from)(\\s*.*))$";
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
        final Select select;
        try {
            select = parseSelect(originalSql);
            new RemoveOrderByHandler(originalSql, select).handleTryRemove();
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
        final ParameterParser paramParser = new ParameterParser(originalSql);
        final Select select;
        try {
            select = parseSelect(paramParser.replace().isReplaced() ? paramParser.getReplaceSql() : originalSql);
            new RemoveOrderByHandler(originalSql, select).handleTryRemove();
        } catch (Exception ignore) {
            return originalSql;
        }
        return paramParser.restore(select.toString());
    }

    /**
     * 解析查询语句
     * @param originalSql 原SQL语句
     * @return {@link Select}
     * @throws JSQLParserException 非完整查询语句将抛出异常
     */
    protected Select parseSelect(final String originalSql) throws JSQLParserException {
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
        final ParameterParser paramParser = new ParameterParser(originalSql);
        final Select select;
        final RemoveOrderByHandler orderByHandler;
        try {
            select = this.parseSelect(paramParser.replace().isReplaced() ? paramParser.getReplaceSql() : originalSql);
            orderByHandler = new RemoveOrderByHandler(originalSql, select);
        } catch (Exception ignore) {
            return this.regexExistsParse(originalSql);
        }
        try {
            orderByHandler.handleTryRemove();
        } catch (Exception ignore) {
            return this.toSelectOneSql(select, paramParser);
        }
        return this.toSelectOneSql(select, paramParser);
    }

    /**
     * 转成select 1 from tab语句
     * @param select {@link Select}
     * @return 新的查询语句
     */
    public String toSelectOneSql(final Select select, final ParameterParser parser) {
        final SelectBody body = select.getSelectBody();
        final PlainSelect psl = (PlainSelect) body;
        final List<SelectItem> selectItems = new ArrayList<>(1);
        selectItems.add(new SelectExpressionItem(new Column("1")));
        psl.setSelectItems(selectItems);
        return parser.restore(select.toString());
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
                    if (Objects.isNotBlank(funcName)) {
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
     * 解析WHERE条件
     * @param originalSql 原SQL语句
     * @return WHERE条件
     */
    public String parseWhereCondition(final String originalSql) {
        final Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(originalSql);
            final Expression where;
            if (statement instanceof Select) {
                where = ((PlainSelect) ((Select) statement).getSelectBody()).getWhere();
            } else if (statement instanceof Update) {
                where = ((Update) statement).getWhere();
            } else if (statement instanceof Delete) {
                where = ((Delete) statement).getWhere();
            } else {
                where = null;
            }
            if (Objects.nonNull(where)) {
                return where.toString();
            }
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }

    /**
     * 表名
     * @param originalSql 原SQL
     * @return 表名
     */
    public String parseTableName(final String originalSql) {
        final Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(originalSql);
            final Table table;
            if (statement instanceof Select) {
                table = ((PlainSelect) ((Select) statement).getSelectBody()).getIntoTables().get(0);
            } else if (statement instanceof Update) {
                table = ((Update) statement).getTable();
            } else if (statement instanceof Delete) {
                table = ((Delete) statement).getTable();
            } else {
                table = null;
            }
            if (Objects.nonNull(table)) {
                return table.toString();
            }
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }

}
