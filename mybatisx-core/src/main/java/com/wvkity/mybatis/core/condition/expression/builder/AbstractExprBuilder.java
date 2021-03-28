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
package com.wvkity.mybatis.core.condition.expression.builder;

import com.wvkity.mybatis.support.condition.criteria.Criteria;
import com.wvkity.mybatis.support.constant.Slot;

/**
 * 抽象表达式构建器
 * @param <T> 条件表达式类型
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-14
 * @since 1.0.0
 */
public abstract class AbstractExprBuilder<T, E> implements ExprBuilder<T, E> {

    /**
     * {@link Criteria}
     */
    protected Criteria<?> criteria;
    /**
     * 字段类型
     */
    protected E target;
    /**
     * {@link Slot}
     */
    protected Slot slot;

    public AbstractExprBuilder<T, E> criteria(Criteria<?> criteria) {
        this.criteria = criteria;
        return this;
    }

    public AbstractExprBuilder<T, E> target(E target) {
        this.target = target;
        return this;
    }

    public AbstractExprBuilder<T, E> slot(Slot slot) {
        this.slot = slot;
        return this;
    }
}
