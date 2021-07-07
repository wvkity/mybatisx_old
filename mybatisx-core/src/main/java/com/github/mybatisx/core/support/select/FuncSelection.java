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
package com.github.mybatisx.core.support.select;

import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.support.basic.Matched;

/**
 * 聚合函数查询
 * @author wvkity
 * @created 2021-04-28
 * @since 1.0.0
 */
public class FuncSelection extends AbstractSelection<String> {

    private static final long serialVersionUID = -7358372932983579414L;

    /**
     * 聚合函数
     */
    private final Function function;

    public FuncSelection(Function function) {
        this.function = function;
        this.matched = Matched.FUNCTION;
    }

    /**
     * 获取聚合函数对象
     * @return {@link Function}
     */
    public Function getFunction() {
        return function;
    }

    @Override
    public String getAlias() {
        return this.function.getAlias();
    }

    @Override
    public String getColumn() {
        return this.function.getColumn();
    }

    @Override
    public String getSegment(boolean isQuery) {
        if (isQuery) {
            return this.function.getSegment();
        }
        return null;
    }

}
