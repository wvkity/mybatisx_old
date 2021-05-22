package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.core.property.Property;

/**
 * 基础条件/查询接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface LambdaQueryWrapper<T, C extends LambdaQueryWrapper<T, C>> extends QueryWrapper<T, C>,
    LambdaSelect<T, C> {

    /**
     * 设置map结果中的key值
     * <p>{@code @MapKey("key")}</p>
     * @param property 属性
     * @return {@code this}
     */
    C mapKey(final Property<T, ?> property);
}
