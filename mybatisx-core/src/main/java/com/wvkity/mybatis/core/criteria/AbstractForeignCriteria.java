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

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.constant.Join;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 抽象连表条件
 * @param <M> 主表实体类型
 * @param <S> 从表实体类型
 * @author wvkity
 * @created 2021-04-12
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractForeignCriteria<M, S> extends AbstractQueryCriteria<S> {

    /**
     * 主条件对象
     */
    protected AbstractQueryCriteria<M> master;
    /**
     * 连接模式
     */
    protected Join join;

    /**
     * 添加联表查询条件
     * @param otherProperty 属性
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> onMaster(final Property<M, ?> otherProperty) {
        return this.onMaster(this.master.convert(otherProperty));
    }

    /**
     * 添加联表查询条件
     * @param otherProperty 属性
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> onMaster(final String otherProperty) {
        if (otherProperty != null) {
            this.ce(this.master, otherProperty);
        }
        return this;
    }

    /**
     * 添加联表查询条件
     * @param otherColumn 字段名
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> onMasterOfCol(final String otherColumn) {
        if (otherColumn != null) {
            this.colCe(this.master, otherColumn);
        }
        return this;
    }

    /**
     * 添加联表查询条件
     * @param property 属性
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> onSlave(final Property<S, ?> property) {
        return this.onSlave(this.convert(property));
    }

    /**
     * 添加联表查询条件
     * @param property 属性
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> onSlave(final String property) {
        this.ce(property, this.master);
        return this;
    }

    /**
     * 添加联表查询条件
     * @param column 字段名
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> onSlaveOfCol(final String column) {
        if (column != null) {
            this.colCe(column, this.master);
        }
        return this;
    }

    /**
     * 添加联表查询条件
     * @param action {@link Consumer}
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> on(final Consumer<AbstractCriteria<S>> action) {
        Optional.ofNullable(action).ifPresent(it -> it.accept(this));
        return this;
    }

    /**
     * 添加联表查询条件
     * @param action {@link BiConsumer}
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> on(final BiConsumer<AbstractCriteria<M>, AbstractCriteria<S>> action) {
        Optional.ofNullable(action).ifPresent(it -> it.accept(this.master, this));
        return this;
    }

    /**
     * 抓取联表查询字段
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> fetch() {
        return this.fetch(true);
    }

    /**
     * 是否抓取联表查询字段
     * @param fetch 是否抓取
     * @return {@code this}
     */
    public AbstractForeignCriteria<M, S> fetch(final boolean fetch) {
        this.fetch = fetch;
        return this;
    }

    public AbstractQueryCriteria<M> getMaster() {
        return master;
    }

    public Join getJoin() {
        return join;
    }

    @Override
    protected String getSelectString() {
        if (this.segmentManager.hasSelect()) {
            return this.segmentManager.getSelectString();
        } else if (this.fetch) {
            this.select();
            return this.segmentManager.getSelectString();
        }
        return Constants.EMPTY;
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder(60);
        builder.append(this.join.getSegment());
        builder.append(this.getTableName(false));
        if (this.isHasCondition()) {
            builder.append(" ON ");
            final String condition = this.segmentManager.getWhereString();
            if (DEF_PATTERN_AND_OR.matcher(condition).matches()) {
                builder.append(condition.replaceFirst(DEF_REGEX_AND_OR_STR, "$2"));
            } else {
                builder.append(condition);
            }
        }
        return builder.toString();
    }

    @Override
    public String getWhereSegment() {
        return this.getSegment();
    }

}
