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
package com.github.mybatisx.basic.metadata;

import com.github.mybatisx.annotation.Executing;

import java.util.Objects;

/**
 * 主键信息
 * @author wvkity
 * @created 2020-10-05
 * @since 1.0.0
 */
public class PrimaryKey {

    /**
     * 优先标识
     */
    private final boolean priority;

    /**
     * 是否为UUID主键
     */
    private final boolean uuid;

    /**
     * 是否为自增主键
     */
    private final boolean identity;

    /**
     * 是否为雪花算法主键
     */
    private final boolean snowflake;

    /**
     * 获取主键SQL
     */
    private final String generator;

    /**
     * SQL执行时机
     */
    private final Executing executing;

    public PrimaryKey(boolean priority, boolean uuid, boolean identity, boolean snowflake,
                      String generator, Executing executing) {
        this.priority = priority;
        this.uuid = uuid;
        this.identity = identity;
        this.snowflake = snowflake;
        this.generator = generator;
        this.executing = executing;
    }

    public boolean isPriority() {
        return priority;
    }

    public boolean isUuid() {
        return uuid;
    }

    public boolean isIdentity() {
        return identity;
    }

    public boolean isSnowflake() {
        return snowflake;
    }

    public String getGenerator() {
        return generator;
    }

    public Executing getExecuting() {
        return executing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrimaryKey)) {
            return false;
        }
        PrimaryKey that = (PrimaryKey) o;
        return priority == that.priority &&
            uuid == that.uuid &&
            identity == that.identity &&
            snowflake == that.snowflake &&
            Objects.equals(generator, that.generator) &&
            executing == that.executing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, uuid, identity, snowflake, generator, executing);
    }

    @Override
    public String toString() {
        return "PrimaryKey{" +
            "priority=" + priority +
            ", uuid=" + uuid +
            ", identity=" + identity +
            ", snowflake=" + snowflake +
            ", generator='" + generator + '\'' +
            ", executing=" + executing +
            '}';
    }
}
