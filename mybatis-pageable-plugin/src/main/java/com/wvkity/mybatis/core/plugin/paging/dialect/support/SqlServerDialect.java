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

import com.wvkity.mybatis.core.cache.CacheFactory;
import com.wvkity.mybatis.core.cache.LocalCache;
import com.wvkity.mybatis.core.plugin.exception.MyBatisPluginException;
import com.wvkity.mybatis.core.plugin.paging.dialect.AbstractPageableDialect;
import com.wvkity.mybatis.core.plugin.paging.parser.SqlServerPageableSqlParser;
import com.wvkity.mybatis.core.plugin.paging.parser.replace.RegexWithNoLockReplacer;
import com.wvkity.mybatis.core.plugin.paging.parser.replace.Replacer;
import com.wvkity.mybatis.core.plugin.paging.parser.replace.SimpleWithNoLockReplacer;
import com.wvkity.mybatis.core.plugin.utils.Objects;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.Properties;

/**
 * SQLSERVER分页方言
 * @author wvkity
 * @created 2021-02-20
 * @since 1.0.0
 */
public class SqlServerDialect extends AbstractPageableDialect {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    protected SqlServerPageableSqlParser pageableSqlParser = new SqlServerPageableSqlParser();
    protected Replacer replacer;
    protected LocalCache<String, String> withNoLockCacheOfCs;
    protected LocalCache<String, String> withNoLockCacheOfPs;

    @Override
    public String makeQueryRecordSQL(MappedStatement ms, BoundSql bs, Object parameter,
                                     RowBounds rb, CacheKey cacheKey) {
        final String originalSql = bs.getSql();
        final String cache = this.withNoLockCacheOfCs.get(originalSql);
        if (Objects.isNotBlank(cache)) {
            return cache;
        }
        String newSql = this.replacer.replace(originalSql);
        newSql = this.recordsSqlParser.smartParse(newSql);
        newSql = this.replacer.restore(newSql);
        this.withNoLockCacheOfCs.put(originalSql, newSql);
        return newSql;
    }

    @Override
    public Object handlePageableParameter(MappedStatement ms, Map<String, Object> paramMap, BoundSql bs,
                                          CacheKey cacheKey, Long rowStart, Long rowEnd, Long offset) {
        return paramMap;
    }

    @Override
    public String makeCorrQueryListSQL(MappedStatement ms, CacheKey cacheKey, String originalSql,
                                       Long rowStart, Long rowEnd, Long offset) {
        cacheKey.update(rowStart);
        cacheKey.update(offset);
        String cache = this.withNoLockCacheOfPs.get(originalSql);
        if (Objects.isBlank(cache)) {
            cache = originalSql;
            cache = this.replacer.replace(cache);
            cache = this.pageableSqlParser.smartParse(cache);
            cache = this.replacer.restore(cache);
            this.withNoLockCacheOfPs.put(originalSql, cache);
        }
        cache = cache.replace(SqlServerPageableSqlParser.START_ROW, rowStart.toString());
        cache = cache.replace(SqlServerPageableSqlParser.END_ROW, offset.toString());
        return cache;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        final String replacerClass = properties.getProperty(PROP_KEY_WITH_NO_LOCK_REPLACER_CLASS);
        if (Objects.isBlank(replacerClass) || "simple".equalsIgnoreCase(replacerClass)) {
            this.replacer = new SimpleWithNoLockReplacer();
        } else if ("regex".equalsIgnoreCase(replacerClass)) {
            this.replacer = new RegexWithNoLockReplacer();
        } else {
            this.replacer = newInstance(replacerClass);
        }
        final String cacheClass = properties.getProperty(PROP_KEY_WITH_NO_LOCK_CACHE_CLASS);
        final String countCfgPrefix = properties.getProperty(PROP_KEY_WITH_NO_LOCK_RECORD_CFG_PREFIX);
        final String pageableCfgPrefix = properties.getProperty(PROP_KEY_WITH_NO_LOCK_PAGEABLE_CFG_PREFIX);
        this.withNoLockCacheOfCs = CacheFactory.create(cacheClass, properties, countCfgPrefix);
        this.withNoLockCacheOfPs = CacheFactory.create(cacheClass, properties, pageableCfgPrefix);
    }

    private Replacer newInstance(final String replacerClass) {
        try {
            final Class<?> clazz = Class.forName(replacerClass);
            return (Replacer) LOOKUP.findConstructor(clazz, MethodType.methodType(clazz)).invokeWithArguments();
        } catch (Throwable e) {
            throw new MyBatisPluginException("The 'replacer' parameter is misconfigured. " +
                "The system defaults to 'simple' and 'regex', " + "or implements the '" +
                Replacer.class.getCanonicalName() + "' interface. Please change the configuration.", e);
        }
    }

}
