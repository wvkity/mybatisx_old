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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.condition.basic.Matched;
import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.constant.Symbol;

/**
 * 条件表达式
 * @author wvkity
 * @created 2021-01-03
 * @since 1.0.0
 */
public interface Expression {

    /**
     * 获取{@link Criteria}对象
     * @return {@link Criteria}
     */
    Criteria<?> getCriteria();

    /**
     * 获取字段/属性
     * @return 字段/属性
     */
    String getTarget();

    /**
     * 获取{@link Slot}对象
     * @return {@link Slot}
     */
    Slot getSlot();

    /**
     * 获取条件符号
     * @return {@link Symbol}
     */
    Symbol getSymbol();

    /**
     * 获取表别名
     * @return 表别名
     */
    String getAlias();

    /**
     * 设置表别名
     * @param alias 别名
     * @return {@link Expression}
     */
    Expression alias(final String alias);

    /**
     * 获取模式
     * @return {@link Matched}
     */
    Matched getExprMode();

    /**
     * 设置{@link Criteria}对象
     * @param criteria {@link Criteria}
     * @return {@link Expression}
     */
    Expression criteria(final Criteria<?> criteria);

    /**
     * 设置{@link Criteria}对象
     * @param criteria {@link Criteria}
     * @return {@link Expression}
     */
    default Expression setIfNecessary(final Criteria<?> criteria) {
        if (getCriteria() == null) {
            criteria(criteria);
        }
        return this;
    }
}
