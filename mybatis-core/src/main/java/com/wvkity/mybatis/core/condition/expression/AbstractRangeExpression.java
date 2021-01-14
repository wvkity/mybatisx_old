/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;

/**
 * 范围条件表达式
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public abstract class AbstractRangeExpression<E> extends AbstractExpression<E> {

    private static final long serialVersionUID = 5735695407715135956L;

    /**
     * 多个值
     */
    protected Collection<Object> values;

    @Override
    public String getSegment() {
        if (Objects.isNull(this.fragment)) {
            return null;
        }
        if (this.fragment instanceof String) {
            return Scripts.convertToConditionArg(this.symbol, this.slot, this.getAlias(), (String) this.fragment,
                this.defPlaceholders(this.values));
        }
        return Scripts.convertToConditionArg(this.symbol, this.slot, this.getAlias(), (Column) this.fragment,
            this.defPlaceholders(this.values));
    }

    public AbstractRangeExpression<E> values(Collection<Object> values) {
        this.values = values;
        return this;
    }
}
