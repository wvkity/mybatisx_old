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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.utils.Objects;

import java.util.function.Consumer;

/**
 * 更新条件
 * @param <T> 泛型类
 * @author wvkity
 * @created 2021-03-21
 * @since 1.0.0
 */
public class Update<T> extends AbstractUpdateCriteria<T> {

    private static final long serialVersionUID = -5656859888218457055L;

    public Update(final Class<T> entityClass) {
        this.entityClass = entityClass;
        this.initialize(null);
    }

    @Override
    protected Update<T> newInstance() {
        final Update<T> instance = new Update<>(this.entityClass);
        instance.clone(this);
        return instance;
    }

    // region Static methods

    /**
     * 创建{@link Update}对象
     * @param entityClass 实体类
     * @param <T>         泛型类型
     * @return {@link Update}
     */
    public static <T> Update<T> from(final Class<T> entityClass) {
        return new Update<>(entityClass);
    }

    /**
     * 创建{@link Update}对象
     * @param entityClass 实体类
     * @param <T>         泛型类型
     * @return {@link Update}
     */
    public static <T> Update<T> from(final Class<T> entityClass, final Consumer<Update<T>> action) {
        final Update<T> update = new Update<>(entityClass);
        if (Objects.nonNull(action)) {
            action.accept(update);
        }
        return update;
    }

    /**
     * 根据实例创建{@link Update}对象
     * @param instance 实例
     * @param <T>      实例类型
     * @return {@link Query}
     */
    @SuppressWarnings("unchecked")
    public static <T> Update<T> from(final T instance) {
        if (Objects.isNull(instance)) {
            throw new NullPointerException("The instance object cannot be null.");
        }
        return Update.from((Class<T>) instance.getClass());
    }

    // endregion

}
