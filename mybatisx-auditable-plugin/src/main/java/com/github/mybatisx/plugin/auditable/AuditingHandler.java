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
package com.github.mybatisx.plugin.auditable;

import com.github.mybatisx.auditable.alter.AuditedAlterData;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * 审计处理器
 * @author wvkity
 * @created 2021-03-14
 * @since 1.0.0
 */
public interface AuditingHandler<E> {

    String PROP_KEY_ROLLBACK = "rollback";
    String PROP_KEY_MA_CACHE_CLASS = "metadataAuditCacheClass";
    String PROP_KEY_MA_CACHE_CFG_PREFIX = "metadataAuditCacheCfgPrefix";
    String PROP_KEY_SA_CACHE_CLASS = "systemAuditCacheClass";
    String PROP_KEY_SA_CACHE_CFG_PREFIX = "systemAuditCacheCfgPrefix";
    String PROP_KEY_AUDITING_INTERCEPT_METHODS = "auditingInterceptMethods";
    String PROP_KEY_AUDITING_FILTER_METHODS = "auditingFilterMethods";
    String PROP_KEY_AUDITING_LOGIC_DELETE_METHODS = "auditingLogicDeleteMethods";

    /**
     * 获取缓存
     * @param cacheKey key
     * @return 缓存数据
     */
    E getCache(final String cacheKey);

    /**
     * 缓存数据
     * @param cacheKey key
     * @param data     数据
     */
    void cache(final String cacheKey, final E data);

    /**
     * 审计
     * @param ms            {@link MappedStatement}
     * @param parameter     原参数对象
     * @param target        目标对象
     * @param isInsert      是否为保存操作
     * @param isLogicDelete 是否为逻辑删除
     * @return {@link AuditedAlterData}列表
     */
    List<AuditedAlterData> invoke(final MappedStatement ms, final Object parameter, final Object target,
                                  final boolean isInsert, final boolean isLogicDelete);

}
