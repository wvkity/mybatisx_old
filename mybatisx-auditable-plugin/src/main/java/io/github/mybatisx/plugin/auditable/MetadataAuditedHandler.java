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

import io.github.mybatisx.auditable.PropertyWrapper;
import io.github.mybatisx.auditable.meta.AuditedMetadata;
import io.github.mybatisx.plugin.handler.Handler;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * 元数据审计拦截处理器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface MetadataAuditedHandler extends Handler {

    /**
     * 加载审计属性
     * @param ms            {@link MappedStatement}
     * @param target        目标对象
     * @param isInsert      是否保存操作
     * @param isLogicDelete 是否逻辑删除操作
     * @return 审计数据列表
     */
    List<PropertyWrapper> loadProperties(final MappedStatement ms, final Object target,
                                         final boolean isInsert, final boolean isLogicDelete);

    /**
     * 审计
     * @param ms         {@link MappedStatement}
     * @param parameter  方法参数
     * @param target     目标对象
     * @param properties 审计属性列表
     * @return 审计数据列表
     */
    List<AuditedMetadata> audited(final MappedStatement ms, final Object parameter, final Object target,
                                  final List<PropertyWrapper> properties);
}
