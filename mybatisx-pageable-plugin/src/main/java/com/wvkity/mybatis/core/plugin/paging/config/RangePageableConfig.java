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
package com.wvkity.mybatis.core.plugin.paging.config;

import com.wvkity.mybatis.core.plugin.paging.RangeFetch;
import com.wvkity.mybatis.core.plugin.paging.RangeMode;
import com.wvkity.mybatis.core.plugin.paging.RangePageable;
import com.wvkity.mybatis.core.plugin.utils.PluginUtil;

/**
 * 范围分页配置
 * @author wvkity
 * @created 2021-02-09
 * @since 1.0.0
 */
public class RangePageableConfig {

    /**
     * 获取范围分页对象
     * @param parameter 方法参数
     * @return {@link RangePageable}
     */
    public RangePageable getPageable(final Object parameter) {
        final RangePageable cache = RangePageableThreadLocalCache.getPageable();
        if (cache != null) {
            return cache;
        }
        final RangeFetch fetch = PluginUtil.getParameter(parameter, RangeFetch.PARAMETER_KEY, RangeFetch.class);
        if (fetch != null) {
            final RangeMode mode = fetch.getMode();
            long rowStart, rowEnd, offset;
            if (mode == RangeMode.SCOPE) {
                rowStart = Math.max(fetch.getRowStart() - 1, 0L);
                rowEnd = Math.max(fetch.getRowEnd(), 0L);
                offset = rowEnd - rowStart;
                final RangePageable pageable = new RangePageable(rowStart, rowEnd, offset, true);
                RangePageableThreadLocalCache.set(pageable);
                return pageable;
            } else if (mode == RangeMode.PAGEABLE) {
                final long pageStart = fetch.getPageStart();
                final long pageEnd = fetch.getPageEnd();
                final long pageSize = fetch.getPageSize();
                rowStart = Math.max((pageStart - 1) * pageSize, 0L);
                rowEnd = Math.max(rowStart + (pageEnd - pageStart + 1) * pageSize, 0L);
                offset = rowEnd - rowStart;
                final RangePageable pageable = new RangePageable(rowStart, rowEnd, offset, true);
                RangePageableThreadLocalCache.set(pageable);
                return pageable;
            }
        }
        return null;
    }
}
