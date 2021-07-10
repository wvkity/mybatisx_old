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
package com.github.mybatisx.core.mapper;

import com.github.mybatisx.batch.BatchDataWrapper;
import org.apache.ibatis.annotations.Param;

/**
 * 批量操作Mapper接口
 * @param <T> 泛型类型
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
public interface BatchMapper<T> {

    /**
     * 批量插入数据
     * @param wrapper {@link BatchDataWrapper}
     * @return 受影响行数
     */
    int insertBatch(@Param(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER) final BatchDataWrapper<T> wrapper);

}