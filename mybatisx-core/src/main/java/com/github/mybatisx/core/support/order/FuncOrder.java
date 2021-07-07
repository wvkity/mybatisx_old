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
package com.github.mybatisx.core.support.order;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.support.func.Function;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 聚合函数排序
 * @author wvkity
 * @created 2021-05-01
 * @since 1.0.0
 */
public class FuncOrder extends AbstractOrder<Function> {

    private static final long serialVersionUID = 6329803612707645772L;

    public FuncOrder(boolean ascending, List<Function> functions) {
        this.ascending = ascending;
        this.addAll(functions);
    }

    @Override
    public String getSegment() {
        if (!this.isEmpty()) {
            final String sortMode = this.ascending ? ASC : DESC;
            return this.fragments.stream().map(it -> it.getFuncBody() + sortMode )
                .collect(Collectors.joining(Constants.COMMA_SPACE));
        }
        return Constants.EMPTY;
    }

    /**
     * 升序
     * @param functions 聚合函数列表
     * @return {@link FuncOrder}
     */
    public static FuncOrder asc(final Function... functions) {
        return sort(true, functions);
    }

    /**
     * 升序
     * @param functions 聚合函数列表
     * @return {@link FuncOrder}
     */
    public static FuncOrder asc(final List<Function> functions) {
        return sort(true, functions);
    }

    /**
     * 降序
     * @param functions 聚合函数列表
     * @return {@link FuncOrder}
     */
    public static FuncOrder desc(final Function... functions) {
        return sort(false, functions);
    }

    /**
     * 降序
     * @param functions 聚合函数列表
     * @return {@link FuncOrder}
     */
    public static FuncOrder desc(final List<Function> functions) {
        return sort(false, functions);
    }

    /**
     * 排序
     * @param ascending 是否升序
     * @param functions 聚合函数列表
     * @return {@link FuncOrder}
     */
    public static FuncOrder sort(final boolean ascending, final Function... functions) {
        if (Objects.isEmpty(functions)) {
            return null;
        }
        return sort(ascending, Objects.asList(functions));
    }

    /**
     * 排序
     * @param ascending 是否升序
     * @param functions 聚合函数列表
     * @return {@link FuncOrder}
     */
    public static FuncOrder sort(final boolean ascending, final List<Function> functions) {
        if (Objects.isEmpty(functions)) {
            return null;
        }
        return new FuncOrder(ascending, functions);
    }

}
