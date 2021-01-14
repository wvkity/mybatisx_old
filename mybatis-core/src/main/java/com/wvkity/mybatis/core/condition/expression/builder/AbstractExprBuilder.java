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

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

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
     * 字段对象
     */
    protected E column;
    /**
     * 属性
     */
    protected Property<?, ?> lambdaProperty;
    /**
     * 属性
     */
    protected String property;
    /**
     * {@link Slot}
     */
    protected Slot slot;
    /**
     * 值
     */
    protected Object value;

    @Override
    @SuppressWarnings("unchecked")
    public E getRealColumn() {
        if (this.column instanceof String) {
            return this.column;
        } else {
            if (this.column instanceof Column) {
                return this.column;
            } else {
                if (Objects.nonNull(this.lambdaProperty)) {
                    return (E) this.criteria.findColumn(this.lambdaProperty);
                } else if (Objects.isNotBlank(this.property)) {
                    return (E) this.criteria.findColumn(this.property);
                }
            }
        }
        return null;
    }

    public AbstractExprBuilder<T, E> criteria(Criteria<?> criteria) {
        this.criteria = criteria;
        return this;
    }

    public AbstractExprBuilder<T, E> column(E column) {
        this.column = column;
        return this;
    }

    public AbstractExprBuilder<T, E> property(Property<?, ?> lambdaProperty) {
        this.lambdaProperty = lambdaProperty;
        return this;
    }

    public AbstractExprBuilder<T, E> property(String property) {
        this.property = property;
        return this;
    }

    public AbstractExprBuilder<T, E> slot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public AbstractExprBuilder<T, E> value(Object value) {
        this.value = value;
        return this;
    }
}
