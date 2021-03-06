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
package io.github.mybatisx.core.support.having;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.support.func.Function;
import io.github.mybatisx.exception.MyBatisException;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 抽象分组筛选子句
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractHaving implements Having {

    protected static final String DEF_PLACEHOLDER_COLUMN = "(?<!\\\\):@";
    /**
     * {@link Criteria}
     */
    protected Criteria<?> criteria;
    /**
     * 聚合函数对象
     */
    protected Function function;
    /**
     * {@link Slot}
     */
    protected Slot slot = Slot.AND;

    /**
     * 获取SQL语句
     * @return SQL语句
     */
    protected String getBody() {
        final StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(this.slot)) {
            builder.append(this.slot.getSegment());
        } else {
            builder.append(Slot.AND.getSegment());
        }
        final String comparator = this.getComparatorString();
        if (Objects.isBlank(comparator)) {
            throw new MyBatisException("Comparators cannot be empty.");
        }
        builder.append(Constants.SPACE);
        final String funcBody = this.function.getFuncBody();
        if (comparator.contains(Constants.DEF_STR_COLUMN_PH)) {
            builder.append(comparator.replaceAll(DEF_PLACEHOLDER_COLUMN, this.function.getFuncBody()));
        } else {
            builder.append(funcBody).append(Constants.SPACE).append(comparator).append(Constants.SPACE);
        }
        return builder.toString();
    }

    @Override
    public Function getFunc() {
        return this.function;
    }

    @Override
    public Slot getSlot() {
        return this.slot;
    }

    /**
     * 获取比较符字符串
     * @return 比较符字符串
     */
    protected abstract String getComparatorString();
}
