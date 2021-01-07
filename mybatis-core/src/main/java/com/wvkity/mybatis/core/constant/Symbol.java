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
package com.wvkity.mybatis.core.constant;

import com.wvkity.mybatis.core.segment.Fragment;

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
    NULL {
        @Override
        public String getSegment() {
            return "IS NULL";
        }
    },

    /**
     * 非空
     */
    NOT_NULL {
        @Override
        public String getSegment() {
            return "IS NOT NULL";
        }
    },

    /**
     * 等于
     */
    EQ {
        @Override
        public String getSegment() {
            return "=";
        }
    },

    /**
     * 不等于
     */
    NE {
        @Override
        public String getSegment() {
            return "<>";
        }
    },

    /**
     * 小于
     */
    LT {
        @Override
        public String getSegment() {
            return "<";
        }
    },

    /**
     * 小于等于
     */
    LE {
        @Override
        public String getSegment() {
            return "<=";
        }
    },

    /**
     * 大于
     */
    GT {
        @Override
        public String getSegment() {
            return ">";
        }
    },

    /**
     * 大于等于
     */
    GE {
        @Override
        public String getSegment() {
            return ">=";
        }
    },

    /**
     * IN
     */
    IN {
        @Override
        public String getSegment() {
            return "IN";
        }
    },

    /**
     * NOT IN
     */
    NOT_IN {
        @Override
        public String getSegment() {
            return "NOT IN";
        }
    },

    /**
     * EXISTS
     */
    EXISTS {
        @Override
        public String getSegment() {
            return "EXISTS";
        }
    },

    /**
     * EXISTS
     */
    NOT_EXISTS {
        @Override
        public String getSegment() {
            return "NOT EXISTS";
        }
    },

    /**
     * 模糊匹配
     */
    LIKE {
        @Override
        public String getSegment() {
            return "LIKE";
        }
    },

    /**
     * 模糊匹配
     */
    NOT_LIKE {
        @Override
        public String getSegment() {
            return "NOT LIKE";
        }
    },

    /**
     * BETWEEN
     */
    BETWEEN {
        @Override
        public String getSegment() {
            return "BETWEEN";
        }
    },

    /**
     * NOT BETWEEN
     */
    NOT_BETWEEN {
        @Override
        public String getSegment() {
            return "NOT BETWEEN";
        }
    };

    /**
     * 是否排除指定符号
     * @param symbol {@link Symbol}
     * @return boolean
     */
    public static boolean filter(final Symbol symbol) {
        return symbol != NULL && symbol != NOT_NULL && symbol != EXISTS && symbol != NOT_EXISTS;
    }
}
