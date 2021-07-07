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

import com.wvkity.mybatis.core.cache.LocalCacheFactory;
import com.wvkity.mybatis.core.cache.LocalCache;
import com.wvkity.mybatis.core.plugin.paging.config.StandardPageableThreadLocalCache;
import com.wvkity.mybatis.core.plugin.utils.PluginUtil;
import com.wvkity.paging.Pageable;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * 抽象数据库分页方言
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public abstract class AbstractPageableDialect extends AbstractDialect implements PageableDialect {

    /**
     * 查询总记录SQL缓存
     */
    protected LocalCache<String, MappedStatement> recordMsCache;

    /**
     * 获取分页对象
     * @return {@link Pageable}
     */
    protected Pageable getPageable() {
        return StandardPageableThreadLocalCache.getPageable();
    }

    @Override
    public boolean canExecutingQueryRecord(MappedStatement ms, Object parameter, RowBounds rb) {
        return Optional.ofNullable(this.getPageable()).map(it -> it.getSize() > 0).orElse(false);
    }

    @Override
    public boolean canExecutePaging(MappedStatement ms, Object parameter, RowBounds rb) {
        if (this.isRange()) {
            return super.canExecutePaging(ms, parameter, rb);
        }
        return Optional.ofNullable(this.getPageable()).map(it -> it.getRecords() > 0).orElse(false);
    }

    @Override
    public Long executingQueryRecord(Executor executor, MappedStatement ms, Object parameter, RowBounds rb,
                                     BoundSql bs, ResultHandler<?> rh) throws SQLException {
        final String msId = ms.getId() + DEF_PAGEABLE_RECORD_SUFFIX;
        final MappedStatement cacheMs = this.getMappedStatementIfExists(ms.getConfiguration(), msId);
        if (cacheMs != null) {
            return this.executingQueryRecordIfExists(executor, cacheMs, parameter, bs, rh);
        }
        final MappedStatement rms = getQueryRecordMappedStatement(ms, msId);
        final Map<String, Object> apMap = PluginUtil.getAdditionalParameter(bs);
        final CacheKey cacheKey = executor.createCacheKey(ms, parameter, RowBounds.DEFAULT, bs);
        final String recordSql = this.makeQueryRecordSql(ms, bs, parameter, rb, cacheKey);
        final BoundSql rbs = new BoundSql(ms.getConfiguration(), recordSql, bs.getParameterMappings(), parameter);
        for (Map.Entry<String, Object> entry : apMap.entrySet()) {
            rbs.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        final List<Object> result = executor.query(rms, parameter, RowBounds.DEFAULT, rh, cacheKey, rbs);
        return Optional.ofNullable(result).map(it -> ((Number) it.get(0)).longValue()).orElse(0L);
    }

    /**
     * 执行查询总记录数
     * @param executor  {@link Executor}
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @param bs        {@link BoundSql}
     * @param rh        {@link ResultHandler}
     * @return 总记录数
     * @throws SQLException SQL异常信息
     */
    protected Long executingQueryRecordIfExists(final Executor executor, final MappedStatement ms,
                                                final Object parameter, final BoundSql bs,
                                                final ResultHandler<?> rh) throws SQLException {
        final CacheKey cacheKey = executor.createCacheKey(ms, parameter, RowBounds.DEFAULT, bs);
        final BoundSql rbs = ms.getBoundSql(parameter);
        final List<Object> result = executor.query(ms, parameter, RowBounds.DEFAULT, rh, cacheKey, rbs);
        return Optional.ofNullable(result).map(it -> ((Number) it.get(0)).longValue()).orElse(0L);
    }

    /**
     * 获取查询总记录数{@link MappedStatement}对象
     * @param ms         {@link MappedStatement}
     * @param recordMsId {@link MappedStatement}唯一标识
     * @return {@link MappedStatement}
     */
    protected MappedStatement getQueryRecordMappedStatement(final MappedStatement ms, final String recordMsId) {
        return Optional.ofNullable(this.recordMsCache.get(recordMsId))
            .orElse(this.newQueryRecordMappedStatement(ms, recordMsId));
    }

    /**
     * 创建查询总记录数{@link MappedStatement}对象
     * @param ms         {@link MappedStatement}
     * @param recordMsId {@link MappedStatement}唯一标识
     * @return {@link MappedStatement}
     */
    protected MappedStatement newQueryRecordMappedStatement(final MappedStatement ms, final String recordMsId) {
        final MappedStatement.Builder it =
            new MappedStatement.Builder(ms.getConfiguration(), recordMsId, ms.getSqlSource(), ms.getSqlCommandType());
        it.resource(ms.getResource());
        it.fetchSize(ms.getFetchSize());
        it.statementType(ms.getStatementType());
        it.keyGenerator(ms.getKeyGenerator());
        final String[] props = ms.getKeyProperties();
        if (props != null && props.length > 0) {
            it.keyProperty(String.join(",", props));
        }
        it.timeout(ms.getTimeout());
        it.parameterMap(ms.getParameterMap());
        final List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(),
            Long.class, new ArrayList<>(0)).build();
        resultMaps.add(resultMap);
        it.resultMaps(resultMaps);
        it.resultSetType(ms.getResultSetType());
        it.cache(ms.getCache());
        it.flushCacheRequired(ms.isFlushCacheRequired());
        it.useCache(ms.isUseCache());
        final MappedStatement rms = it.build();
        this.recordMsCache.put(recordMsId, rms);
        return rms;
    }

    @Override
    public boolean executingQueryRecordAfter(long records, Object parameter, RowBounds rb) {
        final Pageable pageable = this.getPageable();
        pageable.setRecords(records);
        return pageable.getSize() > 0L && (records - (pageable.getPage() - 1) * pageable.getSize()) > 0L;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.recordMsCache = LocalCacheFactory.create(properties.getProperty(PROP_KEY_RECORD_MS_CACHE_CLASS), properties,
            properties.getProperty(PROP_KEY_RECORD_MS_CFG_PREFIX));
    }
}
