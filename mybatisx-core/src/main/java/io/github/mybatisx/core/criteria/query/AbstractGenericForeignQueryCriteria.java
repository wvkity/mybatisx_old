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
package io.github.mybatisx.core.criteria.query;

import io.github.mybatisx.core.criteria.ExtCriteria;
import io.github.mybatisx.support.constant.Join;

/**
 * 抽象通用联表查询条件容器
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractGenericForeignQueryCriteria<T, C extends GenericForeignQueryCriteria<T, C>> extends
    AbstractGenericQueryCriteria<T, C> implements GenericForeignQueryCriteria<T, C> {

    /**
     * 主条件对象
     */
    protected ExtCriteria<?> master;
    /**
     * 联表方式
     */
    protected Join join;

    @Override
    public C fetch(boolean fetch) {
        this.fetch = fetch;
        return this.self();
    }

    @Override
    public ExtCriteria<?> getMaster() {
        return this.master;
    }

    @Override
    public Join getJoin() {
        return this.join;
    }

}
