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
package com.wvkity.mybatis.core.criteria.support;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.criteria.AbstractCriteria;
import com.wvkity.mybatis.core.expr.ImmediateBetween;
import com.wvkity.mybatis.core.expr.ImmediateEqual;
import com.wvkity.mybatis.core.expr.ImmediateGreaterThan;
import com.wvkity.mybatis.core.expr.ImmediateGreaterThanOrEqual;
import com.wvkity.mybatis.core.expr.ImmediateIn;
import com.wvkity.mybatis.core.expr.ImmediateLessThan;
import com.wvkity.mybatis.core.expr.ImmediateLessThanOrEqual;
import com.wvkity.mybatis.core.expr.ImmediateLike;
import com.wvkity.mybatis.core.expr.ImmediateNotBetween;
import com.wvkity.mybatis.core.expr.ImmediateNotEqual;
import com.wvkity.mybatis.core.expr.ImmediateNotIn;
import com.wvkity.mybatis.core.expr.ImmediateNotLike;
import com.wvkity.mybatis.core.expr.ImmediateNotNull;
import com.wvkity.mybatis.core.expr.ImmediateNull;
import com.wvkity.mybatis.core.expr.ImmediateTemplate;
import com.wvkity.mybatis.core.expr.NativeExpression;
import com.wvkity.mybatis.core.expr.TemplateMatch;
import com.wvkity.mybatis.support.constant.Like;
import com.wvkity.mybatis.support.constant.Slot;

import java.util.Collection;
import java.util.Map;

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
    public C colEq(Slot slot, String column, Object value) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateEqual(this, column, slot, value));
        }
        return this.self();
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
    public C colNe(Slot slot, String column, Object value) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateNotEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colGt(Slot slot, String column, Object value) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateGreaterThan(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colGe(Slot slot, String column, Object value) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateGreaterThanOrEqual(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colLt(Slot slot, String column, Object value) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateLessThan(this, column, slot, value));
        }
        return this.self();
    }

    @Override
    public C colLe(Slot slot, String column, Object value) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateLessThanOrEqual(this, column, slot, value));
        }
        return this.self();
    }

    // endregion

    // region Range condition

    @Override
    public C colIn(Slot slot, String column, Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public C colNotIn(Slot slot, String column, Collection<?> values) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateNotIn(this, column, slot, values));
        }
        return this.self();
    }

    @Override
    public C colBetween(Slot slot, String column, Object begin, Object end) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    @Override
    public C colNotBetween(Slot slot, String column, Object begin, Object end) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateNotBetween(this, column, slot, begin, end));
        }
        return this.self();
    }

    // endregion

    // region Like condition

    @Override
    public C colLike(Slot slot, String column, Object value, Like like, Character escape) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    @Override
    public C colNotLike(Slot slot, String column, Object value, Like like, Character escape) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateNotLike(this, column, like, escape, slot, value));
        }
        return this.self();
    }

    // endregion

    // region Template condition

    @Override
    public C tpl(Slot slot, String template, String column, TemplateMatch match, Object value,
                    Collection<Object> values, Map<String, Object> mapValues) {
        if (Objects.isNotBlank(template)) {
            this.where(new ImmediateTemplate(this, column, template, match, slot, value, values, mapValues));
        }
        return this.self();
    }

    // endregion

    // region Null condition

    @Override
    public C colIsNull(Slot slot, String column) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateNull(this, column, slot));
        }
        return this.self();
    }

    @Override
    public C colNotNull(Slot slot, String column) {
        if (Objects.isNotBlank(column)) {
            this.where(new ImmediateNotNull(this, column, slot));
        }
        return this.self();
    }

    // endregion

    // region Other condition

    @Override
    public C nativeCondition(String condition) {
        if (Objects.isNotBlank(condition)) {
            this.where(new NativeExpression(condition));
        }
        return this.self();
    }

    // endregion

}
