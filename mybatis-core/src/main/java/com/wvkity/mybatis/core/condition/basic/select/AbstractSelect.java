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
package com.wvkity.mybatis.core.condition.basic.select;

import com.wvkity.mybatis.core.condition.criteria.Criteria;

/**
 * 抽象查询列包装器
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractSelect<E> implements Select {

    /**
     * 查询接口对象
     */
    protected Criteria<?> criteria;

    /**
     * 表别名
     */
    protected String tableAlias;

    /**
     * 字段
     */
    protected E column;

    /**
     * 字段别名
     */
    protected String alias;

    @Override
    public String getAlias() {
        return this.alias;
    }

}
