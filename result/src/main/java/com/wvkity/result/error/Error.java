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
package com.wvkity.result.error;

import com.wvkity.result.Status;

import java.io.Serializable;

/**
 * 错误
 * @author wvkity
 * @created 2021-02-15
 * @since 1.0.0
 */
public interface Error extends Serializable {

    /**
     * 错误
     * @param status {@link Status}
     */
    default void error(final Status status) {
        this.error(status.getCode(), status.getDesc());
    }

    /**
     * 错误
     * @param error 错误信息
     */
    default void error(final String error) {
        this.error(Status.ERR_FAILURE.getCode(), error);
    }

    /**
     * 错误
     * @param e {@link Throwable}
     */
    default void error(final Throwable e) {
        this.error(Status.ERR_FAILURE.getCode(), e);
    }

    /**
     * 错误
     * @param status {@link Status}
     * @param e  {@link Throwable}
     */
    default void error(final Status status, final Throwable e) {
        this.error(status.getCode(), e);
    }

    /**
     * 错误
     * @param code  响应状态码
     * @param e {@link Throwable}
     */
    default void error(final int code, final Throwable e) {
        this.error(code, e.getMessage());
    }

    /**
     * 错误
     * @param code  响应状态码
     * @param error 错误信息
     */
    void error(final int code, final String error);

    /**
     * 获取响应状态码
     * @return 响应状态码
     */
    int getCode();

    /**
     * 设置响应状态码
     * @param code 响应状态码
     */
    void setCode(final int code);

    /**
     * 获取错误描述信息
     * @return 错误信息
     */
    String getMessage();

    /**
     * 设置错误描述信息
     * @param message 错误信息
     */
    void setMessage(final String message);

}
