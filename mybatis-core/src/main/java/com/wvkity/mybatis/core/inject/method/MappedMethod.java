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
package com.wvkity.mybatis.core.inject.method;

import com.wvkity.mybatis.basic.metadata.Table;
import org.apache.ibatis.builder.MapperBuilderAssistant;

/**
 * 方法映射注入
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
@FunctionalInterface
public interface MappedMethod {

    /**
     * 注入SQL
     * @param assistant       {@link MapperBuilderAssistant}
     * @param mapperInterface Mapper接口
     * @param resultType      返回值类型
     * @param table           表对象
     */
    void invoke(final MapperBuilderAssistant assistant, Class<?> mapperInterface,
                final Class<?> resultType, final Table table);
}
