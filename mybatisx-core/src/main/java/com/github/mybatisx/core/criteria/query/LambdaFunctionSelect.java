package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.core.support.func.Function;

/**
 * 聚合函数查询列接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-08
 * @since 1.0.0
 */
interface LambdaFunctionSelect<T, C extends LambdaFunctionSelect<T, C>> {

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
     * @param property Lambda属性
     * @param alias    别名
     * @return {@code this}
     */
    default C count(final Property<T, ?> property, final String alias) {
        return this.count(property, alias, false);
    }

    /**
     * {@code Count}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    C count(final Property<T, ?> property, final String alias, final boolean distinct);

    /**
     * {@code Count}聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    default C count(final String property, final String alias) {
        return this.count(property, alias, false);
    }

    /**
     * {@code Count}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    C count(final String property, final String alias, final boolean distinct);

    // endregion

    // region Sum function methods

    /**
     * {@code Sum}聚合函数
     * @param property Lambda属性
     * @return {@code this}
     */
    default C sum(final Property<T, ?> property) {
        return this.sum(property, null, null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @return {@code this}
     */
    default C sum(final Property<T, ?> property, final String alias) {
        return this.sum(property, alias, null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C sum(final Property<T, ?> property, final String alias, final boolean distinct) {
        return this.sum(property, alias, null, distinct);
    }

    /**
     * {@code Sum}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C sum(final Property<T, ?> property, final String alias, final Integer scale) {
        return this.sum(property, alias, scale, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C sum(final Property<T, ?> property, final String alias, final Integer scale, final boolean distinct);

    /**
     * {@code Sum}聚合函数
     * @param property 属性
     * @return {@code this}
     */
    default C sum(final String property) {
        return this.sum(property, null, null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    default C sum(final String property, final String alias) {
        return this.sum(property, alias, null, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C sum(final String property, final String alias, final boolean distinct) {
        return this.sum(property, alias, null, distinct);
    }

    /**
     * {@code Sum}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C sum(final String property, final String alias, final Integer scale) {
        return this.sum(property, alias, scale, false);
    }

    /**
     * {@code Sum}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C sum(final String property, final String alias, final Integer scale, final boolean distinct);

    // endregion

    // region Avg function methods

    /**
     * {@code Avg}聚合函数
     * @param property Lambda属性
     * @return {@code this}
     */
    default C avg(final Property<T, ?> property) {
        return this.avg(property, null, null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @return {@code this}
     */
    default C avg(final Property<T, ?> property, final String alias) {
        return this.avg(property, alias, null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C avg(final Property<T, ?> property, final String alias, final boolean distinct) {
        return this.avg(property, alias, null, distinct);
    }

    /**
     * {@code Avg}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C avg(final Property<T, ?> property, final String alias, final Integer scale) {
        return this.avg(property, alias, scale, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C avg(final Property<T, ?> property, final String alias, final Integer scale, final boolean distinct);

    /**
     * {@code Avg}聚合函数
     * @param property 属性
     * @return {@code this}
     */
    default C avg(final String property) {
        return this.avg(property, null, null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    default C avg(final String property, final String alias) {
        return this.avg(property, alias, null, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@code this}
     */
    default C avg(final String property, final String alias, final boolean distinct) {
        return this.avg(property, alias, null, distinct);
    }

    /**
     * {@code Avg}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C avg(final String property, final String alias, final Integer scale) {
        return this.avg(property, alias, scale, false);
    }

    /**
     * {@code Avg}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@code this}
     */
    C avg(final String property, final String alias, final Integer scale, final boolean distinct);

    // endregion

    // region Min function methods

    /**
     * {@code Min}聚合函数
     * @param property Lambda属性
     * @return {@code this}
     */
    default C min(final Property<T, ?> property) {
        return this.min(property, null, null);
    }

    /**
     * {@code Min}聚合函数
     * @param property Lambda属性
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C min(final Property<T, ?> property, final Integer scale) {
        return this.min(property, null, scale);
    }

    /**
     * {@code Min}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @return {@code this}
     */
    default C min(final Property<T, ?> property, final String alias) {
        return this.min(property, alias, null);
    }

    /**
     * {@code Min}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    C min(final Property<T, ?> property, final String alias, final Integer scale);

    /**
     * {@code Min}聚合函数
     * @param property 属性
     * @return {@code this}
     */
    default C min(final String property) {
        return this.min(property, null, null);
    }

    /**
     * {@code Min}聚合函数
     * @param property 属性
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C min(final String property, final Integer scale) {
        return this.min(property, null, scale);
    }

    /**
     * {@code Min}聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    default C min(final String property, final String alias) {
        return this.min(property, alias, null);
    }

    /**
     * {@code Min}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    C min(final String property, final String alias, final Integer scale);

    // endregion

    // region Max function methods

    /**
     * {@code Max}聚合函数
     * @param property Lambda属性
     * @return {@code this}
     */
    default C max(final Property<T, ?> property) {
        return this.max(property, null, null);
    }

    /**
     * {@code Max}聚合函数
     * @param property Lambda属性
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C max(final Property<T, ?> property, final Integer scale) {
        return this.max(property, null, scale);
    }

    /**
     * {@code Max}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @return {@code this}
     */
    default C max(final Property<T, ?> property, final String alias) {
        return this.max(property, alias, null);
    }

    /**
     * {@code Max}聚合函数
     * @param property Lambda属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    C max(final Property<T, ?> property, final String alias, final Integer scale);

    /**
     * {@code Max}聚合函数
     * @param property 属性
     * @return {@code this}
     */
    default C max(final String property) {
        return this.max(property, null, null);
    }

    /**
     * {@code Max}聚合函数
     * @param property 属性
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C max(final String property, final Integer scale) {
        return this.max(property, null, scale);
    }

    /**
     * {@code Max}聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@code this}
     */
    default C max(final String property, final String alias) {
        return this.max(property, alias, null);
    }

    /**
     * {@code Max}聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@code this}
     */
    C max(final String property, final String alias, final Integer scale);

    // endregion

    // region All function methods

    /**
     * 所有聚合函数
     * @param property Lambda属性
     * @return {@code this}
     */
    default C func(final Property<T, ?> property) {
        return this.func(property, (String) null);
    }

    /**
     * 所有聚合函数
     * @param property    Lambda属性
     * @param aliasPrefix 别名前缀
     * @return {@code this}
     */
    default C func(final Property<T, ?> property, final String aliasPrefix) {
        return this.func(property, aliasPrefix, null, false);
    }

    /**
     * 所有聚合函数
     * @param property Lambda属性
     * @param scale    保留小数位数
     * @return {@code this}
     */
    default C func(final Property<T, ?> property, final Integer scale) {
        return this.func(property, null, scale, false);
    }

    /**
     * 所有聚合函数
     * @param property    Lambda属性
     * @param aliasPrefix 别名前缀
     * @param distinct    是否去重
     * @return {@code this}
     */
    default C func(final Property<T, ?> property, final String aliasPrefix, final boolean distinct) {
        return this.func(property, aliasPrefix, null, distinct);
    }

    /**
     * 所有聚合函数
     * @param property    Lambda属性
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @return {@code this}
     */
    default C func(final Property<T, ?> property, final String aliasPrefix, final Integer scale) {
        return this.func(property, aliasPrefix, scale, false);
    }

    /**
     * 所有聚合函数
     * @param property    Lambda属性
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @param distinct    是否去重
     * @return {@code this}
     */
    C func(final Property<T, ?> property, final String aliasPrefix, final Integer scale, final boolean distinct);

    /**
     * 所有聚合函数
     * @param property 属性
     * @return {@code this}
     */
    default C func(final String property) {
        return this.func(property, null);
    }

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @return {@code this}
     */
    default C func(final String property, final String aliasPrefix) {
        return this.func(property, aliasPrefix, null, false);
    }

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @param distinct    是否去重
     * @return {@code this}
     */
    default C func(final String property, final String aliasPrefix, final boolean distinct) {
        return this.func(property, aliasPrefix, null, distinct);
    }

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @return {@code this}
     */
    default C func(final String property, final String aliasPrefix, final Integer scale) {
        return this.func(property, aliasPrefix, scale, false);
    }

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @param distinct    是否去重
     * @return {@code this}
     */
    C func(final String property, final String aliasPrefix, final Integer scale, final boolean distinct);

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
