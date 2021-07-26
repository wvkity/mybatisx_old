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
package com.github.mybatisx.core.criteria;

import com.github.mybatisx.Objects;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.core.condition.Criterion;
import com.github.mybatisx.core.convert.Converter;
import com.github.mybatisx.core.convert.DefaultConditionConverter;
import com.github.mybatisx.core.convert.DefaultParameterConverter;
import com.github.mybatisx.core.convert.DefaultPropertyConverter;
import com.github.mybatisx.core.convert.ParameterConverter;
import com.github.mybatisx.core.convert.PropertyConverter;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.core.sql.SqlManager;
import com.github.mybatisx.core.support.func.Avg;
import com.github.mybatisx.core.support.func.Count;
import com.github.mybatisx.core.support.func.Func;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.core.support.func.Max;
import com.github.mybatisx.core.support.func.Min;
import com.github.mybatisx.core.support.func.Sum;
import com.github.mybatisx.core.support.manager.DefaultStandardFragmentManager;
import com.github.mybatisx.core.support.manager.StandardFragmentManager;
import com.github.mybatisx.core.support.select.FuncSelection;
import com.github.mybatisx.core.support.select.Selection;
import com.github.mybatisx.core.support.select.StandardSelection;
import com.github.mybatisx.exception.MyBatisException;
import com.github.mybatisx.immutable.ImmutableLinkedMap;
import com.github.mybatisx.immutable.ImmutableList;
import com.github.mybatisx.plugin.paging.RangeMode;
import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.support.expr.Expression;
import com.github.mybatisx.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 基础条件/
 * 抽象条件包装
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractExtCriteria<T> implements ExtCriteria<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractExtCriteria.class);

    // region Basic fields

    /**
     * 参数前缀
     */
    protected static final String DEF_PARAMETER_KEY_PREFIX = "_v_idx_";
    /**
     * 参数值映射
     */
    protected static final String DEF_PARAMETER_VALUE_MAPPING = "%s.parameterValueMapping.%s";
    /**
     * #{}参数模板
     */
    protected static final String DEF_PARAMETER_PLACEHOLDER_SAFE = "#{{0}}";
    /**
     * {@link Criteria}默认参数名
     */
    protected static final String DEF_PARAMETER_ALIAS = Constants.PARAM_CRITERIA;
    /**
     * 默认表别名前缀
     */
    protected static final String DEF_TABLE_ALIAS_PREFIX = "_it_";
    /**
     * 参数占位正则
     */
    protected static final String DEF_PATTERN_PM_STR = ".*#\\{((?!#\\{).)*}.*";
    /**
     * 参数占位正则
     */
    protected static final Pattern DEF_PATTERN_PM = Pattern.compile(DEF_PATTERN_PM_STR);
    /**
     * AND/OR运算符
     */
    protected AtomicReference<Slot> refSlot = new AtomicReference<>(Slot.AND);
    /**
     * 实体类型
     */
    protected Class<T> entityClass;
    /**
     * 参数序列
     */
    protected AtomicInteger parameterSequence;
    /**
     * 参数值映射
     */
    protected Map<String, Object> parameterValueMapping;
    /**
     * 参数转换器
     */
    protected transient ParameterConverter parameterConverter;
    /**
     * 条件解析器
     */
    protected transient Converter<Expression<?>, Criterion> conditionConverter;
    /**
     * 属性不匹配是否抛出异常(查找失败)
     */
    protected AtomicBoolean notMatchingWithThrows;
    /**
     * 表别名序列
     */
    protected AtomicInteger tableAliasSequence;
    /**
     * 表别名
     */
    protected AtomicReference<String> tableAliasRef;
    /**
     * 是否使用别名
     */
    protected AtomicBoolean useAlias;
    /**
     * 默认别名
     */
    protected String defTableAlias;
    /**
     * SQL片段管理器
     */
    protected transient StandardFragmentManager<? extends Criteria<?>> fragmentManager;
    /**
     * 是否存在条件(where/group/order/having)
     */
    protected boolean hasCondition;
    /**
     * SQL片段
     */
    protected transient String segment;
    /**
     * 条件片段
     */
    protected transient String whereSegment;
    /**
     * 属性转换器
     */
    protected transient PropertyConverter converter;
    /**
     * SQL管理器
     */
    protected transient SqlManager sqlManager;
    // endregion

    // region Query fields

    /**
     * SQL优化禁止去除order by子句注释
     */
    protected static final String KEEP_ORDER_BY_COMMENT = "/*keep orderby*/";
    /**
     * 引用{@link ExtCriteria}(嵌套/子外联表条件用)
     */
    protected ExtCriteria<?> refQuery;
    /**
     * 是否使用属性名作为别名
     */
    protected boolean propAsAlias = false;
    /**
     * 结果集
     */
    protected String resultMap;
    /**
     * 返回值类型
     */
    protected Class<?> resultType;
    /**
     * Map结果key值
     */
    protected String mapKey;
    /**
     * Map实现类
     */
    @SuppressWarnings("rawtypes")
    protected Class<? extends Map> mapType;
    /**
     * 查询SQL片段
     */
    protected String selectSegment = Constants.EMPTY;
    /**
     * 抓取关联表数据(嵌套子查询/联表查询)
     */
    protected boolean fetch;
    /**
     * 是否去重
     */
    protected boolean distinct;
    /**
     * 继承子查询属性名
     */
    protected boolean propInherit;
    /**
     * 所有字段分组
     */
    protected boolean groupAll;
    /**
     * 查询是否包含聚合函数
     */
    protected boolean containsFunc = true;
    /**
     * 是否仅仅查询聚合函数
     */
    protected boolean onlyFunc = false;
    /**
     * 是否添加保持排序注释
     */
    protected boolean keepOrderBy;
    /**
     * 起始位置
     */
    protected long rowStart;
    /**
     * 结束位置
     */
    protected long rowEnd;
    /**
     * 起始页码
     */
    protected long pageStart;
    /**
     * 结束页码
     */
    protected long pageEnd;
    /**
     * 每页数目
     */
    protected long pageSize;
    /**
     * 联表引用属性
     */
    protected AtomicReference<String> reference = new AtomicReference<>(Constants.EMPTY);
    /**
     * 联表查询对象
     */
    protected Set<ExtCriteria<?>> foreignSet = new CopyOnWriteArraySet<>();
    // endregion

    // region Update fields

    protected Map<Column, Object> updateColumnsOfWrap;
    protected Map<String, Object> updateColumnsOfOrg;
    /**
     * {@code Map<Property, Column>}
     */
    protected Map<String, String> updateProperties;
    protected Set<String> updateColumns;
    protected transient String updateSegment;

    // endregion

    // region Property convert methods

    /**
     * 根据{@link Property}查找{@link Column}对象
     * @param property {@link Property}
     * @return {@link Column}对象
     */
    protected Column toColumn(final Property<?, ?> property) {
        return this.converter.convert(property);
    }

    /**
     * 根据属性名查找{@link Column}对象
     * @param property 属性名
     * @return {@link Column}对象
     */
    protected Column toColumn(final String property) {
        return this.converter.convert(property);
    }

    /**
     * 根据列名获取{@link Column}对象
     * @param column 列名
     * @return {@link Column}
     */
    protected Column toColumnOfOrg(final String column) {
        return this.converter.convertOfOrg(column);
    }

    /**
     * lambda属性转成字符串属性
     * @param property 属性
     * @return 属性
     */
    protected String toProperty(Property<?, ?> property) {
        return this.converter.toProperty(property);
    }

    /**
     * 属性列表转字段名列表
     * @param properties 属性列表
     * @return 字段名列表
     */
    protected List<String> toColumnList(final Collection<String> properties) {
        if (Objects.isNotEmpty(properties)) {
            return properties.stream().map(this::toColumn).filter(Objects::nonNull).map(Column::getColumn)
                .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    // endregion

    // region Basic methods

    /**
     * 初始化方法
     * @param alias    表别名
     * @param category {@link Category}
     */
    protected void initialize(final String alias, Category category) {
        final boolean hasAlias = Objects.isNotBlank(alias);
        this.parameterSequence = new AtomicInteger(0);
        this.parameterValueMapping = new ConcurrentHashMap<>(16);
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.notMatchingWithThrows = new AtomicBoolean(true);
        this.tableAliasSequence = new AtomicInteger(0);
        this.useAlias = new AtomicBoolean(hasAlias);
        this.tableAliasRef = new AtomicReference<>(hasAlias ? alias : Constants.EMPTY);
        this.defTableAlias = category == Category.UPDATE ? Constants.EMPTY : this.genDefTabAlias();
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.fragmentManager = new DefaultStandardFragmentManager(this);
        this.converter = new DefaultPropertyConverter(this);
        if (category == Category.UPDATE) {
            this.updateProperties = new ConcurrentHashMap<>(8);
            this.updateColumnsOfOrg = new ConcurrentHashMap<>(8);
            this.updateColumnsOfWrap = new ConcurrentHashMap<>(8);
            this.updateColumns = new CopyOnWriteArraySet<>();
        }
    }

    /**
     * 生成默认表别名
     * @return 默认表别名
     */
    protected String genDefTabAlias() {
        return DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet() + Constants.UNDER_LINE;
    }

    /**
     * 拷贝属性
     * @param source 源对象
     */
    protected void clone(final AbstractExtCriteria<?> source) {
        this.clone(source, this);
    }

    /**
     * 拷贝属性
     * @param source 源对象
     * @param target 目标对象
     */
    protected void clone(final AbstractExtCriteria<?> source, final AbstractExtCriteria<?> target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            target.parameterSequence = source.parameterSequence;
            target.parameterValueMapping = source.parameterValueMapping;
            target.notMatchingWithThrows = source.notMatchingWithThrows;
            target.tableAliasSequence = source.tableAliasSequence;
            target.useAlias = source.useAlias;
            target.fragmentManager = new DefaultStandardFragmentManager(target);
            target.converter = new DefaultPropertyConverter(target);
        }
    }

    /**
     * 深拷贝属性
     * @param source 源对象
     */
    protected void depClone(final AbstractExtCriteria<T> source) {
        this.depClone(source, this);
    }

    /**
     * 深拷贝属性
     * @param source 源对象
     * @param target 目标对象
     */
    protected void depClone(final AbstractExtCriteria<T> source, final AbstractExtCriteria<T> target) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            target.entityClass = source.entityClass;
            this.clone(source, target);
            target.parameterConverter = source.parameterConverter;
            target.updateProperties = source.updateProperties;
            target.updateColumnsOfOrg = source.updateColumnsOfOrg;
            target.updateColumnsOfWrap = source.updateColumnsOfWrap;
            target.updateColumns = source.updateColumns;
            target.tableAliasRef = source.tableAliasRef;
            target.defTableAlias = source.defTableAlias;
            target.conditionConverter = source.conditionConverter;
            target.sqlManager = source.sqlManager;
        }
    }

    /**
     * 获取主键
     * @return 主键
     */
    protected Column id() {
        return Optional.ofNullable(this.entityClass).map(TableHelper::getId).orElse(null);
    }

    /**
     * 返回当前对象
     * @return {@code this}
     */
    protected ExtCriteria<T> self() {
        return this;
    }

    /**
     * 添加联表查询条件对象
     * @param query 查询对象
     */
    protected void addForeign(final ExtCriteria<?> query) {
        if (Objects.nonNull(query)) {
            this.foreignSet.add(query);
        }
    }

    /**
     * 生成聚合函数别名
     * @param func        {@link Func}
     * @param column      字段名称
     * @param aliasPrefix 别名前缀
     * @return 别名
     */
    protected String genFuncAlias(final Func func, final String column, final String aliasPrefix) {
        final String funcName = func.getSegment();
        String alias;
        if (Objects.isNotBlank(aliasPrefix)) {
            alias = aliasPrefix;
        } else {
            alias = column;
        }
        if (alias.contains(Constants.DOT)) {
            if (alias.endsWith(Constants.DOT) || alias.endsWith(Constants.UNDER_LINE)) {
                return alias + funcName;
            }
            return alias + Constants.DOT + funcName;
        } else if (alias.endsWith(Constants.UNDER_LINE)) {
            return alias + funcName;
        }
        return alias + Constants.UNDER_LINE + funcName;
    }

    /**
     * 生成聚合函数列表
     * @param criteria    {@link Criteria}
     * @param tabAlias    表别名
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param scale       保留小数位数
     * @param distinct    是否去重
     * @return 聚合函数列表
     */
    protected List<Function> genFunctions(final Criteria<?> criteria, final String tabAlias, final String column,
                                          String aliasPrefix, Integer scale, boolean distinct) {
        if (Objects.isNotBlank(column)) {
            final List<Function> it = new ArrayList<>(5);
            it.add(Count.Builder.create().criteria(criteria).tableAlias(tabAlias).column(column)
                .alias(this.genFuncAlias(Func.COUNT, column, aliasPrefix)).distinct(distinct).build());
            it.add(Sum.Builder.create().criteria(criteria).tableAlias(tabAlias).column(column)
                .alias(this.genFuncAlias(Func.SUM, column, aliasPrefix)).scale(scale).distinct(distinct).build());
            it.add(Avg.Builder.create().criteria(criteria).tableAlias(tabAlias).column(column)
                .alias(this.genFuncAlias(Func.AVG, column, aliasPrefix)).scale(scale).distinct(distinct).build());
            it.add(Min.Builder.create().criteria(criteria).tableAlias(tabAlias).column(column)
                .alias(this.genFuncAlias(Func.MIN, column, aliasPrefix)).scale(scale).build());
            it.add(Max.Builder.create().criteria(criteria).tableAlias(tabAlias).column(column)
                .alias(this.genFuncAlias(Func.MAX, column, aliasPrefix)).scale(scale).build());
            return it;
        }
        return new ArrayList<>(0);
    }

    @Override
    public AbstractExtCriteria<T> transfer() {
        return this;
    }

    @Override
    public PropertyConverter getConverter() {
        return this.converter;
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public String as() {
        return this.as(false);
    }

    /**
     * 获取表别名
     * @param force 是否强制使用
     * @return 表别名
     */
    public String as(final boolean force) {
        if (force || this.useAlias.get()) {
            final String alias = this.tableAliasRef.get();
            return Objects.isBlank(alias) ? this.defTableAlias : alias;
        }
        return Constants.EMPTY;
    }

    @Override
    public boolean isStrict() {
        return this.notMatchingWithThrows.get();
    }

    /**
     * 获取表名
     * @return 表名
     */
    public String getTableName() {
        return this.getTableName(true);
    }

    /**
     * 获取表名
     * @param joinAs 是否拼接`AS`关键字
     * @return 表名
     */
    public String getTableName(final boolean joinAs) {
        final String as = this.as();
        final boolean notHasAlias = Objects.isBlank(as);
        if (Objects.nonNull(this.entityClass)) {
            final Table table = TableHelper.getTable(this.entityClass);
            final String tabName = table.getFullName();
            return notHasAlias ? tabName : joinAs ? (tabName + " AS " + as) : (tabName + Constants.SPACE + as);
        }
        return notHasAlias ? Constants.EMPTY : as;
    }

    /**
     * 获取表名
     * @param segment 完整SQL语句
     * @param joinAs  是否拼接`AS`关键字
     * @return 表名
     */
    protected String getTableName(final String segment, final boolean joinAs) {
        final StringBuilder it = new StringBuilder(120);
        it.append(Constants.BRACKET_OPEN);
        it.append(segment);
        it.append(Constants.BRACKET_CLOSE);
        final String as = this.as();
        if (Objects.isNotBlank(as)) {
            if (joinAs) {
                it.append(" AS ");
            }
            it.append(as);
        }
        return it.toString();
    }

    /**
     * 获取参数值映射信息
     * @return 参数值映射
     */
    public Map<String, Object> getParameterValueMapping() {
        if (Objects.isNotEmpty(this.parameterValueMapping)) {
            return ImmutableLinkedMap.of(this.parameterValueMapping);
        }
        return ImmutableLinkedMap.of();
    }

    @Override
    public boolean isKeepOrderBy() {
        return this.keepOrderBy;
    }

    @Override
    public boolean isDistinct() {
        return this.distinct;
    }

    @Override
    public boolean isGroupAll() {
        return this.groupAll;
    }

    @Override
    public boolean isContainsFunc() {
        return this.containsFunc;
    }

    @Override
    public boolean isOnlyFunc() {
        return this.onlyFunc;
    }

    /**
     * 获取{@link Slot}
     * @return {@link Slot}
     */
    public Slot getSlot() {
        return this.refSlot.get();
    }

    /**
     * 获取XML自定义配置的ResultMap结果集
     * @return ResultMap结果集
     */
    public String getResultMap() {
        return this.resultMap;
    }

    /**
     * 获取自定义返回值类型
     * @return 返回值类型
     */
    public Class<?> getResultType() {
        return this.resultType;
    }

    /**
     * 获取返回Map对象指定的key值
     * @return key
     */
    public String getMapKey() {
        return this.mapKey;
    }

    /**
     * 获取{@link Map}实现类
     * @return {@link Map}实现类
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends Map> getMapType() {
        return this.mapType;
    }

    /**
     * 起始位置
     * @return 起始位置
     */
    public long getRowStart() {
        return this.rowStart;
    }

    /**
     * 结束位置
     * @return 结束位置
     */
    public long getRowEnd() {
        return this.rowEnd;
    }

    /**
     * 获取起始页码
     * @return 起始页码
     */
    public long getPageStart() {
        return this.pageStart;
    }

    /**
     * 获取结束页码
     * @return 结束页码
     */
    public long getPageEnd() {
        return this.pageEnd;
    }

    /**
     * 获取每页数目
     * @return 每页数目
     */
    public long getPageSize() {
        return this.pageSize <= 0 ? 20L : this.pageSize;
    }

    /**
     * 是否执行范围查询
     * @return boolean
     */
    public boolean isRange() {
        return this.getMode() != RangeMode.NONE;
    }

    /**
     * 获取范围分页模式
     * @return {@link RangeMode}
     */
    public RangeMode getMode() {
        if (this.rowStart >= 0 && this.rowEnd > 0) {
            return RangeMode.SCOPE;
        } else if (this.pageStart > 0 && this.pageEnd > 0) {
            return RangeMode.PAGEABLE;
        }
        return RangeMode.NONE;
    }

    /**
     * 获取聚合函数
     * @param alias 聚合函数别名
     * @return {@link Function}
     */
    public Function getFunction(final String alias) {
        if (Objects.isNotBlank(alias)) {
            final FuncSelection fs = this.fragmentManager.getFunc(alias);
            if (Objects.nonNull(fs)) {
                return fs.getFunction();
            }
        }
        return null;
    }

    /**
     * 获取聚合函数列表
     * @param funcAliases 聚合函数别名列表
     * @return 聚合函数列表
     */
    protected List<Function> genFunctions(final Collection<String> funcAliases) {
        if (Objects.isNotEmpty(funcAliases)) {
            return funcAliases.stream().map(this::getFunction).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public boolean isPropAsAlias() {
        return this.propAsAlias;
    }

    @Override
    public String getReference() {
        return this.reference.get();
    }

    @Override
    public boolean isHasCondition() {
        return this.fragmentManager.hasSegment();
    }

    /**
     * 禁止调用方法
     */
    protected void invokeThrow() {
        throw new MyBatisException("The current instance object cannot call this method.");
    }

    @Override
    public String completeString() {
        final String sql = this.intactString();
        if (DEF_PATTERN_PM.matcher(sql).matches()) {
            return sql.replaceAll("#\\{(((?!#\\{).)*)}", Constants.QUESTION_MARK);
        }
        return sql;
    }

    /**
     * 完整SQL语句
     * @return SQL语句
     */
    protected String intactString() {
        return this.sqlManager.intactString();
    }

    @Override
    public String getSelectSegment() {
        return this.sqlManager.getSelectSegment();
    }

    @Override
    public String getSelectSegment(boolean self) {
        return this.sqlManager.getSelectSegment(self);
    }

    @Override
    public String getGroupSegment() {
        return this.sqlManager.getGroupSegment();
    }

    @Override
    public List<Selection> fetchSelects() {
        final boolean hasRef = Objects.nonNull(this.refQuery);
        final List<Selection> it = new ArrayList<>();
        if (hasRef) {
            // 是否已加载过
            if (this.hasSelect() && this.fragmentManager.isCached()) {
                return this.fragmentManager.getSelects();
            }
            final List<Selection> selections = this.refQuery.fetchSelects();
            if (Objects.isNotEmpty(selections)) {
                it.addAll(selections);
            }
        } else {
            if (this.hasSelect() || this.isFetch()) {
                final List<Selection> selections = this.fragmentManager.getSelects();
                if (Objects.isNotEmpty(selections)) {
                    it.addAll(selections);
                }
            }
        }
        if (Objects.isNotEmpty(this.foreignSet)) {
            for (ExtCriteria<?> ec : this.foreignSet) {
                if (ec.hasSelect() || ec.isFetch()) {
                    final List<Selection> fss = ec.fetchSelects();
                    if (Objects.isNotEmpty(fss)) {
                        it.addAll(fss);
                    }
                }
            }
        }
        if (hasRef) {
            return this.selectTransform(this, it);
        }
        return Objects.isNotEmpty(it) ? ImmutableList.of(it) : ImmutableList.of();
    }

    /**
     * 查询列转换
     * @param toCriteria {@link ExtCriteria}
     * @param selections 查询列集合
     * @return 新的查询列集合
     */
    protected List<Selection> selectTransform(final ExtCriteria<?> toCriteria, List<Selection> selections) {
        final List<Selection> newSelections = new ArrayList<>(selections.size());
        for (Selection it : selections) {
            final String as = it.as();
            final String column = it.getColumn();
            final Selection selection;
            if (this.propInherit && Objects.isBlank(as)) {
                selection = new StandardSelection(toCriteria, column, it.getProperty(), Matched.IMMEDIATE);
            } else {
                final String realColumn = Objects.isNotBlank(as) ? as : column;
                selection = new StandardSelection(toCriteria, realColumn, null, Matched.IMMEDIATE);
            }
            toCriteria.transfer().fragmentManager.select(selection);
            newSelections.add(selection);
        }
        return ImmutableList.of(newSelections);
    }

    @Override
    public String getWhereSegment() {
        return this.sqlManager.getWhereSegment();
    }

    /**
     * 获取条件片段
     * @param self               是否自身
     * @param appendWhere        是否拼接where
     * @param groupByReplacement 分组替换语句
     * @return 条件语句
     */
    public String getWhereSegment(final boolean self, final boolean appendWhere, final String groupByReplacement) {
        return this.sqlManager.getWhereSegment(self, appendWhere, groupByReplacement);
    }

    /**
     * 获取完整更新字段语句
     * @return 完整更新字段语句
     */
    protected String intactUpdateString() {
        return this.sqlManager.getUpdateSegment();
    }

    @Override
    public String getSegment() {
        return this.intactString();
    }

    // endregion

}
