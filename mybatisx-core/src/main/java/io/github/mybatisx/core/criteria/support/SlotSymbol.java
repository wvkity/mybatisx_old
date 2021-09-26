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
package io.github.mybatisx.core.criteria.support;

import io.github.mybatisx.support.constant.Slot;

/**
 * AND/OR运算符
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-08
 * @since 1.0.0
 */
public interface SlotSymbol<T, C extends SlotSymbol<T, C>> {

    /**
     * {@code Slot.AND}
     * @return {@code this}
     */
    C and();

    /**
     * {@code Slot.OR}
     * @return {@code this}
     */
    C or();

    /**
     * 获取{@link Slot}
     * @return {@link Slot}
     */
    Slot getSlot();
}
