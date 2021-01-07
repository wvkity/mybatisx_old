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
package com.wvkity.mybatis.core.inject.mapping.utils;

import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.constant.Operation;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SQL脚本工具
 * @author wvkity
 * @created 2020-10-22
 * @since 1.0.0
 */
public final class Scripts implements Constants {

    private Scripts() {
    }

    /**
     * 将字段转成占位符参数
     * @param argument  方法参数名
     * @param operation 操作类型
     * @param column    {@link Column}对象
     * @return 占位符参数
     * @see #convertToPartArg(String, Symbol, Operation, String, String, String, String, Class, boolean, Class, JdbcType)
     */
    public static String convertToPartArg(final String argument, final Operation operation, final Column column) {
        return convertToPartArg(argument, operation, NULL, column);
    }

    /**
     * 将字段转成占位符参数
     * @param argument   方法参数名
     * @param operation  操作类型
     * @param tableAlias 表别名
     * @param column     {@link Column}对象
     * @return 占位符参数
     * @see #convertToPartArg(String, Symbol, Operation, String, String, String, String, Class, boolean, Class, JdbcType)
     */
    public static String convertToPartArg(final String argument, final Operation operation,
                                          final String tableAlias, final Column column) {
        return convertToPartArg(argument, Symbol.EQ, operation, tableAlias, column);
    }

    /**
     * 将字段转成占位符参数
     * @param argument   方法参数名
     * @param symbol     条件符号
     * @param operation  操作类型
     * @param tableAlias 表别名
     * @param column     {@link Column}对象
     * @return 占位符参数
     * @see #convertToPartArg(String, Symbol, Operation, String, String, String, String, Class, boolean, Class, JdbcType)
     */
    public static String convertToPartArg(final String argument, final Symbol symbol, final Operation operation,
                                          final String tableAlias, final Column column) {
        return convertToPartArg(argument, symbol, operation, tableAlias, column, NULL);
    }

    /**
     * 将字段转成占位符参数
     * @param argument   方法参数名
     * @param symbol     条件符号
     * @param operation  操作类型
     * @param tableAlias 表别名
     * @param column     {@link Column}对象
     * @param separator  分隔符
     * @return 占位符参数
     * @see #convertToPartArg(String, Symbol, Operation, String, String, String, String, Class, boolean, Class, JdbcType)
     */
    public static String convertToPartArg(final String argument, final Symbol symbol, final Operation operation,
                                          final String tableAlias, final Column column, final String separator) {
        return convertToPartArg(argument, symbol, operation, tableAlias, column, separator, column.getTypeHandler(),
            column.isUseJavaType(), column.getJavaType(), column.getJdbcType());
    }

    /**
     * 将字段转成占位符参数
     * @param argument    方法参数名
     * @param symbol      条件符号
     * @param operation   操作类型
     * @param tableAlias  表别名
     * @param column      {@link Column}对象
     * @param separator   分隔符
     * @param typeHandler 类型处理器
     * @param useJavaType 是否拼接JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    JDBC类型
     * @return 占位符参数
     * @see #convertToPartArg(String, Symbol, Operation, String, String, String, String, Class, boolean, Class, JdbcType)
     */
    public static String convertToPartArg(final String argument, final Symbol symbol, final Operation operation,
                                          final String tableAlias, final Column column, final String separator,
                                          final Class<? extends TypeHandler<?>> typeHandler, final boolean useJavaType,
                                          final Class<?> javaType, final JdbcType jdbcType) {
        return convertToPartArg(argument, symbol, operation, tableAlias, column.getColumn(), column.getProperty(),
            separator, typeHandler, useJavaType, javaType, jdbcType);
    }

