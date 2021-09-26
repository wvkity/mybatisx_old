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
package io.github.mybatisx.plugin.auditable.support;

import io.github.mybatisx.auditable.PropertyWrapper;
import io.github.mybatisx.auditable.meta.AuditedMetadata;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 元数据审计处理
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface MetadataAuditable {

    /**
     * 审计
     * @param ms       {@link MappedStatement}
     * @param property {@link PropertyWrapper}
     * @param target   目标对象
     * @return 审计前的数据
     */
    AuditedMetadata invoke(final MappedStatement ms, final PropertyWrapper property, final Object target);
}
