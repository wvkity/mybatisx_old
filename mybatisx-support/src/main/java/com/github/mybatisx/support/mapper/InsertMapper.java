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
package com.github.mybatisx.support.mapper;

import com.github.mybatisx.auditable.event.AuditedNotListener;
import com.github.mybatisx.constant.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 保存操作Mapper接口
 * @param <T> 实体类型
 * @author wvkity
 * @created 2020-10-02
 * @since 1.0.0
 */
public interface InsertMapper<T> {

    /**
     * 保存一条记录
     * @param entity 待保存记录
     * @return 受影响行数
     */
    @AuditedNotListener
    int insert(@Param(Constants.PARAM_ENTITY) final T entity);

    /**
     * 保存一条记录
     * <p>过滤空值字段</p>
     * @param entity 待保存记录
     * @return 受影响行数
     */
    int insertWithNonNull(@Param(Constants.PARAM_ENTITY) final T entity);
}
