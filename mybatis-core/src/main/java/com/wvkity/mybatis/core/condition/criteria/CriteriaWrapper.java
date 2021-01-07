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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.core.convert.Property;

/**
 * 条件接口包装器
 * @param <T>     泛型类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public interface CriteriaWrapper<T, Chain extends CriteriaWrapper<T, Chain>> extends Criteria<T>,
    Compare<T, Chain> {

    /**
     * 根据方法获取属性名
     * @param property {@link Property}
     * @param <E>      泛型类型
     * @param <V>      属性类型
     * @return 属性名
     */
    <E, V> String methodToProperty(final Property<E, V> property);
}
