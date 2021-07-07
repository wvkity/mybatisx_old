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
package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.core.criteria.Category;
import com.github.mybatisx.core.criteria.sql.DefaultSqlManager;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 基础条件包装容器(支持lambda语法)
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public class LambdaCriteriaImpl<T> extends AbstractLambdaCriteria<T, LambdaCriteriaImpl<T>> {

    private static final long serialVersionUID = 3035749468537708425L;

    private LambdaCriteriaImpl() {
    }

    public LambdaCriteriaImpl(final Class<T> entity) {
        this.entityClass = entity;
        this.initialize(null, Category.BASIC);
        this.sqlManager = new DefaultSqlManager(this, this.fragmentManager);
    }

    @Override
    protected LambdaCriteriaImpl<T> newInstance() {
        final LambdaCriteriaImpl<T> it = new LambdaCriteriaImpl<>();
        it.depClone(this);
        return it;
    }

    /**
     * 创建{@link LambdaCriteriaImpl}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link LambdaCriteriaImpl}
     */
    public static <T> LambdaCriteriaImpl<T> from(final Class<T> entity) {
        return from(entity, null);
    }

    /**
     * 创建{@link LambdaCriteriaImpl}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link LambdaCriteriaImpl}
     */
    public static <T> LambdaCriteriaImpl<T> from(final Class<T> entity,
                                                 final Consumer<LambdaCriteriaImpl<T>> action) {
        final LambdaCriteriaImpl<T> it = new LambdaCriteriaImpl<>(entity);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
