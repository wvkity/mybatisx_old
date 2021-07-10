package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.property.Property;

import java.util.Collection;
import java.util.List;

/**
 * 基础条件/查询接口(支持lambda语法)
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface LambdaQueryWrapper<T, C extends LambdaQueryWrapper<T, C>> extends QueryWrapper<T, C>,
    LambdaSelect<T, C>, LambdaFunctionSelect<T, C>, MixinWrapper<T, C>, HavingWrapper<T, C> {

    /**
     * 设置map结果中的key值
     * <p>{@code @MapKey("key")}</p>
     * @param property 属性
     * @return {@code this}
     */
    C mapKey(final Property<T, ?> property);

    // region Group by methods

    /**
     * 分组
     * @param property Lambda属性
     * @return {@code this}
     */
    C group(final Property<T, ?> property);

    /**
     * 分组
     * @param property 属性
     * @return {@code this}
     */
    C group(final String property);

    /**
     * 多个分组
     * @param properties 属性列表
     * @return {@code this}
     */
    default C group(final String... properties) {
        return this.group(Objects.asList(properties));
    }

    /**
     * 多个分组
     * @param properties 属性列表
     * @return {@code this}
     */
    C group(final Collection<String> properties);

    // endregion

    // region Order by methods

    /**
     * 升序
     * @param property Lambda属性
     * @return {@code this}
     */
    C asc(final Property<T, ?> property);

    /**
     * 升序
     * @param property Lambda属性
     * @return {@code this}
     */
    C asc(final String property);

    /**
     * 升序
     * @param properties 属性列表
     * @return {@code this}
     */
    default C asc(final String... properties) {
        return this.asc(Objects.asList(properties));
    }

    /**
     * 升序
     * @param properties 属性列表
     * @return {@code this}
     */
    C asc(final List<String> properties);

    /**
     * 降序
     * @param property Lambda属性
     * @return {@code this}
     */
    C desc(final Property<T, ?> property);

    /**
     * 降序
     * @param property Lambda属性
     * @return {@code this}
     */
    C desc(final String property);

    /**
     * 降序
     * @param properties 属性列表
     * @return {@code this}
     */
    default C desc(final String... properties) {
        return this.asc(Objects.asList(properties));
    }

    /**
     * 降序
     * @param properties 属性列表
     * @return {@code this}
     */
    C desc(final List<String> properties);

    // endregion

}
