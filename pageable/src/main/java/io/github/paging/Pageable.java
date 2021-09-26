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
package io.github.paging;

import java.io.Serializable;

/**
 * 分页
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public interface Pageable extends Serializable {

    /**
     * 获取当前页
     * @return 当前页
     */
    long getPage();

    /**
     * 设置当前页
     * @param page 当前页
     */
    void setPage(final long page);

    /**
     * 获取每页数目
     * @return 每页数目
     */
    long getSize();

    /**
     * 设置每页数目
     * @param size 每页数目
     */
    void setSize(final long size);

    /**
     * 获取总记录数
     * @return 总记录数
     */
    long getRecords();

    /**
     * 设置总记录数
     * @param records 总记录数
     */
    void setRecords(final long records);

    /**
     * 获取总页数
     * @return 总页数
     */
    long getTotals();

    /**
     * 分页偏移量
     * @return 偏移量
     */
    long offset();

    /**
     * 获取显示页码数
     * @return 显示页码数
     */
    long getVisible();

    /**
     * 设置显示页码数
     * @param visible 页码数
     */
    void setVisible(final long visible);

    /**
     * 获取起始页码
     * @return 起始页码
     */
    long getStart();

    /**
     * 获取结束页码
     * @return 结束页码
     */
    long getEnd();

    /**
     * 是否存在上一页
     * @return boolean
     */
    boolean isHasPrev();

    /**
     * 是否存在下一页
     * @return boolean
     */
    boolean isHasNext();

}
