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

import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.core.property.Property;

/**
 * 字段查找接口
 * @param <T> 实体类
 * @author wvkity
 * @created 2021-01-06
 * @since 1.0.0
 */
public interface SearchColumn<T> {

    /**
     * 根据{@link Property}查找{@link Column}对象
     * @param property {@link Property}
     * @return {@link Column}对象
     */
    Column findColumn(final Property<?, ?> property);

    /**
     * 根据属性名查找{@link Column}对象
     * @param property 属性名
     * @return {@link Column}对象
     */
    Column findColumn(final String property);
}
