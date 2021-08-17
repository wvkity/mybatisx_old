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
package com.github.mybatisx.jdbc.datasource;

import com.github.mybatisx.Objects;

/**
 * 读写数据源切换决策工具
 * @author wvkity
 * @created 2021-08-17
 * @since 1.0.0
 */
public class MultiDataSourceContextHolder {

    private static final ThreadLocal<LocalDataSourceNodeManager> DATA_SOURCE_CACHE =
        ThreadLocal.withInitial(LocalDataSourceNodeManager::new);

    /**
     * 获取{@link LocalDataSourceNodeManager}
     * @return {@link LocalDataSourceNodeManager}
     */
    public static LocalDataSourceNodeManager getLocal() {
        return DATA_SOURCE_CACHE.get();
    }

    /**
     * 是否选择读库
     * @return boolean
     */
    public static boolean isChoiceRead() {
        final LocalDataSource it = MultiDataSourceContextHolder.get();
        return Objects.nonNull(it) && it.isRead();
    }

    /**
     * 是否选择主库
     * @return boolean
     */
    public static boolean isChoiceWrite() {
        final LocalDataSource it = MultiDataSourceContextHolder.get();
        return Objects.nonNull(it) && it.isWrite();
    }

    /**
     * 添加{@link LocalDataSource}
     * @param local {@link LocalDataSource}
     */
    public static void push(final LocalDataSource local) {
        if (Objects.nonNull(local)) {
            final LocalDataSourceNodeManager manager = DATA_SOURCE_CACHE.get();
            manager.push(local);
        }
    }

    /**
     * 添加{@link LocalDataSource}
     * @return {@link LocalDataSource}
     */
    public static LocalDataSource get() {
        return DATA_SOURCE_CACHE.get().get();
    }

    /**
     * 移除当前线程数据源
     */
    public static void remove() {
        final LocalDataSourceNodeManager manager = DATA_SOURCE_CACHE.get();
        manager.remove();
        if (manager.canClear()) {
            clear();
        }
    }

    /**
     * 清空本地线程
     */
    public static void clear() {
        DATA_SOURCE_CACHE.get().clear();
        DATA_SOURCE_CACHE.remove();
    }
}
