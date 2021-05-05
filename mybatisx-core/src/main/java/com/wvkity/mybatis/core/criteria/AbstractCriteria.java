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
package com.wvkity.mybatis.core.criteria;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 抽象条件
 * @param <T> 泛型类
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractCriteria<T> extends AbstractBasicCriteria<T, AbstractCriteria<T>> implements
    SubQueryWrapper<T, AbstractCriteria<T>> {

    protected final Set<AbstractSubCriteria<?>> subQuerySet = new CopyOnWriteArraySet<>();

    @Override
    public <S> SubQuery<S> newQuery(Class<S> entity, String alias) {
        final SubQuery<S> instance = new SubQuery<>(this, entity, alias);
        this.subQueryCache(instance);
        return instance;
    }

    /**
     * 缓存子查询对象
     * @param subQuery {@link SubQuery}
     * @param <S>      实体类型
     */
    protected <S> void subQueryCache(final AbstractSubCriteria<S> subQuery) {
        this.subQuerySet.add(subQuery);
    }

}
