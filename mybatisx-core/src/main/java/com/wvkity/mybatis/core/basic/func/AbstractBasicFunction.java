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
package com.wvkity.mybatis.core.basic.func;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.Optional;

/**
 * 抽象基础聚合函数
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractBasicFunction implements Function {

    /**
     * {@link Criteria}
     */
    protected Criteria<?> criteria;

    /**
     * 字段名
     */
    protected String column;

    /**
     * 别名
     */
    protected String alias;

    protected Optional<Criteria<?>> optional() {
        return Optional.ofNullable(this.criteria);
    }

    @Override
    public String getColumn() {
        return this.column;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getSegment(boolean isQuery) {
        if (isQuery && Objects.isNotBlank(this.alias)) {
            final boolean hasDot = this.alias.contains(Constants.DOT);
            final StringBuilder builder = new StringBuilder(this.getFuncBody());
            builder.append(" AS ");
            if (hasDot) {
                builder.append(Constants.CHAR_QUOTE).append(this.alias).append(Constants.CHAR_QUOTE);
            } else {
                final String refProp = this.optional().map(Criteria::getReference).orElse("");
                if (Objects.isNotBlank(refProp)) {
                    builder.append(Constants.CHAR_QUOTE).append(refProp).append(Constants.DOT);
                    builder.append(this.alias);
                    builder.append(Constants.CHAR_QUOTE);
                } else {
                    builder.append(this.alias);
                }
            }
            return builder.toString();
        }
        return this.getFuncBody();
    }

}
