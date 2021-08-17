/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.github.mybatisx.jdbc.datasource.aop;

import java.util.Map;

/**
 * 切面资源
 * @author wvkity
 * @created 2021-08-17
 * @since 1.0.0
 */
public interface AspectResource {

    /**
     * 当之前操作是写的时候，是否强制选择读库
     * @param forceChoiceReadWhenWrite 是否强制选择读库
     */
    void setForceChoiceReadWhenWrite(final boolean forceChoiceReadWhenWrite);

    /**
     * 只读事务方法缓存
     * @return 方法缓存
     */
    Map<String, Boolean> getReadMethodCache();
}
