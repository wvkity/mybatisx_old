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
package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.core.criteria.ExtCriteria;

/**
 * 抽象嵌套查询条件容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-20
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractNestedSubQueryCriteria<T, C extends NestedSubQueryCriteria<T, C>> extends
    AbstractCommonSubQueryCriteria<T, C> implements NestedSubQueryCriteria<T, C> {

    /**
     * 引用条件对象
     */
    protected ExtCriteria<?> refCriteria;

}
