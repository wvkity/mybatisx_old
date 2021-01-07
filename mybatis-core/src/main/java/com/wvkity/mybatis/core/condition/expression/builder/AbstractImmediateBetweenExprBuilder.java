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

/**
 * 抽象Between范围条件表达式构建器
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public abstract class AbstractImmediateBetweenExprBuilder<T> extends AbstractImmediateExprBuilder<T> {

    /**
     * 开始值
     */
    protected Object begin;

    /**
     * 结束值
     */
    protected Object end;

    public AbstractImmediateBetweenExprBuilder<T> begin(Object begin) {
        this.begin = begin;
        return this;
    }

    public AbstractImmediateBetweenExprBuilder<T> end(Object end) {
        this.end = end;
        return this;
    }
}
