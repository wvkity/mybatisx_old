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
package com.wvkity.mybatis.core.auditable.parser;

import com.wvkity.mybatis.core.auditable.AuditMatching;
import com.wvkity.mybatis.core.auditable.AuditType;
import com.wvkity.mybatis.core.auditable.OriginalProperty;
import com.wvkity.mybatis.core.auditable.annotation.CreatedById;

import java.lang.annotation.Annotation;

/**
 * 审计属性解析器
 * @author wvkity
 * @created 2021-03-01
 * @since 1.0.0
 */
public interface AuditParser {

    /**
     * ID类型标识匹配
     * @param property   包装属性
     * @param matching   模式
     * @param annotation 注解类
     * @return boolean
     * @see CreatedById @CreateUserId
     */
    default boolean idMatches(final OriginalProperty property, final AuditMatching matching,
                              final Class<? extends Annotation> annotation) {
        return this.matching(property, matching, AuditType.ID, annotation);
    }

    /**
     * NAME类型标识匹配
     * @param property   包装属性
     * @param matching   模式
     * @param annotation 注解类
     * @return boolean
     */
    default boolean nameMatches(final OriginalProperty property, final AuditMatching matching,
                                final Class<? extends Annotation> annotation) {
        return this.matching(property, matching, AuditType.NAME, annotation);
    }

    /**
     * DATE类型标识匹配
     * @param property   包装属性
     * @param matching   模式
     * @param annotation 注解类
     * @return boolean
     */
    default boolean dateMatches(final OriginalProperty property, final AuditMatching matching,
                                final Class<? extends Annotation> annotation) {
        return this.matching(property, matching, AuditType.DATE, annotation);
    }

    /**
     * DATE类型标识匹配
     * @param property   包装属性
     * @param matching   模式
     * @param type       类型
     * @param annotation 注解类
     * @return boolean
     */
    boolean matching(final OriginalProperty property, final AuditMatching matching,
                     final AuditType type, final Class<? extends Annotation> annotation);
}
