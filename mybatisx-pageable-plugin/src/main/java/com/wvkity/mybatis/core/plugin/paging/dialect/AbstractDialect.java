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
package com.wvkity.mybatis.core.plugin.paging.dialect;

import com.wvkity.mybatis.core.jsql.parser.SqlParser;
import com.wvkity.mybatis.core.plugin.paging.RangePageable;
import com.wvkity.mybatis.core.plugin.paging.config.RangePageableThreadLocalCache;
import com.wvkity.mybatis.core.plugin.paging.config.StandardPageableThreadLocalCache;
import com.wvkity.mybatis.reflection.MetaObjects;
import com.wvkity.paging.DataPageable;
import com.wvkity.paging.Pageable;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 抽象数据库分页方言
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public abstract class AbstractDialect implements Dialect {

    /**
     * 配置项
     */
    protected Properties properties;
    /**
     * SQL转换器
     */
    protected SqlParser sqlParser = new SqlParser();

    protected boolean isRange() {
        final RangePageable rp = RangePageableThreadLocalCache.getPageable();
        final Pageable sp = StandardPageableThreadLocalCache.getPageable();
        return sp == null && rp != null && rp.isApply();
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter, RowBounds rb) {
        return false;
    }

    @Override
    public String makeQueryRecordSQL(MappedStatement ms, BoundSql bs, Object parameter,
                                     RowBounds rb, CacheKey cacheKey) {
        return this.sqlParser.smartCountParse(bs.getSql());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object handleParameter(MappedStatement ms, BoundSql bs, Object parameter, CacheKey cacheKey) {
        final Map<String, Object> paramMap;
        if (parameter == null) {
            paramMap = new HashMap<>();
        } else if (parameter instanceof Map) {
            paramMap = new HashMap<>((Map<? extends String, ?>) parameter);
        } else {
            paramMap = new HashMap<>();
            final boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry()
                .hasTypeHandler(parameter.getClass());
            if (!hasTypeHandler) {
                final MetaObject metaObject = MetaObjects.forObject(parameter);
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            final List<ParameterMapping> parameterMappings = bs.getParameterMappings();
            if (parameterMappings != null && !parameterMappings.isEmpty()) {
                for (ParameterMapping mapping : parameterMappings) {
                    final String property = mapping.getProperty();
                    if (!DEF_PAGEABLE_START.equals(property) && !DEF_PAGEABLE_OFFSET.equals(property)
                        && paramMap.get(property) != null) {
                        if (hasTypeHandler || mapping.getJavaType().equals(parameter.getClass())) {
                            paramMap.put(property, parameter);
                            break;
                        }
                    }
                }
            }
        }
        if (this.isRange()) {
            final RangePageable pageable = RangePageableThreadLocalCache.getPageable();
            return this.handlePageableParameter(ms, paramMap, bs, cacheKey, pageable.getStart(),
                pageable.getEnd(), pageable.getOffset());
        } else {
            final Pageable pageable = StandardPageableThreadLocalCache.getPageable();
            return this.handlePageableParameter(ms, paramMap, bs, cacheKey, pageable.offset(),
                pageable.getSize() + pageable.offset(), pageable.getSize());
        }
    }

    /**
     * 参数处理
     * @param ms       {@link MappedStatement}
     * @param bs       {@link BoundSql}
     * @param rowStart 分页开始位置
     * @param rowEnd   分页结束位置
     */
    protected void handleParameter(final MappedStatement ms, final BoundSql bs,
                                   final Long rowStart, final Long rowEnd) {
        final List<ParameterMapping> parameterMappings = bs.getParameterMappings();
        if (parameterMappings != null) {
            final List<ParameterMapping> mappings = new ArrayList<>(parameterMappings);
            final Configuration cf = ms.getConfiguration();
            mappings.add(new ParameterMapping.Builder(cf, DEF_PAGEABLE_START, Long.class).build());
            mappings.add(new ParameterMapping.Builder(cf, DEF_PAGEABLE_OFFSET, Long.class).build());
            final MetaObject metaObject = MetaObjects.forObject(bs);
            metaObject.setValue("parameterMappings", mappings);
        }
    }

    /**
     * 获取已存在的{@link MappedStatement}
     * @param configuration {@link Configuration}
     * @param msId          {@link MappedStatement}-ID
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement getMappedStatementIfExists(final Configuration configuration, final String msId) {
        try {
            return configuration.getMappedStatement(msId, false);
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }

    @Override
    public String makeQueryListSQL(MappedStatement ms, BoundSql bs, Object parameter,
                                   RowBounds rb, CacheKey cacheKey) {
        if (this.isRange()) {
            final RangePageable pageable = RangePageableThreadLocalCache.getPageable();
            return this.makeCorrQueryListSQL(ms, cacheKey, bs.getSql(), pageable.getStart(),
                pageable.getEnd(), pageable.getOffset());
        } else {
            final Pageable pageable = StandardPageableThreadLocalCache.getPageable();
            final long size = pageable.getSize();
            final long rowStart = pageable.offset();
            return this.makeCorrQueryListSQL(ms, cacheKey, bs.getSql(), rowStart, rowStart + size, size);
        }
    }

    @Override
    public boolean canExecutePaging(MappedStatement ms, Object parameter, RowBounds rb) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> Object executingPagingAfter(List<E> result, Object parameter, RowBounds rb) {
        final Pageable pageable = StandardPageableThreadLocalCache.getPageable();
        if (pageable instanceof DataPageable) {
            final DataPageable<E> dp = (DataPageable<E>) pageable;
            if (dp.autoFilling()) {
                dp.setData(result);
            }
        }
        return result;
    }

    @Override
    public void completed() {
        // empty
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 处理分页参数
     * @param ms       {@link MappedStatement}
     * @param paramMap 参数
     * @param bs       {@link BoundSql}
     * @param cacheKey 缓存key
     * @param rowStart 分页开始位置
     * @param rowEnd   分页结束位置
     * @param offset   偏移量
     * @return 参数对象
     */
    public abstract Object handlePageableParameter(final MappedStatement ms, final Map<String, Object> paramMap,
                                                   final BoundSql bs, final CacheKey cacheKey,
                                                   final Long rowStart, final Long rowEnd, final Long offset);

    /**
     * 生成对应数据库分页SQL语句
     * @param ms          {@link MappedStatement}
     * @param cacheKey    缓存key
     * @param originalSql 原SQL语句
     * @param rowStart    分页开始位置
     * @param rowEnd      分页结束位置
     * @param offset      偏移量
     * @return 分页SQL语句
     */
    public abstract String makeCorrQueryListSQL(final MappedStatement ms, final CacheKey cacheKey,
                                                final String originalSql, final Long rowStart, final Long rowEnd,
                                                final Long offset);

}
