package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.property.Property;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.core.support.group.Group;
import com.github.mybatisx.core.support.order.FuncOrder;
import com.github.mybatisx.core.support.order.Order;

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
    LambdaSelect<T, C>, LambdaFunctionSelect<T, C> {

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

    /**
     * 所有查询列分组
     * @return {@code this}
     */
    default C group() {
        return this.group(true);
    }

    /**
     * 是否所有查询列分组
     * @param all 是否所有
     * @return {@code this}
     */
    C group(final boolean all);

    /**
     * 分组
     * @param group {@link Group}
     * @return {@code this}
     */
    C group(final Group group);

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
     * 升序
     * @param funcAlias 聚合函数别名
     * @return {@code this}
     */
    default C funcAsc(final String funcAlias) {
        return this.funcAsc(this.getFunction(funcAlias));
    }

    /**
     * 升序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    default C funcAsc(final String... funcAliases) {
        return this.funcAsc(Objects.asList(funcAliases));
    }

    /**
     * 升序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    C funcAsc(final List<String> funcAliases);

    /**
     * 升序
     * @param function {@link Function}
     * @return {@code this}
     */
    default C funcAsc(final Function function) {
        return this.order(FuncOrder.asc(function));
    }

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

    /**
     * 降序
     * @param funcAlias 聚合函数别名
     * @return {@code this}
     */
    default C funcDesc(final String funcAlias) {
        return this.funcDesc(this.getFunction(funcAlias));
    }

    /**
     * 降序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    default C funcDesc(final String... funcAliases) {
        return this.funcDesc(Objects.asList(funcAliases));
    }

    /**
     * 降序
     * @param funcAliases 聚合函数别名列表
     * @return {@code this}
     */
    C funcDesc(final List<String> funcAliases);

    /**
     * 降序
     * @param function {@link Function}
     * @return {@code this}
     */
    default C funcDesc(final Function function) {
        return this.order(FuncOrder.desc(function));
    }

    /**
     * 排序(纯SQL)
     * @param orderBy 排序语句
     * @return {@link C}
     */
    C nativeOrder(final String orderBy);

    /**
     * 排序
     * @param order {@link Order}
     * @return {@code this}
     */
    C order(final Order order);

    /**
     * 排序
     * @param orders {@link Order}列表
     * @return {@code this}
     */
    C order(final List<Order> orders);

    // endregion

}
