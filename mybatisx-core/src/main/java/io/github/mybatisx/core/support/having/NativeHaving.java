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
package io.github.mybatisx.core.support.having;

import io.github.mybatisx.Objects;
import io.github.mybatisx.Placeholders;
import io.github.mybatisx.core.support.func.Function;
import io.github.mybatisx.support.constant.Slot;

import java.util.List;
import java.util.Map;

/**
 * 分组筛选子句(纯SQL)
 * @author wvkity
 * @created 2021-04-29
 * @since 1.0.0
 */
public class NativeHaving implements Having {

    private static final long serialVersionUID = -6665732458167446163L;
    /**
     * 聚合函数体
     */
    private final String havingBody;
    /**
     * 单个值
     */
    private final String arg;
    /**
     * 参数集合
     */
    private final List<String> list;
    /**
     * 参数集合
     */
    private final Map<String, String> map;

    public NativeHaving(String havingBody) {
        this(havingBody, null, null, null);
    }

    public NativeHaving(String havingBody, String arg) {
        this(havingBody, arg, null, null);
    }

    public NativeHaving(String havingBody, List<String> list) {
        this(havingBody, null, list, null);
    }

    public NativeHaving(String havingBody, Map<String, String> map) {
        this(havingBody, null, null, map);
    }

    public NativeHaving(String havingBody, String arg, List<String> list, Map<String, String> map) {
        this.arg = arg;
        this.havingBody = havingBody;
        this.list = list;
        this.map = map;
    }

    @Override
    public Function getFunc() {
        return null;
    }

    @Override
    public Slot getSlot() {
        return null;
    }

    @Override
    public String getSegment() {
        if (Objects.isNotBlank(this.havingBody)) {
            if (Objects.isNotEmpty(this.map)) {
                return Placeholders.format(this.havingBody, this.map);
            }
            if (Objects.isNotEmpty(this.list)) {
                return Placeholders.format(this.havingBody, this.list);
            }
            if (Objects.isNotBlank(this.arg)) {
                return Placeholders.format(this.havingBody, this.arg);
            }
            return this.havingBody;
        }
        return null;
    }
}
