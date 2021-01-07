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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;

/**
 * 抽象Between范围条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public abstract class AbstractBetweenExpression extends AbstractColumnExpression {

    private static final long serialVersionUID = -6327410462652114685L;

    /**
     * 开始值
     */
    protected Object begin;

    /**
     * 结束值
     */
    protected Object end;

    @Override
    public String getSegment() {
        return Scripts.convertToConditionArg(this.symbol, this.slot, this.getAlias(), this.target,
            this.defPlaceholder(this.begin), this.defPlaceholder(this.end));
    }

    public AbstractBetweenExpression begin(Object begin) {
        this.begin = begin;
        return this;
    }

    public AbstractBetweenExpression end(Object end) {
        this.end = end;
        return this;
    }
}
