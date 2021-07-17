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
package com.github.mybatisx.basic.keygen;

import com.github.mybatisx.constant.Constants;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 主键生成器
 * @author wvkity
 * @created 2021-03-15
 * @since 1.0.0
 */
public interface KeyGenerator {

    /**
     * 获取唯一UUID
     * @return uuid
     */
    default String nextUuid() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace("-", Constants.EMPTY);
    }

    /**
     * 获取唯一ID
     * @return id
     */
    Object nextId();

    /**
     * 获取唯一ID
     * @return id
     */
    default String nextIdString() {
        return this.nextId().toString();
    }
}
