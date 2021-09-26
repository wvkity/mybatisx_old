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
import io.github.mybatisx.reflection.MetaObjects;
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
 * HSQLDB分页方言
 * @author wvkity
 * @created 2021-02-19
 * @since 1.0.0
 */
public class HsqldbDialect extends AbstractPageableDialect {

    @Override
    public Object handlePageableParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql bs,
                                          CacheKey cacheKey, Long rowStart, Long rowEnd, Long offset) {
        paramMap.put(DEF_PAGEABLE_OFFSET, offset);
        paramMap.put(DEF_PAGEABLE_START, rowStart);
        cacheKey.update(offset);
        cacheKey.update(rowStart);
        final List<ParameterMapping> parameterMappings = bs.getParameterMappings();
        if (parameterMappings != null) {
            final List<ParameterMapping> mappings = new ArrayList<>(parameterMappings);
            final Configuration cf = ms.getConfiguration();
            if (offset > 0) {
                mappings.add(new ParameterMapping.Builder(cf, DEF_PAGEABLE_OFFSET, Long.class).build());
            }
            if (rowStart >= 0) {
                mappings.add(new ParameterMapping.Builder(cf, DEF_PAGEABLE_START, Long.class).build());
            }
            final MetaObject metaObject = MetaObjects.forObject(bs);
            metaObject.setValue("parameterMappings", mappings);
        }
        return paramMap;
    }

    @Override
    public String makeCorrQueryListSQL(MappedStatement ms, CacheKey cacheKey, String originalSql,
                                       Long rowStart, Long rowEnd, Long offset) {
        final StringBuilder builder = new StringBuilder(originalSql.length() + 20);
        builder.append(originalSql);
        if (offset > 0) {
            builder.append(" LIMIT ?");
        }
        if (rowStart >= 0) {
            builder.append(" OFFSET ?");
        }
        return builder.toString();
    }
}
