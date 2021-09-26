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
package io.github.mybatisx.core.support.having;

import io.github.mybatisx.support.fragment.Fragment;

/**
 * 单个值比较符号
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
public enum SingleComparator implements Fragment {
    /**
     * 等于
     */
    EQ("="),
    /**
     * 不等于
     */
    NE("<>"),
    /**
     * 小于
     */
    LT("<"),
    /**
     * 小于等于
     */
    LE("<="),
    /**
     * 大于
     */
    GT(">"),
    /**
     * 大于等于
     */
    GE(">=");

    private final String symbol;

    SingleComparator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getSegment() {
        return this.symbol;
    }

}
