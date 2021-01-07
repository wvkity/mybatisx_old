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
package com.wvkity.mybatis.core.condition.basic;

import com.wvkity.mybatis.core.condition.expression.Expression;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.immutable.ImmutableList;
import com.wvkity.mybatis.core.segment.AbstractFragmentList;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * WHERE条件片段
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public class WhereSegment extends AbstractFragmentList<Expression> {

    private static final long serialVersionUID = 5417095393578894180L;

    /**
     * 获取所有条件表达式片段
     * @return {@link Expression}集合
     */
    final List<Expression> getConditions() {
        return this.isEmpty() ? ImmutableList.of() : ImmutableList.of(new ArrayList<>(this.fragments));
    }

    @Override
    public String getSegment() {
        if (!this.isEmpty()) {
           final List<String> conditions = new ArrayList<>(this.fragments.size());
           for (Expression it: this.fragments) {
               if (Objects.nonNull(it)) {
                   final String condition = it.getSegment();
                   if (Objects.isNotBlank(condition)) {
                       conditions.add(condition);
                   }
               }
           }
           if (Objects.isNotEmpty(conditions)) {
               return String.join(Constants.SPACE, conditions).trim();
           }
        }
        return Constants.EMPTY;
    }
}
