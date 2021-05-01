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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.expr.AbstractBasicExpression;
import com.wvkity.mybatis.core.expr.AbstractBetweenExpression;
import com.wvkity.mybatis.core.expr.AbstractFuzzyExpression;
import com.wvkity.mybatis.core.expr.AbstractNullableExpression;
import com.wvkity.mybatis.core.expr.AbstractRangeExpression;
import com.wvkity.mybatis.core.expr.AbstractTemplateExpression;
import com.wvkity.mybatis.core.expr.Native;
import com.wvkity.mybatis.core.expr.SpecialExpression;
import com.wvkity.mybatis.core.expr.StandardIdEqual;
import com.wvkity.mybatis.core.expr.StandardNesting;
import com.wvkity.mybatis.core.expr.TemplateMatch;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.utils.Placeholders;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.expr.Expression;
import com.wvkity.mybatis.support.constant.Like;
import com.wvkity.mybatis.support.constant.Slot;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 条件转换器
 * @author wvkity
 * @created 2021-04-11
 * @since 1.0.0
 */
public class ConditionConverter {

    private final AbstractBasicCriteria<?, ?> criteria;

    public ConditionConverter(AbstractBasicCriteria<?, ?> criteria) {
        this.criteria = criteria;
    }

    /**
     * 表达式转成条件对象
     * @param expression {@link Expression}
     * @return {@link Criterion}
     */
    public Criterion convert(final Expression expression) {
        if (Objects.nonNull(expression)) {
            final Matched matched = expression.getExprMode();
            switch (matched) {
                case IMMEDIATE:
                    if (expression instanceof Native) {
                        return ((Native) expression)::getCriterion;
                    }
                    return this.convertToImmediateCondition(expression);
                case STANDARD:
                    return this.convertToStandardCondition(expression);
                default:
                    break;
            }
        }
        return null;
    }

    protected String defPlaceholder(final Object... values) {
        return this.criteria.defPlaceholder(values);
    }

    protected List<String> defPlaceholders(final Object... values) {
        return this.criteria.defPlaceholders(values);
    }

    /**
     * 条件表达式转条件对象
     * @param expression {@link Expression}
     * @return {@link Criterion}
     */
    protected Criterion convertToImmediateCondition(final Expression expression) {
        return convertToCondition(expression, expression.getTarget());
    }

    /**
     * 条件表达式转条件对象
     * @param expression {@link Expression}
     * @return {@link Criterion}
     */
    protected Criterion convertToStandardCondition(final Expression expression) {
        if (expression instanceof StandardIdEqual) {
            return convertToCondition(expression, ((AbstractBasicCriteria<?, ?>) expression.getCriteria()).getId());
        }
        final Column column = this.criteria.findColumn(expression.getTarget());
        if (Objects.nonNull(column)) {
            return convertToCondition(expression, column);
        }
        return null;
    }

    /**
     * 条件表达式转条件对象
     * @param expression {@link Expression}
     * @param column     字段名
     * @return {@link Criterion}
     */
    protected Criterion convertToCondition(final Expression expression, final String column) {
        return convertToCondition(expression, column, null, false, null, null);
    }

    /**
     * 条件表达式转条件对象
     * @param expression {@link Expression}
     * @param column     {@link Column}
     * @return {@link Criterion}
     */
    protected Criterion convertToCondition(final Expression expression, final Column column) {
        return convertToCondition(expression, column.getColumn(), column.getTypeHandler(),
            column.isUseJavaType(), column.getJavaType(), column.getJdbcType());
    }

    /**
     * 条件表达式转条件对象
     * @param expression  {@link Expression}
     * @param column      字段名
     * @param typeHandler 类型处理器类
     * @param useJavaType 是否拼接JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    JDBC类型
     * @return {@link Criterion}
     */
    protected Criterion convertToCondition(final Expression expression, final String column,
                                           final Class<? extends TypeHandler<?>> typeHandler,
                                           final boolean useJavaType,
                                           final Class<?> javaType, final JdbcType jdbcType) {
        if (Objects.isNotBlank(column) || expression instanceof StandardNesting
            || expression instanceof AbstractTemplateExpression) {
            if (expression instanceof AbstractBasicExpression) {
                return this.basicExprConvert((AbstractBasicExpression<?>) expression, column, typeHandler,
                    useJavaType, javaType, jdbcType);
            } else if (expression instanceof AbstractBetweenExpression) {
                return this.betweenExprConvert((AbstractBetweenExpression<?>) expression, column, typeHandler,
                    useJavaType, javaType, jdbcType);
            } else if (expression instanceof AbstractFuzzyExpression) {
                return this.fuzzyExprConvert((AbstractFuzzyExpression<?>) expression, column, typeHandler,
                    useJavaType, javaType, jdbcType);
            } else if (expression instanceof AbstractNullableExpression) {
                return new Condition(expression.getCriteria(), expression.getAlias(), column,
                    Scripts.convertConditionPartArg(expression.getSymbol(), expression.getSlot(),
                        typeHandler, useJavaType, javaType, jdbcType));
            } else if (expression instanceof AbstractRangeExpression) {
                return this.rangeExprConvert((AbstractRangeExpression<?>) expression, column, typeHandler,
                    useJavaType, javaType, jdbcType);
            } else if (expression instanceof AbstractTemplateExpression) {
                return this.templateExprConvert((AbstractTemplateExpression<?>) expression, column, typeHandler,
                    useJavaType, javaType, jdbcType);
            } else if (expression instanceof StandardNesting) {
                return this.nestingExprConvert((StandardNesting) expression, column, typeHandler, useJavaType,
                    javaType, jdbcType);
            } else if (expression instanceof SpecialExpression) {
                return this.specialExprConvert((SpecialExpression) expression);
            }
        }
        return null;
    }

