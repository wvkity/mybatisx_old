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
package com.github.mybatisx.core.support.select;

import com.github.mybatisx.support.basic.Matched;
import com.github.mybatisx.support.fragment.Fragment;

/**
 * 查询列接口
 * @author wvkity
 * @created 2021-01-09
 * @since 1.0.0
 */
public interface Selection extends Fragment {

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

    /**
     * 获取属性
     * @return 属性
     */
    String getProperty();

    /**
     * 获取引用属性
     * @return 引用属性
     */
    String getRefProp();

    /**
     * 获取字段匹配模式
     * @return {@link Matched}
     */
    Matched getMatched();

    /**
     * 别名
     * @return 别名
     */
    String as();

    /**
     * 设置别名
     * @param alias 别名
     * @return {@link Selection}
     */
    Selection as(final String alias);

    /**
     * {@inheritDoc}
     */
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
