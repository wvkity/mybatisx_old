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
package io.github.mybatisx.plugin.paging;

import io.github.mybatisx.plugin.paging.adapter.StandardPageableAdapter;
import io.github.mybatisx.plugin.paging.dialect.Dialect;
import io.github.mybatisx.plugin.paging.dialect.PageableDialect;
import io.github.mybatisx.plugin.exception.MyBatisPluginException;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 分页处理器
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public class StandardPageableHandler extends RangePageableHandler {

    private static final String DEF_DIALECT = StandardPageableAdapter.class.getCanonicalName();

    @Override
    protected PageableMode getMode() {
        return PageableMode.STANDARD;
    }

    @Override
    protected String getDefDialect() {
        return DEF_DIALECT;
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter) {
        return parameter != null;
    }

    @Override
    public Object handle(Invocation invocation, Executor executor, MappedStatement ms, Object parameter, RowBounds rb,
                         ResultHandler<?> rh, CacheKey cacheKey, BoundSql bs) throws Throwable {
        try {
            this.validateDialectExists();
            final List<?> result;
            if (this.dialect.filter(ms, parameter, rb)) {
                final PageableDialect pd = (PageableDialect) this.dialect;
                if (pd.canExecutingQueryRecord(ms, parameter, rb)
                    && !pd.executingQueryRecordAfter(executor, ms, parameter, rb, bs, rh)) {
                    return pd.executingPagingAfter(new ArrayList<>(), parameter, rb);
                }
                result = this.executeQueryPaging(invocation, executor, ms, parameter, rb, rh, cacheKey, bs);
            } else {
                result = executor.query(ms, parameter, rb, rh, cacheKey, bs);
            }
            return this.dialect.executingPagingAfter(result, parameter, rb);
        } catch (Exception e) {
            throw new MyBatisPluginException("The scope paging query failed to execute: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(this.dialect).ifPresent(Dialect::completed);
        }
    }
}
