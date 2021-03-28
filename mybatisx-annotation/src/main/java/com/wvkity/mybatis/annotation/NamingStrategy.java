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
package com.wvkity.mybatis.annotation;

import java.util.function.Predicate;

/**
 * 命名策略枚举
 * @author wvkity
 * @created 2020-10-01
 * @since 1.0.0
 */
public enum NamingStrategy {

    /**
     * 原值
     */
    NORMAL(__ -> false, ""),
    /**
     * 小写
     */
    LOWER(NamingStrategy::isUpper, "") {
        @Override
        String normalized(String word) {
            return NamingStrategy.toLower(word);
        }
    },
    /**
     * 大写
     */
    UPPER(NamingStrategy::isLower, "") {
        @Override
        String normalized(String word) {
            return NamingStrategy.toUpper(word);
        }
    },
    /**
     * 小写驼峰
     */
    LOWER_CAMEL(NamingStrategy::isUpper, "") {
        @Override
        String normalized(String word) {
            return NamingStrategy.firstCharOnlyToUpper(word);
        }

        @Override
        String normalizedOfFirst(String word) {
            return NamingStrategy.toLower(word);
        }
    },
    /**
     * 大写驼峰
     */
    UPPER_CAMEL(NamingStrategy::isUpper, "") {
        @Override
        String normalized(String word) {
            return NamingStrategy.firstCharOnlyToUpper(word);
        }
    },
    /**
     * 下划线小写
     */
    LOWER_UNDERSCORE(c -> '_' == c, "_") {
        @Override
        String normalized(String word) {
            return NamingStrategy.toLower(word);
        }

        @Override
        String convert(NamingStrategy format, String source) {
            if (format == UPPER_UNDERSCORE) {
                return NamingStrategy.toUpper(source);
            }
            return super.convert(format, source);
        }
    },
    /**
     * 下划线大写
     */
    UPPER_UNDERSCORE(c -> '_' == c, "_") {
        @Override
        String normalized(String word) {
            return NamingStrategy.toUpper(word);
        }

        @Override
        String convert(NamingStrategy format, String source) {
            if (format == LOWER_UNDERSCORE) {
                return NamingStrategy.toLower(source);
            }
            return super.convert(format, source);
        }
    };

    private final Predicate<Character> matcher;
    private final String separator;

    NamingStrategy(Predicate<Character> matcher, String separator) {
        this.matcher = matcher;
        this.separator = separator;
    }

    /**
     * 转换字符串
     * @param format {@link NamingStrategy}
     * @param source 待转换字符串
     * @return 转换后的字符串
     */
    String convert(final NamingStrategy format, final String source) {
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        final int len = source.length();
        while (true) {
            ++j;
            if ((j = matches(this.matcher, source, j)) == -1) {
                return i == 0 ? format.normalizedOfFirst(source) :
                    out.append(format.normalized(source.substring(i))).toString();
            }
            if (i == 0) {
                out = new StringBuilder(len + 4 * this.separator.length());
                out.append(format.normalizedOfFirst(source.substring(i, j)));
            } else {
                out.append(format.normalized(source.substring(i, j)));
            }
            out.append(format.separator);
            i = j + this.separator.length();
        }
    }

    /**
     * 将字符串格式化
     * @param format {@link NamingStrategy}
     * @param source 待格式化字符串
     * @return 新的字符串
     */
    public String to(final NamingStrategy format, final String source) {
        return (NamingStrategy.isBlank(source) || format == null || this == format || format == NORMAL) ?
            source : convert(format, source);
    }

    /**
     * 检查字符是否匹配
     * @param matcher {@link Predicate}
     * @param chars   字符串
     * @param index   下标
     * @return 下标
     */
    int matches(final Predicate<Character> matcher, final CharSequence chars, final int index) {
        final int len = chars.length();
        positionIndex(index, len);
        for (int i = index; i < len; i++) {
            if (matcher.test(chars.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    void positionIndex(int index, int size) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index (" + index + ") must not be negative");
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else if (index > size) {
            throw new IndexOutOfBoundsException("index (" + index + ") must not be greater than size (" + size + ")");
        }
    }

    String normalized(final String word) {
        return word;
    }

    String normalizedOfFirst(final String word) {
        return normalized(word);
    }

    // region static methods
    ///////// static methods ////////
    private static final String UNDERSCORE = "_";
    private static final char CASE_MASK = 0X20;

    /**
     * 检查字符串是否为空白值
     * @param value 待检查字符串值
     * @return boolean
     */
    static boolean isBlank(final String value) {
        if (value != null && value.trim().length() > 0) {
            final int size = value.length();
            for (int i = 0; i < size; i++) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查字符是否为小写字母
     * @param ch 待检查字符
     * @return boolean
     */
    static boolean isLower(final char ch) {
        return 'a' <= ch && ch <= 'z';
    }

    /**
     * 判断字符是否为大写字母
     * @param ch 待检查字符
     * @return boolean
     */
    static boolean isUpper(final char ch) {
        return 'A' <= ch && ch <= 'Z';
    }

    /**
     * 大写字母转小写字母
     * @param ch 待转换字符
     * @return 转换后的字符
     */
    static char toLower(final char ch) {
        return isLower(ch) ? ch : (char) (ch ^ CASE_MASK);
    }

    /**
     * 小写字母转大写字母
     * @param ch 待转换字符
     * @return 转换后的字符
     */
    static char toUpper(final char ch) {
        return isUpper(ch) ? ch : (char) (ch ^ CASE_MASK);
    }

    /**
     * 字符串中的大写字母转换成小写字母
     * @param source 待转换字符串
     * @return 转换后的字符串
     */
    static String toLower(final String source) {
        if (NamingStrategy.isBlank(source)) {
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
    static String toUpper(final String source) {
        if (NamingStrategy.isBlank(source)) {
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
    static String firstCharToLower(final String source) {
        return NamingStrategy.isBlank(source) ? source : toLower(source.charAt(0)) + source.substring(1);
    }

    /**
     * 字符串第一个字母转大写
     * @param source 待转换字符串
     * @return 新的字符串
     */
    static String firstCharToUpper(final String source) {
        return NamingStrategy.isBlank(source) ? source : toUpper(source.charAt(0)) + source.substring(1);
    }

    /**
     * 仅仅第一个字母转大写其余转小写
     * @param source 待转换字符串
     * @return 新的字符串
     */
    static String firstCharOnlyToUpper(final String source) {
        return NamingStrategy.isBlank(source) ? source : toUpper(source.charAt(0)) + toLower(source.substring(1));
    }
    // endregion
}
