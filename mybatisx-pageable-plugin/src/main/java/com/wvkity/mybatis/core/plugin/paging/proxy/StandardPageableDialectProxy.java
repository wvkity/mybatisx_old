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
package com.wvkity.mybatis.core.plugin.paging.proxy;

import com.wvkity.mybatis.core.plugin.paging.dialect.AbstractDialect;
import com.wvkity.mybatis.core.plugin.paging.dialect.AbstractPageableDialect;

import java.util.Optional;

/**
 * 分页方言代理
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
public class StandardPageableDialectProxy extends RangePageableDialectProxy {

    @Override
    public AbstractPageableDialect getDelegate() {
        final AbstractDialect it = Optional.ofNullable(this.delegate).orElse(this.threadLocalDelegateDialect.get());
        return it == null ? null : (AbstractPageableDialect) it;
    }
}
