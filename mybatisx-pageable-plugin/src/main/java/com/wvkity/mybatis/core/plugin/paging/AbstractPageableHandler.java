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
package com.wvkity.mybatis.core.plugin.paging;

import com.wvkity.mybatis.core.plugin.handler.AbstractSelectHandler;
import com.wvkity.mybatis.core.plugin.paging.dialect.Dialect;
import com.wvkity.mybatis.core.plugin.utils.Objects;
import com.wvkity.paging.Pageable;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 抽象分页处理器
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public abstract class AbstractPageableHandler extends AbstractSelectHandler {

    /**
     * 锁
     */
    private final Lock lock = new ReentrantLock();
    /**
     * 分页方言
     */
    protected volatile Dialect dialect;
    /**
     * 数据库方言
     */
    protected String dialectClass;

    /**
     * 获取范围分页对象参数
     * @param parameter 方法参数
     * @return {@link RangeFetch}
     */
    protected RangeFetch getRangeFetch(final Object parameter) {
        return getArg(parameter, RangeFetch.PARAMETER_KEY, RangeFetch.class);
    }

    /**
     * 获取分页对象参数
     * @param parameter 方法参数
     * @return {@link Pageable}
     */
    protected Pageable getPageable(final Object parameter) {
        return getArg(parameter, "pageable", Pageable.class);
    }

    /**
     * 检查数据库分页方言是否存在
     */
    protected void validateDialectExists() {
        if (this.dialect == null) {
            if (this.lock.tryLock()) {
                try {
                    if (this.dialect == null) {
                        this.setProperties(new Properties());
                    }
                } finally {
                    this.lock.unlock();
                }
            }
        }
    }

    /**
     * 获取指定类型参数
     * @param parameter 参数对象
     * @param key       键
     * @param clazz     类型
     * @param <T>       泛型类型
     * @return 指定类型参数
     */
    @SuppressWarnings("unchecked")
    protected <T> T getArg(final Object parameter, final String key, final Class<T> clazz) {
        if (parameter != null && clazz != null && Objects.isNotBlank(key)) {
            if (clazz.isAssignableFrom(parameter.getClass())) {
                return (T) parameter;
            }
            if (parameter instanceof Map) {
                final Map<String, Object> paramMap = (Map<String, Object>) parameter;
                if (paramMap.containsKey(key)) {
                    final Object arg = paramMap.get(key);
                    return (T) Optional.ofNullable(arg).filter(it -> clazz.isAssignableFrom(it.getClass())).orElse(null);
                }
            }
        }
        return null;
    }

    /**
     * 执行分页查询
     * @param invocation {@link Invocation}
     * @param executor   {@link Executor}
     * @param ms         {@link MappedStatement}
     * @param parameter  方法参数
     * @param rb         {@link RowBounds}
     * @param rh         {@link ResultHandler}
     * @param cacheKey   缓存key
     * @param bs         {@link BoundSql}
     * @param <E>        结果类型
     * @return 查询结果
     * @throws Exception 异常信息
     */
    protected <E> List<E> executeQueryPaging(final Invocation invocation, final Executor executor,
                                             final MappedStatement ms, Object parameter,
                                             final RowBounds rb, final ResultHandler<?> rh,
                                             final CacheKey cacheKey, final BoundSql bs) throws Exception {
        if (this.dialect.canExecutePaging(ms, parameter, rb)) {
            parameter = this.dialect.handleParameter(ms, bs, parameter, cacheKey);
            final String pageableSql = this.dialect.makeQueryListSql(ms, bs, parameter, rb, cacheKey);
            final BoundSql newBs = new BoundSql(ms.getConfiguration(), pageableSql, bs.getParameterMappings(), parameter);
            return executor.query(ms, parameter, RowBounds.DEFAULT, rh, cacheKey, newBs);
        } else {
            return executor.query(ms, parameter, rb, rh, cacheKey, bs);
        }
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter) {
        final RangeFetch rangeFetch = this.getRangeFetch(parameter);
        final Pageable pageable = this.getPageable(parameter);
        return pageable == null && rangeFetch != null && rangeFetch.isRange();
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        switch (this.getMode()) {
            case RANGE:
                this.dialectClass = properties.getProperty(Dialect.PROP_KEY_RANGE_DIALECT_PROXY);
                break;
            case STANDARD:
                this.dialectClass = properties.getProperty(Dialect.PROP_KEY_STANDARD_DIALECT_PROXY);
                break;
            default:
                break;
        }
        if (Objects.isBlank(this.dialectClass)) {
            this.dialectClass = this.getDefDialect();
        }
        this.dialect = (Dialect) Objects.newInstance(this.dialectClass);
        if (dialect == null) {
            throw new IllegalArgumentException("Failed to initialize database dialect based on the specified " +
                "class name: `" + this.dialectClass + "`");
        }
        this.dialect.setProperties(properties);
    }

    public String getDialectClass() {
        return this.dialectClass;
    }

    /**
     * 获取分页模式
     * @return {@link PageableMode}
     */
    protected abstract PageableMode getMode();

    /**
     * 获取默认分页方言
     * @return 默认分页方言
     */
    protected abstract String getDefDialect();

}
