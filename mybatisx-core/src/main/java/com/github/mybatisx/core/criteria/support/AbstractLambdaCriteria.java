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

import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.AbstractCriteria;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.expr.SpecialExpression;
import com.github.mybatisx.core.expr.StandardBetween;
import com.github.mybatisx.core.expr.StandardEqual;
import com.github.mybatisx.core.expr.StandardGreaterThan;
import com.github.mybatisx.core.expr.StandardGreaterThanOrEqual;
import com.github.mybatisx.core.expr.StandardIn;
import com.github.mybatisx.core.expr.StandardLessThan;
import com.github.mybatisx.core.expr.StandardLessThanOrEqual;
import com.github.mybatisx.core.expr.StandardLike;
import com.github.mybatisx.core.expr.StandardNotBetween;
import com.github.mybatisx.core.expr.StandardNotEqual;
import com.github.mybatisx.core.expr.StandardNotIn;
import com.github.mybatisx.core.expr.StandardNotLike;
import com.github.mybatisx.core.expr.StandardNotNull;
import com.github.mybatisx.core.expr.StandardNull;
import com.github.mybatisx.core.expr.StandardTemplate;
import com.github.mybatisx.core.expr.TemplateMatch;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.support.constant.Like;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.helper.TableHelper;

import java.util.Collection;
import java.util.Map;

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
    public C idEq(Slot slot, Object value) {
        return this.where(new StandardEqual(this, this.id(), slot, value));
    }

    @Override
    public <V> C eq(Slot slot, Property<T, V> property, V value) {
        return this.eq(slot, this.toProperty(property), value);
    }

    @Override
    public C eq(Slot slot, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardEqual(this, column, slot, value));
        }
        return this.self();
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
    public <V> C ne(Slot slot, Property<T, V> property, V value) {
        return this.ne(slot, this.toProperty(property), value);
    }

    @Override
    public C ne(Slot slot, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardNotEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public <V> C gt(Slot slot, Property<T, V> property, V value) {
        return this.gt(slot, this.toProperty(property), value);
    }

    @Override
    public C gt(Slot slot, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardGreaterThan(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public <V> C ge(Slot slot, Property<T, V> property, V value) {
        return this.ge(slot, this.toProperty(property), value);
    }

    @Override
    public C ge(Slot slot, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardGreaterThanOrEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public <V> C lt(Slot slot, Property<T, V> property, V value) {
        return this.lt(slot, this.toProperty(property), value);
    }

    @Override
    public C lt(Slot slot, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardLessThan(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public <V> C le(Slot slot, Property<T, V> property, V value) {
        return this.le(slot, this.toProperty(property), value);
    }

    @Override
    public C le(Slot slot, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardLessThanOrEqual(this, column, slot, value));
        }
        return this.self();
    }

    // endregion

    // region Range condition

    @Override
    public <V> C in(Slot slot, Property<T, V> property, Collection<V> values) {
        return this.in(slot, this.toProperty(property), values);
    }

    @Override
    public C in(Slot slot, String property, Collection<?> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public <V> C notIn(Slot slot, Property<T, V> property, Collection<V> values) {
        return this.notIn(slot, this.toProperty(property), values);
    }

    @Override
    public C notIn(Slot slot, String property, Collection<?> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardNotIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public <V> C between(Slot slot, Property<T, V> property, V begin, V end) {
        return this.between(slot, this.toProperty(property), begin, end);
    }

    @Override
    public C between(Slot slot, String property, Object begin, Object end) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    @Override
    public <V> C notBetween(Slot slot, Property<T, V> property, V begin, V end) {
        return this.notBetween(slot, this.toProperty(property), begin, end);
    }

    @Override
    public C notBetween(Slot slot, String property, Object begin, Object end) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardNotBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    // endregion

    // region Like condition

    @Override
    public <V> C like(Slot slot, Property<T, V> property, V value, Like like, Character escape) {
        return this.like(slot, this.toProperty(property), value, like, escape);
    }

    @Override
    public C like(Slot slot, String property, Object value, Like like, Character escape) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    @Override
    public <V> C notLike(Slot slot, Property<T, V> property, V value, Like like, Character escape) {
        return this.notLike(slot, this.toProperty(property), value, like, escape);
    }

    @Override
    public C notLike(Slot slot, String property, Object value, Like like, Character escape) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardNotLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    // endregion

    // region Template condition

    @Override
    public C tpl(Slot slot, String template, Property<T, ?> property, Object value) {
        return this.tpl(slot, template, this.toProperty(property), value);
    }

    @Override
    public C tpl(Slot slot, String template, String property, Object value) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardTemplate(this, column, template, TemplateMatch.SINGLE, slot, value, null, null));
        }
        return this.self();
    }

    @Override
    public C tpl(Slot slot, String template, Property<T, ?> property, Collection<Object> values) {
        return this.tpl(slot, template, this.toProperty(property), values);
    }

    @Override
    public C tpl(Slot slot, String template, String property, Collection<Object> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardTemplate(this, column, template, null, slot, null, values, null));
        }
        return this.self();
    }

    @Override
    public C tpl(Slot slot, String template, Property<T, ?> property, Map<String, Object> values) {
        return this.tpl(slot, template, this.toProperty(property), values);
    }

    @Override
    public C tpl(Slot slot, String template, String property, Map<String, Object> values) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardTemplate(this, column, template, TemplateMatch.MAP, slot, null, null, values));
        }
        return this.self();
    }

    // endregion

    // region Null condition

    @Override
    public C isNull(Slot slot, Property<T, ?> property) {
        return this.isNull(slot, this.toProperty(property));
    }

    @Override
    public C isNull(Slot slot, String property) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardNull(this, column, slot));
        }
        return this.self();
    }

    @Override
    public C notNull(Slot slot, Property<T, ?> property) {
        return this.notNull(slot, this.toProperty(property));
    }

    @Override
    public C notNull(Slot slot, String property) {
        final Column column;
        if (Objects.nonNull((column = this.toColumn(property)))) {
            this.where(new StandardNotNull(this, column, slot));
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
    public C ce(Property<T, ?> property, ExtCriteria<?> otherCriteria) {
        return this.ce(this.toProperty(property), otherCriteria);
    }

    @Override
    public C ce(String property, ExtCriteria<?> otherCriteria) {
        return this.ce(this.toColumn(property), otherCriteria, TableHelper.getId(otherCriteria.getEntityClass()));
    }

    @Override
    public C ce(Property<T, ?> property, ExtCriteria<?> otherCriteria, String otherProperty) {
        return this.ce(this.toProperty(property), otherCriteria, otherProperty);
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
    public C ceWith(Property<T, ?> property, ExtCriteria<?> otherCriteria, String otherColumn) {
        return this.ceWith(this.toProperty(property), otherCriteria, otherColumn);
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

}
