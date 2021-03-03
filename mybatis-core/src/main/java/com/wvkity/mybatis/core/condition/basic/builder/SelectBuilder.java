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
package com.wvkity.mybatis.core.condition.basic.builder;

import com.wvkity.mybatis.basic.builder.Builder;
import com.wvkity.mybatis.core.condition.basic.select.Selection;

/**
 * 查询列构建器
 * @param <T> 查询列类型
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface SelectBuilder<T extends Selection, E> extends Builder<T> {

    /**
     * 获取字段
     * @return 字段
     */
    default E getColumn() {
        return null;
    }
}
