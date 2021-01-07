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
package com.wvkity.mybatis.core.inject;

import com.wvkity.mybatis.core.immutable.ImmutableList;
import com.wvkity.mybatis.core.inject.method.MappedMethod;
import com.wvkity.mybatis.core.mapper.BaseMapper;
import com.wvkity.mybatis.core.mapper.DeleteMapper;
import com.wvkity.mybatis.core.mapper.InsertMapper;
import com.wvkity.mybatis.core.mapper.QueryMapper;
import com.wvkity.mybatis.core.mapper.UpdateMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 默认SQL注入器
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public class DefaultInjector extends AbstractInjector implements Injector {

    @Override
    public Collection<MappedMethod> getMappedMethods(Class<?> mapperInterface) {
        if (BaseMapper.class.isAssignableFrom(mapperInterface)) {
            return MAPPED_METHOD_CACHE.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        } else if (InsertMapper.class.isAssignableFrom(mapperInterface)) {
            return new ArrayList<>(MAPPED_METHOD_CACHE.get(InsertMapper.class));
        } else if (UpdateMapper.class.isAssignableFrom(mapperInterface)) {
            return new ArrayList<>(MAPPED_METHOD_CACHE.get(UpdateMapper.class));
        } else if (DeleteMapper.class.isAssignableFrom(mapperInterface)) {
            return new ArrayList<>(MAPPED_METHOD_CACHE.get(DeleteMapper.class));
        } else if (QueryMapper.class.isAssignableFrom(mapperInterface)) {
            return new ArrayList<>(MAPPED_METHOD_CACHE.get(QueryMapper.class));
        }
        return ImmutableList.of();
    }
}
