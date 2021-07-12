/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.github.mybatisx.executor;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.executor.resultset.EmbedResult;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.util.Collection;
import java.util.Map;

/**
 * 重写{@link BaseExecutor}
 * @author wvkity
 * @created 2021-07-12
 * @since 1.0.0
 */
public abstract class AbstractMyBatisExecutor extends BaseExecutor {

    protected AbstractMyBatisExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject,
                                   RowBounds rowBounds, BoundSql boundSql) {
        final CacheKey cacheKey = super.createCacheKey(ms, parameterObject, rowBounds, boundSql);
        final EmbedResult embedResult;
        if (Objects.nonNull(embedResult = this.getEmbedResultObject(parameterObject))) {
            this.updateCacheKey(cacheKey, embedResult);
        }
        return cacheKey;
    }

    /**
     * 获取{@link EmbedResult}对象
     * @param parameterObject 方法参数
     * @return {@link EmbedResult}
     */
    @SuppressWarnings("unchecked")
    protected EmbedResult getEmbedResultObject(final Object parameterObject) {
        if (parameterObject instanceof EmbedResult) {
            return (EmbedResult) parameterObject;
        } else if (parameterObject instanceof Map) {
            final Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
            if (parameterMap.containsKey(Constants.PARAM_CRITERIA)) {
                final Object value = parameterMap.get(Constants.PARAM_CRITERIA);
                if (value instanceof EmbedResult) {
                    return (EmbedResult) value;
                }
            }
            final Collection<Object> values = parameterMap.values();
            for (Object value : values) {
                if (value instanceof EmbedResult) {
                    return (EmbedResult) value;
                }
            }
        }
        return null;
    }

    /**
     * 更新{@link CacheKey}
     * @param cacheKey    {@link CacheKey}
     * @param embedResult {@link EmbedResult}
     */
    protected void updateCacheKey(final CacheKey cacheKey, final EmbedResult embedResult) {
        final String resultMap = embedResult.getResultMap();
        final Class<?> resultType = embedResult.getResultType();
        final String mapKey = embedResult.getMapKey();
        final Class<?> mapType = embedResult.getMapType();
        if (Objects.isNotBlank(resultMap)) {
            cacheKey.update(resultMap);
        }
        if (Objects.nonNull(resultType)) {
            cacheKey.update(resultType);
        }
        if (Objects.isNotBlank(mapKey)) {
            cacheKey.update(mapKey);
        }
        if (Objects.nonNull(mapType)) {
            cacheKey.update(mapType);
        }
    }
}
