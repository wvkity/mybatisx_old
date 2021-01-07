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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 查询操作Service接口
 * @param <T>  实体类型
 * @param <U>  返回值类型
 * @param <ID> 主键类型
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public interface QueryService<T, U, ID> {

    /**
     * 根据主键检查记录是否存在
     * @param id 主键值
     * @return boolean
     */
    boolean existsById(final ID id);

    /**
     * 根据指定对象查询记录是否存在
     * @param entity 实体对象
     * @return boolean
     */
    boolean exists(final T entity);

    /**
     * 根据指定对象查询记录数
     * @param entity 实体对象
     * @return 记录数
     */
    long selectCount(final T entity);

    /**
     * 查询总记录数
     * @return 总记录数
     */
    long selectTotal();

    /**
     * 根据指定对象(主键&租户条件)查询记录
     * @param entity 实体对象
     * @return 一条记录
     */
    Optional<U> selectOne(final T entity);

    /**
     * 根据主键查询记录
     * @param ids 主键集合
     * @return 多条记录
     */
    @SuppressWarnings("unchecked")
    List<U> selectList(final ID... ids);

    /**
     * 根据主键查询记录
     * @param ids 主键集合
     * @return 多条记录
     */
    List<U> selectList(final Collection<ID> ids);

    /**
     * 根据指定对象查询记录
     * @param entity 实体对象
     * @return 多条记录
     */
    List<U> selectList(final T entity);

    /**
     * 查询所有记录
     * @return 所有记录
     */
    List<U> selectAll();

}
