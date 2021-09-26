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
package io.github.mybatisx.core.criteria.update;

import io.github.mybatisx.Objects;
import io.github.mybatisx.core.criteria.support.AbstractGenericCriteria;

import java.util.Locale;

/**
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractGenericUpdateCriteria<T, C extends GenericUpdateCriteria<T, C>> extends
    AbstractGenericCriteria<T, C> implements GenericUpdateCriteria<T, C> {

    @Override
    public C set(String property, Object value) {
        return this.setOfUpdate(this.toColumn(property), value);
    }

    @Override
    public C setIfAbsent(String property, Object value) {
        if (Objects.isNotBlank(property) && !this.updateProperties.containsKey(property)) {
            this.set(property, value);
        }
        return this.self();
    }

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
    public Object getVersionUpdateValue() {
        return this.optimisticLockUpdateValue();
    }

    @Override
    public String getUpdateSegment() {
        return this.intactUpdateString();
    }

}
