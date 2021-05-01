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
package com.wvkity.mybatis.core.criteria;

/**
 * 抽象子查询条件
 * @param <S> 实体类型
 * @author wvkity
 * @created 2021-04-16
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractSubCriteria<S> extends AbstractQueryCriteria<S> {

    /**
     * 主条件对象
     */
    protected AbstractQueryCriteria<?> master;

    @SuppressWarnings("unchecked")
    public <R> AbstractQueryCriteria<R> getMaster() {
        return (AbstractQueryCriteria<R>) this.master;
    }

}
