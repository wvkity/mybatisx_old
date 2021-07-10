package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.support.group.StandardGroup;
import com.github.mybatisx.core.support.order.StandardOrder;

import java.util.Collection;
import java.util.List;

/**
 * 基础查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface CommonQueryWrapper<T, C extends CommonQueryWrapper<T, C>> extends QueryWrapper<T, C>,
    CommonSelect<T, C>, CommonFunctionSelect<T, C>, MixinWrapper<T, C>, HavingWrapper<T, C> {

    // region Group by methods

    /**
     * 分组
     * @param column 字段名
     * @return {@code this}
     */
    default C colGroup(final String column) {
        return this.group(StandardGroup.group(this, column));
    }

    /**
     * 多个分组
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colGroup(final String... columns) {
        return this.colGroup(Objects.asList(columns));
    }

    /**
     * 多个分组
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colGroup(final Collection<String> columns) {
        return this.group(StandardGroup.group(this, columns));
    }

    /**
     * 多个分组
     * @param columns 字段列表
     * @return {@code this}
     */
    default C colGroupOnly(final String... columns) {
        return this.colGroupWithAlias(null, columns);
    }

    /**
     * 多个分组
     * @param columns 字段列表
     * @return {@code this}
     */
    default C colGroupOnly(final Collection<String> columns) {
        return this.colGroupWithAlias(null, columns);
    }

    /**
     * 多个分组
     * @param tabAlias 表别名
     * @param columns  字段列表
     * @return {@code this}
     */
    default C colGroupWithAlias(final String tabAlias, final String... columns) {
        return this.colGroupWithAlias(tabAlias, Objects.asList(columns));
    }

    /**
     * 多个分组
     * @param tabAlias 表别名
     * @param columns  字段列表
     * @return {@code this}
     */
    default C colGroupWithAlias(final String tabAlias, final Collection<String> columns) {
        return this.group(StandardGroup.groupWithAlias(tabAlias, columns));
    }

    // endregion

    // region Order by methods

    /**
     * 升序
     * @param column 字段名
     * @return {@code this}
     */
    default C colAsc(final String column) {
        return this.order(StandardOrder.asc(this, column));
    }

    /**
     * 升序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colAsc(final String... columns) {
        return this.colAsc(Objects.asList(columns));
    }

    /**
     * 升序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colAsc(final List<String> columns) {
        return this.order(StandardOrder.asc(this, columns));
    }

    /**
     * 升序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colAscOnly(final String... columns) {
        return this.colAscWithAlias(null, Objects.asList(columns));
    }

    /**
     * 升序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colAscOnly(final List<String> columns) {
        return this.colAscWithAlias(null, columns);
    }

    /**
     * 升序
     * @param tabAlias 表别名
     * @param columns  字段名列表
     * @return {@code this}
     */
    default C colAscWithAlias(final String tabAlias, final String... columns) {
        return this.colAscWithAlias(tabAlias, Objects.asList(columns));
    }

    /**
     * 升序
     * @param tabAlias 表别名
     * @param columns  字段名列表
     * @return {@code this}
     */
    default C colAscWithAlias(final String tabAlias, final List<String> columns) {
        return this.order(StandardOrder.ascWithAlias(tabAlias, columns));
    }

    /**
     * 降序
     * @param column 字段名
     * @return {@code this}
     */
    default C colDesc(final String column) {
        return this.order(StandardOrder.desc(this, column));
    }

    /**
     * 降序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colDesc(final String... columns) {
        return this.colDesc(Objects.asList(columns));
    }

    /**
     * 降序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colDesc(final List<String> columns) {
        return this.order(StandardOrder.desc(this, columns));
    }

    /**
     * 降序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colDescOnly(final String... columns) {
        return this.colDescWithAlias(null, Objects.asList(columns));
    }

    /**
     * 降序
     * @param columns 字段名列表
     * @return {@code this}
     */
    default C colDescOnly(final List<String> columns) {
        return this.colDescWithAlias(null, columns);
    }

    /**
     * 降序
     * @param tabAlias 表别名
     * @param columns  字段名列表
     * @return {@code this}
     */
    default C colDescWithAlias(final String tabAlias, final String... columns) {
        return this.colAscWithAlias(tabAlias, Objects.asList(columns));
    }

    /**
     * 降序
     * @param tabAlias 表别名
     * @param columns  字段名列表
     * @return {@code this}
     */
    default C colDescWithAlias(final String tabAlias, final List<String> columns) {
        return this.order(StandardOrder.descWithAlias(tabAlias, columns));
    }

    // endregion

}
