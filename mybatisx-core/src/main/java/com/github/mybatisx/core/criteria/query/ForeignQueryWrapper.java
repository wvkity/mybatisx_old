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
package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.core.criteria.ExtCriteria;

/**
 * 联表查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-19
 * @since 1.0.0
 */
public interface ForeignQueryWrapper<T, C extends ForeignQueryWrapper<T, C>> extends QCriteria<T, C> {

    /**
     * 获取主条件对象
     * @return {@link ExtCriteria}
     */
    ExtCriteria<?> getMaster();

    /**
     * 抓取联表查询字段
     * @return {@code this}
     */
    default C fetch() {
        return this.fetch(true);
    }

    /**
     * 设置是否抓取联表查询字段
     * @param fetch 是否抓取
     * @return {@code this}
     */
    C fetch(final boolean fetch);

    /**
     * 加入容器
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C join() {
        ((QCriteria<T, C>) this.getMaster()).foreign(this);
        return (C) this;
    }

}
