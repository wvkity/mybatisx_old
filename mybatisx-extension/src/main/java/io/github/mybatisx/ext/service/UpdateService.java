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
package io.github.mybatisx.ext.service;

import io.github.mybatisx.support.criteria.Criteria;

/**
 * 更新操作接口
 * @param <T> 实体类型
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public interface UpdateService<T> {

    /**
     * 根据ID、乐观锁、逻辑删除标识[+多租户标识]更新记录
     * <p>排除逻辑删除标识、保存审计标识、多租户标识等字段
     * @param entity 待更新记录
     * @return 受影响行数
     */
    int update(final T entity);

    /**
     * 根据ID、乐观锁、逻辑删除标识[+多租户标识]更新记录
     * <p>排除逻辑删除标识、保存审计标识、多租户标识等字段
     * @param entity 待更新记录
     * @return 受影响行数
     */
    int updateWithoutNull( final T entity);

    /**
     * 更新记录
     * @param criteria {@link Criteria}对象
     * @return 受影响行数
     */
    int update(final Criteria<T> criteria);

    /**
     * 更新记录(排除空值)
     * <p>排除逻辑删除标识、保存审计标识、多租户标识等字段
     * @param entity   待更新记录
     * @param criteria {@link Criteria}
     * @return 受影响行数
     */
    int updateMixed(final T entity, final Criteria<T> criteria);

    /**
     * 根据ID[+多租户标识]更新记录
     * @param entity 待更新记录
     * @return 受影响行数
     */
    int updateWithSpecial(final T entity);

    /**
     * 根据ID[+多租户标识]更新记录(排除空值)
     * @param entity 待更新记录
     * @return 受影响行数
     */
    int updateWithSpecialExcNull( final T entity);

    /**
     * 更新记录
     * @param entity   待更新记录
     * @param criteria {@link Criteria}
     * @return 受影响行数
     */
    int updateMixedWithSpecial(final T entity, final Criteria<T> criteria);


}
