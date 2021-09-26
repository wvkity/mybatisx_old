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
package io.github.mybatisx.plugin.paging.parser;

import io.github.mybatisx.plugin.exception.MyBatisPluginException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
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
import net.sf.jsqlparser.statement.select.Top;
import net.sf.jsqlparser.statement.select.WithItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * SQLSERVER分页SQL解析器
 * @author wvkity
 * @created 2021-02-19
 * @since 1.0.0
 */
public class SqlServerPageableSqlParser {

    public static final String START_ROW = "" + Long.MIN_VALUE;
    public static final String END_ROW = "" + Long.MAX_VALUE;
    protected static final String WRAP_TABLE = "TMP_WRAP_OUTER_TABLE";
    protected static final String PAGE_TABLE_ALIAS_NAME = "TMP_PAGE_TABLE_ALIAS";
    public static final Alias PAGE_TABLE_ALIAS = new Alias(PAGE_TABLE_ALIAS_NAME);
    protected static final String PAGE_ROW_NUMBER = "PAGE_ROW_NUMBER";
    protected static final Column PAGE_ROW_NUMBER_COLUMN = new Column(PAGE_ROW_NUMBER);
    protected static final String PAGE_COLUMN_ALIAS_PREFIX = "ROW_ALIAS_";
    protected static final Top TOP_PERCENT_100;

    static {
        TOP_PERCENT_100 = new Top();
        TOP_PERCENT_100.setExpression(new LongValue(100));
        TOP_PERCENT_100.setPercentage(true);
    }

    public String smartParse(final String originalSql) {
        return smartParse(originalSql, null, null);
    }

