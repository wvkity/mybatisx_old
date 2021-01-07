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

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;
import com.wvkity.mybatis.core.convert.converter.PlaceholderConverter;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;
import java.util.List;

/**
 * 抽象条件表达式
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractExpression<E> implements Expression, PlaceholderConverter {

    /**
     * {@link Criteria}对象
     */
    protected Criteria<?> criteria;
    /**
     * 表别名
     */
    protected String tableAlias;
    /**
     * 目标对象
     */
    protected E target;
    /**
     * 条件符号
     */
    protected Symbol symbol = Symbol.EQ;
    /**
     * 运算符
     */
    protected Slot slot = Slot.AND;
    /**
     * 值
     */
    protected Object value;

    @Override
    public String convert(String source, boolean format, Object... args) {
        return this.getCriteria().convert(source, format, args);
    }

    @Override
    public List<String> converts(String source, Collection<Object> args) {
        return this.getCriteria().converts(source, args);
    }

    @Override
    public Criteria<?> getCriteria() {
        return this.criteria;
    }

    @Override
    public AbstractExpression<E> setCriteria(Criteria<?> criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public Slot getSlot() {
        return this.slot;
    }

    @Override
    public String getAlias() {
        return Objects.isNotBlank(this.tableAlias) ? this.tableAlias : this.criteria != null ? this.criteria.as() : "";
    }

    @Override
    public AbstractExpression<E> alias(String alias) {
        this.tableAlias = alias;
        return this;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public AbstractExpression<E> setValue(Object value) {
        this.value = value;
        return this;
    }
}
