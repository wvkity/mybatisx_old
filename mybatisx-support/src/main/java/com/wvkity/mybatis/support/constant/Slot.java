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
package com.wvkity.mybatis.support.constant;

import com.wvkity.mybatis.support.fragment.Fragment;

/**
 * AND/OR操作符
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public enum Slot implements Fragment {

    /**
     * AND
     */
    AND,
    /**
     * OR
     */
    OR,
    /**
     * NONE
     */
    NONE {
        @Override
        public String getSegment() {
            return "";
        }
    };

    @Override
    public String getSegment() {
        return this.name();
    }
}
