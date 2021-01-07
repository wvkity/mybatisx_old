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
package com.wvkity.mybatis.core.condition.expression.builder;

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * 抽象字段条件表达式构建器
 * @param <T> 条件表达式类
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public abstract class AbstractColumnExprBuilder<T> implements ExprBuilder<T> {

    /**
     * {@link Criteria}
     */
    protected Criteria<?> criteria;
    /**
     * {@link Column}
     */
    protected Column column;
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

    /**
     * 获取真实{@link Column}对象
     * @return {@link Column}对象
     */
    public Column getRealColumn() {
        if (Objects.nonNull(this.column)) {
            return this.column;
        } else if (Objects.nonNull(this.lambdaProperty)) {
            return this.criteria.findColumn(this.lambdaProperty);
        } else if (Objects.isNotBlank(this.property)) {
            return this.criteria.findColumn(this.property);
        }
        return null;
    }

    public AbstractColumnExprBuilder<T> criteria(Criteria<?> criteria) {
        this.criteria = criteria;
        return this;
    }

    public AbstractColumnExprBuilder<T> column(Column column) {
        this.column = column;
        return this;
    }

    public AbstractColumnExprBuilder<T> property(Property<?, ?> lambdaProperty) {
        this.lambdaProperty = lambdaProperty;
        return this;
    }

    public AbstractColumnExprBuilder<T> property(String property) {
        this.property = property;
        return this;
    }

    public AbstractColumnExprBuilder<T> slot(Slot slot) {
        this.slot = slot;
        return this;
    }

    public AbstractColumnExprBuilder<T> value(Object value) {
        this.value = value;
        return this;
    }

}
