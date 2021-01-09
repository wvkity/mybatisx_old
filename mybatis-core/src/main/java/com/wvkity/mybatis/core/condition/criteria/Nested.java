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
package com.wvkity.mybatis.core.condition.criteria;

import com.wvkity.mybatis.core.condition.expression.Expression;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.List;
import java.util.function.Function;

/**
 * 嵌套条件
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public interface Nested<Chain extends Nested<Chain>> {

    /**
     * and嵌套条件
     * @param conditions 条件列表
     * @return {@link Chain}
     */
    default Chain and(final Expression... conditions) {
        return and(false, conditions);
    }

    /**
     * and嵌套条件
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain and(final List<Expression> conditions) {
        return and(false, conditions);
    }

    /**
     * and嵌套条件
     * @param conditions 条件列表
     * @param not        是否添加NOT
     * @return {@link Chain}
     */
    default Chain and(final boolean not, final Expression... conditions) {
        return and(not, Objects.asList(conditions));
    }

    /**
     * and嵌套条件
     * @param not        是否添加NOT
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    Chain and(final boolean not, final List<Expression> conditions);

    /**
     * and嵌套条件
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain and(final Criteria<?> criteria, final Expression... conditions) {
        return and(false, criteria, conditions);
    }

    /**
     * and嵌套条件
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain and(final Criteria<?> criteria, final List<Expression> conditions) {
        return and(false, criteria, conditions);
    }

    /**
     * and嵌套条件
     * @param not        是否添加NOT
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain and(final boolean not, final Criteria<?> criteria, final Expression... conditions) {
        return and(not, criteria, Objects.asList(conditions));
    }

    /**
     * and嵌套条件
     * @param not        是否添加NOT
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    Chain and(final boolean not, final Criteria<?> criteria, final List<Expression> conditions);

    /**
     * and嵌套条件
     * @param apply {@link Function}
     * @return {@link Chain}
     */
    default Chain and(final Function<Chain, Chain> apply) {
        return and(apply, false);
    }

    /**
     * and嵌套条件
     * @param apply {@link Function}
     * @param not   是否添加not
     * @return {@link Chain}
     */
    Chain and(final Function<Chain, Chain> apply, final boolean not);

    /**
     * or嵌套条件
     * @param conditions 条件列表
     * @return {@link Chain}
     */
    default Chain or(final Expression... conditions) {
        return or(false, conditions);
    }

    /**
     * or嵌套条件
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain or(final List<Expression> conditions) {
        return or(false, conditions);
    }

    /**
     * or嵌套条件
     * @param conditions 条件列表
     * @param not        是否添加NOT
     * @return {@link Chain}
     */
    default Chain or(final boolean not, final Expression... conditions) {
        return or(not, Objects.asList(conditions));
    }

    /**
     * or嵌套条件
     * @param not        是否添加NOT
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    Chain or(final boolean not, final List<Expression> conditions);

    /**
     * or嵌套条件
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain or(final Criteria<?> criteria, final Expression... conditions) {
        return or(false, criteria, conditions);
    }

    /**
     * or嵌套条件
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain or(final Criteria<?> criteria, final List<Expression> conditions) {
        return or(false, criteria, conditions);
    }

    /**
     * or嵌套条件
     * @param not        是否添加NOT
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    default Chain or(final boolean not, final Criteria<?> criteria, final Expression... conditions) {
        return or(not, criteria, Objects.asList(conditions));
    }

    /**
     * or嵌套条件
     * @param not        是否添加NOT
     * @param criteria   {@link Criteria}
     * @param conditions 条件集合
     * @return {@link Chain}
     */
    Chain or(final boolean not, final Criteria<?> criteria, final List<Expression> conditions);

    /**
     * or嵌套条件
     * @param apply {@link Function}
     * @return {@link Chain}
     */
    default Chain or(final Function<Chain, Chain> apply) {
        return or(apply, false);
    }

    /**
     * or嵌套条件
     * @param apply {@link Function}
     * @param not   是否添加not
     * @return {@link Chain}
     */
    Chain or(final Function<Chain, Chain> apply, final boolean not);
}
