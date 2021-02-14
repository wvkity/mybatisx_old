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
package com.wvkity.mybatis.core.condition.basic.builder;

import com.wvkity.mybatis.core.condition.basic.select.Selection;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * 抽象查询列构建器
 * @param <T> {@link Selection}子类
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public abstract class AbstractColumnSelectBuilder<T extends Selection> extends AbstractSelectBuilder<T, Column> {
    /**
     * 属性
     */
    protected String property;
    /**
     * lambda类型属性
     */
    protected Property<?, ?> lambdaProperty;

    @Override
    public Column getColumn() {
        if (Objects.nonNull(this.column)) {
            return this.column;
        } else if (Objects.nonNull(this.lambdaProperty)) {
            return this.criteria.findColumn(this.lambdaProperty);
        } else if (Objects.isNotBlank(this.property)) {
            return this.criteria.findColumn(this.property);
        }
        return null;
    }

    public AbstractColumnSelectBuilder<T> property(String property) {
        this.property = property;
        return this;
    }

    public AbstractColumnSelectBuilder<T> property(Property<?, ?> property) {
        this.lambdaProperty = property;
        return this;
    }
}
