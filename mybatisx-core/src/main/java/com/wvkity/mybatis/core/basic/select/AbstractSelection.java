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
package com.wvkity.mybatis.core.basic.select;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.support.basic.Matched;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Optional;

/**
 * 抽象查询列
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractSelection<E> implements Selection {

    /**
     * 查询接口对象
     */
    protected Criteria<?> criteria;
    /**
     * 表别名
     */
    protected String tableAlias;
    /**
     * 字段
     */
    protected E column;
    /**
     * 字段对应的属性
     */
    protected String property;
    /**
     * 字段别名
     */
    protected String alias;
    /**
     * 实体类引用对象属性
     */
    protected String refProp;
    /**
     * 字段匹配模式
     */
    protected Matched matched;

    /**
     * 获取表别名
     * @return 表别名
     */
    protected String getTableAlias() {
        return Objects.isNotBlank(this.tableAlias) ? this.tableAlias :
            Objects.nonNull(this.criteria) ? this.criteria.as() : "";
    }

    protected Optional<Criteria<?>> optional() {
        return Optional.ofNullable(this.criteria);
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getProperty() {
        return this.property;
    }

    @Override
    public Matched getMatched() {
        return this.matched;
    }

    @Override
    public String getRefProp() {
        return Objects.isNotBlank(this.refProp) ? this.refProp :
            this.optional().map(Criteria::getReference).filter(Objects::isNotBlank).orElse("");
    }

    @Override
    public String as() {
        final String reference = this.getRefProp();
        final boolean refNotEmpty = Objects.isNotBlank(reference);
        if (Objects.isNotBlank(this.alias)) {
            return (refNotEmpty ? (reference + Constants.DOT) : "") + this.alias;
        } else {
            final boolean isUsed = this.optional().map(Criteria::isPropAsAlias)
                .orElse(false);
            return isUsed && Objects.isNotBlank(this.property) ? (refNotEmpty ?
                (reference + Constants.DOT + this.property) : this.property) : "";
        }
    }

    @Override
    public AbstractSelection<E> as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getSegment(boolean isQuery) {
        if (!isQuery && !(this.matched == Matched.STANDARD || this.matched == Matched.IMMEDIATE)) {
            return null;
        }
        final String tabAlias = this.getTableAlias();
        final String realColumn = this.getColumn();
        if (isQuery && Objects.isNotBlank(realColumn)) {
            return Scripts.convertToSelectArg(realColumn.startsWith(Constants.BRACKET_OPEN) ? null : tabAlias,
                realColumn, this.as());
        }
        return Scripts.convertToSelectArg(tabAlias, this.getColumn(), null);
    }
}
