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
package com.wvkity.mybatis.support.mapper;

import com.wvkity.mybatis.basic.constant.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 更新操作Mapper接口
 * @param <T> 实体类型
 * @author wvkity
 * @created 2020-10-02
 * @since 1.0.0
 */
public interface UpdateMapper<T> {

    /**
     * 更新记录
     * <p>排除逻辑删除标识、逻辑删除审计标识、保存审计标识、多租户标识等字段</p>
     * @param entity 待更新记录
     * @return 受影响行数
     */
    int update(@Param(Constants.PARAM_ENTITY) final T entity);

    /**
     * 更新记录(排除空值)
     * <p>排除逻辑删除标识、逻辑删除审计标识、保存审计标识、多租户标识等字段</p>
     * @param entity 待更新记录
     * @return 受影响行数
     */
    int updateWithNonNull(@Param(Constants.PARAM_ENTITY) final T entity);
}
