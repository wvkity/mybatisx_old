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
package io.github.mybatisx.core.convert;

import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.core.property.Property;

/**
 * 属性转换器
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public interface PropertyConverter extends Converter<Property<?, ?>, Column> {

    /**
     * 属性转{@link Column}
     * @param property 属性
     * @return {@link Column}
     */
    Column convert(final String property);

    /**
     * 属性转{@link Column}
     * @param column 字段名
     * @return {@link Column}
     */
    Column convertOfOrg(final String column);

    /**
     * Lambda属性转字符串属性
     * @param property {@link Property}
     * @return 字符串属性
     */
    String toProperty(final Property<?, ?> property);

}
