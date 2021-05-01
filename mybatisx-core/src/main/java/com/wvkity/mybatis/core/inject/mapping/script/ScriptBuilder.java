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
package com.wvkity.mybatis.core.inject.mapping.script;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * 脚本构建器
 * @author wvkity
 * @created 2020-10-22
 * @since 1.0.0
 */
public class ScriptBuilder {

    private static final Logger log = LoggerFactory.getLogger(StringBuilder.class);

    /**
     * 构建SQL脚本
     * @param sql SQL语句
     * @return SQL脚本
     */
    public static String build(final String sql) {
        if (Objects.isNotBlank(sql)) {
            if (sql.toLowerCase(Locale.ENGLISH).startsWith(Constants.SCRIPT_OPEN)) {
                return sql;
            }
            return "<script>" + sql + "</script>";
        }
        return sql;
    }
}
