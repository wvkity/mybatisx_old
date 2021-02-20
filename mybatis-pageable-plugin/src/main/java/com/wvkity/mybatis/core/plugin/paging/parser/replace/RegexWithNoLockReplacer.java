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
package com.wvkity.mybatis.core.plugin.paging.parser.replace;

/**
 * 正则表达式替换/还原
 * @author wvkity
 * @created 2021-02-19
 * @since 1.0.0
 */
public class RegexWithNoLockReplacer implements Replacer {

    @Override
    public String replace(String originalSql) {
        return originalSql.replaceAll("((?i)\\s*(\\w+)\\s*with\\s*\\(\\s*nolock\\s*\\))", " $2_PAGEABLE_WITH_NO_LOCK");
    }

    @Override
    public String restore(String replacementSql) {
        return replacementSql.replaceAll("\\s*(\\w*?)_PAGEABLE_WITH_NO_LOCK", " $1 WITH(NOLOCK)");
    }
}
