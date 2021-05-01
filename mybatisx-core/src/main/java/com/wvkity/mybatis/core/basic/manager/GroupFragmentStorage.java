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
package com.wvkity.mybatis.core.basic.manager;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.core.basic.group.Group;
import com.wvkity.mybatis.support.segment.AbstractFragmentList;
import com.wvkity.mybatis.support.segment.Fragment;

import java.util.stream.Collectors;

/**
 * 分组片段存储器
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public class GroupFragmentStorage extends AbstractFragmentList<Group> {

    private static final long serialVersionUID = 6060283847559352754L;

    @Override
    public String getSegment() {
        if (!this.isEmpty()) {
            return " GROUP BY " + this.fragments.stream().map(Fragment::getSegment)
                .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return Constants.EMPTY;
    }
}
