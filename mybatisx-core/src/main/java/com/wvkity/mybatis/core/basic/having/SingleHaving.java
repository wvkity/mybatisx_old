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
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 分组筛选子句
 * @author wvkity
 * @created 2021-04-30
 * @since 1.0.0
 */
public class SingleHaving extends AbstractHaving {

    private static final long serialVersionUID = 7177851922943695807L;

    /**
     * 比较符
     */
    private SingleComparator comparator = SingleComparator.EQ;
    /**
     * 参数
     */
    private final String arg;

    public SingleHaving(Function function, String arg) {
        this.function = function;
        this.arg = arg;
    }

    public SingleHaving(Function function, Slot slot, String arg) {
        this.function = function;
        this.slot = slot;
        this.arg = arg;
    }

    public SingleHaving(Function function, Slot slot, SingleComparator comparator, String arg) {
        this(null, function, slot, comparator, arg);
    }

    public SingleHaving(Criteria<?> criteria, Function function, Slot slot, String arg) {
        this(criteria, function, slot, SingleComparator.EQ, arg);
    }

    public SingleHaving(Criteria<?> criteria, Function function, Slot slot,
                        SingleComparator comparator, String arg) {
        this.criteria = criteria;
        this.function = function;
        this.slot = slot;
        this.comparator = comparator;
        this.arg = arg;
    }

    @Override
    protected String getComparatorString() {
        return this.comparator.getSegment();
    }

    @Override
    public String getSegment() {
        return this.getBody() + this.arg;
    }

}
