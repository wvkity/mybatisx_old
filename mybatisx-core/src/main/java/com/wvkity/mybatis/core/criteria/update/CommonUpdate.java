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
package com.wvkity.mybatis.core.criteria.update;

import com.wvkity.mybatis.core.criteria.Category;
import com.wvkity.mybatis.core.criteria.sql.DefaultUpdateSqlManager;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 基础条件/更新容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public class CommonUpdate<T> extends AbstractCommonUpdateCriteria<T, CommonUpdate<T>> {

    private static final long serialVersionUID = -1818263259702171624L;

    private CommonUpdate() {
    }

    public CommonUpdate(Class<T> entity) {
        this.entityClass = entity;
        this.initialize(null, Category.UPDATE);
        this.sqlManager = new DefaultUpdateSqlManager(this, this.parameterConverter, this.updateColumnsOfWrap,
            this.updateColumnsOfOrg, this.fragmentManager);
    }

    @Override
    protected CommonUpdate<T> newInstance() {
        final CommonUpdate<T> it = new CommonUpdate<>();
        it.depClone(this);
        return this;
    }

    /**
     * 创建{@link CommonUpdate}
     * @param entity 实体类
     * @param <T>    实体类型
     * @return {@link CommonUpdate}
     */
    public static <T> CommonUpdate<T> from(final Class<T> entity) {
        return from(entity, null);
    }

    /**
     * 创建{@link CommonUpdate}
     * @param entity 实体类
     * @param action {@link Consumer}
     * @param <T>    实体类型
     * @return {@link CommonUpdate}
     */
    public static <T> CommonUpdate<T> from(final Class<T> entity, final Consumer<CommonUpdate<T>> action) {
        final CommonUpdate<T> it = new CommonUpdate<>(entity);
        if (Objects.nonNull(action)) {
            action.accept(it);
        }
        return it;
    }

}
