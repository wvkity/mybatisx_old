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

import com.wvkity.mybatis.core.condition.basic.select.Selection;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 添加查询字段接口
 * @param <T>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface Select<T, Chain extends Select<T, Chain>> {

    /**
     * 筛选查询字段
     * @param accept {@link Predicate}
     * @return {@link Chain}
     */
    Chain filtrate(final Predicate<Column> accept);

    /**
     * 添加查询字段
     * @param property 属性
     * @return {@link Chain}
     */
    default Chain select(final Property<T, ?> property) {
        return select(property, null);
    }

    /**
     * 添加查询字段
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    Chain select(final Property<T, ?> property, final String alias);

    /**
     * 添加多个查询字段
     * @param properties 属性列表
     * @return {@link Chain}
     */
    @SuppressWarnings({"unchecked"})
    default Chain selects(final Property<T, ?>... properties) {
        return selects(Objects.asList(properties));
    }

    /**
     * 添加多个查询字段
     * @param properties 属性集合
     * @return {@link Chain}
     */
    Chain selects(final Collection<Property<T, ?>> properties);

    /**
     * 添加查询字段
     * @param property 属性
     * @return {@link Chain}
     */
    default Chain select(final String property) {
        return select(property, null);
    }

    /**
     * 添加查询字段
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    Chain select(final String property, final String alias);

    /**
     * 添加多个查询字段
     * @param properties 属性列表
     * @return {@link Chain}
     */
    Chain selects(final String... properties);

    /**
     * 添加多个查询字段
     * @param properties 别名-属性Map集合
     * @return {@link Chain}
     */
    Chain selects(final Map<String, String> properties);

    /**
     * 添加查询字段
     * @param column 字段
     * @return {@link Chain}
     */
    default Chain colSelect(final String column) {
        return colSelect(column, null);
    }

    /**
     * 添加查询字段
     * @param column 字段
     * @param alias  别名
     * @return {@link Chain}
     */
    Chain colSelect(final String column, final String alias);

    /**
     * 添加多个查询字段
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain colSelects(final String... columns) {
        return this.colSelects(Objects.asList(columns));
    }

    /**
     * 添加多个查询字段
     * @param columns 字段集合
     * @return {@link Chain}
     */
    Chain colSelects(final Collection<String> columns);

    /**
     * 添加多个查询字段
     * @param columns 别名-字段Map集合
     * @return {@link Chain}
     */
    Chain colSelects(final Map<String, String> columns);

    /**
     * 添加多个查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @return {@link Chain}
     */
    default Chain select(final Property<T, ?> p1, final String as1, final Property<T, ?> p2, final String as2) {
        return this.select(p1, as1).select(p2, as2);
    }

    /**
     * 添加多个查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @param p3  属性3
     * @param as3 属性3对应别名
     * @return {@link Chain}
     */
    default Chain select(final Property<T, ?> p1, final String as1, final Property<T, ?> p2,
                         final String as2, final Property<T, ?> p3, final String as3) {
        return this.select(p1, as1).select(p2, as2).select(p3, as3);
    }

    /**
     * 添加多个查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @return {@link Chain}
     */
    default Chain select(final String p1, final String as1, final String p2, final String as2) {
        return this.select(p1, as1).select(p2, as2);
    }

    /**
     * 添加多个查询字段
     * @param p1  属性1
     * @param as1 属性1对应别名
     * @param p2  属性2
     * @param as2 属性2对应别名
     * @param p3  属性3
     * @param as3 属性3对应别名
     * @return {@link Chain}
     */
    default Chain select(final String p1, final String as1, final String p2,
                         final String as2, final String p3, final String as3) {
        return this.select(p1, as1).select(p2, as2).select(p3, as3);
    }

    /**
     * 添加多个查询字段
     * @param c1  字段1
     * @param as1 字段1对应别名
     * @param c2  字段2
     * @param as2 字段2对应别名
     * @return {@link Chain}
     */
    default Chain colSelect(final String c1, final String as1, final String c2, final String as2) {
        return this.colSelect(c1, as1).colSelect(c2, as2);
    }

    /**
     * 添加多个查询字段
     * @param c1  字段1
     * @param as1 字段1对应别名
     * @param c2  字段2
     * @param as2 字段2对应别名
     * @param c3  字段3
     * @param as3 字段3对应别名
     * @return {@link Chain}
     */
    default Chain colSelect(final String c1, final String as1, final String c2,
                            final String as2, final String c3, final String as3) {
        return this.colSelect(c1, as1).colSelect(c2, as2).colSelect(c3, as3);
    }

    /**
     * 纯SQL查询
     * @param sql SQL语句
     * @return {@link Chain}
     */
    default Chain nativeSelect(final String sql) {
        return this.nativeSelect(sql, null);
    }

    /**
     * 纯SQL查询
     * @param sql   SQL语句
     * @param alias 别名
     * @return {@link Chain}
     */
    Chain nativeSelect(final String sql, final String alias);

    /**
     * 添加查询列
     * @param selection {@link Selection}
     * @return {@link Chain}
     */
    Chain select(final Selection selection);

    /**
     * 过滤查询字段
     * @param property 属性
     * @return {@link Chain}
     */
    Chain exclude(final Property<T, ?> property);

    /**
     * 过滤查询字段
     * @param property 属性
     * @return {@link Chain}
     */
    Chain exclude(final String property);

    /**
     * 过滤查询字段
     * @param column 字段
     * @return {@link Chain}
     */
    Chain colExclude(final String column);

    /**
     * 过滤查询字段
     * @param properties 属性列表
     * @return {@link Chain}
     */
    @SuppressWarnings("unchecked")
    Chain excludes(final Property<T, ?>... properties);

    /**
     * 过滤查询字段
     * @param properties 属性列表
     * @return {@link Chain}
     */
    default Chain excludes(final String... properties) {
        return excludes(Objects.asList(properties));
    }

    /**
     * 过滤查询字段
     * @param properties 属性集合
     * @return {@link Chain}
     */
    Chain excludes(final Collection<String> properties);

    /**
     * 过滤查询字段
     * @param columns 字段列表
     * @return {@link Chain}
     */
    default Chain colExcludes(final String... columns) {
        return colExcludes(Objects.asList(columns));
    }

    /**
     * 过滤查询字段
     * @param columns 字段集合
     * @return {@link Chain}
     */
    Chain colExcludes(final Collection<String> columns);

}
