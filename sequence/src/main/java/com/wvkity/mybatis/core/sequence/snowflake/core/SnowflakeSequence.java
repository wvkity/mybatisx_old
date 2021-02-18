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
package com.wvkity.mybatis.core.sequence.snowflake.core;

import com.wvkity.mybatis.core.sequence.Sequence;
import com.wvkity.mybatis.core.sequence.snowflake.SnowflakeSequenceInfo;

/**
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public interface SnowflakeSequence extends Sequence {

    /**
     * 解析ID
     * @param id ID
     * @return ID信息
     */
    SnowflakeSequenceInfo parse(final long id);

    /**
     * 解析ID
     * @param id ID
     * @return ID信息
     */
    default SnowflakeSequenceInfo parse(final String id) {
        return id == null || id.trim().isEmpty() ? null : this.parse(Long.parseLong(id));
    }

}
