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
package com.github.mybatisx.core.criteria.update;

import com.github.mybatisx.core.criteria.CriteriaWrapper;

/**
 * 更新接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface UCriteria<T, C extends UCriteria<T, C>> extends CriteriaWrapper<T, C> {

    /**
     * 获取乐观锁更新值
     * @return 值
     */
    Object getVersionUpdateValue();

    /**
     * 设置乐观锁值
     * @param value 乐观锁值
     * @return {@code this}
     */
    C version(final Object value);

    /**
     * 获取更新语句片段
     * @return 更新SQL片段
     */
    String getUpdateSegment();

}