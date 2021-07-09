package com.github.mybatisx.core.criteria.query;

import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.criteria.support.GenericCriteriaWrapper;
import com.github.mybatisx.core.support.func.Function;
import com.github.mybatisx.core.support.group.Group;
import com.github.mybatisx.core.support.order.FuncOrder;
import com.github.mybatisx.core.support.order.Order;

import java.util.List;

/**
 * 通用条件/查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface GenericQueryCriteria<T, C extends GenericQueryCriteria<T, C>> extends CommonQueryCriteria<T, C>,
    LambdaQueryCriteria<T, C>, GenericCriteriaWrapper<T, C> {

    /**
     * {@inheritDoc}
     */
    @Override
    default C count() {
        return this.count(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    C count(final String alias);

    /**
     * {@inheritDoc}
     */
    @Override
    default C nativeFunc(final String funcBody) {
        return this.nativeFunc(funcBody, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    C nativeFunc(final String funcBody, final String alias);

    /**
     * {@inheritDoc}
     */
    @Override
    C function(final Function function);

    /**
     * {@inheritDoc}
     */
    @Override
    default C group() {
        return this.group(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    C group(final boolean all);

    /**
     * {@inheritDoc}
     */
    @Override
    C group(final Group group);

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcAsc(final String funcAlias) {
        return this.order(FuncOrder.asc(this.getFunction(funcAlias)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcAsc(final String... funcAliases) {
        return this.funcAsc(Objects.asList(funcAliases));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    C funcAsc(final List<String> funcAliases);

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcAsc(final Function function) {
        return this.order(FuncOrder.asc(function));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcDesc(final String funcAlias) {
        return this.order(FuncOrder.desc(this.getFunction(funcAlias)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcDesc(final String... funcAliases) {
        return CommonQueryCriteria.super.funcDesc(funcAliases);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcDesc(final List<String> funcAliases) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default C funcDesc(final Function function) {
        return this.order(FuncOrder.desc(function));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    C nativeOrder(final String orderBy);

    /**
     * {@inheritDoc}
     */
    @Override
    C order(final Order order);

    /**
     * {@inheritDoc}
     */
    @Override
    C order(final List<Order> orders);

}
