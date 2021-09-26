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
package io.github.mybatisx.plugin.paging;

import java.io.Serializable;

/**
 * 范围分页
 * @author wvkity
 * @created 2021-02-09
 * @since 1.0.0
 */
public class RangePageable implements Serializable {

    private static final long serialVersionUID = -1893845004040013345L;
    /**
     * 开始位置
     */
    private long start;
    /**
     * 结束位置
     */
    private long end;
    /**
     * 偏移量
     */
    private long offset;
    /**
     * 是否应用
     */
    private boolean apply;

    public RangePageable() {
    }

    public RangePageable(long start, long end, long offset, boolean apply) {
        this.start = start;
        this.end = end;
        this.offset = offset;
        this.apply = apply;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }
}
