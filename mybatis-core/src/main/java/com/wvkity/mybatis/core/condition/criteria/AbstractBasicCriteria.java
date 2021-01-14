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

import com.wvkity.mybatis.core.condition.basic.SegmentManager;
import com.wvkity.mybatis.core.condition.expression.Expression;
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
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.constant.LikeMode;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.utils.Objects;

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
    protected AtomicReference<String> tableAlias;
    /**
     * 是否使用别名
     */
    protected AtomicBoolean useAlias;
    /**
     * 默认别名
     */
    protected String defTableAlias;

    // endregion

    /**
     * 初始化方法
     * @param alias 表别名
     */
    protected void inits(final String alias) {
        this.parameterValueMapping = new ConcurrentHashMap<>();
        this.parameterSequence = new AtomicInteger(0);
        this.segmentManager = new SegmentManager();
        this.notMatchingWithThrows = new AtomicBoolean(true);
        this.tableAliasSequence = new AtomicInteger(0);
        this.useAlias = new AtomicBoolean(false);
        this.tableAlias = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
    }

    // region Add criterion methods

    /**
     * 添加条件
     * @param criterion {@link Expression}
     * @return {@link Chain}
     */
    protected Chain add(final Expression criterion) {
        Optional.ofNullable(criterion).ifPresent(it -> this.segmentManager.where(it.setIfNecessary(this)));
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
                this.where(Objects.asList(expressions));
            }
        }
        return this.context;
    }

    @Override
    public Chain where(Collection<Expression> expressions) {
        if (Objects.isNotEmpty(expressions)) {
            this.segmentManager.where(expressions.stream().filter(Objects::nonNull).map(it -> it.setIfNecessary(this))
                .collect(Collectors.toList()));
        }
        return this.context;
    }

    // endregion

    // region Compare conditions

    @Override
    public Chain idEq(Object value, Slot slot) {
        return add(StandardIdEqual.create().criteria(this).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain eq(Property<T, V> property, V value, Slot slot) {
        return add(StandardEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain eq(String property, Object value, Slot slot) {
        return add(StandardEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colEq(String column, Object value, Slot slot) {
        return add(ImmediateEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public Chain eq(Map<String, Object> properties, Slot slot) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                final String property = entry.getKey();
                if (Objects.isNotBlank(property)) {
                    this.add(StandardEqual.create().criteria(this).property(property).value(entry.getValue())
                        .slot(slot).build());
                }
            }
        }
        return this.context;
    }

    @Override
    public Chain colEq(Map<String, Object> properties, Slot slot) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                final String column = entry.getKey();
                if (Objects.isNotBlank(column)) {
                    this.add(ImmediateEqual.create().criteria(this).column(column).value(entry.getValue())
                        .slot(slot).build());
                }
            }
        }
        return this.context;
    }

    @Override
    public <V> Chain ne(Property<T, V> property, V value, Slot slot) {
        return add(StandardNotEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain ne(String property, Object value, Slot slot) {
        return add(StandardNotEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colNe(String column, Object value, Slot slot) {
        return add(ImmediateNotEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain gt(Property<T, V> property, V value, Slot slot) {
        return add(StandardGreaterThan.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain gt(String property, Object value, Slot slot) {
        return add(StandardGreaterThan.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colGt(String column, Object value, Slot slot) {
        return add(ImmediateGreaterThan.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain ge(Property<T, V> property, V value, Slot slot) {
        return add(StandardGreaterThanOrEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain ge(String property, Object value, Slot slot) {
        return add(StandardGreaterThanOrEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colGe(String column, Object value, Slot slot) {
        return add(ImmediateGreaterThanOrEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain lt(Property<T, V> property, V value, Slot slot) {
        return add(StandardLessThan.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain lt(String property, Object value, Slot slot) {
        return add(StandardLessThan.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colLt(String column, Object value, Slot slot) {
        return add(ImmediateLessThan.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain le(Property<T, V> property, V value, Slot slot) {
        return add(StandardLessThanOrEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain le(String property, Object value, Slot slot) {
        return add(StandardLessThanOrEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colLe(String column, Object value, Slot slot) {
        return add(ImmediateLessThanOrEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    // endregion

    // region Range conditions

    @Override
    @SuppressWarnings({"unchecked"})
    public <V> Chain in(Property<T, V> property, Collection<V> values, Slot slot) {
        return add(StandardIn.create().values((Collection<Object>) values).criteria(this).property(property)
            .slot(slot).build());
    }

    @Override
    public Chain in(String property, Collection<Object> values, Slot slot) {
        return add(StandardIn.create().values(values).criteria(this).property(property)
            .slot(slot).build());
    }

    @Override
    public Chain colIn(String column, Collection<Object> values, Slot slot) {
        return add(ImmediateIn.create().values(values).criteria(this).column(column).slot(slot).build());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <V> Chain notIn(Property<T, V> property, Collection<V> values, Slot slot) {
        return add(StandardNotIn.create().values((Collection<Object>) values).criteria(this).property(property)
            .slot(slot).build());
    }

    @Override
    public Chain notIn(String property, Collection<Object> values, Slot slot) {
        return add(StandardNotIn.create().values(values).criteria(this).property(property)
            .slot(slot).build());
    }

    @Override
    public Chain colNotIn(String column, Collection<Object> values, Slot slot) {
        return add(ImmediateNotIn.create().values(values).criteria(this).column(column).slot(slot).build());
    }

    // endregion

    // region Between conditions

    @Override
    public <V> Chain between(Property<T, V> property, V begin, V end, Slot slot) {
        return add(StandardBetween.create().begin(begin).end(end).criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain between(String property, Object begin, Object end, Slot slot) {
        return add(StandardBetween.create().begin(begin).end(end).criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain colBetween(String column, Object begin, Object end, Slot slot) {
        return add(ImmediateBetween.create().begin(begin).end(end).criteria(this).column(column).slot(slot).build());
    }

    @Override
    public <V> Chain notBetween(Property<T, V> property, V begin, V end, Slot slot) {
        return add(StandardNotBetween.create().begin(begin).end(end).criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain notBetween(String property, Object begin, Object end, Slot slot) {
        return add(StandardNotBetween.create().begin(begin).end(end).criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain colNotBetween(String column, Object begin, Object end, Slot slot) {
        return add(ImmediateNotBetween.create().begin(begin).end(end).criteria(this).column(column).slot(slot).build());
    }

    // endregion

    // region Fuzzy conditions

    @Override
    public <V> Chain like(Property<T, V> property, V value, LikeMode mode, Character escape, Slot slot) {
        return add(StandardLike.create().escape(escape).mode(mode).criteria(this).property(property)
            .slot(slot).value(value).build());
    }

    @Override
    public Chain like(String property, Object value, LikeMode mode, Character escape, Slot slot) {
        return add(StandardLike.create().escape(escape).mode(mode).criteria(this).property(property)
            .slot(slot).value(value).build());
    }

    @Override
    public Chain colLike(String column, Object value, LikeMode mode, Character escape, Slot slot) {
        return add(ImmediateLike.create().escape(escape).mode(mode).criteria(this).column(column)
            .slot(slot).value(value).build());
    }

    @Override
    public <V> Chain notLike(Property<T, V> property, V value, LikeMode mode, Character escape, Slot slot) {
        return add(StandardNotLike.create().escape(escape).mode(mode).criteria(this).property(property)
            .slot(slot).value(value).build());
    }

    @Override
    public Chain notLike(String property, Object value, LikeMode mode, Character escape, Slot slot) {
        return add(StandardNotLike.create().escape(escape).mode(mode).criteria(this).property(property)
            .slot(slot).value(value).build());
    }

    @Override
    public Chain colNotLike(String column, Object value, LikeMode mode, Character escape, Slot slot) {
        return add(ImmediateNotLike.create().escape(escape).mode(mode).criteria(this).column(column)
            .slot(slot).value(value).build());
    }

    // endregion

    // region Nullable conditions

    @Override
    public <V> Chain isNull(Property<T, V> property, Slot slot) {
        return add(StandardNull.create().criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain isNull(String property, Slot slot) {
        return add(StandardNull.create().criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain colIsNull(String column, Slot slot) {
        return add(ImmediateNull.create().criteria(this).column(column).slot(slot).build());
    }

    @Override
    public <V> Chain notNull(Property<T, V> property, Slot slot) {
        return add(StandardNotNull.create().criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain notNull(String property, Slot slot) {
        return add(StandardNotNull.create().criteria(this).property(property).slot(slot).build());
    }

    @Override
    public Chain colNotNull(String column, Slot slot) {
        return add(ImmediateNotNull.create().criteria(this).column(column).slot(slot).build());
    }

    // endregion

    // region Nested conditions

    @Override
    public Chain and(boolean not, List<Expression> conditions) {
        return add(StandardNesting.create().criteria(this).slot(Slot.AND).not(not).conditions(conditions).build());
    }

    @Override
    public Chain and(boolean not, Criteria<?> criteria, List<Expression> conditions) {
        return add(StandardNesting.create().criteria(criteria).slot(Slot.AND).not(not).conditions(conditions).build());
    }

    @Override
    public Chain and(Function<Chain, Chain> apply, boolean not) {
        return this.doIt(apply, true, not);
    }

    @Override
    public Chain or(boolean not, List<Expression> conditions) {
        return add(StandardNesting.create().criteria(this).slot(Slot.OR).not(not).conditions(conditions).build());
    }

    @Override
    public Chain or(boolean not, Criteria<?> criteria, List<Expression> conditions) {
        return add(StandardNesting.create().criteria(criteria).slot(Slot.OR).not(not).conditions(conditions).build());
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
                final List<Expression> conditions = instance.segmentManager.getConditions();
                if (Objects.isNotEmpty(conditions)) {
                    if (and) {
                        this.and(not, conditions);
                    } else {
                        this.or(not, conditions);
                    }
                }
            }
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
                target.tableAlias = source.tableAlias;
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
            final String alias = this.tableAlias.get();
            return Objects.isBlank(alias) ? this.defTableAlias : alias;
        }
        return Constants.EMPTY;
    }

    @Override
    public Chain notMatchingWithThrows(boolean throwing) {
        this.notMatchingWithThrows.compareAndSet(!throwing, throwing);
        return this.context;
    }

    @Override
    public String convert(String source, boolean format, Object... args) {
        if (Objects.isBlank(source)) {
            return null;
        }
        if (format && !Objects.isEmpty(args)) {
            final int size = args.length;
            String template = source;
            for (int i = 0; i < size; i++) {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                template = template.replace(String.format(DEF_PARAMETER_PLACEHOLDER, i),
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
                this.parameterValueMapping.put(paramName, args[i]);
            }
            return template;
        }
        return source;
    }

    @Override
    public List<String> converts(String source, Collection<Object> args) {
        if (Objects.isNotBlank(source) && Objects.isNotEmpty(args)) {
            return args.stream().map(it -> {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                final Object value = it == null ? "null" : it;
                this.parameterValueMapping.put(paramName, value);
                return source.replace(String.format(DEF_PARAMETER_PLACEHOLDER, 0),
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getSegment() {
        return this.segmentManager.getSegment();
    }

    // endregion
}
