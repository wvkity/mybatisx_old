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
package com.wvkity.mybatis.core.convert.converter;

import java.io.Serializable;

/**
 * 转换器
 * @param <T> 泛型类型
 * @param <R> 返回值类型
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@FunctionalInterface
public interface Converter<T, R> extends Serializable {

    /**
     * 将对象转换成其他对象
     * @param source 源对象
     * @return 其他对象
     */
    R convert(final T source);

}