    protected Criterion basicExprConvert(final AbstractBasicExpression<?> basic, final String column,
                                         final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                         final Class<?> javaType, final JdbcType jdbcType) {
        return new Condition(basic.getCriteria(), basic.getAlias(), column,
            Scripts.convertConditionPartArg(basic.getSymbol(), basic.getSlot(), typeHandler,
                useJavaType, javaType, jdbcType, this.defPlaceholder(basic.getValue())));
    }

    protected Criterion betweenExprConvert(final AbstractBetweenExpression<?> between, final String column,
                                           final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                           final Class<?> javaType, final JdbcType jdbcType) {
        return new Condition(between.getCriteria(), between.getAlias(), column,
            Scripts.convertConditionPartArg(between.getSymbol(), between.getSlot(), typeHandler, useJavaType,
                javaType, jdbcType, this.defPlaceholders(between.getBegin(), between.getEnd())));
    }

    protected Criterion fuzzyExprConvert(final AbstractFuzzyExpression<?> fuzzy, final String column,
                                         final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                         final Class<?> javaType, final JdbcType jdbcType) {
        final Like like = Optional.ofNullable(fuzzy.getLike()).orElse(Like.ANYWHERE);
        final StringBuilder builder = new StringBuilder(50);
        builder.append(Scripts.convertConditionPartArg(fuzzy.getSymbol(), fuzzy.getSlot(),
            typeHandler, useJavaType, javaType, jdbcType,
            this.defPlaceholder(like.getSegment(String.valueOf(fuzzy.getValue())))));
        if (Objects.nonNull(fuzzy.getEscape())) {
            builder.append(" ESCAPE ").append("'").append(fuzzy.getEscape()).append("'");
        }
        return new Condition(fuzzy.getCriteria(), fuzzy.getAlias(), column, builder.toString());
    }

    protected Criterion rangeExprConvert(final AbstractRangeExpression<?> range, final String column,
                                         final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                         final Class<?> javaType, final JdbcType jdbcType) {
        return new Condition(range.getCriteria(), range.getAlias(), column,
            Scripts.convertConditionPartArg(range.getSymbol(), range.getSlot(), typeHandler, useJavaType,
                javaType, jdbcType, this.defPlaceholders(range.getValues())));
    }

    protected Criterion templateExprConvert(final AbstractTemplateExpression<?> template, final String column,
                                            final Class<? extends TypeHandler<?>> typeHandler,
                                            final boolean useJavaType,
                                            final Class<?> javaType, final JdbcType jdbcType) {
        final String templateStr = template.getTemplate();
        if (Objects.isNotBlank(templateStr)) {
            template.checkMatch();
            final TemplateMatch match = template.getMatch();
            final StringBuilder builder = new StringBuilder(60);
            final Slot slot = template.getSlot();
            final Object value = template.getValue();
            final Collection<Object> listValues = template.getListValues();
            final Map<String, Object> mapValues = template.getMapValues();
            if (Objects.nonNull(slot) && slot != Slot.NONE) {
                builder.append(slot.getSegment()).append(Constants.SPACE);
            }
            switch (match) {
                case MULTIPLE:
                    builder.append(Placeholders.format(templateStr, listValues.stream().map(it ->
                        Scripts.safeJoining(this.defPlaceholder(it),
                            Scripts.concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)))
                        .collect(Collectors.toList())));
                    break;
                case MAP:
                    builder.append(Placeholders.format(templateStr, mapValues.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                            it -> Scripts.safeJoining(this.defPlaceholder(it),
                                Scripts.concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)),
                            (o, n) -> n, (Supplier<LinkedHashMap<String, Object>>) LinkedHashMap::new))));
                    break;
                default:
                    if (Objects.isNotEmpty(listValues)) {
                        builder.append(Placeholders.format(templateStr, listValues.stream().map(it ->
                            Scripts.safeJoining(this.defPlaceholder(it),
                                Scripts.concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)))
                            .collect(Collectors.joining(Constants.COMMA_SPACE))));
                    } else {
                        builder.append(Placeholders.format(templateStr,
                            Scripts.safeJoining(this.defPlaceholder(value),
                                Scripts.concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType))));
                    }
                    break;
            }
            return new Condition(template.getCriteria(), template.getAlias(), column, builder.toString());
        }
        return null;
    }

    protected Criterion nestingExprConvert(final StandardNesting nesting, final String column,
                                           final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                           final Class<?> javaType, final JdbcType jdbcType) {
        final List<Expression> expressions = nesting.getConditions();
        if (Objects.isNotEmpty(expressions)) {
            final List<Criterion> conditions = expressions.stream().map(this::convert)
                .filter(Objects::nonNull).collect(Collectors.toList());
            if (Objects.isNotEmpty(conditions)) {
                return new NestedCondition(nesting.isNot(), nesting.getSlot(), conditions);
            }
        }
        return null;
    }

    protected Criterion specialExprConvert(final SpecialExpression special) {
        return new SpecialCondition(special.getCriteria(), special.getAlias(), special.getTarget(),
            special.getOtherCriteria(), special.getOtherTableAlias(), special.getOtherTarget(), special.getSymbol(),
            special.getSlot());
    }
}
