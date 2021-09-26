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
package io.github.mybatisx.core.support.func;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.support.criteria.Criteria;

/**
 * 抽象聚合函数
 * @author wvkity
 * @created 2021-04-28
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractFunction extends AbstractBasicFunction {

    /**
     * 类型
     */
    protected Func func;
    /**
     * 表别名
     */
    protected String tableAlias;
    /**
     * 是否去重
     */
    protected boolean distinct = false;
    /**
     * 保留小数位数
     */
    protected Integer scale;

    /**
     * 获取表别名
     * @return 表别名
     */
    protected String getTableAlias() {
        return Objects.isNotBlank(this.tableAlias) ? this.tableAlias :
            this.optional().map(Criteria::as).orElse(Constants.EMPTY);
    }

    @Override
    public Func getFuncType() {
        return this.func;
    }

    @Override
    public boolean isDistinct() {
        return this.distinct;
    }

    @Override
    public Integer getScale() {
        return this.scale;
    }

    /**
     * 是否保留小数位数
     * @return boolean
     */
    protected boolean hasScale() {
        return this.scale != null && this.scale > -1;
    }

    @Override
    public String getFuncBody() {
        final StringBuilder builder = new StringBuilder(60);
        final String tabAs = this.getTableAlias();
        final boolean notIsCount = this.func != Func.COUNT;
        final boolean isScale = this.hasScale() && notIsCount;
        final boolean isDistinct = notIsCount || (!Constants.STAR.equals(this.column)
            && !Constants.DEF_STR_ONE.equals(this.column) && !Constants.DEF_STR_ZERO.equals(this.column));
        if (isScale) {
            builder.append("CAST(");
        }
        builder.append(this.func.getSegment()).append(Constants.BRACKET_OPEN);
        if (isDistinct) {
            if (this.distinct) {
                builder.append("DISTINCT ");
            }
            if (Objects.isNotBlank(tabAs)) {
                builder.append(tabAs).append(Constants.DOT);
            }
        }
        builder.append(this.column);
        builder.append(Constants.BRACKET_CLOSE);
        if (isScale) {
            builder.append(" AS DECIMAL(38, ").append(this.scale).append("))");
        }
        return builder.toString();
    }


}
