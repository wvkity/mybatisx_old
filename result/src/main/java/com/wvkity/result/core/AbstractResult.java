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
package com.wvkity.result.core;

import com.wvkity.result.model.AbstractModelResult;

/**
 * 抽象响应结果
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractResult<T> extends AbstractModelResult<T> {

    /**
     * 数据
     */
    protected T data;

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public boolean isEmpty() {
        return this.data == null;
    }
}
