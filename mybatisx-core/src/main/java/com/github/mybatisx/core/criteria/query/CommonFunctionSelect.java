package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.core.support.func.Function;

/**
 * 聚合函数查询列接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-08
 * @since 1.0.0
 */
public interface CommonFunctionSelect<T, C extends CommonFunctionSelect<T, C>> {

    // region Count function methods

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
     * {@code Count}聚合函数
     * @param column 字段名
     * @return {@code this}
     */
    default C colCount(final String column) {
        return this.colCount(column, null, false);
    }

    /**
     * {@code Count}聚合函数
     * @param column   字段名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C colCount(final String column, final boolean distinct) {
        return this.colCount(column, null, distinct);
    }

    /**
     * {@code Count}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    default C colCount(final String column, final String alias) {
        return this.colCount(column, alias, false);
    }

    /**
     * {@code Count}聚合函数
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    C colCount(final String column, final String alias, final boolean distinct);

    /**
     * {@code Count}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @return {@code this}
     */
    default C colCount(final String tabAlias, final String column, final String alias) {
        return this.colCount(tabAlias, column, alias, false);
    }

    /**
     * {@code Count}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    C colCount(final String tabAlias, final String column, final String alias, final boolean distinct);

    // endregion

    // region Sum function methods

    /**
     * {@code Sum}聚合函数
     * @param column 字段名
     * @return {@code this}
     */
    default C colSum(final String column) {
        return this.colSum(column, null, (Integer) null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    default C colSum(final String column, final String alias) {
        return this.colSum(column, alias, (Integer) null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C colSum(final String column, final String alias, final boolean distinct) {
        return this.colSum(column, alias, (Integer) null, distinct);
    }

    /**
     * {@code Sum}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    default C colSum(final String column, final String alias, final Integer scale) {
        return this.colSum(column, alias, scale, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C colSum(final String column, final String alias, final Integer scale, final boolean distinct);

    /**
     * {@code Sum}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @return {@code this}
     */
    default C colSum(final String tabAlias, final String column, final String alias) {
        return this.colSum(tabAlias, column, alias, null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C colSum(final String tabAlias, final String column, final String alias, final boolean distinct) {
        return this.colSum(tabAlias, column, alias, null, distinct);
    }

    /**
     * {@code Sum}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C colSum(final String tabAlias, final String column, final String alias, final Integer scale) {
        return this.colSum(tabAlias, column, alias, scale, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C colSum(final String tabAlias, final String column, final String alias, final Integer scale,
             final boolean distinct);

    // endregion

    // region Avg function methods

    /**
     * {@code Avg}聚合函数
     * @param column 字段名
     * @return {@code this}
     */
    default C colAvg(final String column) {
        return this.colAvg(column, null, (Integer) null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    default C colAvg(final String column, final String alias) {
        return this.colAvg(column, alias, (Integer) null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C colAvg(final String column, final String alias, final boolean distinct) {
        return this.colAvg(column, alias, (Integer) null, distinct);
    }

    /**
     * {@code Avg}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    default C colAvg(final String column, final String alias, final Integer scale) {
        return this.colAvg(column, alias, scale, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C colAvg(final String column, final String alias, final Integer scale, final boolean distinct);

    /**
     * {@code Avg}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @return {@code this}
     */
    default C colAvg(final String tabAlias, final String column, final String alias) {
        return this.colAvg(tabAlias, column, alias, null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C colAvg(final String tabAlias, final String column, final String alias, final boolean distinct) {
        return this.colAvg(tabAlias, column, alias, null, distinct);
    }

    /**
     * {@code Avg}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C colAvg(final String tabAlias, final String column, final String alias, final Integer scale) {
        return this.colAvg(tabAlias, column, alias, scale, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C colAvg(final String tabAlias, final String column, final String alias, final Integer scale,
             final boolean distinct);

    // endregion

    // region Min function methods

    /**
     * {@code Min}聚合函数
     * @param column 字段名
     * @return {@code this}
     */
    default C colMin(final String column) {
        return this.colMin(column, null, (Integer) null);
    }

    /**
     * {@code Min}聚合函数
     * @param column 字段名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    default C colMin(final String column, final Integer scale) {
        return this.colMin(column, null, scale);
    }

    /**
     * {@code Min}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    default C colMin(final String column, final String alias) {
        return this.colMin(column, alias, (Integer) null);
    }

    /**
     * {@code Min}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    C colMin(final String column, final String alias, final Integer scale);

    /**
     * {@code Min}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @return {@code this}
     */
    default C colMin(final String tabAlias, final String column, final String alias) {
        return this.colMin(tabAlias, column, alias, null);
    }

    /**
     * {@code Min}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    C colMin(final String tabAlias, final String column, final String alias, final Integer scale);

    // endregion

    // region Max function methods

    /**
     * {@code Max}聚合函数
     * @param column 字段名
     * @return {@code this}
     */
    default C colMax(final String column) {
        return this.colMax(column, null, (Integer) null);
    }

    /**
     * {@code Max}聚合函数
     * @param column 字段名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    default C colMax(final String column, final Integer scale) {
        return this.colMax(column, null, scale);
    }

    /**
     * {@code Max}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@code this}
     */
    default C colMax(final String column, final String alias) {
        return this.colMax(column, alias, (Integer) null);
    }

    /**
     * {@code Max}聚合函数
     * @param column 字段名
     * @param alias  别名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    C colMax(final String column, final String alias, final Integer scale);

    /**
     * {@code Max}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @return {@code this}
     */
    default C colMax(final String tabAlias, final String column, final String alias) {
        return this.colMax(tabAlias, column, alias, null);
    }

    /**
     * {@code Max}聚合函数
     * @param tabAlias 表别名
     * @param column   字段名
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    C colMax(final String tabAlias, final String column, final String alias, final Integer scale);

    // endregion

    // region All function methods

    /**
     * 所有聚合函数
     * @param column 字段名
     * @return {@code this}
     */
    default C colFunc(final String column) {
        return this.colFunc(column, (String) null);
    }

    /**
     * 所有聚合函数
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @return {@code this}
     */
    default C colFunc(final String column, final String aliasPrefix) {
        return this.colFunc(column, aliasPrefix, (Integer) null, false);
    }

    /**
     * 所有聚合函数
     * @param column 字段名
     * @param scale  保留小数位数
     * @return {@code this}
     */
    default C colFunc(final String column, final Integer scale) {
        return this.colFunc(column, null, scale, false);
    }

    /**
     * 所有聚合函数
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param distinct    是否去重
     * @return {@code this}
     */
    default C colFunc(final String column, final String aliasPrefix, final boolean distinct) {
        return this.colFunc(column, aliasPrefix, (Integer) null, distinct);
    }

    /**
     * 所有聚合函数
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param scale       保留小数位数
     * @return {@code this}
     */
    default C colFunc(final String column, final String aliasPrefix, final Integer scale) {
        return this.colFunc(column, aliasPrefix, scale, false);
    }

    /**
     * 所有聚合函数
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param scale       保留小数位数
     * @param distinct    是否去重
     * @return {@code this}
     */
    C colFunc(final String column, final String aliasPrefix, final Integer scale, final boolean distinct);

    /**
     * 所有聚合函数
     * @param tabAlias    表别名
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @return {@code this}
     */
    default C colFunc(final String tabAlias, final String column, final String aliasPrefix) {
        return this.colFunc(tabAlias, column, aliasPrefix, null, false);
    }

    /**
     * 所有聚合函数
     * @param tabAlias    表别名
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param distinct    是否去重
     * @return {@code this}
     */
    default C colFunc(final String tabAlias, final String column, final String aliasPrefix, final boolean distinct) {
        return this.colFunc(tabAlias, column, aliasPrefix, null, distinct);
    }

    /**
     * 所有聚合函数
     * @param tabAlias    表别名
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param scale       保留小数位数
     * @return {@code this}
     */
    default C colFunc(final String tabAlias, final String column, final String aliasPrefix, final Integer scale) {
        return this.colFunc(tabAlias, column, aliasPrefix, scale, false);
    }

    /**
     * 所有聚合函数
     * @param tabAlias    表别名
     * @param column      字段名
     * @param aliasPrefix 聚合函数别名前缀
     * @param scale       保留小数位数
     * @param distinct    是否去重
     * @return {@code this}
     */
    C colFunc(final String tabAlias, final String column, final String aliasPrefix, final Integer scale,
              final boolean distinct);

    // endregion

    /**
     * 纯SQL聚合函数
     * @param funcBody 聚合函数主体部分
     * @return {@link C}
     */
    default C nativeFunc(final String funcBody) {
        return this.nativeFunc(funcBody, null);
    }

    /**
     * 纯SQL聚合函数
     * @param funcBody 聚合函数主体部分
     * @param alias    别名
     * @return {@link C}
     */
    C nativeFunc(final String funcBody, final String alias);

    /**
     * 添加聚合函数
     * @param function 聚合函数
     * @return {@code this}
     */
    C function(final Function function);

}
