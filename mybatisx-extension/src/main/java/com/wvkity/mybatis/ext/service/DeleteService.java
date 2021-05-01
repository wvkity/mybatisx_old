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

import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 删除操作Service接口
 * @param <T>  实体类型
 * @param <ID> 主键类型
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public interface DeleteService<T, ID> {

    /**
     * 根据指定对象删除记录
     * @param entity 指定对象
     * @return 受影响行数
     */
    int delete(final T entity);

    /**
     * 根据ID删除记录
     * @param id ID
     * @return 受影响行数
     */
    int deleteById(final ID id);

    /**
     * 逻辑删除
     * @param entity 指定对象
     * @return 受影响行数
     */
    int deleteWithLogic(final T entity);

    /**
     * 逻辑删除
     * @param criteria 条件包装对象
     * @return 受影响行数
     */
    int deleteWithLogic(final Criteria<T> criteria);

}
