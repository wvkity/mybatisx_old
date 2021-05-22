package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.support.select.Selection;

import java.util.Map;

/**
 * 查询字段接口
 * @param <T> 实体类型
 * @param <C> 子类
 * @author wvkity
 * @created 2021-05-15
 * @since 1.0.0
 */
interface CommonSelect<T, C extends CommonSelect<T, C>> {

    /**
     * 查询字段
     * @param column 字段名
     * @return {@code this}
     */
    default C colSelect(final String column) {
        return this.colSelect(column, null);
    }

    /**
     * 查询字段
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    C colSelect(final String column, final String alias);

    /**
     * 纯SQL查询字段
     * @param sql SQL语句
     * @return {@code this}
     */
    default C nativeSelect(final String sql) {
        return this.nativeSelect(sql, null);
    }

    /**
     * 纯SQL查询字段
     * @param sql   SQL语句
     * @param alias 别名
     * @return {@code this}
     */
    C nativeSelect(final String sql, final String alias);

    /**
     * 查询列
     * @param selection {@link Selection}
     * @return {@code this}
     */
    C select(final Selection selection);

    /**
     * 查询字段
     * @param columns 字段集合({@code Map<别名, 字段名>})
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colSelect(final Map<String, String> columns) {
        if (Objects.isNotEmpty(columns)) {
            for (Map.Entry<String, String> it : columns.entrySet()) {
                this.colSelect(it.getValue(), it.getKey());
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param columns 字段列表
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colSelects(final String... columns) {
        if (Objects.isEmpty(columns)) {
            for (String it : columns) {
                this.colSelect(it);
            }
        }
        return (C) this;
    }

    /**
     * 查询字段
     * @param c1  字段1
     * @param as1 字段1对应别名
     * @param c2  字段2
     * @param as2 字段2对应别名
     * @return {@code this}
     */
    default C colSelect(final String c1, final String as1, final String c2, final String as2) {
        return this.colSelect(c1, as1).colSelect(c2, as2);
    }

    /**
     * 查询字段
     * @param c1  字段1
     * @param as1 字段1对应别名
     * @param c2  字段2
     * @param as2 字段2对应别名
     * @param c3  字段3
     * @param as3 字段3对应别名
     * @return {@code this}
     */
    default C colSelect(final String c1, final String as1, final String c2,
                        final String as2, final String c3, final String as3) {
        return this.colSelect(c1, as1).colSelect(c2, as2).colSelect(c3, as3);
    }

    /**
     * 忽略查询字段
     * @param column 字段名
     * @return {@code this}
     */
    C colIgnore(final String column);

    /**
     * 忽略查询字段
     * @param columns 字段列表
     * @return {@code this}
     */
    @SuppressWarnings("unchecked")
    default C colIgnores(final String... columns) {
        if (!Objects.isEmpty(columns)) {
            for (String it : columns) {
                this.colIgnore(it);
            }
        }
        return (C) this;
    }
}
