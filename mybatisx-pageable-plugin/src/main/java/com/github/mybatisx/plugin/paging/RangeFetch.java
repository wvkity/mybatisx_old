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
package com.github.mybatisx.plugin.paging;

/**
 * 指定范围查询
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public interface RangeFetch {

    String PARAMETER_KEY = "criteria";

    /**
     * 是否执行范围查询
     * @return boolean
     */
    boolean isRange();

    /**
     * 获取模式
     * @return {@link RangeMode}
     */
    RangeMode getMode();

    /**
     * 起始位置
     * @return 起始位置
     */
    long getRowStart();

    /**
     * 结束位置
     * @return 结束位置
     */
    long getRowEnd();

    /**
     * 获取起始页码
     * @return 起始页码
     */
    long getPageStart();

    /**
     * 获取结束页码
     * @return 结束页码
     */
    long getPageEnd();

    /**
     * 获取每页数目
     * @return 每页数目
     */
    long getPageSize();

}