    /**
     * 将字段转成占位符参数
     * <p>
     * // Examples:
     * // column = {column=USER_NAME, property=userName, jdbcType={@link JdbcType#VARCHAR VARCHAR}, javaType={@link String java.lang.String}, useJavaType=true}
     *
     * <pre>{@code
     *   // 保存操作
     *   Scripts.convertPartArg(Constants.PARAM_ENTITY, Operation.INSERT, column);
     *   // return:
     *   // #{entity.userName, jdbcType=VARCHAR, javaType=java.lang.String}
     *
     *   // 更新操作
     *   Scripts.convertPartArg(Constants.PARAM_ENTITY, Operation.REPLACE, column);
     *   // return:
     *   // USER_NAME = #{entity.userName, jdbcType=VARCHAR, javaType=java.lang.String}
     * }</pre>
     * @param argument    方法参数名
     * @param symbol      条件符号
     * @param operation   操作类型
     * @param tableAlias  表别名
     * @param column      字段名
     * @param property    属性
     * @param separator   分隔符
     * @param typeHandler 类型处理器
     * @param useJavaType 是否拼接JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    JDBC类型
     * @return 占位符参数
     */
    public static String convertToPartArg(final String argument, final Symbol symbol, final Operation operation,
                                          final String tableAlias, final String column, final String property,
                                          final String separator, final Class<? extends TypeHandler<?>> typeHandler,
                                          final boolean useJavaType, final Class<?> javaType, final JdbcType jdbcType) {
        final StringBuilder builder = new StringBuilder(60);
        if (operation != Operation.INSERT) {
            if (Objects.isNotBlank(tableAlias)) {
                builder.append(tableAlias).append(DOT);
            }
            builder.append(column).append(SPACE);
            if (symbol == null) {
                builder.append(Symbol.EQ.getSegment());
            } else {
                builder.append(symbol.getSegment());
            }
            builder.append(SPACE);
        }
        if (operation == Operation.NONE) {
            builder.append(safeJoining(Objects.isBlank(argument) ? "value" : argument,
                concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)));
        } else {
            if (Objects.isBlank(argument)) {
                builder.append(safeJoining(argument, property,
                    concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)));
            } else {
                builder.append(safeJoining(argument, DOT, property,
                    concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)));
            }
        }
        if (Objects.isNotEmpty(separator)) {
            builder.append(separator);
        }
        return builder.toString();
    }

    /**
     * 拼接安全占位符参数
     * <blockquote><pre>
     *     Scripts.safeJoining("entity", ".", "name")
     *     // return:
     *     // #{entity.name}
     * </pre></blockquote>
     * @param args 参数列表
     * @return 占位符参数
     */
    public static String safeJoining(final String... args) {
        final String result = Arrays.stream(args).filter(Objects::nonNull).collect(Collectors.joining(EMPTY));
        return Objects.isBlank(result) ? EMPTY : (HASH_OPEN_BRACE + result + CLOSE_BRACE);
    }

    /**
     * 拼接不安全占位符参数
     * <blockquote><pre>
     *     Scripts.unsafeJoining("entity", ".", "name")
     *     // return:
     *     // ${entity.name}
     * </pre></blockquote>
     * @param args 参数列表
     * @return 占位符参数
     */
    public static String unsafeJoining(final String... args) {
        final String result = Arrays.stream(args).filter(Objects::nonNull).collect(Collectors.joining(EMPTY));
        return Objects.isBlank(result) ? EMPTY : (DOLLAR_OPEN_BRACE + result + CLOSE_BRACE);
    }

    /**
     * 拼接完整类型参数
     * @param typeHandler 类型处理器
     * @param useJavaType 是否拼接JAVA类型
     * @param javaType    JAVA类型
     * @param jdbcType    JDBC类型
     * @return 参数字符串
     */
    public static String concatIntactTypeArg(final Class<? extends TypeHandler<?>> typeHandler,
                                             final boolean useJavaType, final Class<?> javaType,
                                             final JdbcType jdbcType) {
        final StringBuilder builder = new StringBuilder();
        if (jdbcType != null && jdbcType != JdbcType.UNDEFINED) {
            builder.append(", jdbcType=").append(jdbcType.toString());
        }
        if (typeHandler != null && typeHandler != UnknownTypeHandler.class) {
            builder.append(", typeHandler=").append(typeHandler.getCanonicalName());
        }
        if (useJavaType && javaType != null) {
            builder.append(", javaType=").append(javaType.getCanonicalName());
        }
        return builder.toString();
    }

    /**
     * 转成if条件标签脚本
     * @param argument   参数名
     * @param symbol     条件符号
     * @param operation  操作类型
     * @param tableAlias 表别名
     * @param column     {@link Column}
     * @param separator  分隔符
     * @param isQuery    是否为查询
     * @param toValue    转成值
     * @param slot       {@link Slot}
     * @return if条件标签
     */
    public static String convertToIfTag(final String argument, final Symbol symbol,
                                        final Operation operation, final String tableAlias,
                                        final Column column, final String separator,
                                        final boolean isQuery, final boolean toValue, final Slot slot) {
        final StringBuilder script = new StringBuilder(45);
        script.append(SPACE);
        if (toValue) {
            if (slot != null) {
                script.append(slot.getSegment()).append(SPACE);
            }
            script.append(convertToPartArg(argument, symbol, operation, isQuery ? tableAlias : NULL, column, separator));
        } else {
            script.append(column.getColumn()).append(COMMA_SPACE);
        }
        // 非空/非空白值检查
        if (!column.isCheckNotNull() && !column.isCheckNotEmpty()) {
            return script.toString();
        }
        final StringBuilder ifTagCondition = new StringBuilder(45);
        final boolean hasArgument = Objects.isNotBlank(argument);
        final String property = column.getProperty();
        if (hasArgument) {
            ifTagCondition.append(argument).append(DOT);
        }
        if (column.isCheckNotNull() || column.isCheckNotEmpty()) {
            ifTagCondition.append(property).append(" != null");
        }
        if (column.isCheckNotEmpty() && String.class.isAssignableFrom(column.getJavaType())) {
            ifTagCondition.append(" and ");
            if (hasArgument) {
                ifTagCondition.append(argument).append(DOT);
            }
            ifTagCondition.append(property).append(" != ''");
        }
        return convertToIfTag(script.toString(), ifTagCondition.toString(), true);
    }

    /**
     * 转换成if条件标签脚本
     * @param script    if条件标签体
     * @param condition 条件部分
     * @param newLine   是否换行
     * @return if条件标签
     */
    public static String convertToIfTag(final String script, final String condition, final boolean newLine) {
        final String body;
        if (newLine) {
            body = NEW_LINE + script + NEW_LINE;
        } else {
            body = script;
        }
        return "<if test=\"" + condition + "\">" + body + "</if>";
    }

    /**
     * 转换成trim标签脚本
     * @param script          SQL脚本
     * @param prefix          前缀
     * @param suffix          后缀
     * @param prefixOverrides 干掉最前一个
     * @param suffixOverrides 干掉最后一个
     * @return trim标签脚本
     */
    public static String convertToTrimTag(final String script, final String prefix, final String prefixOverrides,
                                          final String suffix, final String suffixOverrides) {
        final StringBuilder builder = new StringBuilder(60);
        builder.append("<trim");
        if (Objects.isNotBlank(prefix)) {
            builder.append(SPACE).append("prefix=").append(CHAR_QUOTE).append(prefix).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(suffix)) {
            builder.append(SPACE).append("suffix=").append(CHAR_QUOTE).append(suffix).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(prefixOverrides)) {
            builder.append(SPACE).append("prefixOverrides=").append(CHAR_QUOTE).append(prefixOverrides).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(suffixOverrides)) {
            builder.append(SPACE).append("suffixOverrides=").append(CHAR_QUOTE).append(suffixOverrides).append(CHAR_QUOTE);
        }
        return builder.append(GT).append(NEW_LINE).append(script).append(NEW_LINE).append("</trim>").toString();
    }

    /**
     * 转换成查询字段参数
     * @param column {@link Column}
     * @return 查询字段参数
     */
    public static String convertToSelectArg(final Column column) {
        return convertToSelectArg(null, column, null, false);
    }

    /**
     * 转换成查询字段参数
     * @param tableAlias 表别名
     * @param column     {@link Column}
     * @param ref        实体中的引用属性
     * @param apply      是否带上属性名
     * @return 查询字段参数
     */
    public static String convertToSelectArg(final String tableAlias, final Column column,
                                            final String ref, final boolean apply) {
        final String columnAlias;
        if (apply) {
            columnAlias = Objects.isNotBlank(ref) ? (ref + DOT + column.getProperty()) : column.getProperty();
        } else {
            columnAlias = null;
        }
        return Scripts.convertToSelectArg(tableAlias, column.getColumn(), columnAlias);
    }

    /**
     * 转换成查询字段参数
     * @param tableAlias  表别名
     * @param column      字段
     * @param columnAlias 字段别名
     * @return 查询字段参数
     */
    public static String convertToSelectArg(final String tableAlias, final String column, final String columnAlias) {
        final StringBuilder builder = new StringBuilder(40);
        if (Objects.isNotBlank(tableAlias)) {
            builder.append(tableAlias).append(DOT);
        }
        builder.append(column);
        if (Objects.isNotBlank(columnAlias)) {
            builder.append(" AS ");
            if (columnAlias.contains(DOT)) {
                builder.append(CHAR_QUOTE).append(columnAlias).append(CHAR_QUOTE);
            } else {
                builder.append(columnAlias);
            }
        }
        return builder.toString();
    }

    /**
     * 转换成where标签
     * @param script SQL语句
     * @return where标签
     */
    public static String convertToWhereTag(final String script) {
        return NEW_LINE + "<where>" + NEW_LINE + script + NEW_LINE + "</where>";
    }

    /**
     * 转换成foreach脚本标签
     * @param collection 数据集
     * @param item       数据
     * @param open       前缀
     * @param close      后缀
     * @param separator  分隔符
     * @param script     SQL脚本
     * @return foreach脚本标签
     */
    public static String convertToForeachTag(final String collection, final String item, final String open,
                                             final String close, final String separator, final String script) {
        return convertToForeachTag(collection, item, null, open, close, separator, script);
    }

    /**
     * 转换成foreach脚本标签
     * @param collection 数据集
     * @param item       数据
     * @param index      索引
     * @param open       前缀
     * @param close      后缀
     * @param separator  分隔符
     * @param script     SQL脚本
     * @return foreach脚本标签
     */
    public static String convertToForeachTag(final String collection, final String item, final String index,
                                             final String open, final String close, final String separator,
                                             final String script) {
        final StringBuilder builder = new StringBuilder("<foreach");
        if (Objects.isNotBlank(collection)) {
            builder.append(" collection=\"").append(collection).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(item)) {
            builder.append(" item=\"").append(item).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(index)) {
            builder.append(" index=\"").append(index).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(open)) {
            builder.append(" open=\"").append(open).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(close)) {
            builder.append(" close=\"").append(close).append(CHAR_QUOTE);
        }
        if (Objects.isNotBlank(separator)) {
            builder.append(" separator=\"").append(separator).append(CHAR_QUOTE);
        }
        return builder.append(GT).append(NEW_LINE).append(script).append(NEW_LINE).append("</foreach>").toString();
    }

    /**
     * 转换成条件参数
     * @param symbol       {@link Symbol}
     * @param slot         {@link Slot}
     * @param tableAlias   表别名
     * @param column       字段名
     * @param placeholders 占位符参数列表
     * @return 参数字符串
     */
    public static String convertToConditionArg(final Symbol symbol, final Slot slot, final String tableAlias,
                                               final String column, final String... placeholders) {
        return convertToConditionArg(symbol, slot, tableAlias, column, Objects.asList(placeholders));
    }

    /**
     * 转换成条件参数
     * @param symbol       {@link Symbol}
     * @param slot         {@link Slot}
     * @param tableAlias   表别名
     * @param column       字段名
     * @param placeholders 占位符参数集合
     * @return 参数字符串
     */
    public static String convertToConditionArg(final Symbol symbol, final Slot slot, final String tableAlias,
                                               final String column, final List<String> placeholders) {
        return convertToConditionArg(symbol, slot, tableAlias, column, null, false, null, null, placeholders);
    }

    /**
     * 转换成条件参数
     * @param symbol       {@link Symbol}
     * @param slot         {@link Slot}
     * @param tableAlias   表别名
     * @param column       {@link Column}
     * @param placeholders 占位符参数列表
     * @return 参数字符串
     */
    public static String convertToConditionArg(final Symbol symbol, final Slot slot, final String tableAlias,
                                               final Column column, final String... placeholders) {
        return convertToConditionArg(symbol, slot, tableAlias, column, Objects.asList(placeholders));
    }

    /**
     * 转换成条件参数
     * @param symbol       {@link Symbol}
     * @param slot         {@link Slot}
     * @param tableAlias   表别名
     * @param column       {@link Column}
     * @param placeholders 占位符参数集合
     * @return 参数字符串
     */
    public static String convertToConditionArg(final Symbol symbol, final Slot slot, final String tableAlias,
                                               final Column column, final List<String> placeholders) {
        return convertToConditionArg(symbol, slot, tableAlias, column.getColumn(), column.getTypeHandler(),
            column.isUseJavaType(), column.getJavaType(), column.getJdbcType(), placeholders);
    }

    /**
     * 转换成条件参数
     * @param symbol       {@link Symbol}
     * @param slot         {@link Slot}
     * @param tableAlias   表别名
     * @param column       字段
     * @param typeHandler  类型处理器
     * @param useJavaType  是否使用Java类型
     * @param javaType     Java类型
     * @param jdbcType     JDBC类型
     * @param placeholders 占位符参数列表
     * @return 参数字符串
     */
    public static String convertToConditionArg(final Symbol symbol, final Slot slot, final String tableAlias,
                                               final String column, final Class<? extends TypeHandler<?>> typeHandler,
                                               final boolean useJavaType, final Class<?> javaType,
                                               final JdbcType jdbcType, final String... placeholders) {
        return convertToConditionArg(symbol, slot, tableAlias, column, typeHandler, useJavaType,
            javaType, jdbcType, Objects.asList(placeholders));
    }

    /**
     * 转换成条件参数
     * @param symbol       {@link Symbol}
     * @param slot         {@link Slot}
     * @param tableAlias   表别名
     * @param column       字段
     * @param typeHandler  类型处理器
     * @param useJavaType  是否使用Java类型
     * @param javaType     Java类型
     * @param jdbcType     JDBC类型
     * @param placeholders 占位符参数集合
     * @return 参数字符串
     */
    public static String convertToConditionArg(final Symbol symbol, final Slot slot, final String tableAlias,
                                               final String column, final Class<? extends TypeHandler<?>> typeHandler,
                                               final boolean useJavaType, final Class<?> javaType,
                                               final JdbcType jdbcType, final List<String> placeholders) {
        final StringBuilder builder = new StringBuilder(100);
        if (Objects.nonNull(slot)) {
            builder.append(slot.getSegment()).append(SPACE);
        }
        if (Objects.isNotBlank(tableAlias)) {
            builder.append(tableAlias).append(DOT);
        }
        final Symbol it = symbol == null ? Symbol.EQ : symbol;
        builder.append(column).append(SPACE).append(symbol.getSegment()).append(SPACE);
        if (Symbol.filter(it) && Objects.isNotEmpty(placeholders)) {
            switch (it) {
                case EQ:
                case NE:
                case GT:
                case GE:
                case LT:
                case LE:
                case LIKE:
                case NOT_LIKE:
                    builder.append(safeJoining(placeholders.get(0), concatIntactTypeArg(typeHandler, useJavaType,
                        javaType, jdbcType)));
                    break;
                case IN:
                case NOT_IN:
                    builder.append(placeholders.stream().map(pd ->
                        safeJoining(pd, concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)))
                        .collect(Collectors.joining(COMMA_SPACE, BRACKET_OPEN, CLOSE_BRACKET)));
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    builder.append(placeholders.stream().limit(2).map(pd ->
                        safeJoining(pd, concatIntactTypeArg(typeHandler, useJavaType, javaType, jdbcType)))
                        .collect(Collectors.joining(SPACE_AND_SPACE)));
                    break;
                default:
                    return EMPTY;
            }
        }
        return builder.toString();
    }
}
