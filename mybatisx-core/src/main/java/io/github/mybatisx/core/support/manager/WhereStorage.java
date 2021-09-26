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
package io.github.mybatisx.core.support.manager;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.condition.Criterion;
import io.github.mybatisx.core.condition.StandardCondition;
import io.github.mybatisx.immutable.ImmutableList;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.support.fragment.AbstractFragmentList;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件片段存储器
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public class WhereStorage extends AbstractFragmentList<Criterion> {

    private static final long serialVersionUID = 3445770521976104346L;

    /**
     * 获取所有条件
     * @return {@link Criterion}列表
     */
    public List<Criterion> getConditions() {
        return this.isEmpty() ? ImmutableList.of() : ImmutableList.of(this.fragments);
    }

    @Override
    public String getSegment() {
        if (!this.isEmpty()) {
            final List<String> conditions = new ArrayList<>(this.fragments.size());
            for (Criterion it : this.fragments) {
                if (Objects.nonNull(it)) {
                    final String condition = it.getSegment();
                    if (Objects.isNotBlank(condition)) {
                        conditions.add(condition);
                    }
                }
            }
            if (Objects.isNotEmpty(conditions)) {
                return String.join(Constants.SPACE, conditions).trim();
            }
        }
        return Constants.EMPTY;
    }

    /**
     * 获取乐观锁条件值
     * @param column {@link Column}
     * @return 乐观锁条件值
     */
    public Object getVersionValue(final Column column) {
        if (Objects.nonNull(column) && !this.isEmpty()) {
            for (Criterion it: this.fragments) {
                if (Symbol.EQ.equals(it.getSymbol()) && it instanceof StandardCondition) {
                    final String target = it.getColumn();
                    if (column.getColumn().equalsIgnoreCase(target)) {
                        return it.getOriginalValue();
                    }
                }
            }
        }
        return null;
    }

}
