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
package com.github.mybatisx.plugin.handler;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 抽象查询操作拦截处理器
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public abstract class AbstractSelectHandler extends AbstractHandler {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        final int size = args.length;
        final MappedStatement ms = (MappedStatement) args[0];
        final Object parameter = args[1];
        final RowBounds rowBounds = (RowBounds) args[2];
        final ResultHandler<?> resultHandler = (ResultHandler<?>) args[3];
        final Executor executor = (Executor) invocation.getTarget();
        final CacheKey cacheKey;
        final BoundSql boundSql;
        if (size == ARG_SIZE_OF_FOUR) {
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }
        if (this.filter(ms, parameter)) {
            return this.handle(invocation, executor, ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
        } else {
            if (size == ARG_SIZE_OF_FOUR) {
                return executor.query(ms, parameter, rowBounds, resultHandler);
            }
            return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
        }
    }

    /**
     * 拦截处理
     * @param invocation 代理对象
     * @param executor   执行器
     * @param ms         {@link MappedStatement}对象
     * @param parameter  方法参数
     * @param rb         内存分页参数
     * @param rh         结果集处理器
     * @param cacheKey   缓存key
     * @param bs         SQL绑定对象
     * @return 结果
     * @throws Throwable 异常信息
     */
    public abstract Object handle(Invocation invocation, Executor executor, MappedStatement ms,
                                  Object parameter, RowBounds rb, ResultHandler<?> rh,
                                  CacheKey cacheKey, BoundSql bs) throws Throwable;
}
