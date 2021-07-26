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
package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.Objects;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.core.support.group.Group;
import com.github.mybatisx.core.support.order.FuncOrder;
import com.github.mybatisx.core.support.order.Order;

import java.util.List;

/**
 * 混合接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-10
 * @since 1.0.0
 */
interface MixinWrapper<T, C extends MixinWrapper<T, C>> {

    // region Aggregate function methods

    /**
     * {@code Count}聚合函数
     * @return {@code this}
     */
    default C count() {
        return this.count(null);
    }

    /**
     * {@code Count}聚合函数
     * @param alias 别名
     * @return {@code this}
     */
    C count(final String alias);

    /**
     * 纯SQL聚合函数
     * @param funcBody 聚合函数主体部分
     * @return {@code this}
     */
    default C nativeFunc(final String funcBody) {
        return this.nativeFunc(funcBody, null);
    }

    /**
     * 纯SQL聚合函数
     * @param funcBody 聚合函数主体部分
     * @param alias    别名
     * @return {@code this}
     */
    C nativeFunc(final String funcBody, final String alias);

    /**
     * 添加聚合函数
     * @param function 聚合函数
     * @return {@code this}
     */
    C function(final Function function);

    // endregion

    // region Group by methods

    /**
     * 所有查询列分组
     * @return {@code this}
     */
    default C group() {
        return this.group(true);
    }

    /**
     * 是否所有查询列分组
     * @param all 是否所有
     * @return {@code this}
     */
    C group(final boolean all);

    /**
     * 纯SQL分组
     * @param groupBody 分组语句
     * @return {@code this}
     */
    C nativeGroup(final String groupBody);

    /**
     * 分组
     * @param group {@link Group}
     * @return {@code this}
     */
    C group(final Group group);

    // endregion

    // region Order by methods

    /**
     * 升序
     * @param funcAlias 聚合函数别名
     * @return {@code this}
     */
    C funcAsc(final String funcAlias);

    /**
     * 升序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    default C funcAsc(final String... funcAliases) {
        return this.funcAsc(Objects.asList(funcAliases));
    }

    /**
     * 升序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    C funcAsc(final List<String> funcAliases);

    /**
     * 升序
     * @param function {@link Function}
     * @return {@code this}
     */
    default C funcAsc(final Function function) {
        return this.order(FuncOrder.asc(function));
    }

    /**
     * 降序
     * @param funcAlias 聚合函数别名
     * @return {@code this}
     */
    C funcDesc(final String funcAlias);

    /**
     * 降序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    default C funcDesc(final String... funcAliases) {
        return this.funcDesc(Objects.asList(funcAliases));
    }

    /**
     * 降序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    C funcDesc(final List<String> funcAliases);

    /**
     * 降序
     * @param function {@link Function}
     * @return {@code this}
     */
    default C funcDesc(final Function function) {
        return this.order(FuncOrder.asc(function));
    }

    /**
     * 排序(纯SQL)
     * @param orderBy 排序语句
     * @return {@code this}
     */
    C nativeOrder(final String orderBy);

    /**
     * 排序
     * @param order {@link Order}
     * @return {@code this}
     */
    C order(final Order order);

    /**
     * 排序
     * @param orders {@link Order}列表
     * @return {@code this}
     */
    C order(final List<Order> orders);

    // endregion

}
