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
package io.github.mybatisx.plugin.paging.dialect.support;

import io.github.mybatisx.plugin.paging.dialect.AbstractPageableDialect;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * DB2分页方言
 * @author wvkity
 * @created 2021-02-19
 * @since 1.0.0
 */
public class Db2Dialect extends AbstractPageableDialect {

    @Override
    public Object handlePageableParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql bs,
                                          CacheKey cacheKey, Long rowStart, Long rowEnd, Long offset) {
        paramMap.put(DEF_PAGEABLE_START, rowStart + 1);
        paramMap.put(DEF_PAGEABLE_OFFSET, rowEnd);
        cacheKey.update(rowStart + 1);
        cacheKey.update(rowEnd);
        this.handleParameter(ms, bs, rowStart, rowEnd);
        return paramMap;
    }

    @Override
    public String makeCorrQueryListSQL(MappedStatement ms, CacheKey cacheKey, String originalSql,
                                       Long rowStart, Long rowEnd, Long offset) {
        return "SELECT TMP_O_TAB_PAGE.* FROM (" +
            "SELECT TMP_I_TAB_PAGE.*, ROWNUMBER() OVER() AS PAGE_R_ID FROM ("+originalSql+") TMP_I_TAB_PAGE" +
            ") TMP_O_TAB_PAGE WHERE TMP_O_TAB_PAGE.PAGE_R_ID BETWEEN ? AND ?";
    }

}
