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
package com.wvkity.mybatis.core.segment;

import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;

/**
 * 零散碎片列表
 * @param <E> 碎片类型
 * @author wvkity
 * @created 2021-01-16
 * @since 1.0.0
 */
public interface FragmentCollection<E> extends Fragment {

    /**
     * 添加零散片段
     * @param fragment 零散片段
     */
    void add(final E fragment);

    /**
     * 添加多个零散片段
     * @param fragments 零散片段集合
     */
    void addAll(final Collection<E> fragments);

    /**
     * 检查零散碎片是否为空
     * @return boolean
     */
    boolean isEmpty();

    /**
     * 过滤
     * @param fragment 碎片对象
     * @return boolean
     */
    default boolean filter(final E fragment) {
        return Objects.isNotBlank(this.toString(fragment));
    }

    /**
     * 转换成字符串
     * @param fragment 碎片对象
     * @return 字符串
     */
    String toString(final E fragment);
}
