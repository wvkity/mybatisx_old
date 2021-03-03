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
package com.wvkity.mybatis.support.data.audit;

/**
 * 审计模式枚举
 * @author wvkity
 * @created 2020-10-14
 * @since 1.0.0
 */
public enum AuditMatching {
    /**
     * ID类型
     */
    ID,
    /**
     * 时间类型
     */
    TIME,
    /**
     * 名称类型
     */
    NAME
}
