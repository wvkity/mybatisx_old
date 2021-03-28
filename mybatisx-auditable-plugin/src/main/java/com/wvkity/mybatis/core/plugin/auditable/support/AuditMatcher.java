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
package com.wvkity.mybatis.core.plugin.auditable.support;

import com.wvkity.mybatis.core.auditable.MatchMode;
import com.wvkity.mybatis.core.auditable.OriginalProperty;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * 审计属性匹配器
 * @author wvkity
 * @created 2021-03-05
 * @since 1.0.0
 */
public interface AuditMatcher {

    /**
     * 获取审计属性
     * @param ms          {@link MappedStatement}
     * @param target      目标对象
     * @param mode        匹配模式
     * @param insert      是否为保存操作
     * @param logicDelete 是否为逻辑删除操作
     * @return {@link OriginalProperty}列表
     */
    List<OriginalProperty> matches(final MappedStatement ms, final Object target, final MatchMode mode,
                                   final boolean insert, final boolean logicDelete);
}
