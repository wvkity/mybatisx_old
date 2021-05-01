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
package com.wvkity.mybatis.core.basic.having;

import com.wvkity.mybatis.core.basic.func.Function;
import com.wvkity.mybatis.core.utils.Placeholders;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 分组筛选子句
 * @author wvkity
 * @created 2021-04-30
 * @since 1.0.0
 */
public class MultiHaving extends AbstractHaving {

    private static final long serialVersionUID = -2375890368686468575L;
    /**
     * 比较符
     */
    private MultiComparator comparator = MultiComparator.GE_AND_LE;
    /**
     * 参数1
     */
    private final String first;
    /**
     * 参数2
     */
    private final String last;

    public MultiHaving(Function function, String first, String last) {
        this.function = function;
        this.first = first;
        this.last = last;
    }

    public MultiHaving(Function function, Slot slot, String first, String last) {
        this(null, function, slot, first, last);
    }

    public MultiHaving(Function function, Slot slot, MultiComparator comparator, String first, String last) {
        this(null, function, slot, comparator, first, last);
    }

    public MultiHaving(Criteria<?> criteria, Function function, Slot slot, String first, String last) {
        this.criteria = criteria;
        this.function = function;
        this.slot = slot;
        this.first = first;
        this.last = last;
    }

    public MultiHaving(Criteria<?> criteria, Function function, Slot slot,
                       MultiComparator comparator, String first, String last) {
        this.criteria = criteria;
        this.function = function;
        this.slot = slot;
        this.comparator = comparator;
        this.first = first;
        this.last = last;
    }

    @Override
    protected String getComparatorString() {
        return this.comparator.getSegment();
    }

    @Override
    public String getSegment() {
        return Placeholders.format(this.getBody(), this.first, this.last);
    }
}
