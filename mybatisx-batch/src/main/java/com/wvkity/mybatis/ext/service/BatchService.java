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
package com.wvkity.mybatis.ext.service;

import com.wvkity.mybatis.core.batch.BatchDataWrapper;

import java.util.List;

/**
 * 批量操作Service接口
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
public interface BatchService<T> {

    /**
     * 批量保存记录
     * @param entities 记录列表
     * @return 受影响行数
     */
    int saveBatch(final List<T> entities);

    /**
     * 批量保存记录
     * @param batchSize 批量大小
     * @param entities  记录列表
     * @return 受影响行数
     */
    int saveBatch(final int batchSize, final List<T> entities);

    /**
     * 批量保存数据
     * @param wrapper {@link BatchDataWrapper}
     * @return 受影响行数
     */
    int saveBatch(final BatchDataWrapper<T> wrapper);

}
