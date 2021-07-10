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

import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.expr.ImmediateTemplate;
import com.github.mybatisx.core.expr.StandardTemplate;
import com.github.mybatisx.support.constant.Slot;

/**
 * 其他条件接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-10
 * @since 1.0.0
 */
interface OtherCondition<T, C extends OtherCondition<T, C>> extends SlotSymbol<T, C> {

    /**
     * exists
     * @param query {@link ExtCriteria}
     * @return {@code this}
     */
    default C exists(final ExtCriteria<?> query) {
        return this.exists(this.getSlot(), query);
    }

    /**
     * exists
     * @param slot  {@link Slot}
     * @param query {@link ExtCriteria}
     * @return {@code this}
     */
    C exists(final Slot slot, final ExtCriteria<?> query);

    /**
     * not exists
     * @param query {@link ExtCriteria}
     * @return {@code this}
     */
    default C notExists(final ExtCriteria<?> query) {
        return this.notExists(this.getSlot(), query);
    }

    /**
     * not exists
     * @param slot  {@link Slot}
     * @param query {@link ExtCriteria}
     * @return {@code this}
     */
    C notExists(final Slot slot, final ExtCriteria<?> query);

    /**
     * 纯SQL条件
     * <p>本方法存在SQL注入风险，谨慎使用，可参考{@link StandardTemplate StandardTemplate}
     * 或{@link ImmediateTemplate ImmediateTemplate}模板条件表达式实现对应的功能.</p>
     * @param condition 条件
     * @return {@code this}
     * @see CommonTemplate
     * @see LambdaTemplate
     */
    default C nativeCondition(final String condition) {
        return this.nativeCondition(this.getSlot(), condition);
    }

    /**
     * 纯SQL条件
     * <p>本方法存在SQL注入风险，谨慎使用，可参考{@link StandardTemplate StandardTemplate}
     * 或{@link ImmediateTemplate ImmediateTemplate}模板条件表达式实现对应的功能.</p>
     * @param slot      {@link Slot}
     * @param condition 条件
     * @return {@code this}
     * @see CommonTemplate
     * @see LambdaTemplate
     */
    C nativeCondition(final Slot slot, final String condition);

}
