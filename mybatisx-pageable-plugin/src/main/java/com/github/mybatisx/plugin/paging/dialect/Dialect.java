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
package com.github.mybatisx.plugin.paging.dialect;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * 数据库分页方言
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public interface Dialect {

    String DEF_PAGEABLE_RECORD_SUFFIX = ".plugin_inline_records";
    String DEF_PAGEABLE_SUFFIX = "pageable";
    String DEF_PAGEABLE_START = DEF_PAGEABLE_SUFFIX + "_start";
    String DEF_PAGEABLE_OFFSET = DEF_PAGEABLE_SUFFIX + "_offset";
    String PROP_KEY_DIALECT = "dialect";
    String PROP_KEY_AUTO_RUNTIME_PARSING_JDBC = "autoRuntimeParsingJdbc";
    String PROP_KEY_AUTO_RELEASE_CONNECT = "autoReleaseConnect";
    String PROP_KEY_RANGE_DIALECT_PROXY = "rangePageableDialect";
    String PROP_KEY_STANDARD_DIALECT_PROXY = "standardPageableDialect";
    String PROP_KEY_RECORD_MS_CACHE_CLASS = "recordMsCacheClass";
    String PROP_KEY_RECORD_MS_CFG_PREFIX = "recordMsCacheCfgPrefix";
    String PROP_KEY_WITH_NO_LOCK_REPLACER_CLASS = "withNoLockReplacerClass";
    String PROP_KEY_WITH_NO_LOCK_CACHE_CLASS = "withNoLockCacheClass";
    String PROP_KEY_WITH_NO_LOCK_RECORD_CFG_PREFIX = "withNoLockRecordCacheCfgPrefix";
    String PROP_KEY_WITH_NO_LOCK_PAGEABLE_CFG_PREFIX = "withNoLockPageableCacheCfgPrefix";

    /**
     * 过滤分页查询
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @return boolean
     */
    boolean filter(final MappedStatement ms, final Object parameter, final RowBounds rb);

    /**
     * 生成查询总记录数SQL语句
     * @param ms        {@link MappedStatement}
     * @param bs        {@link BoundSql}
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @param cacheKey  {@link CacheKey}
     * @return SQL语句
     */
    String makeQueryRecordSql(final MappedStatement ms, final BoundSql bs, final Object parameter,
                              final RowBounds rb, final CacheKey cacheKey);

    /**
     * 处理参数
     * @param ms        {@link MappedStatement}
     * @param bs        {@link BoundSql}
     * @param parameter 方法参数
     * @param cacheKey  {@link CacheKey}
     * @return 处理后的参数对象
     */
    Object handleParameter(final MappedStatement ms, final BoundSql bs, final Object parameter,
                           final CacheKey cacheKey);

    /**
     * 生成分页查询SQL语句
     * @param ms        {@link MappedStatement}
     * @param bs        {@link BoundSql}
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @param cacheKey  {@link CacheKey}
     * @return SQL语句
     */
    String makeQueryListSql(final MappedStatement ms, final BoundSql bs, final Object parameter,
                            final RowBounds rb, final CacheKey cacheKey);

    /**
     * 检查是否需要执行分页查询
     * @param ms        {@link MappedStatement}
     * @param parameter 方法查询
     * @param rb        {@link RowBounds}
     * @return boolean
     */
    boolean canExecutePaging(final MappedStatement ms, final Object parameter, final RowBounds rb);

    /**
     * 执行分页后处理结果
     * @param result    结果
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @param <E>       泛型类型
     * @return 结果
     */
    <E> Object executingPagingAfter(final List<E> result, final Object parameter, final RowBounds rb);

    /**
     * 所有查询执行完成后
     */
    void completed();

    /**
     * 设置分页相关配置
     * @param properties 属性配置
     */
    void setProperties(final Properties properties);

}
