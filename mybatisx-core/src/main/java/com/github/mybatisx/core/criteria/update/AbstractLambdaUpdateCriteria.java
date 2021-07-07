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
package com.github.mybatisx.core.criteria.update;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.inject.mapping.utils.Scripts;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.core.criteria.support.AbstractLambdaCriteria;

import java.util.stream.Collectors;

/**
 * 抽象基本条件/更新容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractLambdaUpdateCriteria<T, C extends LambdaUpdateCriteria<T, C>> extends
    AbstractLambdaCriteria<T, C> implements LambdaUpdateCriteria<T, C> {

    @Override
    public <V> C set(Property<T, V> property, V value) {
        return this.set(this.toProperty(property), value);
    }

    @Override
    public <V> C setIfAbsent(Property<T, V> property, V value) {
        return this.setIfAbsent(this.toProperty(property), value);
    }

    @Override
    public C set(String property, Object value) {
        return this.setOfUpdate(this.toColumn(property), value);
    }

    @Override
    public C setIfAbsent(String property, Object value) {
        if (Objects.isNotBlank(property) && !this.updateProperties.containsKey(property)) {
            this.set(property, value);
        }
        return this.self();
    }

    @Override
    public String getUpdateSegment() {
        if (Objects.isNotEmpty(this.updateColumnsOfWrap)) {
            return this.updateColumnsOfWrap.entrySet().stream().map(it -> Scripts.convertToConditionArg(Symbol.EQ,
                Slot.NONE, null, it.getKey(), this.parameterConverter.convert(it.getValue())))
                .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return Constants.EMPTY;
    }

}
