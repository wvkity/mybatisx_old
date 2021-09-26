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
package io.github.mybatisx.core.criteria.support;

import io.github.mybatisx.Objects;
import io.github.mybatisx.PlaceholderPattern;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.core.criteria.AbstractCriteria;
import io.github.mybatisx.core.criteria.ExtCriteria;
import io.github.mybatisx.core.expr.ExistsExpression;
import io.github.mybatisx.core.expr.ImdBetween;
import io.github.mybatisx.core.expr.ImdEqual;
import io.github.mybatisx.core.expr.ImdGreaterThan;
import io.github.mybatisx.core.expr.ImdGreaterThanOrEqual;
import io.github.mybatisx.core.expr.ImdIn;
import io.github.mybatisx.core.expr.ImdLessThan;
import io.github.mybatisx.core.expr.ImdLessThanOrEqual;
import io.github.mybatisx.core.expr.ImdLike;
import io.github.mybatisx.core.expr.ImdNotBetween;
import io.github.mybatisx.core.expr.ImdNotEqual;
import io.github.mybatisx.core.expr.ImdNotIn;
import io.github.mybatisx.core.expr.ImdNotLike;
import io.github.mybatisx.core.expr.ImdNotNull;
import io.github.mybatisx.core.expr.ImdNull;
import io.github.mybatisx.core.expr.ImdTemplate;
import io.github.mybatisx.core.expr.NativeExpression;
import io.github.mybatisx.core.expr.SpecialExpression;
import io.github.mybatisx.support.constant.Like;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.helper.TableHelper;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 抽象基础条件包装容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractCommonCriteria<T, C extends CommonCriteriaWrapper<T, C>> extends
        AbstractCriteria<T, C> implements CommonCriteriaWrapper<T, C> {

    // region Compare conditions

    @Override
    public <V> C colEq(Slot slot, String column, V value, Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && this.early(value, predicate)) {
            this.where(new ImdEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colEqq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.EQ);
    }

    @Override
    public C colEq(Slot slot, Map<String, Object> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, Object> entry : columns.entrySet()) {
                this.colEq(slot, entry.getKey(), entry.getValue());
            }
        }
        return this.self();
    }

    @Override
    public <V> C colNe(Slot slot, String column, V value, Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && this.early(value, predicate)) {
            this.where(new ImdNotEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colNeq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.NE);
    }

    @Override
    public <V> C colGt(Slot slot, String column, V value, Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && this.early(value, predicate)) {
            this.where(new ImdGreaterThan(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colGtq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.GT);
    }

    @Override
    public <V> C colGe(Slot slot, String column, V value, Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && this.early(value, predicate)) {
            this.where(new ImdGreaterThanOrEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colGeq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.GE);
    }

    @Override
    public <V> C colLt(Slot slot, String column, V value, Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && this.early(value, predicate)) {
            this.where(new ImdLessThan(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colLtq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.LT);
    }

    @Override
    public <V> C colLe(Slot slot, String column, V value, Predicate<V> predicate) {
        if (Objects.isNotBlank(column) && this.early(value, predicate)) {
            this.where(new ImdLessThanOrEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colLeq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.LE);
    }

    // endregion

    // region Range condition

    @Override
    public C colIn(Slot slot, String column, Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public C colInq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.IN);
    }

    @Override
    public C colNotIn(Slot slot, String column, Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdNotIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public C colNotInq(Slot slot, String column, ExtCriteria<?> query) {
        return this.addSubCondition(slot, column, query, Symbol.NOT_IN);
    }

    @Override
    public <V> C colBetween(Slot slot, String column, V begin, V end) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    @Override
    public <V> C colNotBetween(Slot slot, String column, V begin, V end) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdNotBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    // endregion

    // region Like condition

    @Override
    public C colLike(Slot slot, String column, String value, Like like, Character escape) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    @Override
    public C colNotLike(Slot slot, String column, String value, Like like, Character escape) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdNotLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    // endregion

    // region Template condition

    @Override
    public C tpl(Slot slot, String template, String column, PlaceholderPattern pattern, Object value,
                 Collection<Object> values, Map<String, Object> mapValues) {
        if (Objects.isNotBlank(template)) {
            this.where(new ImdTemplate(this, column, template, pattern, slot, value, values, mapValues));
        }
        return this.self();
    }

    // endregion

    // region Null condition

    @Override
    public C colIsNull(Slot slot, String column) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdNull(this, column, slot));
        }
        return this.self();
    }

    @Override
    public C colNotNull(Slot slot, String column) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImdNotNull(this, column, slot));
        }
        return this.self();
    }

    // endregion

    // region Column equal to condition

    @Override
    public C colCe(String column, ExtCriteria<?> otherCriteria, String otherColumn) {
        if (Objects.isNotBlank(column) && Objects.isNotBlank(otherColumn)) {
            this.where(new SpecialExpression(this, column, otherCriteria, otherColumn));
        }
        return this.self();
    }

    @Override
    public C colCeWith(String column, ExtCriteria<?> otherCriteria) {
        if (Objects.isNotBlank(column)) {
            final Column oid = TableHelper.getId(otherCriteria.getEntityClass());
            if (Objects.nonNull(oid)) {
                this.where(new SpecialExpression(this, column, otherCriteria, oid.getColumn()));
            }
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
