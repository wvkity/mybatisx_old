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
package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.Objects;
import com.github.mybatisx.PlaceholderPattern;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.core.criteria.AbstractCriteria;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.expr.ExistsExpression;
import com.github.mybatisx.core.expr.NativeExpression;
import com.github.mybatisx.core.expr.SpecialExpression;
import com.github.mybatisx.core.expr.StdBetween;
import com.github.mybatisx.core.expr.StdEqual;
import com.github.mybatisx.core.expr.StdGreaterThan;
import com.github.mybatisx.core.expr.StdGreaterThanOrEqual;
import com.github.mybatisx.core.expr.StdIn;
import com.github.mybatisx.core.expr.StdLessThan;
import com.github.mybatisx.core.expr.StdLessThanOrEqual;
import com.github.mybatisx.core.expr.StdLike;
import com.github.mybatisx.core.expr.StdNotBetween;
import com.github.mybatisx.core.expr.StdNotEqual;
import com.github.mybatisx.core.expr.StdNotIn;
import com.github.mybatisx.core.expr.StdNotLike;
import com.github.mybatisx.core.expr.StdNotNull;
import com.github.mybatisx.core.expr.StdNull;
import com.github.mybatisx.core.expr.StdTemplate;
import com.github.mybatisx.support.constant.Like;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.helper.TableHelper;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 抽象基础条件容器(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractLambdaCriteria<T, C extends LambdaCriteriaWrapper<T, C>> extends
    AbstractCriteria<T, C> implements LambdaCriteriaWrapper<T, C> {

    // region Compare conditions

    @Override
    public <V> C idEq(Slot slot, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            this.where(new StdEqual(this, this.id(), slot, value));
        }
        return this.self();
    }

    @Override
    public <V> C eq(Slot slot, String property, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            final Column column;
            if (Objects.nonNull((column = this.toColumn(property)))) {
                this.where(new StdEqual(this, column, slot, value));
            }
        }
        return this.self();
    }

    @Override
    public C eqq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.EQ);
    }

    @Override
    public C eq(Slot slot, Map<String, Object> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                final String property = entry.getKey();
                if (Objects.isNotBlank(property)) {
                    this.eq(slot, property, entry.getValue());
                }
            }
        }
        return this.self();
    }

    @Override
    public <V> C ne(Slot slot, String property, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            final Column column;
            if (Objects.nonNull((column = this.toColumn(property)))) {
                this.where(new StdNotEqual(this, column, slot, value));
            }
        }
        return this.self();
    }

    @Override
    public C neq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.NE);
    }

    @Override
    public <V> C gt(Slot slot, String property, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            final Column column;
            if (Objects.nonNull((column = this.toColumn(property)))) {
                this.where(new StdGreaterThan(this, column, slot, value));
            }
        }
        return this.self();
    }

    @Override
    public C gtq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.GT);
    }

    @Override
    public <V> C ge(Slot slot, String property, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            final Column column;
            if (Objects.nonNull((column = this.toColumn(property)))) {
                this.where(new StdGreaterThanOrEqual(this, column, slot, value));
            }
        }
        return this.self();
    }

    @Override
    public C geq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.GE);
    }

    @Override
    public <V> C lt(Slot slot, String property, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            final Column column;
            if (Objects.nonNull((column = this.toColumn(property)))) {
                this.where(new StdLessThan(this, column, slot, value));
            }
        }
        return this.self();
    }

    @Override
    public C ltq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.LT);
    }


    @Override
    public <V> C le(Slot slot, String property, V value, Predicate<V> predicate) {
        if (this.early(value, predicate)) {
            final Column column;
            if (Objects.nonNull((column = this.toColumn(property)))) {
                this.where(new StdLessThanOrEqual(this, column, slot, value));
            }
        }
        return this.self();
    }

    @Override
    public C leq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.LE);
    }

    // endregion

    // region Range condition

    @Override
    public C in(Slot slot, String property, Collection<?> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public C inq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.IN);
    }

    @Override
    public C notIn(Slot slot, String property, Collection<?> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdNotIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public C notInq(Slot slot, String property, ExtCriteria<?> query) {
        return this.addSubCondition(slot, this.toColumn(property), query, Symbol.NOT_IN);
    }

    @Override
    public <V> C between(Slot slot, String property, V begin, V end) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    @Override
    public <V> C notBetween(Slot slot, String property, V begin, V end) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdNotBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    // endregion

    // region Like condition

    @Override
    public C like(Slot slot, String property, String value, Like like, Character escape) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    @Override
    public C notLike(Slot slot, String property, String value, Like like, Character escape) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdNotLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    // endregion

    // region Template condition

    @Override
    public C tpl(Slot slot, String template, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdTemplate(this, column, template, PlaceholderPattern.SINGLE, slot, value, null,
                null));
        }
        return this.self();
    }

    @Override
    public C tpl(Slot slot, String template, String property, Collection<Object> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdTemplate(this, column, template, null, slot, null, values, null));
        }
        return this.self();
    }

    @Override
    public C tpl(Slot slot, String template, String property, Map<String, Object> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdTemplate(this, column, template, PlaceholderPattern.MAP, slot, null, null, values));
        }
        return this.self();
    }

    // endregion

    // region Null condition

    @Override
    public C isNull(Slot slot, String property) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdNull(this, column, slot));
        }
        return this.self();
    }

    @Override
    public C notNull(Slot slot, String property) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StdNotNull(this, column, slot));
        }
        return this.self();
    }

    // endregion

    // region Column equal to condition

    @Override
    public C ce(ExtCriteria<?> otherCriteria, String otherProperty) {
        return this.ce(this.id(), otherCriteria, otherCriteria.getConverter().convert(otherProperty));
    }

    @Override
    public C ce(String property, ExtCriteria<?> otherCriteria) {
        return this.ce(this.toColumn(property), otherCriteria, TableHelper.getId(otherCriteria.getEntityClass()));
    }

    @Override
    public C ce(String property, ExtCriteria<?> otherCriteria, String otherProperty) {
        return this.ce(this.toColumn(property), otherCriteria, otherCriteria.getConverter().convert(property));
    }

    @Override
    public C ceWith(ExtCriteria<?> otherCriteria, String otherColumn) {
        final Column id = this.id();
        if (Objects.nonNull(id) && Objects.isNotBlank(otherColumn)) {
            this.where(new SpecialExpression(this, id.getColumn(), otherCriteria, otherColumn));
        }
        return this.self();
    }

    @Override
    public C ceWith(String property, ExtCriteria<?> otherCriteria, String otherColumn) {
        final Column cc = this.toColumn(property);
        if (cc != null && Objects.isNotBlank(otherColumn)) {
            this.where(new SpecialExpression(this, cc.getColumn(), otherCriteria, otherColumn));
        }
        return this.self();
    }

    protected C ce(final Column cc, final ExtCriteria<?> otherCriteria, final Column oc) {
        if (Objects.nonNull(cc) && Objects.nonNull(oc)) {
            this.where(new SpecialExpression(this, cc.getColumn(), otherCriteria, oc.getColumn()));
        }
        return this.self();
    }

    // endregion

    // region Other condition

    @Override
    public C exists(Slot slot, ExtCriteria<?> query) {
        if (Objects.nonNull(query)) {
            this.where(new ExistsExpression(query, slot));
        }
        return this.self();
    }

    @Override
    public C notExists(Slot slot, ExtCriteria<?> query) {
        if (Objects.nonNull(query)) {
            this.where(new ExistsExpression(query, true, slot));
        }
        return this.self();
    }

    @Override
    public C nativeCondition(Slot slot, String condition) {
        if (Objects.isNotBlank(condition)) {
            this.where(new NativeExpression(condition, slot));
        }
        return this.self();
    }

    // endregion

}
