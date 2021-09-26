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
package io.github.mybatisx.backup.message;

import io.github.mybatisx.backup.meta.BackupMetadata;
import io.github.mybatisx.event.EventPhase;

import java.util.List;

/**
 * 广播通知
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public interface Broadcast {

    /**
     * 备份数据处理前通知(事务成功/失败都会执行)
     * @param metadata {@link BackupMetadata}
     * @param phase    {@link EventPhase}
     */
    void before(final BackupMetadata metadata, final EventPhase phase);

    /**
     * 备份数据处理准备工作(查找相关处理Bean)
     * @param metadata {@link BackupMetadata}
     * @param e        异常信息
     */
    void early(final BackupMetadata metadata, final Exception e);

    /**
     * 备份数据处理
     * @param metadata {@link BackupMetadata}
     * @param dataList 处理后的数据
     * @param <T>      数据类型
     * @param e        异常信息
     */
    <T> void processed(final BackupMetadata metadata, List<T> dataList, final Exception e);

    /**
     * 数据备份
     * @param metadata {@link BackupMetadata}
     * @param dataList 处理后的数据
     * @param success  是否成功
     * @param <T>      数据类型
     * @param e        异常信息
     */
    <T> void after(final BackupMetadata metadata, List<T> dataList, final boolean success, final Exception e);

    /**
     * 数据备份完成
     * @param metadata {@link BackupMetadata}
     * @param success  是否成功
     * @param e        异常信息
     */
    void completed(final BackupMetadata metadata, final boolean success, final Exception e);
}
