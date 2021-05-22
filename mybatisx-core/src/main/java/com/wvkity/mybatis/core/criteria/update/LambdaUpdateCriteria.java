package com.wvkity.mybatis.core.criteria.update;

import com.wvkity.mybatis.core.criteria.support.LambdaCriteriaWrapper;
import com.wvkity.mybatis.core.property.Property;

/**
 * 基础条件/更新接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-18
 * @since 1.0.0
 */
public interface LambdaUpdateCriteria<T, C extends LambdaUpdateCriteria<T, C>> extends UpdateWrapper<T, C>,
    LambdaCriteriaWrapper<T, C> {

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@code this}
     */
    <V> C set(final Property<T, V> property, final V value);

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @param <V>      值类型
     * @return {@code this}
     */
    <V> C setIfAbsent(final Property<T, V> property, final V value);

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C set(final String property, final Object value);

    /**
     * 更新字段值
     * @param property 属性
     * @param value    值
     * @return {@code this}
     */
    C setIfAbsent(final String property, final Object value);

    /**
     * 更新字段值
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param <V1> 值类型
     * @param <V2> 值类型
     * @return {@code this}
     */
    default <V1, V2> C set(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2, final V2 v2) {
        return this.set(p1, v1).set(p2, v2);
    }

    /**
     * 更新字段值
     * @param p1   属性1
     * @param v1   属性1对应值
     * @param p2   属性2
     * @param v2   属性2对应值
     * @param p3   属性3
     * @param v3   属性3对应值
     * @param <V1> 值类型
     * @param <V2> 值类型
     * @param <V3> 值类型
     * @return {@code this}
     */
    default <V1, V2, V3> C set(final Property<T, V1> p1, final V1 v1, final Property<T, V2> p2,
                               final V2 v2, final Property<T, V3> p3, final V3 v3) {
        return this.set(p1, v1).set(p2, v2).set(p3, v3);
    }

    /**
     * 更新字段值
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @return {@code this}
     */
    default <V1, V2, V3> C set(final String p1, final Object v1, final String p2, final Object v2) {
        return this.set(p1, v1).set(p2, v2);
    }

    /**
     * 更新字段值
     * @param p1 属性1
     * @param v1 属性1对应值
     * @param p2 属性2
     * @param v2 属性2对应值
     * @param p3 属性3
     * @param v3 属性3对应值
     * @return {@code this}
     */
    default <V1, V2, V3> C set(final String p1, final Object v1, final String p2,
                               final Object v2, final String p3, final Object v3) {
        return this.set(p1, v1).set(p2, v2).set(p3, v3);
    }
}