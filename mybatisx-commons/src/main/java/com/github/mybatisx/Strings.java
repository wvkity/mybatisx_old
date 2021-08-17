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
package com.github.mybatisx;

import java.util.function.Predicate;

/**
 * 字符串工具
 * @author wvkity
 * @created 2020-10-17
 * @since 1.0.0
 */
public final class Strings {

    private Strings() {
    }

    private static final char CASE_MASK = 0X20;

    /**
     * 检查字符是否为小写字母
     * @param ch 待检查字符
     * @return boolean
     */
    public static boolean isLower(final char ch) {
        return 'a' <= ch && ch <= 'z';
    }

    /**
     * 判断字符是否为大写字母
     * @param ch 待检查字符
     * @return boolean
     */
    public static boolean isUpper(final char ch) {
        return 'A' <= ch && ch <= 'Z';
    }

    /**
     * 大写字母转小写字母
     * @param ch 待转换字符
     * @return 转换后的字符
     */
    public static char toLower(final char ch) {
        return isLower(ch) ? ch : (char) (ch ^ CASE_MASK);
    }

    /**
     * 小写字母转大写字母
     * @param ch 待转换字符
     * @return 转换后的字符
     */
    public static char toUpper(final char ch) {
        return isUpper(ch) ? ch : (char) (ch ^ CASE_MASK);
    }

    /**
     * 字符串中的大写字母转换成小写字母
     * @param source 待转换字符串
     * @return 转换后的字符串
     */
    public static String toLower(final String source) {
        if (Objects.isBlank(source)) {
            return source;
        }
        for (int i = 0, len = source.length(); i < len; i++) {
            if (isUpper(source.charAt(i))) {
                final char[] array = source.toCharArray();
                for (; i < len; i++) {
                    final char ch = array[i];
                    if (isUpper(ch)) {
                        array[i] = (char) (ch ^ CASE_MASK);
                    }
                }
                return String.valueOf(array);
            }
        }
        return source;
    }

    /**
     * 字符串中的小写字母转换成大写字母
     * @param source 待转换字符串
     * @return 转换后的字符串
     */
    public static String toUpper(final String source) {
        if (Objects.isBlank(source)) {
            return source;
        }
        for (int i = 0, len = source.length(); i < len; i++) {
            if (isLower(source.charAt(i))) {
                final char[] array = source.toCharArray();
                for (; i < len; i++) {
                    final char ch = array[i];
                    if (isLower(ch)) {
                        array[i] = (char) (ch ^ CASE_MASK);
                    }
                }
                return String.valueOf(array);
            }
        }
        return source;
    }

    /**
     * 字符串第一个字母转小写
     * @param source 待转换字符串
     * @return 新的字符串
     */
    public static String firstCharToLower(final String source) {
        return Objects.isBlank(source) ? source : toLower(source.charAt(0)) + source.substring(1);
    }

    /**
     * 字符串第一个字母转大写
     * @param source 待转换字符串
     * @return 新的字符串
     */
    public static String firstCharToUpper(final String source) {
        return Objects.isBlank(source) ? source : toUpper(source.charAt(0)) + source.substring(1);
    }

    /**
     * 仅仅第一个字母转大写其余转小写
     * @param source 待转换字符串
     * @return 新的字符串
     */
    public static String firstCharOnlyToUpper(final String source) {
        return Objects.isBlank(source) ? source : toUpper(source.charAt(0)) + toLower(source.substring(1));
    }

    /**
     * 如果s1不为空值则返回s1否则返回s2
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 字符串
     */
    public static String nvl(final String s1, final String s2) {
        return nvl(Objects::isNotBlank, s1, s2);
    }

    /**
     * 如果表达式结果为真则返回s1否则返回s2
     * @param expression 表达式结果
     * @param s1         字符串1
     * @param s2         字符串2
     * @return 字符串
     */
    public static String nvl(final Predicate<String> expression, final String s1, final String s2) {
        return expression.test(s1) ? s1 : s2;
    }

    /**
     * 如果表达式结果为真则返回s1否则返回s2
     * @param expression 表达式结果
     * @param s1         字符串1
     * @param s2         字符串2
     * @return 字符串
     */
    public static String nvl(final boolean expression, final String s1, final String s2) {
        return expression ? s1 : s2;
    }

    /**
     * 下划线字符串转小驼峰
     * @param value 待转换字符串
     * @return 新的字符串
     */
    public static String underscoreToLowerCamelCase(final String value) {
        if (Objects.isNotBlank(value)) {
            String[] ss = value.split("_");
            if (ss.length == 1) {
                return value;
            }
            final StringBuilder builder = new StringBuilder(value.length());
            builder.append(ss[0]);
            for (int i = 1; i < ss.length; i++) {
                builder.append(firstCharOnlyToUpper(ss[i]));
            }
            return firstCharToLower(builder.toString());
        }
        return null;
    }
}
