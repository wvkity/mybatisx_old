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
package io.github.mybatisx.support.constant;

import io.github.mybatisx.support.fragment.Fragment;

/**
 * 数据库条件符号
 * @author wvkity
 * @created 2020-10-24
 * @since 1.0.0
 */
public enum Symbol implements Fragment {

    /**
     * 空
     */
    NULL("IS NULL"),
    /**
     * 非空
     */
    NOT_NULL("IS NOT NULL"),
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
    GE(">="),
    /**
     * IN
     */
    IN("IN"),
    /**
     * NOT IN
     */
    NOT_IN("NOT IN"),
    /**
     * EXISTS
     */
    EXISTS("EXISTS"),
    /**
     * EXISTS
     */
    NOT_EXISTS("NOT EXISTS"),
    /**
     * 模糊匹配
     */
    LIKE("LIKE"),
    /**
     * 模糊匹配
     */
    NOT_LIKE("NOT LIKE"),
    /**
     * BETWEEN
     */
    BETWEEN("BETWEEN"),
    /**
     * NOT BETWEEN
     */
    NOT_BETWEEN("NOT BETWEEN"),
    //////// 扩展用 ////////
    /**
     * 嵌套
     */
    NESTING("NESTING"),
    /**
     * 模板
     */
    TPL("TEMPLATE"),
    /**
     * 特殊
     */
    SPECIAL("SPECIAL"),
    /**
     * 其他
     */
    OTHER("OTHER");


    final String symbol;

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getSegment() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return this.getSegment();
    }

    /**
     * 是否排除指定符号
     * @param symbol {@link Symbol}
     * @return boolean
     */
    public static boolean filter(final Symbol symbol) {
        return symbol != NULL && symbol != NOT_NULL && symbol != EXISTS && symbol != NOT_EXISTS;
    }

}
