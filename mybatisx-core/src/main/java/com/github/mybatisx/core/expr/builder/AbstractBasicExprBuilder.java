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
package com.github.mybatisx.core.expr.builder;

/**
 * 抽象基本条件表达式构建器
 * @author wvkity
 * @created 2021-01-21
 * @since 1.0.0
 */
public abstract class AbstractBasicExprBuilder<T, E> extends AbstractExprBuilder<T, E> {

    /**
     * 值
     */
    protected Object value;

    public AbstractBasicExprBuilder<T, E> value(Object value) {
        this.value = value;
        return this;
    }
}
