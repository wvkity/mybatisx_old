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

import java.util.List;

/**
 * 默认多值缓存数据
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultMultiCacheData implements CacheData<List<PropertyWrapper>> {

    private static final long serialVersionUID = 5743451825760166183L;
    private final List<PropertyWrapper> data;

    public DefaultMultiCacheData(List<PropertyWrapper> data) {
        this.data = data;
    }

    @Override
    public List<PropertyWrapper> getData() {
        return this.data;
    }
}
