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
package com.wvkity.result.model;

import com.wvkity.result.error.AbstractError;

import java.util.Collection;

/**
 * 抽象{@link java.util.Map Map}类型数据结果集
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractMultiModelResult extends AbstractError implements MultiModelResult {

    /**
     * 检查字符串是否不为空
     * @param value 字符串值
     * @return boolean
     */
    protected boolean isNotBlank(final String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 检查集合是否存在元素
     * @param values 集合
     * @param <E>    值类型
     * @return boolean
     */
    protected <E> boolean isNotEmpty(final Collection<E> values) {
        return values != null && !values.isEmpty();
    }
}
