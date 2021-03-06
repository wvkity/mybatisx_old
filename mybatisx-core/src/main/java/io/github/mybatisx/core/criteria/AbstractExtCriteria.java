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
package io.github.mybatisx.core.criteria;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.condition.Criterion;
import io.github.mybatisx.core.convert.Converter;
import io.github.mybatisx.core.convert.DefaultConditionConverter;
import io.github.mybatisx.core.convert.DefaultParameterConverter;
import io.github.mybatisx.core.convert.DefaultPropertyConverter;
import io.github.mybatisx.core.convert.ParameterConverter;
import io.github.mybatisx.core.convert.PropertyConverter;
import io.github.mybatisx.core.property.Property;
import io.github.mybatisx.core.sql.SqlManager;
import io.github.mybatisx.core.support.func.Avg;
import io.github.mybatisx.core.support.func.Count;
import io.github.mybatisx.core.support.func.Func;
import io.github.mybatisx.core.support.func.Function;
import io.github.mybatisx.core.support.func.Max;
import io.github.mybatisx.core.support.func.Min;
import io.github.mybatisx.core.support.func.Sum;
import io.github.mybatisx.core.support.manager.DefaultStandardFragmentManager;
import io.github.mybatisx.core.support.manager.StandardFragmentManager;
import io.github.mybatisx.core.support.select.FuncSelection;
import io.github.mybatisx.core.support.select.Selection;
import io.github.mybatisx.core.support.select.StandardSelection;
import io.github.mybatisx.exception.MyBatisException;
import io.github.mybatisx.immutable.ImmutableLinkedMap;
import io.github.mybatisx.immutable.ImmutableList;
import io.github.mybatisx.plugin.paging.RangeMode;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.expr.Expression;
import io.github.mybatisx.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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
 * ????????????/
 * ??????????????????
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractExtCriteria<T> implements ExtCriteria<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractExtCriteria.class);

    // region Basic fields

    /**
     * ????????????
     */
    protected static final String DEF_PARAMETER_KEY_PREFIX = "_v_idx_";
    /**
     * ???????????????
     */
    protected static final String DEF_PARAMETER_VALUE_MAPPING = "%s.parameterValueMapping.%s";
    /**
     * #{}????????????
     */
    protected static final String DEF_PARAMETER_PLACEHOLDER_SAFE = "#{{0}}";
    /**
     * {@link Criteria}???????????????
     */
    protected static final String DEF_PARAMETER_ALIAS = Constants.PARAM_CRITERIA;
    /**
     * ?????????????????????
     */
    protected static final String DEF_TABLE_ALIAS_PREFIX = "_it_";
    /**
     * ??????????????????
     */
    protected static final String DEF_PATTERN_PM_STR = ".*#\\{((?!#\\{).)*}.*";
    /**
     * ??????????????????
     */
    protected static final Pattern DEF_PATTERN_PM = Pattern.compile(DEF_PATTERN_PM_STR);
    /**
     * AND/OR?????????
     */
    protected AtomicReference<Slot> refSlot = new AtomicReference<>(Slot.AND);
    /**
     * ????????????
     */
    protected Class<T> entityClass;
    /**
     * ????????????
     */
    protected AtomicInteger parameterSequence;
    /**
     * ???????????????
     */
    protected Map<String, Object> parameterValueMapping;
    /**
     * ???????????????
     */
    protected transient ParameterConverter parameterConverter;
    /**
     * ???????????????
     */
    protected transient Converter<Expression<?>, Criterion> conditionConverter;
    /**
     * ?????????????????????????????????(????????????)
     */
    protected AtomicBoolean notMatchingWithThrows;
    /**
     * ???????????????
     */
    protected AtomicInteger tableAliasSequence;
    /**
     * ?????????
     */
    protected AtomicReference<String> tableAliasRef;
    /**
     * ??????????????????
     */
    protected AtomicBoolean useAlias;
    /**
     * ????????????
     */
    protected String defTableAlias;
    /**
     * SQL???????????????
     */
    protected transient StandardFragmentManager<? extends Criteria<?>> fragmentManager;
    /**
     * ??????????????????(where/group/order/having)
     */
    protected boolean hasCondition;
    /**
     * SQL??????
     */
    protected transient String segment;
    /**
     * ????????????
     */
    protected transient String whereSegment;
    /**
     * ???????????????
     */
    protected transient PropertyConverter converter;
    /**
     * SQL?????????
     */
    protected transient SqlManager sqlManager;
    // endregion

    // region Query fields

    /**
     * SQL??????????????????order by????????????
     */
    protected static final String KEEP_ORDER_BY_COMMENT = "/*keep orderby*/";
    /**
     * ??????{@link ExtCriteria}(??????/?????????????????????)
     */
    protected ExtCriteria<?> refQuery;
    /**
     * ?????????????????????????????????
     */
    protected boolean propAsAlias = false;
    /**
     * ?????????
     */
    protected String resultMap;
    /**
     * ???????????????
     */
    protected Class<?> resultType;
    /**
     * Map??????key???
     */
    protected String mapKey;
    /**
     * Map?????????
     */
    @SuppressWarnings("rawtypes")
    protected Class<? extends Map> mapType;
    /**
     * ??????SQL??????
     */
    protected String selectSegment = Constants.EMPTY;
    /**
     * ?????????????????????(???????????????/????????????)
     */
    protected boolean fetch;
    /**
     * ????????????
     */
    protected boolean distinct;
    /**
     * ????????????????????????
     */
    protected boolean propInherit;
    /**
     * ??????????????????
     */
    protected boolean groupAll;
    /**
     * ??????????????????????????????
     */
    protected boolean containsFunc = true;
    /**
     * ??????????????????????????????
     */
    protected boolean onlyFunc = false;
    /**
     * ??????????????????????????????
     */
    protected boolean keepOrderBy;
    /**
     * ????????????
     */
    protected long rowStart;
    /**
     * ????????????
     */
    protected long rowEnd;
    /**
     * ????????????
     */
    protected long pageStart;
    /**
     * ????????????
     */
    protected long pageEnd;
    /**
     * ????????????
     */
    protected long pageSize;
    /**
     * ??????????????????
     */
    protected AtomicReference<String> reference = new AtomicReference<>(Constants.EMPTY);
    /**
     * ??????????????????
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
     * ??????{@link Property}??????{@link Column}??????
     * @param property {@link Property}
     * @return {@link Column}??????
     */
    protected Column toColumn(final Property<?, ?> property) {
        return this.converter.convert(property);
    }

    /**
     * ?????????????????????{@link Column}??????
     * @param property ?????????
     * @return {@link Column}??????
     */
    protected Column toColumn(final String property) {
        return this.converter.convert(property);
    }

    /**
     * ??????????????????{@link Column}??????
     * @param column ??????
     * @return {@link Column}
     */
    protected Column toColumnOfOrg(final String column) {
        return this.converter.convertOfOrg(column);
    }

    /**
     * lambda???????????????????????????
     * @param property ??????
     * @return ??????
     */
    protected String toProperty(Property<?, ?> property) {
        return this.converter.toProperty(property);
    }

    /**
     * ??????????????????????????????
     * @param properties ????????????
     * @return ???????????????
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
     * ???????????????
     * @param alias    ?????????
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
     * ?????????????????????
     * @return ???????????????
     */
    protected String genDefTabAlias() {
        return DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet() + Constants.UNDER_LINE;
    }

    /**
     * ????????????
     * @param source ?????????
     */
    protected void clone(final AbstractExtCriteria<?> source) {
        this.clone(source, this);
    }

    /**
     * ????????????
     * @param source ?????????
     * @param target ????????????
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
     * ???????????????
     * @param source ?????????
     */
    protected void depClone(final AbstractExtCriteria<T> source) {
        this.depClone(source, this);
    }

    /**
     * ???????????????
     * @param source ?????????
     * @param target ????????????
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
     * ????????????
     * @return ??????
     */
    protected Column id() {
        return Optional.ofNullable(this.entityClass).map(TableHelper::getId).orElse(null);
    }

    /**
     * ??????????????????
     * @return {@code this}
     */
    protected ExtCriteria<T> self() {
        return this;
    }

    /**
     * ??????????????????????????????
     * @param query ????????????
     */
    protected void addForeign(final ExtCriteria<?> query) {
        if (Objects.nonNull(query)) {
            this.foreignSet.add(query);
        }
    }

    /**
     * ?????????????????????????????????
     * @return {@link Column}
     */
    protected Optional<Column> optimisticLockColumn() {
        return Optional.ofNullable(TableHelper.getTable(this.entityClass).getOptimisticLockColumn());
    }

    /**
     * ????????????????????????
     * @param func        {@link Func}
     * @param column      ????????????
     * @param aliasPrefix ????????????
     * @return ??????
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
     * ????????????????????????
     * @param criteria    {@link Criteria}
     * @param tabAlias    ?????????
     * @param column      ?????????
     * @param aliasPrefix ????????????????????????
     * @param scale       ??????????????????
     * @param distinct    ????????????
     * @return ??????????????????
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

    /**
     * ????????????????????????
     * @return ??????????????????
     */
    protected Object optimisticLockUpdateValue() {
        final Optional<Column> optional = this.optimisticLockColumn();
        if (optional.isPresent() && Objects.isNotEmpty(this.updateColumns)) {
            final Column it = optional.get();
            final String column = it.getColumn();
            if (this.updateColumns.contains(column.toUpperCase(Locale.ENGLISH))) {
                if (this.updateColumnsOfWrap.containsKey(it)) {
                    return this.updateColumnsOfWrap.get(it);
                }
                for (Map.Entry<String, Object> entry : this.updateColumnsOfOrg.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(column)) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object getVersionConditionValue() {
        return this.fragmentManager.getVersionValue(this.optimisticLockColumn().orElse(null));
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
     * ???????????????
     * @param force ??????????????????
     * @return ?????????
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
     * ????????????
     * @return ??????
     */
    public String getTableName() {
        return this.getTableName(true);
    }

    /**
     * ????????????
     * @param joinAs ????????????`AS`?????????
     * @return ??????
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
     * ????????????
     * @param segment ??????SQL??????
     * @param joinAs  ????????????`AS`?????????
     * @return ??????
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
     * ???????????????????????????
     * @return ???????????????
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
     * ??????{@link Slot}
     * @return {@link Slot}
     */
    public Slot getSlot() {
        return this.refSlot.get();
    }

    /**
     * ??????XML??????????????????ResultMap?????????
     * @return ResultMap?????????
     */
    public String getResultMap() {
        return this.resultMap;
    }

    /**
     * ??????????????????????????????
     * @return ???????????????
     */
    public Class<?> getResultType() {
        return this.resultType;
    }

    /**
     * ????????????Map???????????????key???
     * @return key
     */
    public String getMapKey() {
        return this.mapKey;
    }

    /**
     * ??????{@link Map}?????????
     * @return {@link Map}?????????
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends Map> getMapType() {
        return this.mapType;
    }

    /**
     * ????????????
     * @return ????????????
     */
    public long getRowStart() {
        return this.rowStart;
    }

    /**
     * ????????????
     * @return ????????????
     */
    public long getRowEnd() {
        return this.rowEnd;
    }

    /**
     * ??????????????????
     * @return ????????????
     */
    public long getPageStart() {
        return this.pageStart;
    }

    /**
     * ??????????????????
     * @return ????????????
     */
    public long getPageEnd() {
        return this.pageEnd;
    }

    /**
     * ??????????????????
     * @return ????????????
     */
    public long getPageSize() {
        return this.pageSize <= 0 ? 20L : this.pageSize;
    }

    /**
     * ????????????????????????
     * @return boolean
     */
    public boolean isRange() {
        return this.getMode() != RangeMode.NONE;
    }

    /**
     * ????????????????????????
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
     * ??????????????????
     * @param alias ??????????????????
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
     * ????????????????????????
     * @param funcAliases ????????????????????????
     * @return ??????????????????
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
     * ??????????????????
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
     * ??????SQL??????
     * @return SQL??????
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
            // ??????????????????
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
     * ???????????????
     * @param toCriteria {@link ExtCriteria}
     * @param selections ???????????????
     * @return ?????????????????????
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
     * ??????????????????
     * @param self               ????????????
     * @param appendWhere        ????????????where
     * @param groupByReplacement ??????????????????
     * @return ????????????
     */
    public String getWhereSegment(final boolean self, final boolean appendWhere, final String groupByReplacement) {
        return this.sqlManager.getWhereSegment(self, appendWhere, groupByReplacement);
    }

    /**
     * ??????????????????????????????
     * @return ????????????????????????
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
