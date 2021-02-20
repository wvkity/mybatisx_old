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
package com.wvkity.mybatis.core.plugin.paging.dialect.support;

import com.wvkity.mybatis.core.plugin.paging.dialect.AbstractPageableDialect;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * SQLSERVER({@code version >= 2012})分页方言
 * @author wvkity
 * @created 2021-02-20
 * @since 1.0.0
 */
public class SqlServer2012LaterDialect extends AbstractPageableDialect {

    @Override
    public Object handlePageableParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql bs,
                                          CacheKey cacheKey, Long rowStart, Long rowEnd, Long offset) {
        paramMap.put(DEF_PAGEABLE_START, rowStart);
        paramMap.put(DEF_PAGEABLE_OFFSET, offset);
        cacheKey.update(rowStart);
        cacheKey.update(offset);
        this.handleParameter(ms, bs, rowStart, offset);
        return paramMap;
    }

    @Override
    public String makeCorrQueryListSQL(MappedStatement ms, CacheKey cacheKey, String originalSql,
                                       Long rowStart, Long rowEnd, Long offset) {
        cacheKey.update(offset);
        return originalSql + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    }

}
