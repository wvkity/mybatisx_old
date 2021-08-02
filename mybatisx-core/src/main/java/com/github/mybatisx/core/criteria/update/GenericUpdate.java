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
package com.github.mybatisx.core.criteria.update;

import com.github.mybatisx.core.criteria.Category;
import com.github.mybatisx.core.sql.DefaultUpdateSqlManager;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 通用条件/更新容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public class GenericUpdate<T> extends AbstractGenericUpdateCriteria<T, GenericUpdate<T>> {

    private static final long serialVersionUID = 7323717191251632130L;

    private GenericUpdate() {
    }

    public GenericUpdate(Class<T> entity) {
        this.entityClass = entity;
        this.initialize(null, Category.UPDATE);
        this.sqlManager = new DefaultUpdateSqlManager(this, this.parameterConverter, this.updateColumnsOfWrap,
            this.updateColumnsOfOrg, this.fragmentManager);
    }

    @Override
    protected GenericUpdate<T> newInstance() {
        final GenericUpdate<T> it = new GenericUpdate<>();
        it.depClone(this);
        return this;
    }

    /**
     * 创建{@link GenericUpdate}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link GenericUpdate}
     */
    public static <T> GenericUpdate<T> from(final Class<T> entity) {
        return from(entity, null);
    }

    /**
     * 创建{@link GenericUpdate}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link GenericUpdate}
     */
    public static <T> GenericUpdate<T> from(final Class<T> entity, final Consumer<GenericUpdate<T>> action) {
        final GenericUpdate<T> it = new GenericUpdate<>(entity);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }
}
