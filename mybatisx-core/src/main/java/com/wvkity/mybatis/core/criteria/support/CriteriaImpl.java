/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.core.criteria.Category;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 基础条件包装容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public class CriteriaImpl<T> extends AbstractCommonCriteria<T, CriteriaImpl<T>> {

    private static final long serialVersionUID = -8805733440783034715L;

    private CriteriaImpl() {
    }

    public CriteriaImpl(final Class<T> entity) {
        this.entityClass = entity;
        this.initialize(null, Category.BASIC);
    }

    @Override
    protected CriteriaImpl<T> newInstance() {
        final CriteriaImpl<T> it = new CriteriaImpl<>();
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link CriteriaImpl}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link CriteriaImpl}
     */
    public static <T> CriteriaImpl<T> from(final Class<T> entity) {
        return from(entity, null);
    }

    /**
     * 创建{@link CriteriaImpl}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link CriteriaImpl}
     */
    public static <T> CriteriaImpl<T> from(final Class<T> entity, final Consumer<CriteriaImpl<T>> action) {
        final CriteriaImpl<T> it = new CriteriaImpl<>(entity);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
