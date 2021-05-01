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
package com.wvkity.mybatis.core.inject.method.support;

import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.inject.mapping.sql.Supplier;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 抽象{@link Criteria Criteria}相关方法
 * @param <T> {@link Supplier}类型
 * @author wvkity
 * @created 2021-02-02
 * @since 1.0.0
 */
public abstract class AbstractCriteriaMethod<T extends Supplier> extends AbstractInvokeMethod<T> {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperInterface, Class<?> resultType, Table table) {
        final Class<?> returnType = this.getResultType();
        return this.addSelectMappedStatement(mapperInterface, returnType == null ? resultType : returnType, table);
    }

    /**
     * 获取指定返回值类型
     * @return 返回值类型
     */
    public Class<?> getResultType() {
        return null;
    }
}
