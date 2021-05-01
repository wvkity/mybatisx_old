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
import com.wvkity.mybatis.core.basic.select.Selection;

import java.util.List;

/**
 * 抽象嵌套子查询
 * @param <R> 实体类型
 * @author wvkity
 * @created 2021-04-20
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractNestedSubCriteria<R> extends AbstractSubCriteria<R> {

    @Override
    public AbstractNestedSubCriteria<R> select() {
        this.fetch = true;
        return this;
    }

    @Override
    public String getTableName(boolean joinAs) {
        final StringBuilder builder = new StringBuilder(100);
        builder.append(Constants.BRACKET_OPEN).append(this.master.getSegment()).append(Constants.BRACKET_CLOSE);
        final String as = this.as();
        if (Objects.isNotBlank(as)) {
            if (joinAs) {
                builder.append(" AS");
            }
            builder.append(Constants.SPACE).append(as);
        }
        return builder.toString();
    }

    @Override
    protected List<Selection> getSelects() {
        if (this.segmentManager.hasSelect()) {
            return this.segmentManager.getSelects();
        } else if (this.fetch) {
            return this.master.getSelects();
        }
        return null;
    }

    @Override
    protected String getSelectString() {
        this.loadSelectionFrom(this.master);
        return this.segmentManager.getSelectString();
    }

}
