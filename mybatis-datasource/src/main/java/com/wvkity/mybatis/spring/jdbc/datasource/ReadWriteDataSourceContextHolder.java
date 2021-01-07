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
package com.wvkity.mybatis.spring.jdbc.datasource;

import java.util.Objects;

/**
 * 读写数据源决策者
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
public class ReadWriteDataSourceContextHolder {

    /**
     * 策略处理
     */
    private static final ThreadLocal<DSType> STRATEGY_HOLDER = new ThreadLocal<>();

    /**
     * 选择只读
     */
    public static void chooseRead() {
        STRATEGY_HOLDER.set(DSType.SLAVE);
    }

    /**
     * 选择写
     */
    public static void chooseWrite() {
        STRATEGY_HOLDER.set(DSType.MASTER);
    }

    /**
     * 重置
     */
    public static void reset() {
        STRATEGY_HOLDER.set(null);
    }

    /**
     * 是否无选择
     * @return boolean
     */
    public static boolean isNone() {
        return Objects.isNull(STRATEGY_HOLDER.get());
    }

    /**
     * 是否只读
     * @return boolean
     */
    public static boolean isRead() {
        return DSType.SLAVE == STRATEGY_HOLDER.get();
    }

    /**
     * 是否写
     * @return boolean
     */
    public static boolean isWrite() {
        return DSType.MASTER == STRATEGY_HOLDER.get();
    }
}
