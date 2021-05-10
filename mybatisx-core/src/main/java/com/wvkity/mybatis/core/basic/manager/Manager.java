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
package com.wvkity.mybatis.core.basic.manager;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.condition.Criterion;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.fragment.Fragment;

import java.util.Collection;
import java.util.List;

/**
 * 片段管理器接口
 * @param <C> 条件类型
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public interface Manager<C extends Criteria<?>> extends Fragment {

    /**
     * 添加条件
     * @param condition 条件
     * @return {@link Manager}
     */
    Manager<C> where(final Criterion condition);

    /**
     * 添加多个条件
     * @param conditions 条件列表
     * @return {@link Manager}
     */
    default Manager<C> where(final Criterion... conditions) {
        return this.where(Objects.asList(conditions));
    }

    /**
     * 添加多个条件
     * @param conditions 条件列表
     * @return {@link Manager}
     */
    Manager<C> where(final Collection<Criterion> conditions);

    /**
     * 获取所有条件
     * @return 条件列表
     */
    List<Criterion> getConditions();

    /**
     * 是否存在条件
     * @return boolean
     */
    boolean hasCondition();

    /**
     * 是否存在片段
     * @return boolean
     */
    default boolean hasSegment() {
        return this.hasCondition();
    }

    /**
     * 获取SQL条件语句
     * @return SQL条件语句
     */
    String getWhereString();
}
