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
package com.wvkity.mybatis.core.condition.basic.select;

import com.wvkity.mybatis.core.segment.Fragment;

/**
 * 查询列接口
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface Select extends Fragment {

    /**
     * 获取字段名
     * @return 字段名
     */
    String getColumn();

    /**
     * 获取别名
     * @return 别名
     */
    String getAlias();

    @Override
    default String getSegment() {
        return getSegment(true);
    }

    /**
     * 获取SQL片段
     * @param isQuery 是否应用为查询
     * @return SQL片段
     */
    String getSegment(final boolean isQuery);
}
