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
package com.wvkity.mybatis.core.utils;

import com.wvkity.mybatis.basic.immutable.ImmutableMap;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.condition.expression.TemplateMatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 占位符工具
 * <pre>{@code
 *     // For examples:
 *     // No1:
 *     final String template1 = "select * from tab where user_name = ?0 and pwd = ?1 and state = \\?2";
 *     log.info("格式化字符串: {}", Placeholders.format(template1, "admin", "123456", 1));
 *     // output
 *     // select * from tab where user_name = admin and pwd = 123456 and state = \?2
 *
 *     // No2:
 *     final String template2 = "select * from tab where user_name = :0 and pwd = :1 and state = \\:2";
 *     log.info("格式化字符串: {}", Placeholders.format(template2, "root", "654321", 1));
 *     // output
 *     // select * from tab where user_name = root and pwd = 654321 and state = \:2
 *
 *     // No3:
 *     final String template3 = "select * from tab where user_name = :0 and pwd = :1 and state = \\:2";
 *     final Map<String, Object> args1 = new LinkedHashMap();
 *     args1.put("0", "root");
 *     args1.put("1", "123456");
 *     args1.put("2", 2);
 *     log.info("格式化字符串: {}", Placeholders.format(template3, args1));
 *     // output
 *     // select * from tab where user_name = root and pwd = 123456 and state = \:2
 *
 *     // No4:
 *     final String template4 = "select * from tab where user_name = :userName and pwd = :password and state = \\:state";
 *     final Map<String, Object> args2 = new HashMap();
 *     args2.put("userName", "admin");
 *     args2.put("password", "123456");
 *     args2.put("state", 2);
 *     log.info("格式化字符串: {}", Placeholders.format(template4, args2));
 *     // output
 *     // select * from tab where user_name = admin and pwd = 123456 and state = \:state
 * }</pre>
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public final class Placeholders {

    private Placeholders() {
    }

    /**
     * 整数正则表达式
     */
    private static final Pattern PATTERN_INTEGER = Pattern.compile("(0|[1-9]\\d*)");
    /**
     * 数字占位符
     */
    private static final Pattern PLACEHOLDER_MATCHER_DIGIT = Pattern.compile(".*((?<!\\\\)\\?(\\d+)).*");
    /**
     * 数字占位符
     */
    private static final Pattern PLACEHOLDER_REGEX_DIGIT = Pattern.compile("((?<!\\\\)\\?(\\d+))");
    /**
     * 字符占位符
     */
    private static final Pattern PLACEHOLDER_MATCHER_CHAR = Pattern.compile(".*((?<!\\\\):(\\w+)).*");
    /**
     * 字符占位符
     */
    private static final Pattern PLACEHOLDER_REGEX_CHAR = Pattern.compile("((?<!\\\\):(\\w+))");

    /**
     * 格式化占位符模板
     * @param template 模板
     * @param arg      参数
     * @return 新字符串
     */
    public static String format(final String template, final Object arg) {
        return parse(template, TemplateMatch.SINGLE, arg);
    }

    /**
     * 格式占位符模板
     * @param template 模板
     * @param args     参数列表
     * @return 新字符串
     */
    public static String format(final String template, final Object... args) {
        return format(template, Objects.asList(args));
    }

    /**
     * 格式占位符模板
     * @param template 模板
     * @param args     参数集合
     * @return 新字符串
     */
    public static String format(final String template, final List<Object> args) {
        return parse(template, TemplateMatch.MULTIPLE, args);
    }

    /**
     * 格式占位符模板
     * @param template 模板
     * @param args     参数集合
     * @return 新字符串
     */
    public static String format(final String template, final Map<String, Object> args) {
        return parse(template, TemplateMatch.MAP, args);
    }

    /**
     * 解析占位符模板
     * @param template 模板
     * @param match    匹配模式
     * @param arg      参数
     * @return 新字符串
     */
    public static String parse(final String template, final TemplateMatch match, final Object arg) {
        if (Objects.isNull(arg) || Objects.isBlank(template)) {
            return template;
        }
        final boolean isMatchesOfDigit = PLACEHOLDER_MATCHER_DIGIT.matcher(template).matches();
        final boolean isMatchesOfChar = PLACEHOLDER_MATCHER_CHAR.matcher(template).matches();
        if (isMatchesOfDigit || isMatchesOfChar) {
            final boolean isMap = match == TemplateMatch.MAP;
            final List<Object> args = new ArrayList<>();
            if (isMap) {
                args.addAll(((Map<?, ?>) arg).values());
            } else if (Objects.isArray(arg)) {
                args.addAll(Objects.asList((Object[]) arg));
            } else if (arg instanceof Collection) {
                args.addAll((Collection<?>) arg);
            } else {
                args.add(arg);
            }
            if (Objects.isEmpty(args)) {
                return template;
            }
            final int size = args.size() - 1;
            String target = template;
            final Matcher matcher;
            if (!isMap) {
                matcher = PLACEHOLDER_REGEX_DIGIT.matcher(target);
                while (matcher.find()) {
                    final int i = toInt(matcher.group(2));
                    final Object value = (i < 0 || i > size) ? null : args.get(i);
                    target = replaceFirst(matcher, target, value);
                }
            } else {
                matcher = PLACEHOLDER_REGEX_CHAR.matcher(target);
                final Map<?, ?> map = isMap ? (Map<?, ?>) arg : ImmutableMap.of();
                while (matcher.find()) {
                    final String key = matcher.group(2);
                    final Object value;
                    if (isMap) {
                        value = map.get(key);
                    } else {
                        final int i = toInt(key);
                        value = (i < 0 || i > size) ? null : args.get(i);
                    }
                    target = replaceFirst(matcher, target, value);
                }
            }
            return target;
        }
        return template;
    }

    private static String replaceFirst(final Matcher matcher, final String target, final Object arg) {
        final String group = matcher.group();
        final String regex;
        if (group.startsWith("?")) {
            regex = String.format("\\%s", group);
        } else {
            regex = group;
        }
        return target.replaceFirst(regex, String.valueOf(arg));
    }

    private static int toInt(final String target) {
        return Objects.isBlank(target) ? -1 : PATTERN_INTEGER.matcher(target).matches() ? Integer.parseInt(target) : -1;
    }

}
