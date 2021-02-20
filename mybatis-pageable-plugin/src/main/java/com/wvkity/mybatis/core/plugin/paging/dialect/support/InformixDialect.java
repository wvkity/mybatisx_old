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
import com.wvkity.mybatis.reflection.MetaObjects;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Informix分页方言
 * @author wvkity
 * @created 2021-02-19
 * @since 1.0.0
 */
public class InformixDialect extends AbstractPageableDialect {
    @Override
    public Object handlePageableParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql bs,
                                          CacheKey cacheKey, Long rowStart, Long rowEnd, Long offset) {
        paramMap.put(DEF_PAGEABLE_START, rowStart);
        paramMap.put(DEF_PAGEABLE_OFFSET, offset);
        cacheKey.update(rowStart);
        cacheKey.update(offset);
        final List<ParameterMapping> parameterMappings = bs.getParameterMappings();
        if (parameterMappings != null) {
            final List<ParameterMapping> mappings = new ArrayList<>(parameterMappings);
            final Configuration cf = ms.getConfiguration();
            if (rowStart >= 0) {
                mappings.add(new ParameterMapping.Builder(cf, DEF_PAGEABLE_START, Long.class).build());
            }
            if (offset > 0) {
                mappings.add(new ParameterMapping.Builder(cf, DEF_PAGEABLE_OFFSET, Long.class).build());
            }
            final MetaObject metaObject = MetaObjects.forObject(bs);
            metaObject.setValue("parameterMappings", mappings);
        }
        return paramMap;
    }

    @Override
    public String makeCorrQueryListSQL(MappedStatement ms, CacheKey cacheKey, String originalSql,
                                       Long rowStart, Long rowEnd, Long offset) {
        final StringBuilder builder = new StringBuilder(originalSql.length() + 30);
        builder.append("SELECT");
        if (rowStart >= 0) {
            builder.append(" SKIP ?");
        }
        if (offset > 0) {
            builder.append(" FIRST ?");
        }
        builder.append(" * FROM (").append(originalSql).append(") TMP_TAB_PAGE");
        return builder.toString();
    }
}
