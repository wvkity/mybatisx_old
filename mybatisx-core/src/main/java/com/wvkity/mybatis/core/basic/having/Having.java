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
package com.wvkity.mybatis.core.basic.having;

import com.wvkity.mybatis.core.basic.func.Function;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.segment.Fragment;


/**
 * 分组筛选条件
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
public interface Having extends Fragment {

    /**
     * 获取{@link Function}
     * @return {@link Function}
     */
    Function getFunc();

    /**
     * 获取{@link Slot}
     * @return {@link Slot}
     */
    Slot getSlot();
}
