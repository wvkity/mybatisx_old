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
package com.wvkity.mybatis.core.plugin.batch;

import com.wvkity.mybatis.core.plugin.annotation.Order;
import com.wvkity.mybatis.core.plugin.handler.Handler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.sql.Statement;
import java.util.Properties;

/**
 * 批量操作拦截器
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
@Order(98)
@Intercepts({
    @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
    @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class BatchStatementInterceptor implements Interceptor {

    private final Handler handler;

    public BatchStatementInterceptor() {
        this.handler = new BatchStatementInvokeHandler();
    }

    public BatchStatementInterceptor(Handler handler) {
        this.handler = handler;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return this.handler.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.handler.setProperties(properties);
    }
}
