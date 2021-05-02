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
import com.wvkity.mybatis.core.basic.func.Function;
import com.wvkity.mybatis.core.property.Property;

import java.util.Collection;

/**
 * 聚合函数接口
 * @param <T>     实体类型
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-04-28
 * @since 1.0.0
 */
public interface FunctionWrapper<T, Chain extends FunctionWrapper<T, Chain>> {

    // region Count function methods

    /**
     * COUNT聚合函数
     * @return {@link Chain}
     */
    default Chain count() {
        return count(null);
    }

    /**
     * COUNT聚合函数
     * @param alias 别名
     * @return {@link Chain}
     */
    Chain count(final String alias);

    /**
     * COUNT聚合函数
     * @param property {@link Property}
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain count(final Property<T, ?> property, final String alias) {
        return this.count(property, alias, false);
    }

    /**
     * COUNT聚合函数
     * @param property {@link Property}
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain count(final Property<T, ?> property, final String alias, final boolean distinct);

    /**
     * COUNT聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain count(final String property, final String alias) {
        return this.count(property, alias, false);
    }

    /**
     * COUNT聚合函数
     * @param property 属性
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain count(final String property, final String alias, final boolean distinct);

    /**
     * COUNT聚合函数
     * @return {@link Chain}
     */
    default Chain colCount() {
        return this.colCount("*", null);
    }

    /**
     * COUNT聚合函数
     * @param alias 别名
     * @return {@link Chain}
     */
    default Chain colCount(final String alias) {
        return this.colCount("*", alias);
    }

    /**
     * COUNT聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@link Chain}
     */
    default Chain colCount(final String column, final String alias) {
        return this.colCount(column, alias, false);
    }

    /**
     * COUNT聚合函数
     * @param column   字段名
     * @param alias    别名
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain colCount(final String column, final String alias, final boolean distinct);

    /**
     * COUNT聚合函数
     * @param tableAlias 表别名
     * @param column     字段名
     * @param alias      别名
     * @return {@link Chain}
     */
    default Chain colCount(final String tableAlias, final String column, final String alias) {
        return this.colCount(tableAlias, column, alias, false);
    }

    /**
     * COUNT聚合函数
     * @param tableAlias 表别名
     * @param column     字段名
     * @param alias      别名
     * @param distinct   是否去重
     * @return {@link Chain}
     */
    Chain colCount(final String tableAlias, final String column, final String alias, final boolean distinct);

    // endregion

    // region Sum function methods

    /**
     * SUM聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain sum(final Property<T, ?> property, final String alias) {
        return this.sum(property, alias, null, false);
    }

    /**
     * SUM聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain sum(final String property, final String alias) {
        return this.sum(property, alias, null, false);
    }

    /**
     * SUM聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    default Chain sum(final Property<T, ?> property, final String alias, final Integer scale) {
        return this.sum(property, alias, scale, false);
    }

    /**
     * SUM聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    default Chain sum(final String property, final String alias, final Integer scale) {
        return this.sum(property, alias, scale, false);
    }

    /**
     * SUM聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain sum(final Property<T, ?> property, final String alias, final Integer scale, final boolean distinct);

    /**
     * SUM聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain sum(final String property, final String alias, final Integer scale, final boolean distinct);

    /**
     * SUM聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @return {@link Chain}
     */
    default Chain colSum(final String tableAlias, final String column) {
        return this.colSum(tableAlias, column, null);
    }

    /**
     * SUM聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @return {@link Chain}
     */
    default Chain colSum(final String tableAlias, final String column, final String alias) {
        return this.colSum(tableAlias, column, alias, null, false);
    }

    /**
     * SUM聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @param distinct   是否去重
     * @return {@link Chain}
     */
    default Chain colSum(final String tableAlias, final String column, final String alias, final boolean distinct) {
        return this.colSum(tableAlias, column, alias, null, distinct);
    }

    /**
     * SUM聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @param scale      保留小数位数
     * @return {@link Chain}
     */
    default Chain colSum(final String tableAlias, final String column, final String alias, final Integer scale) {
        return this.colSum(tableAlias, column, alias, scale, false);
    }

