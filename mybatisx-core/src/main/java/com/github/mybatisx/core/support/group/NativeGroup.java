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
package com.github.mybatisx.core.support.group;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;

/**
 * 分组(纯SQL)
 * @author wvkity
 * @created 2021-07-11
 * @since 1.0.0
 */
public class NativeGroup implements Group {

    private static final long serialVersionUID = -2246328812945443040L;
    protected String groupBy;

    public NativeGroup(String groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public String getSegment() {
        if (Objects.isNotBlank(this.groupBy)) {
            return groupBy;
        }
        return Constants.EMPTY;
    }
}
