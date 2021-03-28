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

import com.wvkity.mybatis.core.plugin.paging.PageableUtil;
import com.wvkity.mybatis.core.plugin.paging.dialect.Dialect;
import com.wvkity.mybatis.core.plugin.utils.Objects;
import com.wvkity.paging.Pageable;
import com.wvkity.paging.StandardPageable;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * 分页配置
 * @author wvkity
 * @created 2021-02-09
 * @since 1.0.0
 */
public class StandardPageableConfig {

    /**
     * {@link org.apache.ibatis.session.RowBounds RowBounds}参数offset是否作为Pages使用
     */
    private boolean offsetAsPage = false;

    /**
     * 获取分页对象
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @return {@link Pageable}
     */
    @SuppressWarnings("unchecked")
    public Pageable getPageable(final Object parameter, final RowBounds rb) {
        final Pageable cache = StandardPageableThreadLocalCache.getPageable();
        if (cache != null) {
            return cache;
        }
        try {
            final Pageable pageable;
            if (parameter instanceof Pageable) {
                pageable = (Pageable) parameter;
            } else if (parameter instanceof Map) {
                final Map<String, Object> paramMap = (Map<String, Object>) parameter;
                final boolean isMatch = paramMap.containsKey(Dialect.DEF_PAGEABLE_SUFFIX)
                    && Optional.ofNullable(paramMap.get(Dialect.DEF_PAGEABLE_SUFFIX)).map(Object::getClass)
                    .map(Pageable.class::isAssignableFrom).orElse(false);
                if (isMatch) {
                    pageable = (Pageable) paramMap.get(Dialect.DEF_PAGEABLE_SUFFIX);
                } else {
                    pageable = (Pageable) paramMap.values().stream().filter(java.util.Objects::nonNull)
                        .filter(it -> Pageable.class.isAssignableFrom(it.getClass())).findFirst().orElse(null);
                }
            } else {
                pageable = PageableUtil.getPageable(parameter);
            }
            if (pageable != null) {
                StandardPageableThreadLocalCache.set(pageable);
                return pageable;
            } else if (this.offsetAsPage && rb != RowBounds.DEFAULT) {
                final long offset = rb.getOffset();
                final long size = rb.getLimit();
                final long page = size != 0 ? (long) Math.ceil(((double) (offset + size)) / size) : 0;
                final Pageable _pageable = StandardPageable.of(page, size);
                StandardPageableThreadLocalCache.set(_pageable);
                return _pageable;
            }
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }

    /**
     * 设置配置项
     * @param properties 配置项
     */
    public void setProperties(final Properties properties) {
        this.offsetAsPage = Objects.toBool(properties.getProperty("offsetAsPage"));
    }

    public boolean isOffsetAsPage() {
        return offsetAsPage;
    }
}
