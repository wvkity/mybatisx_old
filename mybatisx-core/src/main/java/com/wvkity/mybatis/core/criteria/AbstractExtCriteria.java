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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.condition.Criterion;
import com.wvkity.mybatis.core.convert.Converter;
import com.wvkity.mybatis.core.convert.DefaultConditionConverter;
import com.wvkity.mybatis.core.convert.DefaultParameterConverter;
import com.wvkity.mybatis.core.convert.DefaultPropertyConverter;
import com.wvkity.mybatis.core.convert.ParameterConverter;
import com.wvkity.mybatis.core.convert.PropertyConverter;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.core.support.manager.IntactFragmentManager;
import com.wvkity.mybatis.core.support.manager.StandardFragmentManager;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.expr.Expression;
import com.wvkity.mybatis.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

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
     * AND、OR运算符正则字符串
     */
    protected static final String DEF_REGEX_AND_OR_STR = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    /**
     * AND、OR运算符正则
     */
    protected static final Pattern DEF_PATTERN_AND_OR = Pattern.compile(DEF_REGEX_AND_OR_STR, Pattern.CASE_INSENSITIVE);
    /**
     * 参数前缀
     */
    protected static final String DEF_PARAMETER_KEY_PREFIX = "_v_idx_";
    /**
     * 参数值映射
     */
    protected static final String DEF_PARAMETER_VALUE_MAPPING = "%s.parameterValueMapping.%s";
    /**
     * 参数占位符
     */
    protected static final String DEF_PARAMETER_PLACEHOLDER = "{%s}";
    /**
     * 默认模板
     */
    protected static final String DEF_PARAMETER_PLACEHOLDER_ZERO = "{0}";
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
    protected ParameterConverter parameterConverter;
    /**
     * 条件解析器
     */
    protected Converter<Expression<?>, Criterion> conditionConverter;
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
    protected IntactFragmentManager<? extends Criteria<?>> fragmentManager;
    /**
     * 是否存在条件(where/group/order/having)
     */
    protected boolean hasCondition;
    /**
     * SQL片段
     */
    protected String segment;
    /**
     * 条件片段
     */
    protected String whereSegment;
    /**
     * 属性转换器
     */
    protected PropertyConverter propertyConverter;

    // endregion

    // region Query fields

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
    protected boolean onlyFunc;
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
    // protected final Set<AbstractForeignCriteria<T, ?>> foreignSet = new CopyOnWriteArraySet<>();
    // endregion

    // region Update fields

    protected Map<Column, Object> updateColumnsOfWrap;
    protected Map<String, Object> updateColumnsOfOrg;
    /**
     * {@code Map<Property, Column>}
     */
    protected Map<String, String> updateProperties;
    protected Set<String> updateColumns;
    protected String updateSegment;

    // endregion

    // region Property convert methods

    /**
     * 根据{@link Property}查找{@link Column}对象
     * @param property {@link Property}
     * @return {@link Column}对象
     */
    protected Column toColumn(final Property<?, ?> property) {
        return this.propertyConverter.convert(property);
    }

    /**
     * 根据属性名查找{@link Column}对象
     * @param property 属性名
     * @return {@link Column}对象
     */
    protected Column toColumn(final String property) {
        return this.propertyConverter.convert(property);
    }

    /**
     * 根据列名获取{@link Column}对象
     * @param column 列名
     * @return {@link Column}
     */
    protected Column toColumnOfOrg(final String column) {
        return this.propertyConverter.convertOfOrg(column);
    }

    /**
     * lambda属性转成字符串属性
     * @param property 属性
     * @return 属性
     */
    protected String toProperty(Property<?, ?> property) {
        return this.propertyConverter.toProperty(property);
    }

    // endregion

    // region Basic methods

    /**
     * 初始化方法
     * @param alias 表别名
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
        this.defTableAlias = category == Category.UPDATE ? Constants.EMPTY :
            (DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet());
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
        this.fragmentManager = new StandardFragmentManager(this);
        this.propertyConverter = new DefaultPropertyConverter(this);
        if (category == Category.UPDATE) {
            this.updateProperties = new ConcurrentHashMap<>(8);
            this.updateColumnsOfOrg = new ConcurrentHashMap<>(8);
            this.updateColumnsOfWrap = new ConcurrentHashMap<>(8);
            this.updateColumns = new CopyOnWriteArraySet<>();
        }
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
            target.fragmentManager = new StandardFragmentManager(target);
            target.propertyConverter = new DefaultPropertyConverter(target);
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
        }
    }

    /**
     * 返回当前对象
     * @return {@code this}
     */
    protected ExtCriteria<T> self() {
        return this;
    }

    @Override
    public AbstractExtCriteria<T> transfer() {
        return this;
    }

    @Override
    public PropertyConverter getConvert() {
        return this.propertyConverter;
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
    protected String getTableName(boolean joinAs) {
        final Table table = TableHelper.getTable(this.entityClass);
        final String tabName = table.getFullName();
        final String as = this.as();
        return Objects.isBlank(as) ? tabName : joinAs ? (tabName + " AS " + as) : (tabName + Constants.SPACE + as);
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

    @Override
    public boolean isHasCondition() {
        return this.fragmentManager.hasSegment();
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
        return this.getWhereSegment();
    }

    /**
     * 获取条件片段
     * @return 条件语句
     */
    public String getWhereSegment() {
        return this.getWhereSegment(Constants.NULL);
    }

    /**
     * 获取条件片段
     * @param groupByReplacement 分组替换语句
     * @return 条件语句
     */
    public String getWhereSegment(final String groupByReplacement) {
        final String condition = this.fragmentManager.getWhereString();
        if (Objects.isNotBlank(condition)) {
            if (DEF_PATTERN_AND_OR.matcher(condition).matches()) {
                return "WHERE " + condition.replaceFirst(DEF_REGEX_AND_OR_STR, "$2");
            }
            return "WHERE " + condition;
        }
        return Constants.EMPTY;
    }

    /**
     * 完整where子句(where/group/having/order)
     * @param groupByReplacement group替换语句
     * @return where子句
     */
    protected String intactWhereString(final String groupByReplacement) {
        if (this.isHasCondition()) {
            final String condition = this.fragmentManager.getSegment(groupByReplacement);
            if (this.fragmentManager.hasCondition()) {
                if (DEF_PATTERN_AND_OR.matcher(condition).matches()) {
                    return "WHERE " + condition.replaceFirst(DEF_REGEX_AND_OR_STR, "$2");
                }
                return "WHERE " + condition;
            }
            return condition;
        }
        return Constants.EMPTY;
    }

    /**
     * 获取完整更新字段语句
     * @return 完整更新字段语句
     */
    protected String intactUpdateString() {
        final boolean isNotEmptyOfWrap = Objects.isNotEmpty(this.updateColumnsOfWrap);
        final boolean isNotEmptyOfOrg = Objects.isNotEmpty(this.updateColumnsOfOrg);
        if (isNotEmptyOfOrg || isNotEmptyOfWrap) {
            final List<String> segments = new ArrayList<>();
            if (isNotEmptyOfWrap) {
                this.updateColumnsOfWrap.forEach((c, v) -> segments.add(Scripts.convertToConditionArg(Symbol.EQ,
                    Slot.NONE, null, c, this.parameterConverter.convert(v))));
            }
            if (isNotEmptyOfOrg) {
                this.updateColumnsOfOrg.forEach((c, v) -> segments.add(Scripts.convertToConditionArg(Symbol.EQ,
                    Slot.NONE, null, c, this.parameterConverter.convert(v))));
            }
            return String.join(Constants.COMMA_SPACE, segments);
        }
        return Constants.EMPTY;
    }

    @Override
    public String getSegment() {
        return this.intactString();
    }

    // endregion

}
