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
package io.github.mybatisx.jsql.parser;

import io.github.mybatisx.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 占位符参数解析器
 * @author wvkity
 * @created 2021-05-06
 * @since 1.0.0
 */
public class ParameterParser {
    public static final Pattern DEF_PATTERN_MATCHER = Pattern.compile(".*#\\{((?!#\\{).)*}.*");
    public static final Pattern DEF_PATTERN_PM_RESTORE = Pattern.compile("((?<!\\\\)(\\?))");
    public static final String DEF_PATTERN_PM_STR = "(#\\{[^(#{)]+})";
    public static final Pattern DEF_PATTERN_PM = Pattern.compile(DEF_PATTERN_PM_STR);
    /**
     * 是否已替换
     */
    private final AtomicBoolean replaced = new AtomicBoolean(false);
    /**
     * 原SQL语句
     */
    private final String originalSql;
    /**
     * 参数集合
     */
    private final Map<Integer, String> params;
    /**
     * 替换后的SQL语句
     */
    private String replaceSql;

    public ParameterParser(String originalSql) {
        this.originalSql = originalSql;
        this.params = new HashMap<>();
    }

    /**
     * 替换原SQL语句
     * @return {@link ParameterParser}
     */
    public ParameterParser replace() {
        final StringBuffer buffer = new StringBuffer(this.originalSql.length());
        if (DEF_PATTERN_MATCHER.matcher(this.originalSql).matches()) {
            int normal = -1;
            Integer index = normal;
            final Matcher matcher = DEF_PATTERN_PM.matcher(this.originalSql);
            while (matcher.find()) {
                final String placeholder = matcher.group();
                matcher.appendReplacement(buffer, "?");
                index++;
                this.params.put(index, placeholder);
            }
            matcher.appendTail(buffer);
            this.replaceSql = buffer.toString();
            this.replaced.compareAndSet(false, index > normal);
        }
        return this;
    }

    /**
     * 还原SQL语句
     * @param replacement SQL语句
     * @return SQL语句
     */
    public String restore(final String replacement) {
        if (this.hasParameter() && Objects.isNotBlank(replacement)) {
            final StringBuffer buffer = new StringBuffer();
            final Matcher matcher = DEF_PATTERN_PM_RESTORE.matcher(replacement);
            Integer index = 0;
            while (matcher.find()) {
                final String placeholder = params.get(index);
                matcher.appendReplacement(buffer, placeholder);
                index++;
            }
            matcher.appendTail(buffer);
            return buffer.toString();
        }
        return replacement;
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public String getReplaceSql() {
        return replaceSql;
    }

    /**
     * SQL语句是否已替换成功
     * @return boolean
     */
    public boolean isReplaced() {
        return this.replaced.get();
    }

    /**
     * 是否存在占位符参数
     * @return boolean
     */
    public boolean hasParameter() {
        return !this.params.isEmpty();
    }
}
