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
package io.github.mybatisx.plugin.auditable;

import io.github.mybatisx.auditable.event.AuditedEvent;
import io.github.mybatisx.plugin.handler.Handler;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 审计拦截处理器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface AuditedHandler<T> extends Handler {

    /**
     * 获取缓存
     * @param cacheKey 缓存key
     * @return 缓存值
     */
    T getCache(final String cacheKey);

    /**
     * 缓存数据
     * @param cacheKey 缓存key
     * @param data     数据
     */
    void cache(final String cacheKey, final T data);

    /**
     * 检查是否可审计
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @return boolean
     */
    boolean canAudited(final MappedStatement ms, final Object parameter);

    /**
     * 审计处理
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @return {@link AuditedEvent}
     */
    AuditedEvent auditedHandle(final MappedStatement ms, final Object parameter);

}
