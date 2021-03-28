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
package com.wvkity.mybatis.support.parser;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.auditable.AuditMatching;
import com.wvkity.mybatis.core.auditable.AuditType;
import com.wvkity.mybatis.core.auditable.OriginalProperty;
import com.wvkity.mybatis.core.auditable.parser.AuditParser;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * 默认审计属性解析器
 * @author wvkity
 * @created 2021-03-04
 * @since 1.0.0
 */
public class DefaultAuditParser implements AuditParser {

    @Override
    public boolean matching(OriginalProperty property, AuditMatching matching, AuditType type,
                            Class<? extends Annotation> annotation) {
        return annotation != null && Optional.ofNullable(property).map(it ->
            !it.isPrimaryKey() && Objects.isNotEmpty(it.getAnnotations())
                && it.getAnnotations().stream().anyMatch(at -> annotation.equals(at.annotationType()))).orElse(false);
    }

}
