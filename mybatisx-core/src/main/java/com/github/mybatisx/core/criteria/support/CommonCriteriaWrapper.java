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
package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.core.criteria.CriteriaWrapper;
import com.github.mybatisx.core.expr.ImmediateTemplate;
import com.github.mybatisx.core.expr.StandardTemplate;
import com.github.mybatisx.support.constant.Slot;

/**
 * 基础条件接口
 * @param <T> 实体类型
 * @param <C> 值类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface CommonCriteriaWrapper<T, C extends CommonCriteriaWrapper<T, C>> extends CriteriaWrapper<T, C>,
    CommonCompare<T, C>, CommonRange<T, C>, CommonLike<T, C>, CommonTemplate<T, C>, QueryCriteriaBuilder<T, C> {

    /**
     * IS NULL
     * @param column 字段
     * @return {@link C}
     */
    default C colIsNull(final String column) {
        return this.colIsNull(Slot.AND, column);
    }

    /**
     * IS NULL
     * @param slot   {@link Slot}
     * @param column 字段
     * @return {@link C}
     */
    C colIsNull(final Slot slot, final String column);

    /**
     * IS NOT NULL
     * @param column 字段
     * @return {@link C}
     */
    default C colNotNull(final String column) {
        return this.colNotNull(Slot.AND, column);
    }

    /**
     * IS NOT NULL
     * @param slot   {@link Slot}
     * @param column 字段
     * @return {@link C}
     */
    C colNotNull(final Slot slot, final String column);

    /**
     * 纯SQL条件
     * <p>本方法存在SQL注入风险，谨慎使用，可参考{@link StandardTemplate StandardTemplate}
     * 或{@link ImmediateTemplate ImmediateTemplate}模板条件表达式实现对应的功能.</p>
     * @param condition 条件
     * @return {@code this}
     * @see CommonTemplate
     * @see LambdaTemplate
     */
    C nativeCondition(final String condition);
}
