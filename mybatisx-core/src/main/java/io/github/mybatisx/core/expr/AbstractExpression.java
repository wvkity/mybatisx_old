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
package io.github.mybatisx.core.expr;

import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.expr.Expression;

/**
 * 抽象条件表达式
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public abstract class AbstractExpression<E> implements Expression<E> {

    /**
     * {@link Criteria}对象
     */
    protected Criteria<?> criteria;
    /**
     * 字段
     */
    protected E column;
    /**
     * 表别名
     */
    protected String alias;
    /**
     * 条件符号
     */
    protected Symbol symbol = Symbol.EQ;
    /**
     * 运算符
     */
    protected Slot slot = Slot.AND;
    /**
     * 表达式模式
     */
    protected Matched matched;

    @Override
    public Criteria<?> getCriteria() {
        return this.criteria;
    }

    @Override
    public E getColumn() {
        return this.column;
    }

    @Override
    public Slot getSlot() {
        return this.slot;
    }

    @Override
    public Symbol getSymbol() {
        return this.symbol;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public AbstractExpression<E> criteria(Criteria<?> criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public AbstractExpression<E> alias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public Matched getExprMatched() {
        return this.matched;
    }
}
