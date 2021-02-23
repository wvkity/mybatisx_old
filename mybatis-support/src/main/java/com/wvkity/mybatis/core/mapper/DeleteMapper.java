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
package com.wvkity.mybatis.core.mapper;

import com.wvkity.mybatis.core.constant.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 删除操作Mapper接口
 * @param <T>  实体类型
 * @param <ID> 主键类型
 * @author wvkity
 * @created 2020-10-02
 * @since 1.0.0
 */
public interface DeleteMapper<T, ID> {

    /**
     * 根据指定对象删除记录
     * @param entity 指定对象
     * @return 受影响行数
     */
    int delete(@Param(Constants.PARAM_ENTITY) T entity);

    /**
     * 根据ID删除记录
     * @param id ID
     * @return 受影响行数
     */
    int deleteById(@Param(Constants.PARAM_ID) ID id);
}
