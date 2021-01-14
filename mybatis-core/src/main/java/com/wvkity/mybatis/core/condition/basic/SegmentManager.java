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
import com.wvkity.mybatis.core.segment.Fragment;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * SQL片段管理器
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public class SegmentManager implements Fragment {

    private static final long serialVersionUID = -884244554745201766L;

    /**
     * WHERE条件管理器
     */
    private final WhereManager whereManager = new WhereManager();
    /**
     * 分组管理器
     */
    private final GroupManager groupManager = new GroupManager();
    /**
     * ORDER排序管理器
     */
    private final OrderManager orderManager = new OrderManager();

    /**
     * 添加{@link Expression}对象
     * @param expression {@link Expression}对象
     * @return {@link SegmentManager}
     */
    public SegmentManager where(final Expression expression) {
        Optional.ofNullable(expression).ifPresent(this.whereManager::add);
        return this;
    }

    /**
     * 添加多个{@link Expression}对象
     * @param expressions {@link Expression}集合
     * @return {@link SegmentManager}
     */
    public SegmentManager where(final Collection<Expression> expressions) {
        if (Objects.isNotEmpty(expressions)) {
            this.whereManager.addAll(expressions);
        }
        return this;
    }

    /**
     * 获取where条件
     * @return 条件字符串
     */
    public String getWhereString() {
        return this.whereManager.getSegment();
    }

    /**
     * 获取所有{@link Expression}对象
     * @return {@link Expression}集合
     */
    public List<Expression> getConditions() {
        return this.whereManager.getConditions();
    }

    @Override
    public String getSegment() {
        return this.getWhereString();
    }
}
