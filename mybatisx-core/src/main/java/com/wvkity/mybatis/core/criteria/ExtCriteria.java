package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.core.convert.PropertyConverter;
import com.wvkity.mybatis.core.support.select.Selection;
import com.wvkity.mybatis.support.constant.Join;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.expr.Expression;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 条件包装接口
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface ExtCriteria<T> extends Criteria<T> {

    /**
     * 是否抓取联表查询字段
     * @return boolean
     */
    default boolean isFetch() {
        return false;
    }

    /**
     * 是否存在查询字段
     * @return boolean
     */
    default boolean hasSelect() {
        return false;
    }

    /**
     * 是否拼接keep orderby注释
     * @return boolean
     */
    boolean isKeepOrderBy();

    /**
     * 是否去重
     * @return boolean
     */
    boolean isDistinct();

    /**
     * 是否全部字段分组
     * @return boolean
     */
    boolean isGroupAll();

    /**
     * 获取查询列
     * @return {@link Selection}列表
     */
    default List<Selection> fetchSelects() {
        return null;
    }

    /**
     * 获取联表方式
     * @return {@link Join}
     */
    default Join getJoin() {
        return null;
    }

    /**
     * 转换成抽象扩展条件
     * @return {@link AbstractExtCriteria}
     */
    AbstractExtCriteria<T> transfer();

    /**
     * 获取属性转换器
     * @return {@link PropertyConverter}
     */
    PropertyConverter getConverter();

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

    /**
     * 获取查询字段语句
     * @param self 是否为自身
     * @return 查询字段语句
     */
    String getSelectSegment(final boolean self);
}
