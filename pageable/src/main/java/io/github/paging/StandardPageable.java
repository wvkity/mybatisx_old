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

/**
 * 分页
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public class StandardPageable extends AbstractPageable {

    private static final long serialVersionUID = -3406478445939743345L;

    public StandardPageable() {
        super();
    }

    public StandardPageable(String page) {
        super(page);
    }

    public StandardPageable(long page) {
        super(page);
    }

    public StandardPageable(String page, String size) {
        super(page, size);
    }

    public StandardPageable(int page, int size) {
        super(page, size);
    }

    public StandardPageable(long page, long size) {
        super(page, size);
    }

    ///// Static methods /////

    public static StandardPageable of() {
        return new StandardPageable();
    }

    public static StandardPageable of(final String page) {
        return new StandardPageable(page);
    }

    public static StandardPageable of(final long page) {
        return new StandardPageable(page);
    }

    public static StandardPageable of(final String page, final String size) {
        return new StandardPageable(page, size);
    }

    public static StandardPageable of(final int page, final int size) {
        return new StandardPageable(page, size);
    }

    public static StandardPageable of(final long page, final long size) {
        return new StandardPageable(page, size);
    }

}
