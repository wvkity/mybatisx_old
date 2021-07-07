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
package com.github.mybatisx.plugin.paging.config;

import com.github.mybatisx.plugin.paging.RangePageable;

/**
 * 范围分页线程缓存
 * @author wvkity
 * @created 2021-02-09
 * @since 1.0.0
 */
public final class RangePageableThreadLocalCache {

    private RangePageableThreadLocalCache(){}
    private static final ThreadLocal<RangePageable> THREAD_LOCAL_RANGE_PAGEABLE = new ThreadLocal<>();

    public static RangePageable getPageable() {
        return THREAD_LOCAL_RANGE_PAGEABLE.get();
    }

    public static void set(final RangePageable pageable) {
        if (pageable == null) {
            RangePageableThreadLocalCache.remove();
        } else {
            THREAD_LOCAL_RANGE_PAGEABLE.set(pageable);
        }
    }

    public static void remove() {
        THREAD_LOCAL_RANGE_PAGEABLE.remove();
    }
}
