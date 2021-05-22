package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.core.convert.PropertyConverter;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.expr.Expression;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 条件包装接口
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface ExtCriteria<T> extends Criteria<T> {

    /**
     * 转换成抽象扩展条件
     * @return {@link AbstractExtCriteria}
     */
    AbstractExtCriteria<T> transfer();

    /**
     * 获取属性转换器
     * @return {@link PropertyConverter}
     */
    PropertyConverter getConvert();

    /**
     * 添加{@link Expression}
     * @param action {@link Consumer}
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Consumer<Criteria<T>> action);

    /**
     * 添加{@link Expression}
     * @param expression {@link Expression}
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Expression<?> expression);

    /**
     * 添加多个{@link Expression}
     * @param expressions {@link Expression}列表
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Expression<?>... expressions);

    /**
     * 添加多个{@link Expression}
     * @param expressions {@link Expression}集合
     * @return {@link Criteria}
     */
    ExtCriteria<T> where(final Collection<Expression<?>> expressions);

}
