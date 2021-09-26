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
package io.github.mybatisx;

import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.reflect.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
     * 数字占位符正则表达式字符串
     */
    private static final String PLACEHOLDER_REGEX_DIGIT_STR = "((?<!\\\\)\\?(\\d+))";
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
        return parse(template, PlaceholderPattern.SINGLE, arg);
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
    public static String format(final String template, final Iterable<?> args) {
        return parse(template, PlaceholderPattern.MULTIPLE, args);
    }

    /**
     * 格式占位符模板
     * @param template 模板
     * @param args     参数集合
     * @return 新字符串
     */
    public static String format(final String template, final Map<String, ?> args) {
        return parse(template, PlaceholderPattern.MAP, args);
    }

    /**
     * 解析占位符模板
     * @param template 模板
     * @param pattern    匹配模式
     * @param arg      参数
     * @return 新字符串
     */
    public static String parse(final String template, final PlaceholderPattern pattern, final Object arg) {
        if (Objects.isNull(arg) || Objects.isBlank(template)) {
            return template;
        }
        final boolean isMatchesOfDigit = PLACEHOLDER_MATCHER_DIGIT.matcher(template).matches();
        final boolean isMatchesOfChar = PLACEHOLDER_MATCHER_CHAR.matcher(template).matches();
        if (isMatchesOfDigit || isMatchesOfChar) {
            final boolean isMap = pattern == PlaceholderPattern.MAP;
            final List<Object> args = new ArrayList<>();
            if (isMap) {
                args.addAll(((Map<?, ?>) arg).values());
            } else if (Objects.isArray(arg)) {
                args.addAll(Objects.asList((Object[]) arg));
            } else if (arg instanceof Iterable) {
                args.addAll(arg instanceof Collection ? ((Collection<?>) arg) : toList((Iterable<?>) arg));
            } else {
                args.add(arg);
            }
            if (Objects.isEmpty(args)) {
                return template;
            }
            final int size = args.size() - 1;
            String target = template;
            final Matcher matcher;
            if (!isMap || isMatchesOfDigit) {
                // 列表参数且只有一个参数占位符时
                if (pattern == PlaceholderPattern.MULTIPLE && isOnlyOnce(target)) {
                    final String value = Reflections.isPureType(args) ? toString(args) : toString(args.get(0));
                    target = target.replaceAll(PLACEHOLDER_REGEX_DIGIT_STR, value);
                } else {
                    matcher = PLACEHOLDER_REGEX_DIGIT.matcher(target);
                    while (matcher.find()) {
                        final int i = toInt(matcher.group(2));
                        final Object value = (i < 0 || i > size) ? null : args.get(i);
                        target = replaceFirst(matcher, target, value);
                    }
                }
            } else {
                matcher = PLACEHOLDER_REGEX_CHAR.matcher(target);
                final Map<?, ?> map = (Map<?, ?>) arg;
                while (matcher.find()) {
                    final String key = matcher.group(2);
                    final Object value = map.get(key);
                    target = replaceFirst(matcher, target, value);
                }
            }
            return target;
        }
        return template;
    }

    private static List<?> toList(final Iterable<?> arg) {
        return StreamSupport.stream(arg.spliterator(), false).collect(Collectors.toList());
    }

    /**
     * 替换字符串
     * @param matcher {@link Matcher}
     * @param target  待替换字符串
     * @param arg     替换值
     * @return 替换后的字符串
     */
    private static String replaceFirst(final Matcher matcher, final String target, final Object arg) {
        final String group = matcher.group();
        final String regex;
        if (group.startsWith(Constants.QUESTION_MARK)) {
            regex = String.format("\\%s", group);
        } else {
            regex = group;
        }
        return target.replaceFirst(regex, toString(arg));
    }

    /**
     * 参数转成字符串
     * @param arg 参数
     * @return 字符串值
     */
    private static String toString(final Object arg) {
        if (Objects.nonNull(arg)) {
            final Class<?> clazz = arg.getClass();
            if (clazz.isArray()) {
                return Arrays.stream((Object[]) arg).map(it -> it == null ? Constants.DEF_STR_NULL : it.toString())
                    .collect(Collectors.joining(Constants.COMMA_SPACE));
            } else if (Iterable.class.isAssignableFrom(clazz)) {
                return StreamSupport.stream(((Iterable<?>) arg).spliterator(), false).map(it -> it == null ?
                    Constants.DEF_STR_NULL : it.toString()).collect(Collectors.joining(Constants.COMMA_SPACE));
            } else if (Map.class.isAssignableFrom(clazz)) {
                return ((Map<?, ?>) arg).values().stream().map(it -> it == null ?
                    Constants.DEF_STR_NULL : it.toString()).collect(Collectors.joining(Constants.COMMA_SPACE));
            } else {
                return arg.toString();
            }
        }
        return Constants.DEF_STR_NULL;
    }

    /**
     * 检查数字参数占位符是否仅仅出现一次
     * @param target 字符串
     * @return boolean
     */
    public static boolean isOnlyOnce(final String target) {
        String ignore = target;
        int count = 0;
        final Matcher matcher = PLACEHOLDER_REGEX_DIGIT.matcher(ignore);
        String history = "";
        while (matcher.find()) {
            final String replacement = String.format("\\%s", matcher.group());
            ignore = target.replaceFirst(replacement, Constants.QUESTION_MARK);
            if (!Objects.equals(history, replacement)) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查数字参数占位符是否仅仅出现一次
     * @param target 字符串
     * @return boolean
     */
    public static PlaceholderMatcher matcher(final String target) {
        String ignore = target;
        int count = 0;
        final Matcher matcher = PLACEHOLDER_REGEX_DIGIT.matcher(ignore);
        String history = "";
        final PlaceholderMatcher pm = new PlaceholderMatcher();
        while (matcher.find()) {
            final String replacement = String.format("\\%s", matcher.group());
            ignore = target.replaceFirst(replacement, Constants.QUESTION_MARK);
            if (pm.compare(replacement)) {
                break;
            }
        }
        return pm;
    }

    /**
     * 转成整数
     * @param target 字符串值
     * @return 整数
     */
    private static int toInt(final String target) {
        return Objects.isBlank(target) ? -1 : PATTERN_INTEGER.matcher(target).matches() ? Integer.parseInt(target) : -1;
    }

    public static class PlaceholderMatcher {
        /**
         * 占位符出现次数
         */
        private final AtomicInteger placeholderCounter = new AtomicInteger(0);
        /**
         * 同一个占位符出现的次数
         */
        private final AtomicInteger counter = new AtomicInteger(1);
        /**
         * 历史占位符
         */
        private final AtomicReference<String> history = new AtomicReference<>("");

        public PlaceholderMatcher() {
        }

        public boolean compare(final String replacement) {
            final String source = history.get();
            if (history.compareAndSet(source, replacement)) {
                return this.pcIncrement() > 1;
            } else {
                this.counter.incrementAndGet();
            }
            return false;
        }

        private int pcIncrement() {
            return this.placeholderCounter.incrementAndGet();
        }

        /**
         * 是否仅仅只有一个占位符
         * @return boolean
         */
        public boolean isSingle() {
            return this.placeholderCounter.get() != 1;
        }

        /**
         * 相同占位符是否仅仅只出现一次
         * @return boolean
         */
        public boolean isOnlyOnce() {
            return this.counter.get() == 1;
        }
    }

}
