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
package io.github.mybatisx.core.criteria.update;

import io.github.mybatisx.core.criteria.Category;
import io.github.mybatisx.core.sql.DefaultUpdateSqlManager;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 基础条件/更新容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public class Update<T> extends AbstractCommonUpdateCriteria<T, Update<T>> {

    private static final long serialVersionUID = -1818263259702171624L;

    private Update() {
    }

    public Update(Class<T> entity) {
        this.entityClass = entity;
        this.initialize(null, Category.UPDATE);
        this.sqlManager = new DefaultUpdateSqlManager(this, this.parameterConverter, this.updateColumnsOfWrap,
            this.updateColumnsOfOrg, this.fragmentManager);
    }

    @Override
    protected Update<T> newInstance() {
        final Update<T> it = new Update<>();
        it.depClone(this);
        return this;
    }

    /**
     * 创建{@link Update}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link Update}
     */
    public static <T> Update<T> from(final Class<T> entity) {
        return from(entity, null);
    }

    /**
     * 创建{@link Update}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link Update}
     */
    public static <T> Update<T> from(final Class<T> entity, final Consumer<Update<T>> action) {
        final Update<T> it = new Update<>(entity);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

}
