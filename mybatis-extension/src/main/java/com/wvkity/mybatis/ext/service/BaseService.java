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

import com.wvkity.mybatis.support.mapper.BaseMapper;

/**
 * 基础Service接口
 * @param <M>  Mapper接口
 * @param <T>  实体类型
 * @param <U>  返回值类型
 * @param <ID> 主键类型
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public interface BaseService<M extends BaseMapper<T, U, ID>, T, U, ID> extends SaveService<T>, UpdateService<T>,
    DeleteService<T, ID>, BatchService<T>, QueryService<T, U, ID> {

    /**
     * 获取泛型Mapper接口
     * @return {@link BaseMapper}
     */
    M getMapper();

    /**
     * 设置泛型Mapper接口
     * @param mapper {@link BaseMapper}
     */
    void setMapper(M mapper);
}
