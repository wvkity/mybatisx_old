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

import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;

/**
 * 抽象范围条件表达式构建器
 * @param <T> 条件表达式类型
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public abstract class AbstractRangeExprBuilder<T, E> extends AbstractExprBuilder<T, E> {

    /**
     * 多个值
     */
    protected Collection<Object> values;

    public AbstractRangeExprBuilder<T, E> values(final Object... values) {
        return values(Objects.asList(values));
    }

    public AbstractRangeExprBuilder<T, E> values(Collection<Object> values) {
        this.values = values;
        return this;
    }
}
