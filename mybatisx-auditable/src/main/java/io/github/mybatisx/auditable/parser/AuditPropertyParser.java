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
package io.github.mybatisx.auditable.parser;

import io.github.mybatisx.auditable.PropertyWrapper;
import io.github.mybatisx.auditable.matcher.AuditMatcher;

/**
 * 审计属性解析器
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public interface AuditPropertyParser {

    /**
     * 解析审计属性
     * @param property 属性包装器
     * @return {@link AuditMatcher}
     */
    AuditMatcher parse(final PropertyWrapper property);

}
