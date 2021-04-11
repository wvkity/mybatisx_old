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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.exception.MyBatisException;
import com.wvkity.mybatis.basic.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.condition.basic.SegmentManager;
import com.wvkity.mybatis.core.condition.expression.ImmediateBetween;
import com.wvkity.mybatis.core.condition.expression.ImmediateEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateGreaterThan;
import com.wvkity.mybatis.core.condition.expression.ImmediateGreaterThanOrEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateIn;
import com.wvkity.mybatis.core.condition.expression.ImmediateLessThan;
import com.wvkity.mybatis.core.condition.expression.ImmediateLessThanOrEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateLike;
import com.wvkity.mybatis.core.condition.expression.ImmediateNotBetween;
import com.wvkity.mybatis.core.condition.expression.ImmediateNotEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateNotIn;
import com.wvkity.mybatis.core.condition.expression.ImmediateNotLike;
import com.wvkity.mybatis.core.condition.expression.ImmediateNotNull;
import com.wvkity.mybatis.core.condition.expression.ImmediateNull;
import com.wvkity.mybatis.core.condition.expression.ImmediateTemplate;
import com.wvkity.mybatis.core.condition.expression.Native;
import com.wvkity.mybatis.core.condition.expression.StandardBetween;
import com.wvkity.mybatis.core.condition.expression.StandardEqual;
import com.wvkity.mybatis.core.condition.expression.StandardGreaterThan;
import com.wvkity.mybatis.core.condition.expression.StandardGreaterThanOrEqual;
import com.wvkity.mybatis.core.condition.expression.StandardIdEqual;
import com.wvkity.mybatis.core.condition.expression.StandardIn;
import com.wvkity.mybatis.core.condition.expression.StandardLessThan;
import com.wvkity.mybatis.core.condition.expression.StandardLessThanOrEqual;
import com.wvkity.mybatis.core.condition.expression.StandardLike;
import com.wvkity.mybatis.core.condition.expression.StandardNesting;
import com.wvkity.mybatis.core.condition.expression.StandardNotBetween;
import com.wvkity.mybatis.core.condition.expression.StandardNotEqual;
import com.wvkity.mybatis.core.condition.expression.StandardNotIn;
import com.wvkity.mybatis.core.condition.expression.StandardNotLike;
import com.wvkity.mybatis.core.condition.expression.StandardNotNull;
import com.wvkity.mybatis.core.condition.expression.StandardNull;
import com.wvkity.mybatis.core.condition.expression.StandardTemplate;
import com.wvkity.mybatis.core.condition.expression.TemplateMatch;
import com.wvkity.mybatis.core.invoke.SerializedLambda;
import com.wvkity.mybatis.core.property.PropertiesMappingCache;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.condition.criteria.Criteria;
import com.wvkity.mybatis.support.condition.expression.Expression;
import com.wvkity.mybatis.support.constant.Like;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 抽象条件
 * @param <T>     泛型类型
 * @param <Chain> 子类类型
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
abstract class AbstractBasicCriteria<T, Chain extends AbstractBasicCriteria<T, Chain>> implements
    CriteriaWrapper<T, Chain> {

    // region Basic fields

    private final static Logger log = LoggerFactory.getLogger(AbstractBasicCriteria.class);
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
     * {@link Criteria}默认参数名
     */
    protected static final String DEF_PARAMETER_ALIAS = Constants.PARAM_CRITERIA;
    /**
     * 默认表别名前缀
     */
    protected static final String DEF_TABLE_ALIAS_PREFIX = "_it_";
    /**
     * 当前对象
     */
    @SuppressWarnings("unchecked")
    protected final Chain context = (Chain) this;
    /**
     * 实体类型
     */
    protected Class<T> entityClass;
    /**
     * 参数值映射
     */
    protected Map<String, Object> parameterValueMapping;
    /**
     * SQL片段管理器
     */
    protected SegmentManager segmentManager;
    /**
     * SQL片段
     */
    protected String segment;
    /**
     * 条件片段
     */
    protected String whereSegment;
    /**
     * 参数序列
     */
    protected AtomicInteger parameterSequence;
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
     * 表名
     */
    protected String tableName;
    /**
     * 是否存在条件(where/group/order/having)
     */
    protected boolean hasCondition;
    /**
     * 条件解析器
     */
    protected ConditionConverter conditionConverter;

    // endregion

    /**
     * 初始化方法
     * @param alias 表别名
     */
    protected void initialize(final String alias) {
        this.parameterValueMapping = new ConcurrentHashMap<>();
        this.parameterSequence = new AtomicInteger(0);
        this.segmentManager = new SegmentManager();
        this.notMatchingWithThrows = new AtomicBoolean(true);
        this.tableAliasSequence = new AtomicInteger(0);
        this.useAlias = new AtomicBoolean(false);
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.conditionConverter = new ConditionConverter(this);
    }

    // region Add criterion methods

    /**
     * 添加条件
     * @param expression {@link Expression}
     * @return {@link Chain}
     */
    protected Chain add(final Expression expression) {
        Optional.ofNullable(this.conditionConverter.convert(expression)).ifPresent(this.segmentManager::where);
        return this.context;
    }

    @Override
    public Chain where(Expression... expressions) {
        if (!Objects.isEmpty(expressions)) {
            final int size = expressions.length;
            if (size == 1) {
                this.add(expressions[0]);
            } else if (size == 2) {
                this.add(expressions[0]).add(expressions[1]);
            } else {
                for (Expression expression : expressions) {
                    this.add(expression);
                }
            }
        }
        return this.context;
    }

    @Override
    public Chain where(Collection<Expression> expressions) {
        if (Objects.isNotEmpty(expressions)) {
            for (Expression condition : expressions) {
                this.add(condition);
            }
        }
        return this.context;
    }

    // endregion

    // region Conditions

    // region Compare conditions

    @Override
    public Chain idEq(Slot slot, Object value) {
        return add(new StandardIdEqual(this, slot, value));
    }

    @Override
    public <V> Chain eq(Slot slot, Property<T, V> property, V value) {
        return eq(slot, this.convert(property), value);
    }

    @Override
    public Chain eq(Slot slot, String property, Object value) {
        return add(new StandardEqual(this, property, slot, value));
    }

    @Override
    public Chain colEq(Slot slot, String column, Object value) {
        return add(new ImmediateEqual(this, column, slot, value));
    }

    @Override
    public Chain eq(Slot slot, Map<String, Object> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                final String property = entry.getKey();
                if (Objects.isNotBlank(property)) {
                    this.eq(slot, property, entry.getValue());
                }
            }
        }
        return this.context;
    }

    @Override
    public Chain colEq(Slot slot, Map<String, Object> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, Object> entry : columns.entrySet()) {
                final String column = entry.getKey();
                if (Objects.isNotBlank(column)) {
                    this.colEq(slot, column, entry.getValue());
                }
            }
        }
        return this.context;
    }

    @Override
    public <V> Chain ne(Slot slot, Property<T, V> property, V value) {
        return this.ne(slot, this.convert(property), value);
    }

    @Override
    public Chain ne(Slot slot, String property, Object value) {
        return add(new StandardNotEqual(this, property, slot, value));
    }

    @Override
    public Chain colNe(Slot slot, String column, Object value) {
        return add(new ImmediateNotEqual(this, column, slot, value));
    }

    @Override
    public <V> Chain gt(Slot slot, Property<T, V> property, V value) {
        return this.gt(slot, this.convert(property), value);
    }

    @Override
    public Chain gt(Slot slot, String property, Object value) {
        return add(new StandardGreaterThan(this, property, slot, value));
    }

    @Override
    public Chain colGt(Slot slot, String column, Object value) {
        return add(new ImmediateGreaterThan(this, column, slot, value));
    }

    @Override
    public <V> Chain ge(Slot slot, Property<T, V> property, V value) {
        return this.ge(slot, this.convert(property), value);
    }

    @Override
    public Chain ge(Slot slot, String property, Object value) {
        return add(new StandardGreaterThanOrEqual(this, property, slot, value));
    }

    @Override
    public Chain colGe(Slot slot, String column, Object value) {
        return add(new ImmediateGreaterThanOrEqual(this, column, slot, value));
    }

    @Override
    public <V> Chain lt(Slot slot, Property<T, V> property, V value) {
        return this.lt(slot, this.convert(property), value);
    }

    @Override
    public Chain lt(Slot slot, String property, Object value) {
        return add(new StandardLessThan(this, property, slot, value));
    }

    @Override
    public Chain colLt(Slot slot, String column, Object value) {
        return add(new ImmediateLessThan(this, column, slot, value));
    }

    @Override
    public <V> Chain le(Slot slot, Property<T, V> property, V value) {
        return this.le(slot, this.convert(property), value);
    }

    @Override
    public Chain le(Slot slot, String property, Object value) {
        return add(new StandardLessThanOrEqual(this, property, slot, value));
    }

    @Override
    public Chain colLe(Slot slot, String column, Object value) {
        return add(new ImmediateLessThanOrEqual(this, column, slot, value));
    }

    // endregion

    // region Range conditions

    @Override
    @SuppressWarnings("unchecked")
    public <V> Chain in(Slot slot, Property<T, V> property, Collection<V> values) {
        return add(new StandardNotIn(this, this.convert(property), slot, (Collection<Object>) values));
    }

    @Override
    public Chain in(Slot slot, String property, Collection<Object> values) {
        return add(new StandardIn(this, property, slot, values));
    }

    @Override
    public Chain colIn(Slot slot, String column, Collection<Object> values) {
        return add(new ImmediateIn(this, column, slot, values));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Chain notIn(Slot slot, Property<T, V> property, Collection<V> values) {
        return add(new StandardNotIn(this, this.convert(property), slot, (Collection<Object>) values));
    }

    @Override
    public Chain notIn(Slot slot, String property, Collection<Object> values) {
        return add(new StandardNotIn(this, property, slot, values));
    }

    @Override
    public Chain colNotIn(Slot slot, String column, Collection<Object> values) {
        return add(new ImmediateNotIn(this, column, slot, values));
    }

    // endregion

    // region Between conditions

    @Override
    public <V> Chain between(Slot slot, Property<T, V> property, V begin, V end) {
        return this.between(slot, this.convert(property), begin, end);
    }

    @Override
    public Chain between(Slot slot, String property, Object begin, Object end) {
        return add(new StandardBetween(this, property, slot, begin, end));
    }

    @Override
    public Chain colBetween(Slot slot, String column, Object begin, Object end) {
        return add(new ImmediateBetween(this, column, slot, begin, end));
    }

    @Override
    public <V> Chain notBetween(Slot slot, Property<T, V> property, V begin, V end) {
        return this.notBetween(slot, this.convert(property), begin, end);
    }

    @Override
    public Chain notBetween(Slot slot, String property, Object begin, Object end) {
        return add(new StandardNotBetween(this, property, slot, begin, end));
    }

    @Override
    public Chain colNotBetween(Slot slot, String column, Object begin, Object end) {
        return add(new ImmediateNotBetween(this, column, slot, begin, end));
    }

    // endregion

    // region Fuzzy conditions

    @Override
    public <V> Chain like(Slot slot, Property<T, V> property, V value, Like mode, Character escape) {
        return this.like(slot, this.convert(property), value, mode, escape);
    }

    @Override
    public Chain like(Slot slot, String property, Object value, Like mode, Character escape) {
        return add(new StandardLike(this, property, mode, escape, slot, value));
    }

    @Override
    public Chain colLike(Slot slot, String column, Object value, Like mode, Character escape) {
        return add(new ImmediateLike(this, column, mode, escape, slot, value));
    }

    @Override
    public <V> Chain notLike(Slot slot, Property<T, V> property, V value, Like mode, Character escape) {
        return this.notLike(slot, this.convert(property), value, mode, escape);
    }

    @Override
    public Chain notLike(Slot slot, String property, Object value, Like mode, Character escape) {
        return add(new StandardNotLike(this, property, mode, escape, slot, value));
    }

    @Override
    public Chain colNotLike(Slot slot, String column, Object value, Like mode, Character escape) {
        return add(new ImmediateNotLike(this, column, mode, escape, slot, value));
    }

    // endregion

    // region Nullable conditions

    @Override
    public <V> Chain isNull(Slot slot, Property<T, V> property) {
        return this.isNull(slot, this.convert(property));
    }

    @Override
    public Chain isNull(Slot slot, String property) {
        return add(new StandardNull(this, property, slot));
    }

    @Override
    public Chain colIsNull(Slot slot, String column) {
        return add(new ImmediateNull(this, column, slot));
    }

    @Override
    public <V> Chain notNull(Slot slot, Property<T, V> property) {
        return this.notNull(slot, this.convert(property));
    }

    @Override
    public Chain notNull(Slot slot, String property) {
        return add(new StandardNotNull(this, property, slot));
    }

    @Override
    public Chain colNotNull(Slot slot, String column) {
        return add(new ImmediateNotNull(this, column, slot));
    }

    // endregion

    // region Template conditions

    @Override
    public Chain tpl(Slot slot, String template, Property<T, ?> property, Object value) {
        return this.tpl(slot, template, this.convert(property), value);
    }

    @Override
    public Chain tpl(Slot slot, String template, String property, Object value) {
        return add(new StandardTemplate(this, property, template, TemplateMatch.SINGLE, slot, value, null, null));
    }

    @Override
    public Chain tpl(String template, Object value) {
        return add(new ImmediateTemplate(this, null, template, TemplateMatch.SINGLE, Slot.NONE, value, null, null));
    }

    @Override
    public Chain tpl(Slot slot, String template, Object value) {
        return add(new ImmediateTemplate(this, null, template, TemplateMatch.SINGLE, slot, value, null, null));
    }

    @Override
    public Chain colTpl(Slot slot, String template, String column, Object value) {
        return add(new ImmediateTemplate(this, column, template, TemplateMatch.SINGLE, slot, value, null, null));
    }

    @Override
    public Chain tpl(Slot slot, String template, Property<T, ?> property, Collection<Object> values) {
        return this.tpl(slot, template, this.convert(property), values);
    }

    @Override
    public Chain tpl(Slot slot, String template, String property, Collection<Object> values) {
        return add(new StandardTemplate(this, property, template, null, slot, null, values, null));
    }

    @Override
    public Chain tpl(String template, Collection<Object> values) {
        return add(new ImmediateTemplate(this, null, template, null, Slot.NONE, null, values, null));
    }

    @Override
    public Chain tpl(Slot slot, String template, Collection<Object> values) {
        return add(new ImmediateTemplate(this, null, template, null, slot, null, values, null));
    }

    @Override
    public Chain colTpl(Slot slot, String template, String column, Collection<Object> values) {
        return add(new ImmediateTemplate(this, column, template, null, slot, null, values, null));
    }

    @Override
    public Chain tpl(Slot slot, String template, Property<T, ?> property, Map<String, Object> values) {
        return this.tpl(slot, template, this.convert(property), values);
    }

    @Override
    public Chain tpl(Slot slot, String template, String property, Map<String, Object> values) {
        return add(new StandardTemplate(this, property, template, TemplateMatch.MAP, slot, null, null, values));
    }

    @Override
    public Chain tpl(String template, Map<String, Object> values) {
        return add(new ImmediateTemplate(this, null, template, TemplateMatch.MAP, Slot.NONE, null, null, values));
    }

    @Override
    public Chain tpl(Slot slot, String template, Map<String, Object> values) {
        return add(new ImmediateTemplate(this, null, template, TemplateMatch.MAP, slot, null, null, values));
    }

    @Override
    public Chain colTpl(Slot slot, String template, String column, Map<String, Object> values) {
        return add(new ImmediateTemplate(this, column, template, TemplateMatch.MAP, slot, null, null, values));
    }

    // endregion

    // region Nested conditions

    @Override
    public Chain and(boolean not, List<Expression> conditions) {
        return add(new StandardNesting(this, not, Slot.AND, conditions));
    }

    @Override
    public Chain and(boolean not, Criteria<?> criteria, List<Expression> conditions) {
        return add(new StandardNesting(criteria, not, Slot.AND, conditions));
    }

    @Override
    public Chain and(Function<Chain, Chain> apply, boolean not) {
        return this.doIt(apply, true, not);
    }

    @Override
    public Chain or(boolean not, List<Expression> conditions) {
        return add(new StandardNesting(this, not, Slot.OR, conditions));
    }

    @Override
    public Chain or(boolean not, Criteria<?> criteria, List<Expression> conditions) {
        return add(new StandardNesting(criteria, not, Slot.OR, conditions));
    }

    @Override
    public Chain or(Function<Chain, Chain> apply, boolean not) {
        return this.doIt(apply, false, not);
    }

    Chain doIt(final Function<Chain, Chain> apply, final boolean and, final boolean not) {
        if (Objects.nonNull(apply)) {
            final Chain ctx = this.newInstance();
            if (Objects.nonNull(ctx)) {
                final Chain instance = apply.apply(ctx);
                final List<Criterion> conditions = instance.segmentManager.getConditions();
                if (Objects.isNotEmpty(conditions)) {
                    this.segmentManager.where(new NestedCondition(not, and ? Slot.AND : Slot.OR, conditions));
                }
            }
        }
        return this.context;
    }

    // endregion

    @Override
    public Chain nativeCondition(String condition) {
        if (Objects.isNotBlank(condition)) {
            this.add(Native.of(condition));
        }
        return this.context;
    }


    // endregion

    // region Other methods

    /**
     * 创建实例(需要自行实现，默认返回null)
     * @return {@link Chain}
     */
    protected Chain newInstance() {
        // empty
        return null;
    }

    /**
     * 拷贝属性
     * @param source 源对象
     * @return {@link Chain}
     */
    protected Chain clone(final Chain source) {
        this.clone(source, this.context);
        return this.context;
    }

    /**
     * 拷贝属性
     * @param source 源对象
     * @param target 目标对象
     */
    protected void clone(final Chain source, final Chain target) {
        if (target != null) {
            if (source != null) {
                target.defTableAlias = source.defTableAlias;
                target.tableAliasSequence = source.tableAliasSequence;
                target.useAlias = source.useAlias;
                target.tableAliasRef = source.tableAliasRef;
                target.notMatchingWithThrows = source.notMatchingWithThrows;
                target.parameterSequence = source.parameterSequence;
                target.parameterValueMapping = source.parameterValueMapping;
            }
        }
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public String as() {
        if (this.useAlias.get()) {
            final String alias = this.tableAliasRef.get();
            return Objects.isBlank(alias) ? this.defTableAlias : alias;
        }
        return Constants.EMPTY;
    }

    @Override
    public Chain strict(boolean throwing) {
        this.notMatchingWithThrows.compareAndSet(!throwing, throwing);
        return this.context;
    }

    @Override
    public boolean isStrict() {
        return this.notMatchingWithThrows.get();
    }

    // region Placeholder methods

    /**
     * 默认参数值转占位符
     * @param args 参数列表
     * @return SQL字符串
     */
    protected String defPlaceholder(final Object... args) {
        return this.placeholder(DEF_PARAMETER_PLACEHOLDER_ZERO, true, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数列表
     * @return SQL字符串
     */
    protected List<String> defPlaceholders(final Object... args) {
        return this.defPlaceholders(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 默认参数值转占位符
     * @param args 参数集合
     * @return SQL字符串
     */
    protected List<String> defPlaceholders(final Collection<Object> args) {
        return this.placeholder(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符
     * @param template 模板
     * @param args     参数列表
     * @return SQL字符串
     */
    protected List<String> defPlaceholders(final String template, final Object... args) {
        return this.placeholder(template, Arrays.asList(args));
    }

    protected String placeholder(String source, boolean format, Object... args) {
        if (Objects.isBlank(source)) {
            return null;
        }
        if (format && !Objects.isEmpty(args)) {
            final int size = args.length;
            String template = source;
            for (int i = 0; i < size; i++) {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                template = template.replace("{" + i + "}",
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
                this.parameterValueMapping.put(paramName, args[i]);
            }
            return template;
        }
        return source;
    }

    protected List<String> placeholder(String source, Collection<Object> args) {
        if (Objects.isNotBlank(source) && Objects.isNotEmpty(args)) {
            return args.stream().map(it -> {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                final Object value = it == null ? "null" : it;
                this.parameterValueMapping.put(paramName, value);
                return source.replace(DEF_PARAMETER_PLACEHOLDER_ZERO,
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
            }).collect(Collectors.toList());
        }
        return null;
    }

    // endregion

    /**
     * 获取主键字段
     * @return {@link Column}
     */
    protected Column getId() {
        return Objects.nonNull(this.entityClass) ? TableHelper.getId(this.entityClass) : null;
    }

    /**
     * 获取表名
     * @return 表名
     */
    public String getTableName() {
        final Table table = TableHelper.getTable(this.entityClass);
        final String tabName = table.getFullName();
        final String as = this.as();
        return Objects.isBlank(as) ? tabName : (tabName + " AS " + as);
    }

    /**
     * lambda属性转成字符串属性
     * @param properties lambda属性列表
     * @return 属性列表
     */
    protected List<String> convert(final List<Property<T, ?>> properties) {
        if (Objects.isNotEmpty(properties)) {
            return properties.stream().filter(Objects::nonNull).map(this::convert).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    public Map<String, Object> getParameterValueMapping() {
        if (Objects.isNotEmpty(this.parameterValueMapping)) {
            return ImmutableLinkedMap.of(this.parameterValueMapping);
        }
        return ImmutableLinkedMap.of();
    }

    public boolean isHasCondition() {
        return this.segmentManager.hasCondition();
    }

    @Override
    public String getSegment() {
        return this.segmentManager.getSegment();
    }

    public String getWhereSegment() {
        if (this.isHasCondition()) {
            final String condition = this.getSegment();
            if (DEF_PATTERN_AND_OR.matcher(condition).matches()) {
                return "WHERE " + condition.replaceFirst(DEF_REGEX_AND_OR_STR, "$2");
            }
            return "WHERE " + condition;
        }
        return Constants.EMPTY;
    }

    // endregion

    // region other methods

    /**
     * 根据{@link Property}查找{@link Column}对象
     * @param property {@link Property}
     * @return {@link Column}对象
     */
    protected Column findColumn(final Property<?, ?> property) {
        return getColumn(PropertiesMappingCache.parse(property));
    }

    /**
     * 根据属性名查找{@link Column}对象
     * @param property 属性名
     * @return {@link Column}对象
     */
    protected Column findColumn(final String property) {
        return this.getColumn(property);
    }

    /**
     * 根据{@link SerializedLambda}获取{@link Column}对象
     * @param property {@link SerializedLambda}
     * @return {@link Column}对象
     */
    protected Column getColumn(final SerializedLambda property) {
        return getColumn(methodToProperty(property.getImplMethodName()));
    }

    /**
     * 根据属性获取{@link Column}对象
     * @param property 属性
     * @return {@link Column}对象
     */
    protected Column getColumn(final String property) {
        if (Objects.isBlank(property)) {
            return null;
        }
        final Column column = PropertiesMappingCache.getColumn(this.entityClass, property);
        if (column == null) {
            if (this.isStrict()) {
                throw new MyBatisException("The field mapping information for the entity class(" +
                    this.entityClass.getName() + ") cannot be found based on the `" + property + "` " +
                    "attribute. Check to see if the attribute exists or is decorated using the @Transient " +
                    "annotation.");
            } else {
                log.warn("The field mapping information for the entity class({}) cannot be found based on the `{}` " +
                    "attribute. Check to see if the attribute exists or is decorated using the @Transient " +
                    "annotation.", this.entityClass.getName(), property);
            }
        }
        return column;
    }

    /**
     * 根据列名获取{@link Column}对象
     * @param column 列名
     * @return {@link Column}
     */
    protected Column findOrgColumn(final String column) {
        if (Objects.isBlank(column)) {
            return null;
        }
        final Column col = TableHelper.getOrgColumn(this.entityClass, column);
        if (col == null) {
            if (this.isStrict()) {
                throw new MyBatisException("The column mapping information for the entity class(" +
                    this.entityClass.getName() + ") cannot be found based on the `" + column + "` " +
                    ". Check to see if the column exists or if the corresponding property of the entity " +
                    "class is decorated using the @Transient annotation.");
            } else {
                log.warn("The column mapping information for the entity class(" +
                    this.entityClass.getName() + ") cannot be found based on the `" + column + "` " +
                    ". Check to see if the column exists or if the corresponding property of the entity " +
                    "class is decorated using the @Transient annotation.");
            }
        }
        return col;
    }

    /**
     * lambda属性转成字符串属性
     * @param property 属性
     * @return 属性
     */
    protected String convert(Property<T, ?> property) {
        return methodToProperty(PropertiesMappingCache.parse(property).getImplMethodName());
    }

    /**
     * 根据方法获取属性名
     * @param property {@link Property}
     * @param <E>      泛型类型
     * @param <V>      属性类型
     * @return 属性名
     */
    protected <E, V> String methodToProperty(final Property<E, V> property) {
        return methodToProperty(PropertiesMappingCache.parse(property).getImplMethodName());
    }

    /**
     * 根据方法名获取属性名
     * @param method 方法名
     * @return 属性名
     */
    protected String methodToProperty(final String method) {
        return PropertiesMappingCache.methodToProperty(method);
    }

    // endregion
}
