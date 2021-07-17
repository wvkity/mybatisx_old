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
package com.github.mybatisx.plugin.auditable.support;

import com.github.mybatisx.auditable.AuditedPattern;
import com.github.mybatisx.auditable.PropertyWrapper;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * 审计属性加载器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface AuditedPropertyLoader {

    /**
     * 加载审计属性
     * @param ms       {@link MappedStatement}
     * @param target   目标对象
     * @param pattern  匹配模式
     * @param isInsert 是否执行保存操作
     * @param isDelete 是否执行逻辑删除操作
     * @return 审计属性列表
     */
    List<PropertyWrapper> load(final MappedStatement ms, final Object target, final AuditedPattern pattern,
                               final boolean isInsert, final boolean isDelete);
}
