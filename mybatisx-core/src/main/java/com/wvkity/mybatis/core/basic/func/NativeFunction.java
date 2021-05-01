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
package com.wvkity.mybatis.core.basic.func;

import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 纯SQL聚合函数
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
public class NativeFunction extends AbstractBasicFunction {

    private static final long serialVersionUID = 5627738571270412947L;

    public NativeFunction(String column, String alias) {
        this(null, column, alias);
    }

    public NativeFunction(Criteria<?> criteria, String column) {
        this(criteria, column, null);
    }

    public NativeFunction(Criteria<?> criteria, String column, String alias) {
        this.criteria = criteria;
        this.column = column;
        this.alias = alias;
    }

    @Override
    public AggFunc getFuncType() {
        return null;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public Integer getScale() {
        return null;
    }

    @Override
    public String getFuncBody() {
        return this.column;
    }

}
