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
package com.github.mybatisx.plugin.paging.parser.replace;

/**
 * 替换/还原SQL
 * @author wvkity
 * @created 2021-02-19
 * @since 1.0.0
 */
public interface Replacer {

    String WITH_NOT_LOCK = ", PAGEABLE_WITH_NOT_LOCK";

    /**
     * 替换原SQL
     * @param originalSql 原SQL
     * @return 替换后的SQL
     */
    String replace(String originalSql);

    /**
     * 还原替换的SQL
     * @param replacementSql 替换的SQL
     * @return 原SQL
     */
    String restore(String replacementSql);

}
