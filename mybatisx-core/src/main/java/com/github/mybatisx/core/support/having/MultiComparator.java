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
package com.github.mybatisx.core.support.having;

import com.github.mybatisx.support.fragment.Fragment;

/**
 * 多个值比较符
 * @author wvkity
 * @created 2021-04-30
 * @since 1.0.0
 */
public enum MultiComparator implements Fragment {

    /**
     * 小于或大于
     */
    LT_OR_GT(":@ < ?0 OR :@ > ?1"),
    /**
     * 小于等于或大于
     */
    LE_OR_GT(":@ <= ?0 OR :@ > ?1"),
    /**
     * 小于或大于等于
     */
    LT_OR_GE(":@ < ?0 OR :@ >= ?1"),
    /**
     * 小于等于或大于等于
     */
    LE_OR_GE(":@ <= ? OR :@ >= ?1"),
    /**
     * 大于且小于
     */
    GT_AND_LT(":@ > ?0 AND :@ < ?1"),
    /**
     * 大于且小于等于
     */
    GT_AND_LE(":@ > ?0 AND :@ <= ?1"),
    /**
     * 大于等于且小于
     */
    GE_AND_LT(":@ >= ?0 AND :@ < ?1"),
    /**
     * 大于等于且小于等于
     */
    GE_AND_LE(":@ >= ?0 AND :@ <= ?1");

    private final String symbol;

    MultiComparator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getSegment() {
        return this.symbol;
    }
}