    public String smartParse(final String originalSql, final Long start, final Long offset) {
        final Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(originalSql);
        } catch (Exception e) {
            throw new MyBatisPluginException("The current SQL statement cannot convert the component page query statement. " +
                "Please check that the SQL query statement is correct.");
        }
        if (!(statement instanceof Select)) {
            throw new MyBatisPluginException("The SQL statement must be a query statement.");
        }
        final Select select = this.handlePageableSelect((Select) statement);
        String newSql = select.toString();
        if (start != null) {
            newSql = newSql.replace(START_ROW, offset.toString());
        }
        if (offset != null) {
            newSql = newSql.replace(END_ROW, offset.toString());
        }
        return newSql;
    }

    protected Select handlePageableSelect(Select select) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof SetOperationList) {
            selectBody = this.handleSetOperationList((SetOperationList) selectBody);
        }
        // 检查是否已是分页
        if (((PlainSelect) selectBody).getTop() != null) {
            throw new MyBatisPluginException("The current SQL query statement is already a paging query statement, " +
                "and the paging plug-in can no longer be used for paging queries");
        }
        // 获取查询列
        final List<SelectItem> selectItems = getSelectItemsFromPlainSelect((PlainSelect) selectBody);
        // 添加ROW_NUMBER()
        final List<SelectItem> autoItems = new ArrayList<>();
        SelectItem orderByColumn = this.addRowNumberColumn((PlainSelect) selectBody, autoItems);
        // 加入自动生成列
        ((PlainSelect) selectBody).addSelectItems(autoItems.toArray(new SelectItem[0]));
        // 处理子语句中的ORDER BY
        this.handleSelectBodyTryRemoveOrderBy(selectBody, 0);

        // 中层子查询
        final PlainSelect innerSelectBody = new PlainSelect();
        innerSelectBody.addSelectItems(orderByColumn);
        innerSelectBody.addSelectItems(selectItems.toArray(new SelectItem[0]));
        // 将原始查询作为内层子查询
        final SubSelect innerFromItem = new SubSelect();
        innerFromItem.setSelectBody(selectBody);
        innerFromItem.setAlias(PAGE_TABLE_ALIAS);
        innerSelectBody.setFromItem(innerFromItem);

        // 新建SELECT
        final Select newSelect = new Select();
        final PlainSelect newSelectBody = new PlainSelect();
        // 设置TOP
        final Top top = new Top();
        top.setExpression(new LongValue(END_ROW));
        newSelectBody.setTop(top);
        // 设置ORDER BY
        final List<OrderByElement> orderByElements = new ArrayList<>();
        final OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(PAGE_ROW_NUMBER_COLUMN);
        orderByElements.add(orderByElement);
        newSelectBody.setOrderByElements(orderByElements);
        // 设置WHERE
        final GreaterThan greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(PAGE_ROW_NUMBER_COLUMN);
        greaterThan.setRightExpression(new LongValue(START_ROW));
        newSelectBody.setWhere(greaterThan);
        // 设置SELECT ITEMS
        newSelectBody.setSelectItems(selectItems);
        // 设置FROM ITEM
        final SubSelect fromItem = new SubSelect();
        fromItem.setSelectBody(innerSelectBody);
        fromItem.setAlias(PAGE_TABLE_ALIAS);
        newSelectBody.setFromItem(fromItem);
        newSelect.setSelectBody(newSelectBody);
        if (isNotEmpty(select.getWithItemsList())) {
            newSelect.setWithItemsList(select.getWithItemsList());
        }
        return newSelect;
    }

    /**
     * 处理{@link SelectBody}去除ORDER BY
     * @param selectBody {@link SelectBody}对象
     * @param level      级别
     */
    protected void handleSelectBodyTryRemoveOrderBy(SelectBody selectBody, int level) {
        if (selectBody instanceof PlainSelect) {
            this.handlePlainSelectTryRemoveOrderBy((PlainSelect) selectBody, level);
        } else if (selectBody instanceof WithItem) {
            final WithItem withItem = (WithItem) selectBody;
            Optional.ofNullable(withItem.getSelectBody()).ifPresent(it ->
                this.handleSelectBodyTryRemoveOrderBy(it, level + 1));
        } else {
            final SetOperationList setOperationList = (SetOperationList) selectBody;
            final List<SelectBody> selectBodies = setOperationList.getSelects();
            if (this.isNotEmpty(selectBodies)) {
                for (SelectBody plainSelect : selectBodies) {
                    this.handleSelectBodyTryRemoveOrderBy(plainSelect, level + 1);
                }
            }
        }
    }

    /**
     * 处理{@link PlainSelect}类型的{@link SelectBody}去除ORDER BY
     * @param plainSelect {@link PlainSelect}对象
     * @param level       级别
     */
    protected void handlePlainSelectTryRemoveOrderBy(PlainSelect plainSelect, int level) {
        if (level > 1) {
            if (this.isNotEmpty(plainSelect.getOrderByElements())) {
                if (plainSelect.getTop() == null) {
                    plainSelect.setTop(TOP_PERCENT_100);
                }
            }
        }
        Optional.ofNullable(plainSelect.getFromItem()).ifPresent(it ->
            this.handleFromItemTryRemoveOrderBy(it, level + 1));
        this.handleJoinTryRemoveOrderBy(plainSelect.getJoins(), level);
    }

    /**
     * 处理子查询去除ORDER BY
     * @param fromItem 子查询对象
     * @param level    级别
     */
    protected void handleFromItemTryRemoveOrderBy(FromItem fromItem, int level) {
        if (fromItem instanceof SubJoin) {
            final SubJoin subJoin = (SubJoin) fromItem;
            this.handleJoinTryRemoveOrderBy(subJoin.getJoinList(), level);
            Optional.ofNullable(subJoin.getLeft()).ifPresent(it ->
                this.handleFromItemTryRemoveOrderBy(it, level + 1));
        } else if (fromItem instanceof SubSelect) {
            final SubSelect subSelect = (SubSelect) fromItem;
            Optional.ofNullable(subSelect.getSelectBody()).ifPresent(it ->
                this.handleSelectBodyTryRemoveOrderBy(it, level + 1));
        } else if (fromItem instanceof LateralSubSelect) {
            final LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                final SubSelect subSelect = lateralSubSelect.getSubSelect();
                Optional.ofNullable(subSelect.getSelectBody()).ifPresent(it ->
                    this.handleSelectBodyTryRemoveOrderBy(it, level + 1));
            }
        }
    }

    private void handleJoinTryRemoveOrderBy(List<Join> joins, int level) {
        if (this.isNotEmpty(joins)) {
            for (Join join : joins) {
                Optional.ofNullable(join.getRightItem()).ifPresent(it ->
                    this.handleFromItemTryRemoveOrderBy(it, level + 1));
            }
        }
    }

    protected SelectBody handleSetOperationList(SetOperationList setOperationList) {
        final List<SelectBody> selectBodies = setOperationList.getSelects();
        final SelectBody setSelectBody = selectBodies.get(selectBodies.size() - 1);
        if (!(setSelectBody instanceof PlainSelect)) {
            throw new MyBatisPluginException("The last 'SelectBody' object must be of type 'PlainSelect': "
                + setSelectBody.toString());
        }
        final PlainSelect plainSelect = (PlainSelect) setSelectBody;
        final PlainSelect selectBody = new PlainSelect();
        final List<SelectItem> selectItems = this.getSelectItemsFromPlainSelect(plainSelect);
        selectBody.setSelectItems(selectItems);

        // 设置fromItem
        final SubSelect fromItem = new SubSelect();
        fromItem.setSelectBody(setOperationList);
        fromItem.setAlias(new Alias(WRAP_TABLE));
        selectBody.setFromItem(fromItem);
        if (this.isNotEmpty(plainSelect.getOrderByElements())) {
            selectBody.setOrderByElements(plainSelect.getOrderByElements());
            plainSelect.setOrderByElements(null);
        }
        return selectBody;
    }

    protected List<SelectItem> getSelectItemsFromPlainSelect(PlainSelect plainSelect) {
        List<SelectItem> selectItems = new ArrayList<>();
        for (SelectItem selectItem : plainSelect.getSelectItems()) {
            if (selectItem instanceof SelectExpressionItem) {
                final SelectExpressionItem sei = (SelectExpressionItem) selectItem;
                if (sei.getAlias() != null) {
                    final Column column = new Column(sei.getAlias().getName());
                    selectItems.add(new SelectExpressionItem(column));
                } else if (sei.getExpression() instanceof Column) {
                    final Column column = (Column) sei.getExpression();
                    if (column.getTable() != null) {
                        final Column newColumn = new Column(column.getColumnName());
                        selectItems.add(new SelectExpressionItem(newColumn));
                    } else {
                        selectItems.add(selectItem);
                    }
                } else {
                    selectItems.add(selectItem);
                }
            } else if (selectItem instanceof AllTableColumns) {
                selectItems.add(new AllColumns());
            } else {
                selectItems.add(selectItem);
            }
        }
        for (SelectItem selectItem : selectItems) {
            if (selectItem instanceof AllColumns) {
                return Collections.singletonList(selectItem);
            }
        }
        return selectItems;
    }

    protected SelectItem addRowNumberColumn(PlainSelect plainSelect, List<SelectItem> autoItems) {
        final StringBuilder orderByBuilder = new StringBuilder();
        orderByBuilder.append("ROW_NUMBER() OVER(");
        if (isNotEmpty(plainSelect.getOrderByElements())) {
            orderByBuilder.append(PlainSelect.orderByToString(
                this.getOrderByElementsFromPlainSelect(plainSelect, autoItems)).substring(1));
            plainSelect.setOrderByElements(null);
        } else {
            orderByBuilder.append("ORDER BY RAND()");
        }
        orderByBuilder.append(") ");
        orderByBuilder.append(PAGE_ROW_NUMBER);
        return new SelectExpressionItem(new Column(orderByBuilder.toString()));
    }

    protected List<OrderByElement> getOrderByElementsFromPlainSelect(PlainSelect plainSelect,
                                                                     List<SelectItem> autoItems) {
        final List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
        final ListIterator<OrderByElement> iterator = orderByElements.listIterator();
        // 常规查询列(非`*`且非`tableAlias.*`)
        final Map<String, SelectExpressionItem> routineSelectMap = new HashMap<>();
        // 别名集合
        final Set<String> aliases = new HashSet<>();
        // `tableAlias.*`查询列的表名集合
        final Set<String> allColumnsTables = new HashSet<>();
        // 是否包含`*`查询列
        boolean hasAllColumns = false;
        OrderByElement orderByElement;
        for (SelectItem item : plainSelect.getSelectItems()) {
            if (item instanceof SelectExpressionItem) {
                SelectExpressionItem expressionItem = (SelectExpressionItem) item;
                routineSelectMap.put(expressionItem.getExpression().toString(), expressionItem);
                Alias alias = expressionItem.getAlias();
                if (alias != null) {
                    aliases.add(alias.getName());
                }
            } else if (item instanceof AllColumns) {
                hasAllColumns = true;
            } else if (item instanceof AllTableColumns) {
                allColumnsTables.add(((AllTableColumns) item).getTable().getName());
            }
        }
        int aliasIndex = 1;
        while (iterator.hasNext()) {
            orderByElement = iterator.next();
            Expression expression = orderByElement.getExpression();
            SelectExpressionItem selectExpressionItem = routineSelectMap.get(expression.toString());
            if (selectExpressionItem != null) { // OrderByElement在查询列中
                Alias alias = selectExpressionItem.getAlias();
                if (alias != null) {
                    // 查询列中存在别名时直接使用原别名
                    iterator.set(this.clone(orderByElement, alias.getName()));
                } else {
                    // 查询列不存在别名
                    if (expression instanceof Column) {
                        /*
                         * eg:
                         * SELECT TABLE.COLUMN FROM TABLE ORDER BY TABLE.COLUMN
                         * transform ->
                         * SELECT COLUMN FROM (SELECT TABLE.COLUMN FROM TABLE) ORDER BY COLUMN
                         * */
                        ((Column) expression).setTable(null);
                    } else {
                        // 查询列不是普通列(如聚合函数系列)不支持分页
                        throw new MyBatisPluginException("查询列`" + expression + "`需要定义别名");
                    }
                }
            } else { // OrderByElement不在查询列表中，需要自动生成一个查询列
                if (expression instanceof Column) {
                    // OrderByElement为普通列
                    final Table table = ((Column) expression).getTable();
                    if (table == null) {
                        if (hasAllColumns || (allColumnsTables.size() == 1 && plainSelect.getJoins() == null)
                            || aliases.contains(((Column) expression).getColumnName())) {
                            continue;
                        }
                    } else {
                        final String tableName = table.getName();
                        if (hasAllColumns || allColumnsTables.contains(tableName)) {
                            ((Column) expression).setTable(null);
                            continue;
                        }
                    }
                }
                String aliasName = PAGE_COLUMN_ALIAS_PREFIX + aliasIndex++;
                SelectExpressionItem newExpressionItem = new SelectExpressionItem();
                newExpressionItem.setExpression(expression);
                newExpressionItem.setAlias(new Alias(aliasName));
                autoItems.add(newExpressionItem);
                iterator.set(this.clone(orderByElement, aliasName));
            }
        }
        return orderByElements;
    }

    protected OrderByElement clone(OrderByElement original, String alias) {
        return clone(original, new Column(alias));
    }

    protected OrderByElement clone(OrderByElement original, Expression expression) {
        OrderByElement orderBy = new OrderByElement();
        orderBy.setAsc(original.isAsc());
        orderBy.setAscDescPresent(original.isAscDescPresent());
        orderBy.setNullOrdering(original.getNullOrdering());
        orderBy.setExpression(expression);
        return orderBy;
    }

    private boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
