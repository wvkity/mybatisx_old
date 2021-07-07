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
package com.github.mybatisx.spring.jdbc.datasource.strategy;

import com.github.mybatisx.spring.jdbc.datasource.ReadWriteDataSourceContextHolder;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 均匀分配数据源策略
 * @author wvkity
 * @created 2020-11-21
 * @since 1.0.0
 */
public class BalanceDataSourceStrategy implements DataSourceStrategy {

    /**
     * 写数据源计数器
     */
    private final AtomicInteger writeCounter = new AtomicInteger(1);
    /**
     * 读数据源计数器
     */
    private final AtomicInteger readCounter = new AtomicInteger(1);

    @Override
    public DataSource determineDataSource(List<DataSource> dataSources) {
        if (!CollectionUtils.isEmpty(dataSources)) {
            final int size = dataSources.size();
            if (size == 1) {
                return dataSources.get(0);
            }
            return dataSources.get(Math.abs(getIndex() % size));
        }
        return null;
    }

    /**
     * 获取数据源索引
     * @return 索引值
     */
    private int getIndex() {
        final int index;
        if (ReadWriteDataSourceContextHolder.isRead()) {
            index = this.readCounter.incrementAndGet();
            if (index > MAX_VALUE) {
                this.readCounter.set(1);
            }
        } else {
            index = this.writeCounter.incrementAndGet();
            if (index > MAX_VALUE) {
                this.writeCounter.set(1);
            }
        }
        return index;
    }
}
