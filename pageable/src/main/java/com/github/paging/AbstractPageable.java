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
package com.github.paging;

import java.util.regex.Pattern;

/**
 * 抽象分页
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractPageable implements Pageable {

    /**
     * 正整数正则表达式
     */
    private static final Pattern REGEX_POSITIVE_INTEGER = Pattern.compile("^\\+?(0|[1-9]+\\d?)$");
    /**
     * 0
     */
    public static final long DEF_ZERO = 0L;
    /**
     * 1
     */
    public static final long DEF_ONE = 1L;
    /**
     * 第一页
     */
    public static final long DEF_PAGE_FIRST = DEF_ONE;
    /**
     * 每页显示数目
     */
    public static final long DEF_PAGE_SIZE = 20L;
    /**
     * 每页最大显示数目
     */
    public static final long DEF_MAX_PAGE_SIZE = 2000L;
    /**
     * 显示页码数目
     */
    public static final long DEF_VISIBLE_PAGE_SIZE = 8L;
    /**
     * 最小显示页码数目
     */
    public static final long DEF_VISIBLE_MIN_PAGE_SIZE = 2L;
    /**
     * 最大显示页码数目
     */
    public static final long DEF_VISIBLE_MAX_PAGE_SIZE = 30L;
    /**
     * 当前页
     */
    protected long page;
    /**
     * 每页显示数目
     */
    protected long size;
    /**
     * 总记录数
     */
    protected long records;
    /**
     * 总页数
     */
    protected long totals;
    /**
     * 显示页码数
     */
    protected long visible = DEF_VISIBLE_PAGE_SIZE;
    /**
     * 起始页码
     */
    protected long start;
    /**
     * 结束页码
     */
    protected long end;

    /**
     * 构造方法
     */
    public AbstractPageable() {
        this(DEF_PAGE_FIRST, DEF_PAGE_SIZE);
    }

    /**
     * 构造方法
     * @param page 当前页
     */
    public AbstractPageable(String page) {
        this(toLong(page), DEF_PAGE_SIZE);
    }

    /**
     * 构造方法
     * @param page 当前页
     */
    public AbstractPageable(long page) {
        this(page, DEF_PAGE_SIZE);
    }

    /**
     * 构造方法
     * @param page 当前页
     * @param size 每页数目
     */
    public AbstractPageable(String page, String size) {
        this(toLong(page), toLong(size));
    }

    /**
     * 构造方法
     * @param page 当前页
     * @param size 每页数目
     */
    public AbstractPageable(int page, int size) {
        this.setPage(page);
        this.setSize(size);
    }

    /**
     * 构造方法
     * @param page 当前页
     * @param size 每页数目
     */
    public AbstractPageable(long page, long size) {
        this.setPage(page);
        this.setSize(size);
    }

    /**
     * 计算总页数
     */
    protected void calculateTotal() {
        final long pages = this.records / this.size;
        if (this.records % this.size == 0) {
            this.totals = pages;
        } else {
            this.totals = pages + 1;
        }
        this.calculatePageStartAndEnd();
    }

    /**
     * 计算页码起始位置和结束位置
     */
    protected void calculatePageStartAndEnd() {
        if (this.totals <= this.visible) {
            this.start = DEF_ONE;
            this.end = this.totals;
        } else {
            if (this.visible <= DEF_VISIBLE_MIN_PAGE_SIZE) {
                this.start = this.page;
                this.end = this.page + DEF_ONE;
                if (this.end > this.totals) {
                    this.end = this.totals;
                }
            } else {
                final long avg = this.visible / 2;
                final long num = avg + DEF_ONE;
                if (this.page <= num) {
                    this.start = DEF_ONE;
                    this.end = this.visible;
                } else {
                    if ((this.totals - this.page) >= num) {
                        this.start = this.page - avg;
                        this.end = this.page - avg + this.visible - DEF_ONE;
                    } else {
                        this.start = this.page - (this.visible - DEF_ONE - (this.totals - this.page));
                        this.end = this.totals;
                    }
                }
            }
        }
    }

    @Override
    public long getPage() {
        return this.page;
    }

    @Override
    public void setPage(long page) {
        this.page = Math.max(page, DEF_PAGE_FIRST);
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public void setSize(long size) {
        this.size = Math.min(size <= DEF_ZERO ? DEF_PAGE_SIZE : size, DEF_MAX_PAGE_SIZE);
    }

    @Override
    public long getRecords() {
        return this.records;
    }

    @Override
    public void setRecords(long records) {
        this.records = Math.max(records, DEF_ZERO);
        this.calculateTotal();
    }

    @Override
    public long getTotals() {
        return this.totals;
    }

    @Override
    public long offset() {
        return Math.max(DEF_ZERO, (this.page - 1) * this.size);
    }

    @Override
    public long getVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(long visible) {
        this.visible = Math.min(DEF_VISIBLE_MAX_PAGE_SIZE, visible < DEF_VISIBLE_MIN_PAGE_SIZE ?
            DEF_VISIBLE_PAGE_SIZE : visible);
    }

    @Override
    public long getStart() {
        return this.start;
    }

    @Override
    public long getEnd() {
        return this.end;
    }

    @Override
    public boolean isHasPrev() {
        return this.records > DEF_ZERO && this.page > DEF_ZERO && (this.page - DEF_ONE) > DEF_ZERO
            && (this.page - DEF_ONE) <= this.totals;
    }

    @Override
    public boolean isHasNext() {
        return this.records > DEF_ZERO && this.page > DEF_ZERO && (this.page + DEF_ONE) > DEF_ZERO
            && this.page < this.totals;
    }


    ///// Static methods /////

    /**
     * 检测是否为有效正整数
     * @param value 待校验值
     * @return boolean
     */
    private static boolean isPositiveInteger(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return REGEX_POSITIVE_INTEGER.matcher(value).matches();
    }

    /**
     * 字符串整数转数值
     * @param value 值
     * @return long
     */
    private static long toLong(final String value) {
        return isPositiveInteger(value) ? Long.parseLong(value) : 0;
    }

}
