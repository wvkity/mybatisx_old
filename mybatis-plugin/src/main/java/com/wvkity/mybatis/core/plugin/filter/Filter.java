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
package com.wvkity.mybatis.core.plugin.filter;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 过滤器
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public interface Filter {

    /**
     * 过滤
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @return boolean
     */
    boolean filter(final MappedStatement ms, final Object parameter);
}
