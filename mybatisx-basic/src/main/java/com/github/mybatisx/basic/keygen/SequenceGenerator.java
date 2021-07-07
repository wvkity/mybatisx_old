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
package com.github.mybatisx.basic.keygen;

/**
 * 序列ID生成器
 * @author wvkity
 * @created 2020-10-22
 * @since 1.0.0
 */
public interface SequenceGenerator {

    /**
     * 获取生成序列SQL脚本
     * @param sequence 序列名称
     * @return SQL脚本
     */
    String sequenceScript(final String sequence);
}