    /**
     * SUM聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @param scale      保留小数位数
     * @param distinct   是否去重
     * @return {@link Chain}
     */
    Chain colSum(final String tableAlias, final String column, final String alias, final Integer scale,
                 final boolean distinct);

    // endregion

    // region Avg function methods

    /**
     * AVG聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain avg(final Property<T, ?> property, final String alias) {
        return this.avg(property, alias, null, false);
    }

    /**
     * AVG聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain avg(final String property, final String alias) {
        return this.avg(property, alias, null, false);
    }

    /**
     * AVG聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    default Chain avg(final Property<T, ?> property, final String alias, final Integer scale) {
        return this.avg(property, alias, scale, false);
    }

    /**
     * AVG聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    default Chain avg(final String property, final String alias, final Integer scale) {
        return this.avg(property, alias, scale, false);
    }

    /**
     * AVG聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain avg(final Property<T, ?> property, final String alias, final Integer scale, final boolean distinct);

    /**
     * AVG聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @param distinct 是否去重
     * @return {@link Chain}
     */
    Chain avg(final String property, final String alias, final Integer scale, final boolean distinct);

    /**
     * AVG聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @return {@link Chain}
     */
    default Chain colAvg(final String tableAlias, final String column) {
        return this.colAvg(tableAlias, column, null);
    }

    /**
     * AVG聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @return {@link Chain}
     */
    default Chain colAvg(final String tableAlias, final String column, final String alias) {
        return this.colAvg(tableAlias, column, alias, null, false);
    }

    /**
     * AVG聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @param distinct   是否去重
     * @return {@link Chain}
     */
    default Chain colAvg(final String tableAlias, final String column, final String alias, final boolean distinct) {
        return this.colAvg(tableAlias, column, alias, null, distinct);
    }

    /**
     * AVG聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @param scale      保留小数位数
     * @return {@link Chain}
     */
    default Chain colAvg(final String tableAlias, final String column, final String alias, final Integer scale) {
        return this.colAvg(tableAlias, column, alias, scale, false);
    }

    /**
     * AVG聚合函数
     * @param tableAlias 表别名
     * @param column     字段
     * @param alias      别名
     * @param scale      保留小数位数
     * @param distinct   是否去重
     * @return {@link Chain}
     */
    Chain colAvg(final String tableAlias, final String column, final String alias, final Integer scale,
                 final boolean distinct);

    // endregion

    // region Min function methods

    /**
     * MIN聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain min(final Property<T, ?> property, final String alias) {
        return this.min(property, alias, null);
    }

    /**
     * MIN聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain min(final String property, final String alias) {
        return this.min(property, alias, null);
    }

    /**
     * MIN聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    Chain min(final Property<T, ?> property, final String alias, final Integer scale);

    /**
     * MIN聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    Chain min(final String property, final String alias, final Integer scale);

    /**
     * MIN聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@link Chain}
     */
    default Chain colMin(final String column, final String alias) {
        return this.colMin(column, alias, (Integer) null);
    }

    /**
     * MIN聚合函数
     * @param column 字段名
     * @param alias  别名
     * @param scale  保留小数位数
     * @return {@link Chain}
     */
    default Chain colMin(final String column, final String alias, final Integer scale) {
        return this.colMin(null, column, alias, scale);
    }

    /**
     * MIN聚合函数
     * @param tableAlias 表别名
     * @param column     字段名
     * @param alias      别名
     * @return {@link Chain}
     */
    default Chain colMin(final String tableAlias, final String column, final String alias) {
        return this.colMin(tableAlias, column, alias, null);
    }

    /**
     * MIN聚合函数
     * @param tableAlias 表别名
     * @param column     字段名
     * @param alias      别名
     * @param scale      保留小数位数
     * @return {@link Chain}
     */
    Chain colMin(final String tableAlias, final String column, final String alias, final Integer scale);

    // endregion

    // region Max function methods

    /**
     * MAX聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain max(final Property<T, ?> property, final String alias) {
        return this.max(property, alias, null);
    }

    /**
     * MAX聚合函数
     * @param property 属性
     * @param alias    别名
     * @return {@link Chain}
     */
    default Chain max(final String property, final String alias) {
        return this.max(property, alias, null);
    }

