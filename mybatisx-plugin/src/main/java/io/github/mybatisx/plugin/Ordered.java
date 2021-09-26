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
package io.github.mybatisx.plugin;

/**
 * 拦截器执行顺序(值越大，越优先)
 * @author wvkity
 * @created 2021-02-10
 * @since 1.0.0
 */
@FunctionalInterface
public interface Ordered {

    int HIGHEST_PRECEDENCE = Integer.MAX_VALUE;
    int LOWEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * 获取排序值
     * @return 排序值
     */
    int getOrder();
}
