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
package io.github.mybatisx.core.criteria.query;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.core.property.Property;
import io.github.mybatisx.core.support.select.Selection;
import io.github.mybatisx.core.support.select.StandardSelection;
import io.github.mybatisx.support.basic.Matched;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.helper.TableHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 查询字段接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类
 * @author wvkity
 * @created 2021-05-14
 * @since 1.0.0
 */
interface LambdaSelect<T, C extends LambdaSelect<T, C>> extends Criteria<T> {

    /**
     * 查询字段
     * @param property 属性
     * @return {@code this}
     */
    default C select(final Property<T, ?> property) {
        return this.select(property, null);
    }

    /**
     * 查询字段
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    default C select(final Property<T, ?> property, final String alias) {
        return this.select(property.toProp(), alias);
    }

    /**
     * 查询字段
     * @param property 属性
     * @return {@code this}
     */
    default C select(final String property) {
        return this.select(property, null);
    }

    /**
     * 查询字段
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    C select(final String property, final String alias);

    /**
     * 查询列
     * @param selection {@link Selection}
     * @return {@code this}
     */
    C select(final Selection selection);

    /**
     * 筛选字段
     * @param accept {@link Predicate}
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C filtrate(final Predicate<Column> accept) {
        final List<Column> columns;
        if (Objects.isNotEmpty((columns = TableHelper.getColumns(this.getEntityClass(), accept)))) {
            for (Column c : columns) {
                this.select(new StandardSelection(this, null, c.getColumn(), null, c.getProperty(), Matched.STANDARD));
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param properties 属性集合({@code Map<别名, 属性名>})
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C select(final Map<String, String> properties) {
        if (Objects.isNotEmpty(properties)) {
            for (Map.Entry<String, String> it : properties.entrySet()) {
                this.select(it.getValue(), it.getKey());
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param properties 属性列表
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C selects(final String... properties) {
        if (!Objects.isEmpty(properties)) {
            for (String it : properties) {
                this.select(it, null);
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @return {@code this}
     */
    default C select(final Property<T, ?> p1, final String as1, final Property<T, ?> p2, final String as2) {
        return this.select(p1, as1).select(p2, as2);
    }

    /**
     * 查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @param p3  属性3
     * @param as3 属性3对应别名
     * @return {@code this}
     */
    default C select(final Property<T, ?> p1, final String as1, final Property<T, ?> p2, final String as2,
                     final Property<T, ?> p3, final String as3) {
        return this.select(p1, as1).select(p2, as2).select(p3, as3);
    }

    /**
     * 查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @return {@code this}
     */
    default C select(final String p1, final String as1, final String p2, final String as2) {
        return this.select(p1, as1).select(p2, as2);
    }

    /**
     * 查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @param p3  属性3
     * @param as3 属性3对应别名
     * @return {@code this}
     */
    default C select(final String p1, final String as1, final String p2, final String as2,
                     final String p3, final String as3) {
        return this.select(p1, as1).select(p2, as2).select(p3, as3);
    }

    /**
     * 忽略查询字段
     * @param property 属性
     * @return {@code this}
     */
    default C ignore(final Property<T, ?> property) {
        return this.ignore(property.toProp());
    }

    /**
     * 忽略查询字段
     * @param property 属性
     * @return {@code this}
     */
    C ignore(final String property);

    /**
     * 忽略查询字段
     * @param properties 属性
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C ignores(final String... properties) {
        if (!Objects.isEmpty(properties)) {
            for (String it : properties) {
                this.ignore(it);
            }
        }
        return (C) this;
    }

}
