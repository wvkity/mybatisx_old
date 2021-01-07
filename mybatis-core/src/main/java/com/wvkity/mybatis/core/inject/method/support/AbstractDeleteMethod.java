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
package com.wvkity.mybatis.core.inject.method.support;

import com.wvkity.mybatis.core.inject.mapping.sql.Supplier;
import com.wvkity.mybatis.core.metadata.Table;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 抽象删除方法
 * @author wvkity
 * @created 2021-01-03
 * @since 1.0.0
 */
public class AbstractDeleteMethod<T extends Supplier> extends AbstractInvokeMethod<T> {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperInterface, Class<?> resultType, Table table) {
        return addDeleteMappedStatement(mapperInterface, resultType, table);
    }
}
