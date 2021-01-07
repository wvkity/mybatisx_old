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

import com.wvkity.mybatis.core.utils.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象转换器
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
public interface ObjectConverter<T> extends Converter<T, String> {

    /**
     * 将对象列表转成字符串列表
     * @param resources 源对象列表
     * @return 字符串列表
     */
    default List<String> convert(final Collection<T> resources) {
        if (Objects.isNotEmpty(resources)) {
            return resources.stream().filter(Objects::nonNull).map(this::convert).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }
}
