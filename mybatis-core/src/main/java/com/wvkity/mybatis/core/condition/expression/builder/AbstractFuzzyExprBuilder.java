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

import com.wvkity.mybatis.core.constant.LikeMode;

/**
 * 抽象模糊匹配条件表达式构建器
 * @param <T> 条件表达式类型
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-08
 * @since 1.0.0
 */
public abstract class AbstractFuzzyExprBuilder<T, E> extends AbstractExprBuilder<T, E> {
    /**
     * 匹配模式
     */
    protected LikeMode mode;
    /**
     * 转义字符
     */
    protected Character escape;
    /**
     * 值
     */
    protected Object value;

    public AbstractFuzzyExprBuilder<T, E> mode(LikeMode mode) {
        this.mode = mode;
        return this;
    }

    public AbstractFuzzyExprBuilder<T, E> escape(Character escape) {
        this.escape = escape;
        return this;
    }

    public AbstractFuzzyExprBuilder<T, E> value(Object value) {
        this.value = value;
        return this;
    }
}
