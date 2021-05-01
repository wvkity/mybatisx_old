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
package com.wvkity.mybatis.core.condition.basic;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.core.condition.basic.group.Group;
import com.wvkity.mybatis.support.segment.AbstractFragmentList;
import com.wvkity.mybatis.support.segment.Fragment;

import java.util.stream.Collectors;

/**
 * 分组片段管理器
 * @author wvkity
 * @created 2021-01-12
 * @since 1.0.0
 */
public class GroupManager extends AbstractFragmentList<Group> {

    private static final long serialVersionUID = -2313453087586057198L;

    @Override
    public String getSegment() {
        if (!isEmpty()) {
            return " GROUP BY " + this.fragments.stream().map(Fragment::getSegment)
                .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return Constants.EMPTY;
    }
}