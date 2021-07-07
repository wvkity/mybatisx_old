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
package com.github.mybatisx.core.inject.method.support;

import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.support.inject.mapping.sql.Supplier;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 抽象查询方法
 * @param <T> {@link Supplier}类型
 * @author wvkity
 * @created 2020-10-21
 * @since 1.0.0
 */
public abstract class AbstractSelectMethod<T extends Supplier> extends AbstractInvokeMethod<T> {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperInterface, Class<?> resultType, Table table) {
        return addSelectMappedStatement(mapperInterface, resultType, table);
    }
}
