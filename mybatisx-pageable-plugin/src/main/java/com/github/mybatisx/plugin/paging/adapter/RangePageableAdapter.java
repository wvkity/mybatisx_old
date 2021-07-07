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
package com.github.mybatisx.plugin.paging.adapter;

import com.github.mybatisx.plugin.paging.config.RangePageableConfig;
import com.github.mybatisx.plugin.paging.config.RangePageableThreadLocalCache;
import com.github.mybatisx.plugin.paging.dialect.AbstractDialect;
import com.github.mybatisx.plugin.paging.dialect.Dialect;
import com.github.mybatisx.plugin.paging.proxy.RangePageableDialectProxy;
import com.github.mybatisx.plugin.paging.RangePageable;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * 范围分页适配器
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public class RangePageableAdapter implements Dialect {

    /**
     * 配置类
     */
    private RangePageableConfig config;
    /**
     * 分页方言代理对象
     */
    protected RangePageableDialectProxy proxy;

    @Override
    public boolean filter(MappedStatement ms, Object parameter, RowBounds rb) {
        final RangePageable pageable = this.config.getPageable(parameter);
        if (pageable != null && pageable.isApply()) {
            this.proxy.initDialect(ms);
            return true;
        }
        return false;
    }

    @Override
    public String makeQueryRecordSql(MappedStatement ms, BoundSql bs, Object parameter,
                                     RowBounds rb, CacheKey cacheKey) {
        return this.getDelegate().makeQueryRecordSql(ms, bs, parameter, rb, cacheKey);
    }

    @Override
    public Object handleParameter(MappedStatement ms, BoundSql bs, Object parameter, CacheKey cacheKey) {
        return this.getDelegate().handleParameter(ms, bs, parameter, cacheKey);
    }

    @Override
    public String makeQueryListSql(MappedStatement ms, BoundSql bs, Object parameter,
                                   RowBounds rb, CacheKey cacheKey) {
        return this.getDelegate().makeQueryListSql(ms, bs, parameter, rb, cacheKey);
    }

    @Override
    public boolean canExecutePaging(MappedStatement ms, Object parameter, RowBounds rb) {
        return this.getDelegate().canExecutePaging(ms, parameter, rb);
    }

    @Override
    public <E> Object executingPagingAfter(List<E> result, Object parameter, RowBounds rb) {
        return Optional.ofNullable(this.getDelegate()).map(it ->
            it.executingPagingAfter(result, parameter, rb)).orElse(result);
    }

    @Override
    public void completed() {
        Optional.ofNullable(this.getDelegate()).ifPresent(it -> {
            it.completed();
            this.proxy.removeDelegate();
        });
        RangePageableThreadLocalCache.remove();
    }

    @Override
    public void setProperties(Properties properties) {
        this.proxy = new RangePageableDialectProxy();
        this.proxy.setProperties(properties);
        this.config = new RangePageableConfig();
    }

    protected AbstractDialect getDelegate() {
        return this.proxy.getDelegate();
    }
}
