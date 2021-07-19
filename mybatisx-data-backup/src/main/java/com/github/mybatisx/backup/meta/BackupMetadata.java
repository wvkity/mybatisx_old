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
package com.github.mybatisx.backup.meta;

import com.github.mybatisx.CommandType;

import java.io.Serializable;
import java.util.List;

/**
 * 备份元数据
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
public interface BackupMetadata extends Serializable {

    /**
     * 获取唯一标识
     * @return 唯一标识
     */
    String getUniqueCode();

    /**
     * 原数据ID属性
     * @return ID属性
     */
    String getOrgIdProp();

    /**
     * 备份数据ID属性
     * @return ID属性
     */
    String getTargetIdProp();

    /**
     * 数据备份处理bean名
     * @return bean名
     */
    String getProcessBean();

    /**
     * 数据备份处理方法名
     * @return 目标方法名
     */
    String getProcessMethod();

    /**
     * 原始参数
     * @return 原始参数
     */
    Object getOrgParam();

    /**
     * 获取真实参数
     * @return 真实参数
     */
    Object getRealParam();

    /**
     * 备份数据目标类型
     * @return 备份数据类型
     */
    Class<?> getTarget();

    /**
     * 备份数据列表
     * @return 备份数据列表
     */
    List<Object> getSources();

    /**
     * 参数类型列表
     * @return 类型列表
     */
    Class<?>[] getArgs();

    /**
     * 当前执行操作类型
     * @return {@link CommandType}
     */
    CommandType getCommandType();

}
