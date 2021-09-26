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
package io.github.mybatisx;

/**
 * 模板匹配模式
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public enum PlaceholderPattern {
    /**
     * 单个值
     */
    SINGLE,
    /**
     * 多个值
     */
    MULTIPLE,
    /**
     * Map值
     */
    MAP
}
