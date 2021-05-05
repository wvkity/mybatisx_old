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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.expr.Expression;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 嵌套查询
 * @author wvkity
 * @created 2021-05-05
 * @since 1.0.0
 */
public interface NestSubQueryWrapper<R, Chain extends NestSubQueryWrapper<R, Chain>> {

    /**
     * 创建子查询条件
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery() {
        return this.nestQuery((String) null);
    }

    /**
     * 创建子查询条件
     * @param alias 别名
     * @return {@link NestedSubQuery}
     */
    NestedSubQuery<R> nestQuery(final String alias);

    /**
     * 创建子查询条件
     * @param action {@link Consumer}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final Consumer<NestedSubQuery<R>> action) {
        return this.nestQuery((String) null, action);
    }

    /**
     * 创建子查询条件
     * @param alias  别名
     * @param action {@link Consumer}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final String alias, final Consumer<NestedSubQuery<R>> action) {
        final NestedSubQuery<R> instance = this.nestQuery(alias);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 创建子查询条件
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final Expression... expressions) {
        return this.nestQuery(Objects.asList(expressions));
    }

    /**
     * 创建子查询条件
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final Collection<Expression> expressions) {
        final NestedSubQuery<R> instance = this.nestQuery();
        instance.where(expressions);
        return instance;
    }

    /**
     * 创建子查询条件
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final String alias, final Expression... expressions) {
        return this.nestQuery(alias, Objects.asList(expressions));
    }

    /**
     * 创建子查询条件
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final String alias, final Collection<Expression> expressions) {
        final NestedSubQuery<R> instance = this.nestQuery(alias);
        instance.where(expressions);
        return instance;
    }

    /**
     * 创建子查询条件
     * @param query {@link AbstractQueryCriteria}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query) {
        return this.nestQuery(query, (String) null);
    }

    /**
     * 创建子查询条件
     * @param query {@link AbstractQueryCriteria}
     * @param alias 别名
     * @return {@link NestedSubQuery}
     */
    NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query, final String alias);

    /**
     * 创建子查询条件
     * @param query  {@link AbstractQueryCriteria}
     * @param action {@link Consumer}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query,
                                        final Consumer<NestedSubQuery<R>> action) {
        return this.nestQuery(query, null, action);
    }

    /**
     * 创建子查询条件
     * @param query  {@link AbstractQueryCriteria}
     * @param alias  别名
     * @param action {@link Consumer}
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query, final String alias,
                                        final Consumer<NestedSubQuery<R>> action) {
        final NestedSubQuery<R> instance = this.nestQuery(query, alias);
        if (action != null) {
            action.accept(instance);
        }
        return instance;
    }

    /**
     * 创建子查询条件
     * @param query       {@link AbstractQueryCriteria}
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query, final Expression... expressions) {
        return this.nestQuery(query, Objects.asList(expressions));
    }

    /**
     * 创建子查询条件
     * @param query       {@link AbstractQueryCriteria}
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query,
                                        final Collection<Expression> expressions) {
        return this.nestQuery(query, null, expressions);
    }

    /**
     * 创建子查询条件
     * @param query       {@link AbstractQueryCriteria}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query, final String alias,
                                        final Expression... expressions) {
        return this.nestQuery(query, alias, Objects.asList(expressions));
    }

    /**
     * 创建子查询条件
     * @param query       {@link AbstractQueryCriteria}
     * @param alias       别名
     * @param expressions {@link Expression}列表
     * @return {@link NestedSubQuery}
     */
    default NestedSubQuery<R> nestQuery(final AbstractQueryCriteria<R> query, final String alias,
                                        final Collection<Expression> expressions) {
        final NestedSubQuery<R> instance = this.nestQuery(query, alias);
        instance.where(expressions);
        return instance;
    }
}
