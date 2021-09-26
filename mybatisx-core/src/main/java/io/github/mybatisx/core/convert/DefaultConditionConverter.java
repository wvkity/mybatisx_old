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
package io.github.mybatisx.core.convert;

import io.github.mybatisx.Objects;
import io.github.mybatisx.PlaceholderPattern;
import io.github.mybatisx.Placeholders;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.condition.Criterion;
import io.github.mybatisx.core.condition.ExistsCondition;
import io.github.mybatisx.core.condition.NestedCondition;
import io.github.mybatisx.core.condition.SpecialCondition;
import io.github.mybatisx.core.condition.StandardCondition;
import io.github.mybatisx.core.condition.SubQueryCondition;
import io.github.mybatisx.core.expr.AbstractBasicExpression;
import io.github.mybatisx.core.expr.AbstractBetweenExpression;
import io.github.mybatisx.core.expr.AbstractInExpression;
import io.github.mybatisx.core.expr.AbstractLikeExpression;
import io.github.mybatisx.core.expr.AbstractNullableExpression;
import io.github.mybatisx.core.expr.AbstractTemplateExpression;
import io.github.mybatisx.core.expr.ExistsExpression;
import io.github.mybatisx.core.expr.NativeExpression;
import io.github.mybatisx.core.expr.SpecialExpression;
import io.github.mybatisx.core.expr.StdNesting;
import io.github.mybatisx.core.expr.SubQueryExpression;
import io.github.mybatisx.core.inject.mapping.utils.Scripts;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Like;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.expr.Expression;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 条件转换器
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public class DefaultConditionConverter implements Converter<Expression<?>, Criterion> {

    private static final long serialVersionUID = 5410096049163817413L;
    protected final Criteria<?> criteria;
    protected final ParameterConverter parameterConverter;
    protected final PlaceholderConverter placeholderConverter;

    public DefaultConditionConverter(Criteria<?> criteria, ParameterConverter parameterConverter) {
        this.criteria = criteria;
        this.parameterConverter = parameterConverter;
        this.placeholderConverter = new DefaultPlaceholderConverter(this.parameterConverter);
    }

    @Override
    public Criterion convert(Expression<?> source) {
        if (Objects.nonNull(source)) {
            final Matched matched = source.getExprMatched();
            if (Objects.nonNull(matched)) {
                switch (matched) {
                    case STANDARD:
                        return this.standardExprConvert(source);
                    case IMMEDIATE:
                        if (source instanceof NativeExpression) {
                            return ((NativeExpression) source)::getCriterion;
                        }
                        return this.immediateExprConvert(source);
                    case EXISTS:
                        return this.existsExprConvert(source);
                    case QUERY:
                        return this.subQueryExprConvert(source);
                    case OTHER:
                    default:
                        return this.otherExprConvert(source);
                }
            }
        }
        return this.otherExprConvert(source);
    }

    /**
     * 字段对象条件表达式转条件对象
     * @param expr 表达式对象
     * @return {@link Criterion}
     */
    protected Criterion standardExprConvert(final Expression<?> expr) {
        final Column column = (Column) expr.getColumn();
        return this.convert(expr, column.getColumn(), column.getTypeHandler(), column.isUseJavaType(),
            column.getJavaType(), column.getJdbcType());
    }

    /**
     * 直接指定字段条件表达式转条件对象
     * @param expr 表达式对象
     * @return {@link Criterion}
     */
    protected Criterion immediateExprConvert(final Expression<?> expr) {
        return this.convert(expr, (String) expr.getColumn(), null, false, null, null);
    }

    /**
     * 表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否拼接JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion convert(final Expression<?> expr, final String column,
                                final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                final Class<?> javaType, final JdbcType jdbcType) {
        final Symbol symbol = expr.getSymbol();
        if (Objects.nonNull(symbol)) {
            switch (symbol) {
                case BETWEEN:
                case NOT_BETWEEN:
                    return this.betweenExprConvert(expr, column, typeHandler, useJavaType, javaType, jdbcType);
                case LIKE:
                case NOT_LIKE:
                    return this.likeExprConvert(expr, column, typeHandler, useJavaType, javaType, jdbcType);
                case NULL:
                case NOT_NULL:
                    return this.nullExprConvert(expr, column, typeHandler, useJavaType, javaType, jdbcType);
                case IN:
                case NOT_IN:
                    return this.inExprConvert(expr, column, typeHandler, useJavaType, javaType, jdbcType);
                case EXISTS:
                case NOT_EXISTS:
                    return this.existsExprConvert(expr);
                case TPL:
                    return this.templateExprConvert(expr, column, typeHandler, useJavaType, javaType, jdbcType);
                case SPECIAL:
                    return this.specialExprConvert(expr);
                case NESTING:
                    return this.nestingExprConvert(expr);
                case OTHER:
                    return this.otherExprConvert(expr);
                default:
                    return this.basicExprConvert(expr, column, typeHandler, useJavaType, javaType, jdbcType);
            }
        }
        return this.otherExprConvert(expr);
    }

    /**
     * 基础表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否凭借JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion basicExprConvert(final Expression<?> expr, final String column,
                                         final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                         final Class<?> javaType, final JdbcType jdbcType) {
        final AbstractBasicExpression<?> it = (AbstractBasicExpression<?>) expr;
        return new StandardCondition(it.getCriteria(), it.getAlias(), column,
            Scripts.convertConditionPartArg(it.getSymbol(), it.getSlot(), typeHandler, useJavaType,
                javaType, jdbcType, this.parameterConverter.convert(it.getValue())), it.getSymbol(), it.getValue());
    }

    /**
     * between表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否凭借JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion betweenExprConvert(final Expression<?> expr, final String column,
                                           final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                           final Class<?> javaType, final JdbcType jdbcType) {
        final AbstractBetweenExpression<?> it = (AbstractBetweenExpression<?>) expr;
        return new StandardCondition(it.getCriteria(), it.getAlias(), column,
            Scripts.convertConditionPartArg(it.getSymbol(), it.getSlot(), typeHandler, useJavaType,
                javaType, jdbcType, this.parameterConverter.converts(it.getBegin(), it.getEnd())), it.getSymbol());
    }

    /**
     * like表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否凭借JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion likeExprConvert(final Expression<?> expr, final String column,
                                        final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                        final Class<?> javaType, final JdbcType jdbcType) {
        final AbstractLikeExpression<?> it = (AbstractLikeExpression<?>) expr;
        final Like like = Optional.ofNullable(it.getLike()).orElse(Like.EXACT);
        final StringBuilder builder = new StringBuilder(80);
        builder.append(Scripts.convertConditionPartArg(it.getSymbol(), it.getSlot(), typeHandler, useJavaType,
            javaType, jdbcType, this.parameterConverter.convert(like.getSegment(String.valueOf(it.getValue())))));
        if (Objects.nonNull(it.getEscape())) {
            builder.append(" ESCAPE ").append(Constants.SINGLE_QUOTES).append(it.getEscape())
                .append(Constants.SINGLE_QUOTES);
        }
        return new StandardCondition(it.getCriteria(), it.getAlias(), column, builder.toString(), it.getSymbol(),
            it.getValue());
    }

    /**
     * 基础表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否凭借JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion nullExprConvert(final Expression<?> expr, final String column,
                                        final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                        final Class<?> javaType, final JdbcType jdbcType) {
        final AbstractNullableExpression<?> it = (AbstractNullableExpression<?>) expr;
        return new StandardCondition(it.getCriteria(), it.getAlias(), column,
            Scripts.convertConditionPartArg(it.getSymbol(), it.getSlot(), typeHandler, useJavaType,
                javaType, jdbcType), it.getSymbol());
    }

    /**
     * in表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否凭借JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion inExprConvert(final Expression<?> expr, final String column,
                                      final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                      final Class<?> javaType, final JdbcType jdbcType) {
        final AbstractInExpression<?> it = (AbstractInExpression<?>) expr;
        return new StandardCondition(it.getCriteria(), it.getAlias(), column,
            Scripts.convertConditionPartArg(it.getSymbol(), it.getSlot(), typeHandler, useJavaType,
                javaType, jdbcType, this.parameterConverter.converts(it.getValues())), it.getSymbol());
    }

    /**
     * exists条件表达式转条件对象
     * @param expr {@link Expression}
     * @return {@link Criterion}
     */
    protected Criterion existsExprConvert(final Expression<?> expr) {
        final ExistsExpression exists = (ExistsExpression) expr;
        return new ExistsCondition(exists.getCriteria(), exists.getSymbol(), exists.getSlot());
    }

    /**
     * template表达式转条件
     * @param expr        {@link Expression}
     * @param column      字段名
     * @param typeHandler {@link TypeHandler}
     * @param useJavaType 是否凭借JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    {@link JdbcType}
     * @return {@link Criterion}
     */
    protected Criterion templateExprConvert(final Expression<?> expr, final String column,
                                            final Class<? extends TypeHandler<?>> typeHandler,
                                            final boolean useJavaType,
                                            final Class<?> javaType, final JdbcType jdbcType) {
        final AbstractTemplateExpression<?> it = (AbstractTemplateExpression<?>) expr;
        final String templateStr = it.getTemplate();
        if (Objects.isNotBlank(templateStr)) {
            it.checkMatch();
            final PlaceholderPattern pattern = it.getPattern();
            final Slot slot = it.getSlot();
            final Object value = it.getValue();
            final Collection<Object> listValues = it.getListValues();
            final Map<String, Object> mapValues = it.getMapValues();
            final StringBuilder builder = new StringBuilder(100);
            if (Objects.nonNull(slot) && slot != Slot.NONE) {
                builder.append(slot.getSegment());
            }
            switch (pattern) {
                case MULTIPLE:
                    if (it.getExprMatched() == Matched.STANDARD) {
                        builder.append(Placeholders.format(templateStr, this.placeholderConverter.convert(listValues,
                            typeHandler, useJavaType, javaType, jdbcType)));
                    } else {
                        builder.append(Placeholders.format(templateStr, this.placeholderConverter.convert(listValues)));
                    }
                    break;
                case MAP:
                    builder.append(Placeholders.format(templateStr, this.placeholderConverter.convert(mapValues)));
                    break;
                default:
                    if (Objects.isNotEmpty(listValues)) {
                        builder.append(Placeholders.format(templateStr, this.placeholderConverter.convert(listValues,
                            typeHandler, useJavaType, javaType, jdbcType)));
                    } else {
                        builder.append(Placeholders.format(templateStr,
                            Scripts.safeJoining(this.parameterConverter.convert(value),
                                Scripts.concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType))));
                    }
                    break;
            }
            return new StandardCondition(it.getCriteria(), it.getAlias(), column, builder.toString(), it.getSymbol());
        }
        return null;
    }

    /**
     * 特殊条件表达式转条件对象
     * @param expr {@link Expression}
     * @return {@link Criterion}
     */
    protected Criterion specialExprConvert(final Expression<?> expr) {
        final SpecialExpression it = (SpecialExpression) expr;
        return new SpecialCondition(it.getCriteria(), it.getAlias(), it.getColumn(),
            it.getOtherCriteria(), it.getOtherTableAlias(), it.getOtherTarget(), Symbol.EQ,
            it.getSlot());
    }

    /**
     * 嵌套条件表达式转条件对象
     * @param expr {@link Expression}
     * @return {@link Criterion}
     */
    protected Criterion nestingExprConvert(final Expression<?> expr) {
        final StdNesting it = (StdNesting) expr;
        final List<Expression<?>> expressions = it.getConditions();
        if (Objects.isNotEmpty(expressions)) {
            final List<Criterion> conditions =
                expressions.stream().map(this::convert).filter(Objects::nonNull).collect(Collectors.toList());
            if (Objects.isNotEmpty(conditions)) {
                return new NestedCondition(it.isNot(), it.getSlot(), conditions);
            }
        }
        return null;
    }

    /**
     * 查询表达式转条件
     * @param expr {@link Expression}
     * @return {@link Criterion}
     */
    protected Criterion subQueryExprConvert(final Expression<?> expr) {
        final SubQueryExpression it = (SubQueryExpression) expr;
        return new SubQueryCondition(it.getCriteria(), it.getAlias(), it.getColumn(), it.getSlot(), it.getSymbol(),
            it.getQuery());
    }

    /**
     * 其他条件表达式转条件
     * @param expr 条件表达式
     * @return {@link Criterion}
     */
    protected Criterion otherExprConvert(final Expression<?> expr) {
        // Empty body
        return null;
    }

    public Criteria<?> getCriteria() {
        return criteria;
    }

    public ParameterConverter getParameterConverter() {
        return parameterConverter;
    }

    public PlaceholderConverter getPlaceholderConverter() {
        return placeholderConverter;
    }
}
