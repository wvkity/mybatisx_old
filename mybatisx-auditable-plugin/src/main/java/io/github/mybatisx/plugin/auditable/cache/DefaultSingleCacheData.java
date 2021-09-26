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
package io.github.mybatisx.plugin.auditable.cache;

import io.github.mybatisx.auditable.PropertyWrapper;

/**
 * 默认单值缓存数据
 * @author wvkity
 * @created 2021-07-22
 * @since 1.0.0
 */
public class DefaultSingleCacheData implements CacheData<PropertyWrapper> {

    private static final long serialVersionUID = -290832704805231004L;
    private final PropertyWrapper data;

    public DefaultSingleCacheData(PropertyWrapper data) {
        this.data = data;
    }

    @Override
    public PropertyWrapper getData() {
        return this.data;
    }
}
