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
package com.github.mybatisx.backup.additional;

import com.github.mybatisx.CommandType;
import com.github.mybatisx.backup.meta.BackupMetadata;

import java.util.List;

/**
 * 附加处理器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public interface AdditionalProcessor {

    /**
     * 处理
     * @param metadata     备份数据
     * @param source       元数据
     * @param sourceIdProp 元数据ID属性
     * @param targetClass  目标类
     * @param target       目标类ID属性对应元数据ID属性
     * @param targetIdProp 目标类ID属性对应元数据ID属性
     * @param commandType  当前执行类型
     */
    void process(final BackupMetadata metadata, final Object source, final String sourceIdProp,
                 final Class<?> targetClass, final Object target, final String targetIdProp,
                 final CommandType commandType);

    /**
     * 转换成最终参数
     * @param metadata    {@link BackupMetadata}
     * @param data        备份数据
     * @param commandType 当前执行类型
     * @return 参数列表
     */
    List<Object> convert(final BackupMetadata metadata, final List<Object> data, final CommandType commandType);
}
