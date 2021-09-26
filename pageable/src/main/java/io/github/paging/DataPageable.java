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
package io.github.paging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分页
 * @param <E> 数据泛型
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public class DataPageable<E> extends AbstractPageable {

    private static final long serialVersionUID = 4059347367341232695L;

    /**
     * 数据列表
     */
    private List<E> data;
    /**
     * 分页查询时是否自动填充数据
     */
    private boolean autoFilling = true;

    public DataPageable() {
        super();
    }

    public DataPageable(String page) {
        super(page);
    }

    public DataPageable(long page) {
        super(page);
    }

    public DataPageable(String page, String size) {
        super(page, size);
    }

    public DataPageable(int page, int size) {
        super(page, size);
    }

    public DataPageable(long page, long size) {
        super(page, size);
    }

    @SuppressWarnings("unchecked")
    public DataPageable<E> add(final E... elements) {
        if (elements.length > 0) {
            final List<E> tmpData = Arrays.stream(elements).filter(Objects::nonNull).collect(Collectors.toList());
            if (!tmpData.isEmpty()) {
                if (this.data == null) {
                    this.data = new ArrayList<>();
                }
                this.data.addAll(tmpData);
            }
        }
        return this;
    }

    public DataPageable<E> clear() {
        if (this.data != null && !this.data.isEmpty()) {
            this.data.clear();
        }
        return this;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return this.data == null || this.data.isEmpty();
    }

    public boolean autoFilling() {
        return autoFilling;
    }

    public void autoFilling(boolean autoFilling) {
        this.autoFilling = autoFilling;
    }

    public long getLength() {
        return this.data == null ? 0L : this.data.size();
    }

    ///// Static methods /////

    public static <E> DataPageable<E> of() {
        return new DataPageable<>();
    }

    public static <E> DataPageable<E> of(final String page) {
        return new DataPageable<>(page);
    }

    public static <E> DataPageable<E> of(final long page) {
        return new DataPageable<>(page);
    }

    public static <E> DataPageable<E> of(final String page, final String size) {
        return new DataPageable<>(page, size);
    }

    public static <E> DataPageable<E> of(final int page, final int size) {
        return new DataPageable<>(page, size);
    }

    public static <E> DataPageable<E> of(final long page, final long size) {
        return new DataPageable<>(page, size);
    }

}
