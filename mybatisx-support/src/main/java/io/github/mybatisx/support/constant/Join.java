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
 * 连接模式
 * @author wvkity
 * @created 2021-04-12
 * @since 1.0.0
 */
public enum Join implements Fragment {

    /**
     * INNER JOIN
     */
    INNER {
        @Override
        public String getSegment() {
            return "INNER JOIN ";
        }
    },

    /**
     * LEFT JOIN
     */
    LEFT {
        @Override
        public String getSegment() {
            return "LEFT JOIN ";
        }
    },

    /**
     * RIGHT JOIN
     */
    RIGHT {
        @Override
        public String getSegment() {
            return "RIGHT JOIN ";
        }
    },

    /**
     * FULL JOIN
     */
    FULL {
        @Override
        public String getSegment() {
            return "FULL JOIN ";
        }
    }

}
