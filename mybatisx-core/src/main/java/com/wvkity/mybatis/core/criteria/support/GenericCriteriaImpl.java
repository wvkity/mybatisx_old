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
import com.wvkity.mybatis.core.criteria.sql.DefaultSqlManager;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 通用条件包装容器
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public class GenericCriteriaImpl<T> extends AbstractGenericCriteria<T, GenericCriteriaImpl<T>> {

    private static final long serialVersionUID = -9137144868955615461L;

    private GenericCriteriaImpl() {
    }

    public GenericCriteriaImpl(Class<T> entity) {
        this.entityClass = entity;
        this.initialize(null, Category.BASIC);
        this.sqlManager = new DefaultSqlManager(this, this.fragmentManager);
    }

    @Override
    protected GenericCriteriaImpl<T> newInstance() {
        final GenericCriteriaImpl<T> it = new GenericCriteriaImpl<>();
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link GenericCriteriaImpl}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link GenericCriteriaImpl}
     */
    public static <T> GenericCriteriaImpl<T> from(final Class<T> entity) {
        return from(entity, null);
    }

    /**
     * 创建{@link GenericCriteriaImpl}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link GenericCriteriaImpl}
     */
    public static <T> GenericCriteriaImpl<T> from(final Class<T> entity,
                                                  final Consumer<GenericCriteriaImpl<T>> action) {
        final GenericCriteriaImpl<T> it = new GenericCriteriaImpl<>(entity);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
