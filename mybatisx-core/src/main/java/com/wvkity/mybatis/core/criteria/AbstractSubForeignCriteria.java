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
import com.wvkity.mybatis.basic.utils.Objects;

/**
 * 抽象子外联表查询条件
 * @param <M> 主表实体类型
 * @param <S> 子查询实体类型
 * @author wvkity
 * @created 2021-04-19
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractSubForeignCriteria<M, S> extends AbstractForeignCriteria<M, S> {

    /**
     * 子查询对象
     */
    protected AbstractSubCriteria<S> subQuery;

    @Override
    public AbstractSubForeignCriteria<M, S> select() {
        this.fetch = true;
        return this;
    }

    @Override
    protected String getGroupByString() {
        this.loadSelectionFrom(this.subQuery);
        return super.getGroupByString();
    }

    @Override
    protected String getSelectString() {
        if (this.segmentManager.hasSelect()) {
            return this.segmentManager.getSelectString();
        } else if (this.fetch) {
            this.loadSelectionFrom(this.subQuery);
            return this.segmentManager.getSelectString();
        }
        return Constants.EMPTY;
    }

    @Override
    public String getTableName(boolean joinAs) {
        final StringBuilder builder = new StringBuilder(60);
        builder.append(Constants.BRACKET_OPEN);
        builder.append(this.subQuery.getSegment());
        builder.append(Constants.BRACKET_CLOSE);
        final String as = this.as();
        if (Objects.isNotBlank(as)) {
            if (joinAs) {
                builder.append(" AS");
            }
            builder.append(Constants.SPACE).append(as);
        }
        return builder.toString();
    }
}
