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
package com.github.mybatisx.plugin.auditable.cache;

import java.io.Serializable;

/**
 * 缓存数据
 * @param <T> 数据类型
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public interface CacheData<T> extends Serializable {

    /**
     * 获取数据
     * @return 数据
     */
    T getData();
}
