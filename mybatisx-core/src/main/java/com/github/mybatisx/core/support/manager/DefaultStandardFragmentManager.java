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
package com.github.mybatisx.core.support.manager;

import com.github.mybatisx.support.criteria.Criteria;

/**
 * 标准片段管理器
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public class DefaultStandardFragmentManager extends AbstractStandardFragmentManager<Criteria<?>> {

    private static final long serialVersionUID = -1495527581698544576L;

    public DefaultStandardFragmentManager(Criteria<?> criteria) {
        super(criteria);
    }

    public DefaultStandardFragmentManager(Criteria<?> criteria, boolean forQuery) {
        super(criteria, forQuery);
    }
}
