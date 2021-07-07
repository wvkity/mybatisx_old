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
package com.github.mybatisx.core.criteria.update;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.support.AbstractCommonCriteria;

import java.util.Locale;

/**
 * 抽象基本条件/更新容器
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractCommonUpdateCriteria<T, C extends CommonUpdateCriteria<T, C>> extends
    AbstractCommonCriteria<T, C> implements CommonUpdateCriteria<T, C> {

    @Override
    public C colSet(String column, Object value) {
        return this.setOfUpdate(column, value);
    }

    @Override
    public C colSetIfAbsent(String column, Object value) {
        if (Objects.isNotBlank(column) && !this.updateColumns.contains(column.toUpperCase(Locale.ENGLISH))) {
            this.colSet(column, value);
        }
        return this.self();
    }

    @Override
    public String getUpdateSegment() {
        return this.intactUpdateString();
    }

}
