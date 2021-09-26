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
package io.github.mybatisx;

import java.util.regex.Pattern;

/**
 * 正则匹配器
 * @author wvkity
 * @created 2021-07-10
 * @since 1.0.0
 */
public final class RegexMatcher {
    private RegexMatcher() {
    }

    /**
     * AND、OR运算符正则字符串
     */
    public static final String REGEX_AND_OR_STR = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    /**
     * AND、OR运算符正则
     */
    public static final Pattern PATTERN_AND_OR = Pattern.compile(REGEX_AND_OR_STR, Pattern.CASE_INSENSITIVE);

    /**
     * 检查字符串是否以AND或者OR关键字开头
     * @param arg 待检查字符串
     * @return boolean
     */
    public static boolean startWithAndOr(final String arg) {
        return Objects.isNotBlank(arg) && RegexMatcher.PATTERN_AND_OR.matcher(arg).matches();
    }

    /**
     * 以AND或OR开头的字符串移除其AND或OR字符串
     * @param arg 待检查移除字符串
     * @return 新的字符串
     */
    public static String startWithAndOrRemove(final String arg) {
        if (RegexMatcher.startWithAndOr(arg)) {
            return arg.replaceFirst(RegexMatcher.REGEX_AND_OR_STR, "$2");
        }
        return arg;
    }
}
