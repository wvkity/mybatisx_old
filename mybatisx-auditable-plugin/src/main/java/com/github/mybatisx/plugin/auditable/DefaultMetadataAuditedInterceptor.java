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
package com.github.mybatisx.plugin.auditable;

import com.github.mybatisx.plugin.AbstractInterceptor;
import com.github.mybatisx.plugin.annotation.Order;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.util.Properties;

/**
 * 元数据审计拦截器
 * <pre>{@code
 *
 * }</pre>
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
@Order(168)
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class DefaultMetadataAuditedInterceptor extends AbstractInterceptor {

    private final MetadataAuditedHandler metadataAuditedHandler;

    public DefaultMetadataAuditedInterceptor(MetadataAuditedHandler metadataAuditedHandler) {
        this.metadataAuditedHandler = metadataAuditedHandler;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return this.metadataAuditedHandler.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.metadataAuditedHandler.setProperties(properties);
    }
}
