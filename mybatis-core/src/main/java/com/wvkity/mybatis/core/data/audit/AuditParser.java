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
package com.wvkity.mybatis.core.data.audit;

import com.wvkity.mybatis.core.metadata.Field;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * 自动审计属性解析器
 * @author wvkity
 * @created 2020-10-14
 * @since 1.0.0
 */
public final class AuditParser {

    private AuditParser() {
    }

    /**
     * 检查实体属性是否符合自动审计
     * @param field           {@link Field}
     * @param autoDiscern     是否自动识别
     * @param strategy        {@link AuditStrategy}
     * @param matching        {@link AuditMatching}
     * @param annotationClass 注解类
     * @return boolean
     */
    private static boolean matching(final Field field, final boolean autoDiscern,
                                    final AuditStrategy strategy, final AuditMatching matching,
                                    final Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(field).map(it ->
            field.isAnnotationPresent(annotationClass) ||
                (autoDiscern && DefaultAuditProperties.matching(strategy, matching, field.getName()))
        ).orElse(false);
    }

    /**
     * {@link AuditMatching#ID ID}类型审计匹配
     * @param field           {@link Field}
     * @param autoDiscern     是否自动识别
     * @param strategy        {@link AuditStrategy}
     * @param annotationClass 对应注解类
     * @return boolean
     */
    public static boolean matchingWithId(final Field field, final boolean autoDiscern,
                                         final AuditStrategy strategy,
                                         final Class<? extends Annotation> annotationClass) {
        return matching(field, autoDiscern, strategy, AuditMatching.ID, annotationClass);
    }

    /**
     * {@link AuditMatching#NAME NAME}类型审计匹配
     * @param field           {@link Field}
     * @param autoDiscern     是否自动识别
     * @param strategy        {@link AuditStrategy}
     * @param annotationClass 对应注解类
     * @return boolean
     */
    public static boolean matchingWithName(final Field field, final boolean autoDiscern,
                                           final AuditStrategy strategy,
                                           final Class<? extends Annotation> annotationClass) {
        return matching(field, autoDiscern, strategy, AuditMatching.NAME, annotationClass);
    }

    /**
     * {@link AuditMatching#NAME NAME}类型审计匹配
     * @param field           {@link Field}
     * @param autoDiscern     是否自动识别
     * @param strategy        {@link AuditStrategy}
     * @param annotationClass 对应注解类
     * @return boolean
     */
    public static boolean matchingWithTime(final Field field, final boolean autoDiscern,
                                           final AuditStrategy strategy,
                                           final Class<? extends Annotation> annotationClass) {
        return matching(field, autoDiscern, strategy, AuditMatching.TIME, annotationClass);
    }
}
