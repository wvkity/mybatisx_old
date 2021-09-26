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
package io.github.mybatisx.jdbc.datasource;

import io.github.mybatisx.Objects;

import java.util.Optional;

/**
 * 读写数据源切换决策工具
 * @author wvkity
 * @created 2021-08-17
 * @since 1.0.0
 */
public class MultiDataSourceContextHolder {

    private static final ThreadLocal<LocalDataSourceNodeManager> DATA_SOURCE_CACHE = new ThreadLocal<>();

    /**
     * {@link Optional}
     * @return {@link Optional}
     */
    private static Optional<LocalDataSourceNodeManager> optional() {
        return Optional.ofNullable(DATA_SOURCE_CACHE.get());
    }

    /**
     * 检查当前线程是否存在节点
     * @return boolean
     */
    public static boolean nonNull() {
        return Objects.nonNull(DATA_SOURCE_CACHE.get());
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
            if (optional().isPresent()) {
                optional().get().push(local);
            } else {
                final LocalDataSourceNodeManager manager = new LocalDataSourceNodeManager();
                manager.push(local);
                DATA_SOURCE_CACHE.set(manager);
            }
        }
    }

    /**
     * 获取{@link LocalDataSource}
     * @return {@link LocalDataSource}
     */
    public static LocalDataSource get() {
        return optional().map(LocalDataSourceNodeManager::get).orElse(null);
    }

    /**
     * 移除当前线程数据源
     */
    public static void remove() {
        optional().ifPresent(it -> {
            it.remove();
            if (it.canClear()) {
                clear();
            }
        });
    }

    /**
     * 清空本地线程
     */
    public static void clear() {
        optional().ifPresent(LocalDataSourceNodeManager::clear);
        DATA_SOURCE_CACHE.remove();
    }
}
