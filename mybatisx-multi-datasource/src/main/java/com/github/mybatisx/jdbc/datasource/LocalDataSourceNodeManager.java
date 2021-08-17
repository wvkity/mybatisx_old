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

import java.util.LinkedList;

/**
 * 线程数据源节点
 * @author wvkity
 * @created 2021-08-17
 * @since 1.0.0
 */
public class LocalDataSourceNodeManager {

    /**
     * 第一个线程数据源
     */
    private LocalDataSource first;
    /**
     * 是否存在多个
     */
    private boolean more = false;
    /**
     * 标识是否已移除
     */
    private boolean clear = false;
    /**
     * 线程数据源节点
     */
    private LinkedList<LocalDataSource> nodes;

    /**
     * 添加数据源
     * @param local {@link LocalDataSource}
     */
    public void push(final LocalDataSource local) {
        if (Objects.nonNull(this.first)) {
            if (Objects.isNull(nodes)) {
                this.more = true;
                this.nodes = new LinkedList<>();
            }
            this.nodes.addFirst(local);
        } else {
            this.first = local;
        }
    }

    /**
     * 获取{@link LocalDataSource}
     * @return {@link LocalDataSource}
     */
    public LocalDataSource get() {
        if (this.more && Objects.isNotEmpty(this.nodes)) {
            return this.nodes.getFirst();
        }
        return this.first;
    }

    /**
     * 移除{@link LocalDataSource}
     */
    public void remove() {
        if (this.more && Objects.isNotEmpty(this.nodes)) {
            this.nodes.removeFirst();
        } else {
            this.first = null;
        }
    }

    /**
     * 清空
     */
    public void clear() {
        if (!this.clear) {
            this.first = null;
            if (Objects.nonNull(this.nodes)) {
                this.nodes.clear();
                this.nodes = null;
            }
            this.clear = true;
        }
    }

    /**
     * 是否可清空
     * @return boolean
     */
    public boolean canClear() {
        return Objects.isNull(this.first) && Objects.isEmpty(this.nodes);
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public boolean isClear() {
        return clear;
    }

    @Override
    public String toString() {
        return "LocalDataSourceNodeManager{" +
            "first=" + first +
            ", more=" + more +
            ", clear=" + clear +
            ", nodes=" + nodes +
            '}';
    }
}
