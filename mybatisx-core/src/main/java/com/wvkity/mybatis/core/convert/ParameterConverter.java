package com.wvkity.mybatis.core.convert;

import com.wvkity.mybatis.basic.utils.Objects;

import java.util.List;
import java.util.Map;

/**
 * 参数转换器
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public interface ParameterConverter extends Converter<Object, String> {

    String DEF_PARAMETER_PLACEHOLDER_ZERO = "{0}";

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数
     */
    default String convert(final Object... args) {
        return this.convert(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数
     */
    String convert(final String template, final Object... args);

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数列表
     */
    default List<String> converts(final Object... args) {
        return this.converts(Objects.asList(args));
    }

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数列表
     */
    default List<String> converts(final Iterable<?> args) {
        return this.converts(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数列表
     */
    List<String> converts(final String template, final Iterable<?> args);

    /**
     * 参数值转占位符参数
     * @param args 参数列表
     * @return 占位符参数列表
     */
    default Map<String, String> converts(final Map<String, ?> args) {
        return this.converts(DEF_PARAMETER_PLACEHOLDER_ZERO, args);
    }

    /**
     * 参数值转占位符参数
     * @param template 模板
     * @param args     参数列表
     * @return 占位符参数列表
     */
    Map<String, String> converts(final String template, final Map<String, ?> args);

}