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
package io.github.mybatisx.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量数据包装器
 * @param <T> 泛型类
 * @author wvkity
 * @created 2021-02-22
 * @since 1.0.0
 */
public class BatchDataWrapper<T> implements Serializable {

    private static final long serialVersionUID = -8849912741334043802L;
    public static final int DEF_BATCH_SIZE = 500;
    public static final String PARAM_BATCH_DATA_WRAPPER = "batchDataWrapper";
    /**
     * 批量大小
     */
    private final int batchSize;
    /**
     * 数据
     */
    private final List<T> data;
    /**
     * 执行结果
     */
    private final List<Integer> affectedRows;

    private BatchDataWrapper(List<T> data) {
        this.data = data;
        this.batchSize = DEF_BATCH_SIZE;
        this.affectedRows = new ArrayList<>(data.size());
    }

    private BatchDataWrapper(int batchSize, List<T> data) {
        this.batchSize = batchSize;
        this.data = data;
        this.affectedRows = new ArrayList<>(data.size());
    }

    public int getBatchSize() {
        return batchSize;
    }

    public List<T> getData() {
        return data;
    }

    public List<Integer> getAffectedRows() {
        return affectedRows;
    }

    public void addRows(final int[] rows) {
        if (rows != null && rows.length > 0) {
            for (int v: rows) {
                this.affectedRows.add(v);
            }
        }
    }

    public int getRowCount() {
        return (int) this.affectedRows.stream().filter(it -> it != null && it > 0).count();
    }

    ///// Static methods /////

    public static <T> BatchDataWrapper<T> wrap(final List<T> data) {
        return wrap(DEF_BATCH_SIZE, data);
    }

    public static <T> BatchDataWrapper<T> wrap(final int batchSize, final List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("The data must not be empty.");
        }
        return new BatchDataWrapper<>(batchSize, data);
    }

}
