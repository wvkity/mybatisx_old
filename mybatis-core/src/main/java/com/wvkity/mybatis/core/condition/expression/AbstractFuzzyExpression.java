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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.core.constant.LikeMode;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;

/**
 * 抽象模糊条件表达式
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-08
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractFuzzyExpression<E> extends AbstractExpression<E> {

    /**
     * 匹配模式
     */
    protected LikeMode mode;
    /**
     * 转义字符
     */
    protected Character escape;

    @Override
    public String getSegment() {
        if (Objects.isNull(this.fragment)) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        final LikeMode matching = this.mode == null ? LikeMode.ANYWHERE : this.mode;
        if (this.fragment instanceof String) {
            builder.append(Scripts.convertToConditionArg(this.symbol, this.slot, this.getAlias(),
                (String) this.fragment, this.defPlaceholder(matching.getSegment(String.valueOf(this.value)))));
        } else if (this.fragment instanceof Column) {
            builder.append(Scripts.convertToConditionArg(this.symbol, this.slot, this.getAlias(),
                (Column) this.fragment, this.defPlaceholder(matching.getSegment(String.valueOf(this.value)))));
        }
        if (Objects.nonNull(this.escape)) {
            builder.append(" ESCAPE ").append("'").append(this.escape).append("'");
        }
        return builder.toString();
    }

    public AbstractFuzzyExpression mode(LikeMode mode) {
        this.mode = mode;
        return this;
    }

    public AbstractFuzzyExpression escape(Character escape) {
        this.escape = escape;
        return this;
    }
}
