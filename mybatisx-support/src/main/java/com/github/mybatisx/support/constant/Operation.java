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
package com.github.mybatisx.support.constant;

/**
 * 执行操作类型
 * @author wvkity
 * @created 2020-10-24
 * @since 1.0.0
 */
public enum Operation {

    /**
     * 不操作
     */
    NONE,
    /**
     * 保存
     */
    INSERT,
    /**
     * 替换/更新
     */
    REPLACE
}