    /**
     * MAX聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    Chain max(final Property<T, ?> property, final String alias, final Integer scale);

    /**
     * MAX聚合函数
     * @param property 属性
     * @param alias    别名
     * @param scale    保留小数位数
     * @return {@link Chain}
     */
    Chain max(final String property, final String alias, final Integer scale);

    /**
     * MAX聚合函数
     * @param column 字段名
     * @param alias  别名
     * @return {@link Chain}
     */
    default Chain colMax(final String column, final String alias) {
        return this.colMax(column, alias, (Integer) null);
    }

    /**
     * MAX聚合函数
     * @param column 字段名
     * @param alias  别名
     * @param scale  保留小数位数
     * @return {@link Chain}
     */
    default Chain colMax(final String column, final String alias, final Integer scale) {
        return this.colMax(null, column, alias, scale);
    }

    /**
     * MAX聚合函数
     * @param tableAlias 表别名
     * @param column     字段名
     * @param alias      别名
     * @return {@link Chain}
     */
    default Chain colMax(final String tableAlias, final String column, final String alias) {
        return this.colMax(tableAlias, column, alias, null);
    }

    /**
     * MAX聚合函数
     * @param tableAlias 表别名
     * @param column     字段名
     * @param alias      别名
     * @param scale      保留小数位数
     * @return {@link Chain}
     */
    Chain colMax(final String tableAlias, final String column, final String alias, final Integer scale);

    // endregion

    // region All function methods

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @return {@link Chain}
     */
    default Chain func(final Property<T, ?> property, final String aliasPrefix) {
        return this.func(property, aliasPrefix, null);
    }

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @return {@link Chain}
     */
    default Chain func(final String property, final String aliasPrefix) {
        return this.func(property, aliasPrefix, null);
    }

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @return {@link Chain}
     */
    Chain func(final Property<T, ?> property, final String aliasPrefix, final Integer scale);

    /**
     * 所有聚合函数
     * @param property    属性
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @return {@link Chain}
     */
    Chain func(final String property, final String aliasPrefix, final Integer scale);

    /**
     * 所有聚合函数
     * @param column      字段名
     * @param aliasPrefix 别名前缀
     * @return {@link Chain}
     */
    default Chain colFunc(final String column, final String aliasPrefix) {
        return this.colFunc(column, aliasPrefix, (Integer) null);
    }

    /**
     * 所有聚合函数
     * @param column      字段名
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @return {@link Chain}
     */
    Chain colFunc(final String column, final String aliasPrefix, final Integer scale);

    /**
     * 所有聚合函数
     * @param tableAlias  表别名
     * @param column      字段名
     * @param aliasPrefix 别名前缀
     * @return {@link Chain}
     */
    default Chain colFunc(final String tableAlias, final String column, final String aliasPrefix) {
        return this.colFunc(tableAlias, column, aliasPrefix, null);
    }

    /**
     * 所有聚合函数
     * @param tableAlias  表别名
     * @param column      字段名
     * @param aliasPrefix 别名前缀
     * @param scale       保留小数位数
     * @return {@link Chain}
     */
    Chain colFunc(final String tableAlias, final String column, final String aliasPrefix, final Integer scale);

    // endregion

    /**
     * 纯SQL聚合函数
     * @param funcBody 聚合函数主体部分
     * @return {@link Chain}
     */
    default Chain nativeFunc(final String funcBody) {
        return this.nativeFunc(funcBody, null);
    }

    /**
     * 纯SQL聚合函数
     * @param funcBody 聚合函数主体部分
     * @param alias    别名
     * @return {@link Chain}
     */
    Chain nativeFunc(final String funcBody, final String alias);

    /**
     * 添加聚合函数
     * @param function {@link Function}
     * @return {@link Chain}
     */
    Chain func(final Function function);

    /**
     * 添加多个聚合函数
     * @param functions {@link Function}列表
     * @return {@link Chain}
     */
    default Chain func(final Function... functions) {
        return this.func(Objects.asList(functions));
    }

    /**
     * 添加多个聚合函数
     * @param functions {@link Function}列表
     * @return {@link Chain}
     */
    Chain func(final Collection<Function> functions);

}
